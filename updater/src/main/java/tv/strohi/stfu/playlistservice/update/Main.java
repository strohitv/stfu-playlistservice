package tv.strohi.stfu.playlistservice.update;

import tv.strohi.stfu.playlistservice.update.gdrive.VersionLoader;
import tv.strohi.stfu.playlistservice.update.model.PlaylistServiceVersion;

import java.io.IOException;

public class Main {
    public static void main(String... args) throws IOException {
        PlaylistServiceVersion[] versions = new VersionLoader("AIzaSyC8l0tHuvhFLU6HtzHXwW2Nraf-ARMm1wE").getServiceVersions();
        System.out.println(versions);
    }
}
