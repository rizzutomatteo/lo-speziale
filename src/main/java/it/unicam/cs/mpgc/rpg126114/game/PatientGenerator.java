package it.unicam.cs.mpgc.rpg126114.game;

import it.unicam.cs.mpgc.rpg126114.content.ContentRepository;
import it.unicam.cs.mpgc.rpg126114.model.ailment.Affliction;
import it.unicam.cs.mpgc.rpg126114.model.ailment.Ailment;
import it.unicam.cs.mpgc.rpg126114.model.character.Patient;
import it.unicam.cs.mpgc.rpg126114.model.world.Village;
import it.unicam.cs.mpgc.rpg126114.util.Preconditions;
import it.unicam.cs.mpgc.rpg126114.util.RandomSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Produces the sick villagers an apothecary meets on the road. The ailment is
 * drawn from those common to the village, so each place feels different.
 */
public final class PatientGenerator {

    private static final List<String> NAMES = List.of(
            "Bianca", "Corrado", "Mafalda", "Teobaldo", "Ginevra", "Aldo", "Ermellina", "Baldo",
            "Rosa", "Gualtiero", "Cosimo", "Assunta", "Bertoldo", "Nuccia", "Manfredo", "Imelda");

    private final ContentRepository content;
    private final RandomSource random;
    private int sequence;

    public PatientGenerator(ContentRepository content, RandomSource random) {
        this.content = Preconditions.requireNonNull(content, "content");
        this.random = Preconditions.requireNonNull(random, "random");
    }

    public Patient generateFor(Village village) {
        Preconditions.requireNonNull(village, "village");
        Ailment ailment = content.getAilment(random.pick(village.getLocalAilments()));
        String name = random.pick(NAMES);
        int age = 12 + random.nextInt(64);
        String id = "paziente-" + (++sequence);
        return new Patient(id, name, age, village.getName(), new Affliction(ailment));
    }

    public List<Patient> generate(Village village, int count) {
        Preconditions.require(count >= 0, "count must not be negative");
        List<Patient> patients = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            patients.add(generateFor(village));
        }
        return patients;
    }
}
