package it.unicam.cs.mpgc.rpg126114.model.ailment;

import it.unicam.cs.mpgc.rpg126114.model.AbstractEntity;
import it.unicam.cs.mpgc.rpg126114.model.humor.HumoralBalance;
import it.unicam.cs.mpgc.rpg126114.util.Preconditions;

import java.util.List;

/**
 * The static definition of an illness: the humoral imbalance that characterises
 * it, the symptoms it can produce and how quickly it worsens when left untreated.
 *
 * <p>An {@code Ailment} is a piece of immutable content; the changing state of a
 * sick person lives in {@link Affliction}.
 */
public final class Ailment extends AbstractEntity {

    private final String name;
    private final String description;
    private final HumoralBalance signature;
    private final List<Symptom> symptoms;
    private final int virulence;

    public Ailment(String id, String name, String description,
                   HumoralBalance signature, List<Symptom> symptoms, int virulence) {
        super(id);
        this.name = Preconditions.requireNonBlank(name, "name");
        this.description = Preconditions.requireNonBlank(description, "description");
        this.signature = Preconditions.requireNonNull(signature, "signature");
        this.symptoms = List.copyOf(Preconditions.requireNonNull(symptoms, "symptoms"));
        this.virulence = Preconditions.requireInRange(virulence, 1, 5, "virulence");
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /** The humoral imbalance a freshly afflicted body starts from. */
    public HumoralBalance getSignature() {
        return signature;
    }

    /** The symptoms this ailment may manifest, depending on the humoral state. */
    public List<Symptom> getSymptoms() {
        return symptoms;
    }

    /** How fast the imbalance deepens each day if the body is not treated. */
    public int getVirulence() {
        return virulence;
    }
}
