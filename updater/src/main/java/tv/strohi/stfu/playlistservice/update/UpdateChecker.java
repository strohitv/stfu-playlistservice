package tv.strohi.stfu.playlistservice.update;

import tv.strohi.stfu.playlistservice.update.gdrive.VersionLoader;
import tv.strohi.stfu.playlistservice.update.model.PlaylistServiceVersion;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import static tv.strohi.stfu.playlistservice.update.model.PlaylistServiceVersion.getVersionArray;

public class UpdateChecker {
    private final VersionLoader loader;
    private final String parentDir;
    private final PlaylistServiceVersion currentVersion;

    public UpdateChecker(String key, String parentDir, String currentVersion) {
        this.loader = new VersionLoader(key);
        this.parentDir = parentDir;
        this.currentVersion = new PlaylistServiceVersion(currentVersion, currentVersion.toUpperCase().contains("SNAPSHOT"), null, getVersionArray(currentVersion));
    }

    public String downloadUpdate(boolean downloadPreviewUpdates) {
        try {
            PlaylistServiceVersion[] versions = sortVersions(loader.getServiceVersions());
            PlaylistServiceVersion versionToDownload = Arrays.stream(versions)
                    .filter(v -> v.isNewerThan(currentVersion))
                    .filter(v -> downloadPreviewUpdates || !v.isPreview())
                    .findFirst()
                    .orElse(null);

            if (versionToDownload != null) {
                String zipPath = Path.of(parentDir, versionToDownload.getName()).normalize().toString();
                loader.download(versionToDownload, zipPath);
                return zipPath;
            }
        } catch (IOException e) {
            // do nothing
        }

        return null;
    }

    private PlaylistServiceVersion[] sortVersions(PlaylistServiceVersion[] versions) {
        for (int i = 0; i < versions.length - 1; i++) {
            for (int j = 0; j < versions.length - 1; j++) {
                if (versions[j + 1].isNewerThan(versions[j])) {
                    PlaylistServiceVersion save = versions[j];
                    versions[j] = versions[j + 1];
                    versions[j + 1] = save;
                }
            }
        }

        return versions;
    }
}
