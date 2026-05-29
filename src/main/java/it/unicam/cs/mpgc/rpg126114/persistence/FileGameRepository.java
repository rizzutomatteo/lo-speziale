package it.unicam.cs.mpgc.rpg126114.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import it.unicam.cs.mpgc.rpg126114.content.ContentRepository;
import it.unicam.cs.mpgc.rpg126114.game.GameState;
import it.unicam.cs.mpgc.rpg126114.model.character.Apothecary;
import it.unicam.cs.mpgc.rpg126114.model.economy.Coins;
import it.unicam.cs.mpgc.rpg126114.model.inventory.Inventory;
import it.unicam.cs.mpgc.rpg126114.model.inventory.RecipeBook;
import it.unicam.cs.mpgc.rpg126114.model.inventory.Stockpile;
import it.unicam.cs.mpgc.rpg126114.model.progression.Reputation;
import it.unicam.cs.mpgc.rpg126114.model.progression.Skill;
import it.unicam.cs.mpgc.rpg126114.model.progression.SkillType;
import it.unicam.cs.mpgc.rpg126114.model.world.GameClock;
import it.unicam.cs.mpgc.rpg126114.util.Preconditions;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Saves and loads the game as a JSON file under the user's home folder.
 *
 * <p>The save stores only ids and quantities; on load the apothecary's full
 * inventory, recipes and skills are rebuilt and the world is taken from the
 * {@link ContentRepository}. This keeps the file small and lets the static
 * content evolve independently of saved games.
 */
public final class FileGameRepository implements GameRepository {

    private final ContentRepository content;
    private final Path file;
    private final Gson gson;

    public FileGameRepository(ContentRepository content) {
        this(content, defaultLocation());
    }

    public FileGameRepository(ContentRepository content, Path file) {
        this.content = Preconditions.requireNonNull(content, "content");
        this.file = Preconditions.requireNonNull(file, "file");
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();
    }

    private static Path defaultLocation() {
        return Path.of(System.getProperty("user.home"), ".lo-speziale", "partita.json");
    }

    @Override
    public boolean exists() {
        return Files.isRegularFile(file);
    }

    @Override
    public void save(GameState state) {
        Preconditions.requireNonNull(state, "state");
        SaveData data = toSaveData(state);
        try {
            Files.createDirectories(file.getParent());
            try (Writer writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
                gson.toJson(data, writer);
            }
        } catch (IOException e) {
            throw new PersistenceException("impossibile salvare la partita", e);
        }
    }

    @Override
    public Optional<GameState> load() {
        if (!exists()) {
            return Optional.empty();
        }
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            SaveData data = gson.fromJson(reader, SaveData.class);
            if (data == null) {
                throw new PersistenceException("la partita salvata è vuota", null);
            }
            return Optional.of(fromSaveData(data));
        } catch (IOException | JsonParseException e) {
            throw new PersistenceException("partita salvata illeggibile", e);
        }
    }

    @Override
    public void delete() {
        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new PersistenceException("impossibile eliminare la partita", e);
        }
    }

    // --- mapping ------------------------------------------------------------

    private SaveData toSaveData(GameState state) {
        Apothecary apothecary = state.getApothecary();
        Map<String, SkillData> skills = new LinkedHashMap<>();
        apothecary.getSkills().forEach((type, skill) ->
                skills.put(type.name(), new SkillData(skill.getLevel(), skill.getExperience())));
        return new SaveData(
                apothecary.getName(),
                state.getClock().getDay(),
                state.getCurrentVillageId(),
                state.getPatientsHealed(),
                state.getPatientsLost(),
                apothecary.getReputation().getPoints(),
                apothecary.getPurse().getAmount(),
                skills,
                List.copyOf(apothecary.getRecipeBook().all()),
                new LinkedHashMap<>(apothecary.getInventory().getIngredients().asMap()),
                new LinkedHashMap<>(apothecary.getInventory().getRemedies().asMap()),
                LocalDate.now());
    }

    private GameState fromSaveData(SaveData data) {
        Map<SkillType, Skill> skills = new EnumMap<>(SkillType.class);
        for (SkillType type : SkillType.values()) {
            SkillData saved = data.skills() == null ? null : data.skills().get(type.name());
            skills.put(type, saved == null ? new Skill(type) : new Skill(type, saved.level(), saved.experience()));
        }
        Inventory inventory = new Inventory(
                new Stockpile(orEmpty(data.ingredients())),
                new Stockpile(orEmpty(data.remedies())));
        RecipeBook recipeBook = new RecipeBook(data.knownRecipes() == null ? List.of() : data.knownRecipes());
        Apothecary apothecary = new Apothecary("speziale", data.apothecaryName(), skills,
                new Reputation(data.reputationPoints()), Coins.of(data.coins()), inventory, recipeBook);
        return new GameState(apothecary, new GameClock(data.day()),
                data.currentVillageId(), data.patientsHealed(), data.patientsLost());
    }

    private static Map<String, Integer> orEmpty(Map<String, Integer> map) {
        return map == null ? Map.of() : map;
    }

    // --- save format --------------------------------------------------------

    private record SaveData(String apothecaryName, int day, String currentVillageId,
                            int patientsHealed, int patientsLost, int reputationPoints, int coins,
                            Map<String, SkillData> skills, List<String> knownRecipes,
                            Map<String, Integer> ingredients, Map<String, Integer> remedies,
                            LocalDate savedAt) {
    }

    private record SkillData(int level, int experience) {
    }
}
