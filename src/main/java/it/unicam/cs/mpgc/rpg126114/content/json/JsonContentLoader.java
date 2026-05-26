package it.unicam.cs.mpgc.rpg126114.content.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import it.unicam.cs.mpgc.rpg126114.content.ContentException;
import it.unicam.cs.mpgc.rpg126114.content.ContentLoader;
import it.unicam.cs.mpgc.rpg126114.content.ContentRepository;
import it.unicam.cs.mpgc.rpg126114.content.GameContent;
import it.unicam.cs.mpgc.rpg126114.model.ailment.Ailment;
import it.unicam.cs.mpgc.rpg126114.model.ailment.Symptom;
import it.unicam.cs.mpgc.rpg126114.model.alchemy.Ingredient;
import it.unicam.cs.mpgc.rpg126114.model.alchemy.Rarity;
import it.unicam.cs.mpgc.rpg126114.model.alchemy.Recipe;
import it.unicam.cs.mpgc.rpg126114.model.alchemy.Remedy;
import it.unicam.cs.mpgc.rpg126114.model.alchemy.effect.RemedyEffect;
import it.unicam.cs.mpgc.rpg126114.model.economy.Coins;
import it.unicam.cs.mpgc.rpg126114.model.humor.Humor;
import it.unicam.cs.mpgc.rpg126114.model.humor.HumoralBalance;
import it.unicam.cs.mpgc.rpg126114.model.progression.SkillType;
import it.unicam.cs.mpgc.rpg126114.model.world.Region;
import it.unicam.cs.mpgc.rpg126114.model.world.Village;
import it.unicam.cs.mpgc.rpg126114.model.world.WorldMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Loads the game content from the JSON files bundled under the {@code data}
 * resource folder.
 *
 * <p>The files are read through character streams, parsed with Gson into small
 * data-transfer records, then mapped onto the domain objects. Mapping in a
 * dedicated step keeps the domain free of any knowledge of JSON and lets the
 * loader resolve references — a recipe to its remedy, an ailment to its symptoms —
 * and reject inconsistent content early.
 */
public final class JsonContentLoader implements ContentLoader {

    private static final String BASE = "/it/unicam/cs/mpgc/rpg126114/data/";

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(RemedyEffect.class, new RemedyEffectAdapter())
            .create();

    @Override
    public ContentRepository load() {
        List<Symptom> symptoms = loadSymptoms();
        List<Ailment> ailments = loadAilments(indexById(symptoms, Symptom::getId));
        List<Ingredient> ingredients = loadIngredients();
        List<Remedy> remedies = loadRemedies();
        List<Recipe> recipes = loadRecipes();
        WorldMap world = loadWorld();

        checkReferences(recipes, remedies, ingredients, ailments, world);
        return new GameContent(symptoms, ailments, ingredients, remedies, recipes, world);
    }

    private List<Symptom> loadSymptoms() {
        return readList("symptoms.json", new TypeToken<List<SymptomDto>>() { }).stream()
                .map(dto -> new Symptom(dto.id(), dto.name(), dto.description(),
                        humor(dto.humor()), dto.gravity()))
                .toList();
    }

    private List<Ailment> loadAilments(Map<String, Symptom> symptomsById) {
        return readList("ailments.json", new TypeToken<List<AilmentDto>>() { }).stream()
                .map(dto -> new Ailment(dto.id(), dto.name(), dto.description(),
                        toBalance(dto.signature()),
                        resolveSymptoms(dto, symptomsById),
                        dto.virulence()))
                .toList();
    }

    private List<Symptom> resolveSymptoms(AilmentDto dto, Map<String, Symptom> symptomsById) {
        return dto.symptomIds().stream()
                .map(id -> {
                    Symptom symptom = symptomsById.get(id);
                    if (symptom == null) {
                        throw new ContentException(
                                "ailment '" + dto.id() + "' refers to unknown symptom '" + id + "'");
                    }
                    return symptom;
                })
                .toList();
    }

    private List<Ingredient> loadIngredients() {
        return readList("ingredients.json", new TypeToken<List<IngredientDto>>() { }).stream()
                .map(dto -> new Ingredient(dto.id(), dto.name(), dto.description(),
                        rarity(dto.rarity()), Coins.of(dto.price()),
                        dto.affinity() == null ? null : humor(dto.affinity())))
                .toList();
    }

    private List<Remedy> loadRemedies() {
        return readList("remedies.json", new TypeToken<List<RemedyDto>>() { }).stream()
                .map(dto -> new Remedy(dto.id(), dto.name(), dto.description(), dto.effects()))
                .toList();
    }

    private List<Recipe> loadRecipes() {
        return readList("recipes.json", new TypeToken<List<RecipeDto>>() { }).stream()
                .map(dto -> new Recipe(dto.id(), dto.name(), dto.description(), dto.ingredients(),
                        dto.remedyId(), skill(dto.requiredSkill()), dto.requiredLevel(),
                        dto.rewardExperience()))
                .toList();
    }

