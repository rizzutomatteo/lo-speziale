package it.unicam.cs.mpgc.rpg126114.game;

import it.unicam.cs.mpgc.rpg126114.model.character.Apothecary;
import it.unicam.cs.mpgc.rpg126114.model.world.GameClock;
import it.unicam.cs.mpgc.rpg126114.util.Preconditions;

/**
 * The mutable state of a playthrough: the apothecary, the calendar, where they
 * currently are and a tally of how many people they have saved or lost.
 *
 * <p>The state holds only a reference (the village id) into the static world, so
 * it stays small and easy to persist.
 */
public final class GameState {

    private final Apothecary apothecary;
    private final GameClock clock;
    private String currentVillageId;
    private int patientsHealed;
    private int patientsLost;

    public GameState(Apothecary apothecary, GameClock clock, String currentVillageId) {
        this(apothecary, clock, currentVillageId, 0, 0);
    }

    public GameState(Apothecary apothecary, GameClock clock, String currentVillageId,
                     int patientsHealed, int patientsLost) {
        this.apothecary = Preconditions.requireNonNull(apothecary, "apothecary");
        this.clock = Preconditions.requireNonNull(clock, "clock");
        this.currentVillageId = Preconditions.requireNonBlank(currentVillageId, "currentVillageId");
        this.patientsHealed = Math.max(0, patientsHealed);
        this.patientsLost = Math.max(0, patientsLost);
    }

    public Apothecary getApothecary() {
        return apothecary;
    }

    public GameClock getClock() {
        return clock;
    }

    public String getCurrentVillageId() {
        return currentVillageId;
    }

    public void moveTo(String villageId) {
        this.currentVillageId = Preconditions.requireNonBlank(villageId, "villageId");
    }

    public int getPatientsHealed() {
        return patientsHealed;
    }

    public int getPatientsLost() {
        return patientsLost;
    }

    public void recordHealed() {
        patientsHealed++;
    }

    public void recordLost() {
        patientsLost++;
    }
}
