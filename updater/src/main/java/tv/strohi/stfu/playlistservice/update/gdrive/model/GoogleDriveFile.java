package tv.strohi.stfu.playlistservice.update.gdrive.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.net.URL;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleDriveFile {
    private String id;
    private String name;
    private String mimeType;
    private URL webContentLink;

    public GoogleDriveFile() {
    }

    public GoogleDriveFile(String id, String name, String mimeType, URL webContentLink) {
        this.id = id;
        this.name = name;
        this.mimeType = mimeType;
        this.webContentLink = webContentLink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public URL getWebContentLink() {
        return webContentLink;
    }

    public void setWebContentLink(URL webContentLink) {
        this.webContentLink = webContentLink;
    }
}
