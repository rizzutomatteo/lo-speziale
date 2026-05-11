package it.unicam.cs.mpgc.rpg126114.model.ailment;

import it.unicam.cs.mpgc.rpg126114.model.humor.Humor;
import it.unicam.cs.mpgc.rpg126114.model.humor.HumoralBalance;
import it.unicam.cs.mpgc.rpg126114.util.Preconditions;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The changing medical state of a sick person while they are being treated.
 *
 * <p>This is the object every treatment action and remedy effect works on. The
 * set of manifest symptoms is always derived from the current humoral state, so
 * bringing the humors back toward balance is what makes the symptoms fade.
 */
public final class Affliction {

    /** A symptom shows once its humor deviates from the ideal by at least this much. */
    private static final int SYMPTOM_THRESHOLD = 2;
    private static final int DEATH_SCORE = 20;
    private static final int MAX_TOXICITY = 10;
    private static final int SAFE_TOXICITY = 2;

    private final Ailment ailment;
    private HumoralBalance balance;
    private int toxicity;
    private final Set<Symptom> activeSymptoms = new LinkedHashSet<>();

    public Affliction(Ailment ailment) {
        this.ailment = Preconditions.requireNonNull(ailment, "ailment");
        this.balance = ailment.getSignature();
        refreshSymptoms();
    }

    public Ailment getAilment() {
        return ailment;
    }

    public HumoralBalance getBalance() {
        return balance;
    }

    public int getToxicity() {
        return toxicity;
    }

    public Set<Symptom> getActiveSymptoms() {
        return Collections.unmodifiableSet(activeSymptoms);
    }

    /**
     * Moves a humor by {@code delta} and recomputes the manifest symptoms.
     */
    public void shiftHumor(Humor humor, int delta) {
        balance = balance.shift(humor, delta);
        refreshSymptoms();
    }

    public void addToxicity(int amount) {
        Preconditions.require(amount >= 0, "amount must not be negative");
        toxicity = Math.min(MAX_TOXICITY, toxicity + amount);
    }

    public void reduceToxicity(int amount) {
        Preconditions.require(amount >= 0, "amount must not be negative");
        toxicity = Math.max(0, toxicity - amount);
    }

    /**
     * Pushes the most imbalanced humor further from the ideal, the way a neglected
     * illness deepens.
     */
    public void worsen(int step) {
        Humor humor = balance.mostImbalanced();
        if (balance.deviation(humor) == 0) {
            return;
        }
        int direction = balance.deviation(humor) > 0 ? 1 : -1;
        shiftHumor(humor, direction * step);
    }

    /**
     * Nudges every humor toward the ideal, the way a stabilised body recovers.
     */
    public void recover(int step) {
        for (Humor humor : Humor.values()) {
            int deviation = balance.deviation(humor);
            if (deviation != 0) {
                shiftHumor(humor, deviation > 0 ? -step : step);
            }
        }
    }

    /**
     * Aggregate gravity of the affliction, combining humoral imbalance, the weight
     * of the manifest symptoms and any toxicity built up from harsh remedies.
     */
    public int gravityScore() {
        int symptomGravity = 0;
        for (Symptom symptom : activeSymptoms) {
            symptomGravity += symptom.getGravity();
        }
        return balance.totalImbalance() + toxicity + symptomGravity;
    }

    public Severity getSeverity() {
        return Severity.fromScore(gravityScore());
    }

    /**
     * Whether every humor lies within {@code tolerance} of the ideal.
     */
    public boolean isStable(int tolerance) {
        return balance.isWithinTolerance(tolerance);
    }

    /**
     * Whether the person can be declared healed: humors within tolerance, no
     * lingering symptoms and only a harmless amount of residual toxicity.
     */
    public boolean isCured(int tolerance) {
        return isStable(tolerance) && activeSymptoms.isEmpty() && toxicity <= SAFE_TOXICITY;
    }

    public boolean isFatal() {
        return gravityScore() >= DEATH_SCORE;
    }

    private void refreshSymptoms() {
        activeSymptoms.clear();
        for (Symptom symptom : ailment.getSymptoms()) {
            if (Math.abs(balance.deviation(symptom.getHumor())) >= SYMPTOM_THRESHOLD) {
                activeSymptoms.add(symptom);
            }
        }
    }
}
