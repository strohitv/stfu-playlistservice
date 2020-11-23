package tv.strohi.stfu.playlistservice.update.utils;

@FunctionalInterface
public interface Condition {
    boolean applies(String str);
}
