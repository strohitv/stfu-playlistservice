package tv.strohi.stfu.playlistservice.update.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaylistServiceVersionTest {
    @Test
    void isNewerThan() {
        PlaylistServiceVersion newest = new PlaylistServiceVersion("newest", false, null, new int[] {1,1,3});
        PlaylistServiceVersion oldestButRelease = new PlaylistServiceVersion("older release", false, null, new int[] {1,1,0});
        PlaylistServiceVersion oldest = new PlaylistServiceVersion("first older snapshot", true, null, new int[] {1,1,0});
        PlaylistServiceVersion oldest2 = new PlaylistServiceVersion("second older snapshot", true, null, new int[] {1,1,0});
        PlaylistServiceVersion oldestButPatch = new PlaylistServiceVersion("older snapshot with patch", true, null, new int[] {1,1,0,0});

        assertAll(
                () -> assertTrue(newest.isNewerThan(oldest)),
                () -> assertFalse(oldest.isNewerThan(newest)),
                () -> assertFalse(oldest2.isNewerThan(oldest)),
                () -> assertFalse(oldest2.isNewerThan(oldestButRelease)),
                () -> assertTrue(oldestButRelease.isNewerThan(oldest)),
                () -> assertTrue(oldestButPatch.isNewerThan(oldest))
        );
    }
}