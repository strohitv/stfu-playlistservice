package tv.strohi.stfu.playlistservice.update;

import tv.strohi.stfu.playlistservice.update.gdrive.VersionLoader;
import tv.strohi.stfu.playlistservice.update.model.PlaylistServiceVersion;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class Main {
    public static void main(String... args) throws IOException, URISyntaxException {
        System.out.println("HALLOWELT DAS LEEFT JA SUPPER!!!");

        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                for (int k = 0; k < 1000; k++) {
                    int result = i + j + k;
                    System.out.printf("%d - %d - %d - %d%n", i, j, k, result);
                }
            }
        }

        byte[] test = System.in.readAllBytes();
    }
}
