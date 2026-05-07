package it.unicam.cs.mpgc.rpg126114.util;

/**
 * Small collection of argument-checking helpers used to keep constructors and
 * public methods free of repetitive validation code.
 */
public final class Preconditions {

    private Preconditions() {
    }

    /**
     * Ensures that {@code value} is not {@code null}.
     *
     * @return the value itself, so the call can be used inline
     */
    public static <T> T requireNonNull(T value, String name) {
        if (value == null) {
            throw new NullPointerException(name + " must not be null");
        }
        return value;
    }

    /**
     * Ensures that {@code condition} holds, throwing {@link IllegalArgumentException}
     * with the given message otherwise.
     */
    public static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Ensures that {@code value} is neither {@code null} nor blank.
     *
     * @return the value itself, so the call can be used inline
     */
    public static String requireNonBlank(String value, String name) {
        requireNonNull(value, name);
        if (value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        return value;
    }

    /**
     * Ensures that {@code value} lies within the inclusive range {@code [min, max]}.
     *
     * @return the value itself, so the call can be used inline
     */
    public static int requireInRange(int value, int min, int max, String name) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(
                    name + " must be in [" + min + ", " + max + "] but was " + value);
        }
        return value;
    }
}
