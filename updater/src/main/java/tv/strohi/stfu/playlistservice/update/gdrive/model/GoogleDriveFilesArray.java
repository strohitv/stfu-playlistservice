package tv.strohi.stfu.playlistservice.update.gdrive.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleDriveFilesArray {
    private GoogleDriveFile[] files;

    public GoogleDriveFilesArray() {
    }

    public GoogleDriveFilesArray(GoogleDriveFile[] files) {
        this.files = files;
    }

    public GoogleDriveFile[] getFiles() {
        return files;
    }

    public void setFiles(GoogleDriveFile[] files) {
        this.files = files;
    }
}
