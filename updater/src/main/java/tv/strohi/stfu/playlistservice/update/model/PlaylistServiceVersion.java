package tv.strohi.stfu.playlistservice.update.model;

import java.net.URL;
import java.util.Arrays;

public class PlaylistServiceVersion {
    private String name;
    private boolean isPreview;
    private URL downloadPath;
    private int[] versions;

    public PlaylistServiceVersion() {
    }

    public PlaylistServiceVersion(String name, boolean isPreview, URL downloadPath, int[] versions) {
        this.name = name;
        this.isPreview = isPreview;
        this.downloadPath = downloadPath;
        this.versions = versions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPreview() {
        return isPreview;
    }

    public void setPreview(boolean preview) {
        isPreview = preview;
    }

    public URL getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(URL downloadPath) {
        this.downloadPath = downloadPath;
    }

    public int[] getVersions() {
        return versions;
    }

    public void setVersions(int[] versions) {
        this.versions = versions;
    }

    public boolean isNewerThan(PlaylistServiceVersion other) {
        int maxLength = Math.min(other.versions.length, versions.length);

        for (int i = 0; i < maxLength; i++) {
            if (other.versions[i] != versions[i]) {
                return versions[i] > other.versions[i];
            }
        }

        return versions.length > other.versions.length || (!isPreview && other.isPreview);
    }

    public static int[] getVersionArray(String name) {
        return Arrays.stream(name.split("-"))
                .filter(parts -> parts.replaceAll("[0-9.]", "").equals(""))
                .flatMap(s -> Arrays.stream(s.split("\\.").clone()))
                .mapToInt(Integer::parseInt)
                .toArray();
    }
}
