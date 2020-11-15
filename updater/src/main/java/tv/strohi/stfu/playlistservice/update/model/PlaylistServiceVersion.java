package tv.strohi.stfu.playlistservice.update.model;

import java.net.URL;

public class PlaylistServiceVersion {
    private String name;
    private boolean isPreview;
    private URL downloadPath;

    public PlaylistServiceVersion() {
    }

    public PlaylistServiceVersion(String name, boolean isPreview, URL downloadPath) {
        this.name = name;
        this.isPreview = isPreview;
        this.downloadPath = downloadPath;
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
}