    private WorldMap loadWorld() {
        WorldDto dto = readObject("world.json", WorldDto.class);
        List<Region> regions = dto.regions().stream()
                .map(region -> new Region(region.id(), region.name(), region.description()))
                .toList();
        List<Village> villages = dto.villages().stream()
                .map(village -> new Village(village.id(), village.name(), village.description(),
                        village.regionId(), village.marketIngredients(), village.localAilments()))
                .toList();
        Map<String, Integer> routes = new HashMap<>();
        for (RouteDto route : dto.routes()) {
            routes.put(WorldMap.routeKey(route.from(), route.to()), route.days());
        }
        return new WorldMap(regions, villages, routes);
    }

    private void checkReferences(List<Recipe> recipes, List<Remedy> remedies, List<Ingredient> ingredients,
                                 List<Ailment> ailments, WorldMap world) {
        Set<String> remedyIds = ids(remedies, Remedy::getId);
        Set<String> ingredientIds = ids(ingredients, Ingredient::getId);
        Set<String> ailmentIds = ids(ailments, Ailment::getId);

        for (Recipe recipe : recipes) {
            require(remedyIds.contains(recipe.getRemedyId()),
                    "recipe '" + recipe.getId() + "' yields unknown remedy '" + recipe.getRemedyId() + "'");
            for (String ingredientId : recipe.getIngredients().keySet()) {
                require(ingredientIds.contains(ingredientId),
                        "recipe '" + recipe.getId() + "' needs unknown ingredient '" + ingredientId + "'");
            }
        }
        for (Village village : world.getVillages()) {
            for (String ingredientId : village.getMarketIngredients()) {
                require(ingredientIds.contains(ingredientId),
                        "village '" + village.getId() + "' sells unknown ingredient '" + ingredientId + "'");
            }
            for (String ailmentId : village.getLocalAilments()) {
                require(ailmentIds.contains(ailmentId),
                        "village '" + village.getId() + "' lists unknown ailment '" + ailmentId + "'");
            }
        }
    }

    // --- low-level reading --------------------------------------------------

    private <T> List<T> readList(String file, TypeToken<List<T>> token) {
        try (Reader reader = open(file)) {
            List<T> list = gson.fromJson(reader, token.getType());
            if (list == null) {
                throw new ContentException("content resource is empty: " + file);
            }
            return list;
        } catch (IOException e) {
            throw new ContentException("failed to read content resource: " + file, e);
        } catch (JsonParseException e) {
            throw new ContentException("malformed content resource: " + file, e);
        }
    }

    private <T> T readObject(String file, Class<T> type) {
        try (Reader reader = open(file)) {
            T value = gson.fromJson(reader, type);
            if (value == null) {
                throw new ContentException("content resource is empty: " + file);
            }
            return value;
        } catch (IOException e) {
            throw new ContentException("failed to read content resource: " + file, e);
        } catch (JsonParseException e) {
            throw new ContentException("malformed content resource: " + file, e);
        }
    }

    private Reader open(String file) {
        String path = BASE + file;
        InputStream in = JsonContentLoader.class.getResourceAsStream(path);
        if (in == null) {
            throw new ContentException("missing content resource: " + path);
        }
        return new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
    }

    // --- mapping helpers ----------------------------------------------------

    private HumoralBalance toBalance(Map<String, Integer> raw) {
        if (raw == null) {
            return HumoralBalance.balanced();
        }
        Map<Humor, Integer> levels = new EnumMap<>(Humor.class);
        raw.forEach((name, level) -> levels.put(humor(name), level));
        return HumoralBalance.of(levels);
    }

    private static Humor humor(String name) {
        try {
            return Humor.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new ContentException("unknown humor: " + name, e);
        }
    }

    private static Rarity rarity(String name) {
        try {
            return Rarity.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new ContentException("unknown rarity: " + name, e);
        }
    }

    private static SkillType skill(String name) {
        try {
            return SkillType.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new ContentException("unknown skill: " + name, e);
        }
    }

    private static <T> Map<String, T> indexById(List<T> items, java.util.function.Function<T, String> id) {
        return items.stream().collect(Collectors.toMap(id, item -> item, (a, b) -> a, LinkedHashMap::new));
    }

    private static <T> Set<String> ids(List<T> items, java.util.function.Function<T, String> id) {
        return items.stream().map(id).collect(Collectors.toSet());
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new ContentException(message);
        }
    }

    // --- data-transfer records ----------------------------------------------

    private record SymptomDto(String id, String name, String description, String humor, int gravity) {
    }

    private record AilmentDto(String id, String name, String description,
                              Map<String, Integer> signature, List<String> symptomIds, int virulence) {
    }

    private record IngredientDto(String id, String name, String description,
                                 String rarity, int price, String affinity) {
    }

    private record RemedyDto(String id, String name, String description, List<RemedyEffect> effects) {
    }

    private record RecipeDto(String id, String name, String description, Map<String, Integer> ingredients,
                             String remedyId, String requiredSkill, int requiredLevel, int rewardExperience) {
    }

    private record RegionDto(String id, String name, String description) {
    }

    private record VillageDto(String id, String name, String description, String regionId,
                              List<String> marketIngredients, List<String> localAilments) {
    }

    private record RouteDto(String from, String to, int days) {
    }

    private record WorldDto(List<RegionDto> regions, List<VillageDto> villages, List<RouteDto> routes) {
    }
}
