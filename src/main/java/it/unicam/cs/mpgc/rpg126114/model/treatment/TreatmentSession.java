package it.unicam.cs.mpgc.rpg126114.model.treatment;

import it.unicam.cs.mpgc.rpg126114.model.ailment.Affliction;
import it.unicam.cs.mpgc.rpg126114.model.ailment.Symptom;
import it.unicam.cs.mpgc.rpg126114.model.character.Apothecary;
import it.unicam.cs.mpgc.rpg126114.model.character.Patient;
import it.unicam.cs.mpgc.rpg126114.model.progression.SkillType;
import it.unicam.cs.mpgc.rpg126114.util.Preconditions;
import it.unicam.cs.mpgc.rpg126114.util.RandomSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

/**
 * Runs a single treatment as a sequence of turns.
 *
 * <p>The session is the only object that drives time forward and decides the
 * outcome. Each turn the apothecary performs one {@link TreatmentAction}; the
 * session then lets the illness progress — worsening while the body is unbalanced,
 * recovering once it is stable — spends a unit of the patient's patience and checks
 * whether the treatment has concluded. Observers are notified after every turn.
 */
public final class TreatmentSession implements TreatmentContext {

    /** How close every humour must be to the ideal for the patient to be healed. */
    public static final int CURE_TOLERANCE = 1;

    /** Turns of patience a patient grants before they give up on the treatment. */
    public static final int INITIAL_PATIENCE = 8;

    private final Patient patient;
    private final Apothecary apothecary;
    private final RandomSource random;
    private final List<String> journal = new ArrayList<>();
    private final List<TreatmentObserver> observers = new ArrayList<>();

    private int patience = INITIAL_PATIENCE;
    private int turn;
    private boolean diagnosed;
    private TreatmentOutcome outcome = TreatmentOutcome.IN_CORSO;

    public TreatmentSession(Patient patient, Apothecary apothecary, RandomSource random) {
        this.patient = Preconditions.requireNonNull(patient, "patient");
        this.apothecary = Preconditions.requireNonNull(apothecary, "apothecary");
        this.random = Preconditions.requireNonNull(random, "random");
        journal.add(describePresentation());
    }

    /**
     * Performs the chosen action, advances the illness and re-evaluates the
     * outcome. Has no effect once the treatment is over.
     */
    public void perform(TreatmentAction action) {
        Preconditions.requireNonNull(action, "action");
        if (isOver()) {
            return;
        }
        action.perform(this);
        advanceIllness();
        turn++;
        patience--;
        evaluateOutcome();
        notifyObservers(journal.isEmpty() ? "" : journal.get(journal.size() - 1));
    }

    private void advanceIllness() {
        Affliction affliction = getAffliction();
        if (affliction.isStable(CURE_TOLERANCE)) {
            affliction.recover(1);
            affliction.reduceToxicity(1);
        } else {
            affliction.worsen(worseningStep());
        }
    }

    private int worseningStep() {
        return getAffliction().getAilment().getVirulence() >= 4 ? 2 : 1;
    }

    private void evaluateOutcome() {
        Affliction affliction = getAffliction();
        if (affliction.isCured(CURE_TOLERANCE)) {
            outcome = TreatmentOutcome.GUARITO;
            journal.add(patient.getName() + " si è ristabilito. Hai avuto la meglio sul male.");
        } else if (affliction.isFatal()) {
            outcome = TreatmentOutcome.DECEDUTO;
            journal.add("Le forze abbandonano " + patient.getName() + ". Non c'era più nulla da fare.");
        } else if (patience <= 0) {
            outcome = TreatmentOutcome.ABBANDONATO;
            journal.add(patient.getName() + " perde fiducia e se ne va in cerca di un altro guaritore.");
        }
    }

    private String describePresentation() {
        StringJoiner symptoms = new StringJoiner(", ");
        for (Symptom symptom : getAffliction().getActiveSymptoms()) {
            symptoms.add(symptom.getName());
        }
        String opening = patient.getName() + ", " + patient.getAge()
                + " anni, da " + patient.getOrigin() + ", si presenta sofferente.";
        return symptoms.length() == 0
                ? opening + " Non mostra sintomi evidenti."
                : opening + " Sintomi: " + symptoms + ".";
    }

    public void addObserver(TreatmentObserver observer) {
        observers.add(Preconditions.requireNonNull(observer, "observer"));
    }

    public void removeObserver(TreatmentObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(String message) {
        for (TreatmentObserver observer : observers) {
            observer.onUpdate(this, message);
        }
    }

    public int getTurn() {
        return turn;
    }

    public int getPatience() {
        return patience;
    }

    public boolean isDiagnosed() {
        return diagnosed;
    }

    public TreatmentOutcome getOutcome() {
        return outcome;
    }

    public boolean isOver() {
        return outcome.isTerminal();
    }

    public List<String> getJournal() {
        return Collections.unmodifiableList(journal);
    }

    // --- TreatmentContext ---------------------------------------------------

    @Override
    public Patient getPatient() {
        return patient;
    }

    @Override
    public Affliction getAffliction() {
        return patient.getAffliction();
    }

    @Override
    public Apothecary getApothecary() {
        return apothecary;
    }

    @Override
    public RandomSource getRandom() {
        return random;
    }

    @Override
    public void log(String message) {
        journal.add(Preconditions.requireNonBlank(message, "message"));
    }

    @Override
    public void grantExperience(SkillType skill, int amount) {
        apothecary.train(skill, amount);
    }

    @Override
    public void restorePatience(int amount) {
        Preconditions.require(amount >= 0, "amount must not be negative");
        patience += amount;
    }

    @Override
    public void revealDiagnosis() {
        diagnosed = true;
    }
}
