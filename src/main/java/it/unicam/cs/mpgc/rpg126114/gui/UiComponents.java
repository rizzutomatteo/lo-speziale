package it.unicam.cs.mpgc.rpg126114.gui;

import it.unicam.cs.mpgc.rpg126114.model.ailment.Severity;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * Small factory of styled nodes shared by the screens, so the look of tags,
 * badges and spacers is defined once.
 */
final class UiComponents {

    private UiComponents() {
    }

    static Label label(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        return label;
    }

    static Label tag(String text) {
        return label(text, "tag");
    }

    static Label severityBadge(Severity severity) {
        Label badge = label(severity.getDisplayName(), "badge");
        badge.getStyleClass().add(severityStyleClass(severity));
        return badge;
    }

    static String severityStyleClass(Severity severity) {
        return switch (severity) {
            case LIEVE -> "badge-lieve";
            case MODERATA -> "badge-moderata";
            case GRAVE -> "badge-grave";
            case CRITICA -> "badge-critica";
        };
    }

    static Region horizontalSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }
}
