package tv.strohi.stfu.playlistservice.utils;

public class EmtyOrNull {
    private EmtyOrNull() { }

    public static boolean nullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static boolean nullOrWhitespace(String value) {
        return value == null || value.isBlank();
    }
}
