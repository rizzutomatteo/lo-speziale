package it.unicam.cs.mpgc.rpg126114.game;

import it.unicam.cs.mpgc.rpg126114.model.treatment.TreatmentOutcome;

/**
 * The aftermath of a concluded treatment: its outcome and a sentence summarising
 * its consequences for the apothecary, ready to show on the result screen.
 */
public record TreatmentReport(TreatmentOutcome outcome, String message) {
}
