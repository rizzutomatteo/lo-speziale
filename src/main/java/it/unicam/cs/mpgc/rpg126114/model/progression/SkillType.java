package it.unicam.cs.mpgc.rpg126114.model.progression;

/**
 * The three crafts an apothecary improves over a career.
 */
public enum SkillType {

    DIAGNOSI("Diagnosi", "Leggere i sintomi e gli umori di un paziente"),
    ERBORISTERIA("Erboristeria", "Riconoscere e raccogliere gli ingredienti"),
    DISTILLAZIONE("Distillazione", "Comporre rimedi a partire dagli ingredienti");

    private final String displayName;
    private final String description;

    SkillType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
