package it.unicam.cs.mpgc.rpg126114.game;

import it.unicam.cs.mpgc.rpg126114.content.ContentRepository;
import it.unicam.cs.mpgc.rpg126114.model.ailment.Ailment;
import it.unicam.cs.mpgc.rpg126114.model.alchemy.Ingredient;
import it.unicam.cs.mpgc.rpg126114.model.alchemy.Recipe;
import it.unicam.cs.mpgc.rpg126114.model.character.Apothecary;
import it.unicam.cs.mpgc.rpg126114.model.character.Patient;
import it.unicam.cs.mpgc.rpg126114.model.economy.Coins;
import it.unicam.cs.mpgc.rpg126114.model.progression.ReputationTier;
import it.unicam.cs.mpgc.rpg126114.model.progression.SkillType;
import it.unicam.cs.mpgc.rpg126114.model.treatment.TreatmentSession;
import it.unicam.cs.mpgc.rpg126114.model.world.Village;
import it.unicam.cs.mpgc.rpg126114.model.world.WorldMap;
import it.unicam.cs.mpgc.rpg126114.util.Preconditions;
import it.unicam.cs.mpgc.rpg126114.util.RandomSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The façade the user interface talks to.
 *
 * <p>It coordinates the parts of a playthrough — travelling, meeting patients,
 * running and concluding treatments, brewing and trading — over the static
 * {@link ContentRepository} and the mutable {@link GameState}. Keeping this
 * orchestration in one place leaves the controllers thin and the model unaware of
 * any interface.
 */
public final class Game {

    private static final int WAITING_PATIENTS = 3;
    private static final int TREATMENT_DAYS = 1;

    private final ContentRepository content;
    private final GameState state;
    private final RandomSource random;
    private final PatientGenerator patientGenerator;
    private final Brewer brewer;
    private final Market market;
    private final List<Patient> waitingPatients = new ArrayList<>();

    public Game(ContentRepository content, GameState state, RandomSource random) {
        this.content = Preconditions.requireNonNull(content, "content");
        this.state = Preconditions.requireNonNull(state, "state");
        this.random = Preconditions.requireNonNull(random, "random");
        this.patientGenerator = new PatientGenerator(content, random);
        this.brewer = new Brewer(content);
        this.market = new Market(content);
        refreshWaitingPatients();
    }

    public ContentRepository getContent() {
        return content;
    }

    public GameState getState() {
        return state;
    }

    public Apothecary getApothecary() {
        return state.getApothecary();
    }

    public WorldMap getWorld() {
        return content.getWorldMap();
    }

    public Village getCurrentVillage() {
        return getWorld().getVillage(state.getCurrentVillageId());
    }

    public List<Patient> getWaitingPatients() {
        return Collections.unmodifiableList(waitingPatients);
    }

    // --- travel -------------------------------------------------------------

    public List<Village> destinations() {
        return getWorld().villagesReachableFrom(state.getCurrentVillageId());
    }

    public int travelDaysTo(String villageId) {
        return getWorld().travelDays(state.getCurrentVillageId(), villageId);
    }

    public void travelTo(String villageId) {
        int days = travelDaysTo(villageId);
        state.getClock().advance(days);
        state.moveTo(villageId);
        refreshWaitingPatients();
    }

    // --- treatment ----------------------------------------------------------

    public TreatmentSession startTreatment(Patient patient) {
        Preconditions.require(waitingPatients.contains(patient), "this patient is not waiting here");
        return new TreatmentSession(patient, getApothecary(), random);
    }

    /**
     * Applies the consequences of a finished treatment to the apothecary and the
     * world, and removes the patient from those still waiting.
     */
    public TreatmentReport concludeTreatment(TreatmentSession session) {
        Preconditions.requireNonNull(session, "session");
        Preconditions.require(session.isOver(), "the treatment is not over yet");
        waitingPatients.remove(session.getPatient());
        state.getClock().advance(TREATMENT_DAYS);
        return applyOutcome(session);
    }

    private TreatmentReport applyOutcome(TreatmentSession session) {
        Apothecary apothecary = getApothecary();
        Patient patient = session.getPatient();
        Ailment ailment = patient.getAffliction().getAilment();
        return switch (session.getOutcome()) {
            case GUARITO -> {
                int fame = 4 + ailment.getVirulence() * 2;
                Coins reward = Coins.of(10 + ailment.getVirulence() * 5);
                apothecary.getReputation().reward(fame);
                apothecary.earn(reward);
                apothecary.train(SkillType.DIAGNOSI, 10);
                state.recordHealed();
                yield new TreatmentReport(session.getOutcome(),
                        "Hai guarito " + patient.getName() + ". Ricompensa: " + reward
                                + ", e la tua fama cresce di " + fame + ".");
            }
            case DECEDUTO -> {
                apothecary.getReputation().penalise(6);
                state.recordLost();
                yield new TreatmentReport(session.getOutcome(),
                        patient.getName() + " è spirato tra le tue mani. La voce si spargerà.");
            }
            case ABBANDONATO -> {
                apothecary.getReputation().penalise(2);
                state.recordLost();
                yield new TreatmentReport(session.getOutcome(),
                        patient.getName() + " ti ha lasciato per cercare aiuto altrove.");
            }
            case IN_CORSO -> throw new IllegalStateException("the treatment is not over yet");
        };
    }

    // --- workshop and market ------------------------------------------------

    public List<Recipe> knownRecipes() {
        return content.getRecipes().stream()
                .filter(recipe -> getApothecary().getRecipeBook().knows(recipe.getId()))
                .toList();
    }

    public Brewer.BrewResult brew(Recipe recipe) {
        return brewer.brew(getApothecary(), recipe);
    }

    public List<Ingredient> marketWares() {
        return market.waresOf(getCurrentVillage());
    }

    public Coins priceOf(Ingredient ingredient) {
        return market.priceOf(ingredient);
    }

    public Market.PurchaseResult buy(String ingredientId) {
        return market.buy(getApothecary(), getCurrentVillage(), ingredientId);
    }

    // --- progress -----------------------------------------------------------

    public boolean isWon() {
        return getApothecary().getReputation().getTier() == ReputationTier.LUMINARE;
    }

    private void refreshWaitingPatients() {
        waitingPatients.clear();
        waitingPatients.addAll(patientGenerator.generate(getCurrentVillage(), WAITING_PATIENTS));
    }
}
