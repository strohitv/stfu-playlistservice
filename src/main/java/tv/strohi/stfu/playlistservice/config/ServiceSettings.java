package tv.strohi.stfu.playlistservice.config;

public class ServiceSettings {
    private short port = 10800;
    private String user = "";
    private String pass = "";
    private boolean checkForUpdatesAtStartup = true;
    private boolean checkForUpdatesEach24h = true;
    private boolean checkForPreviewUpdates = false;
    private String loglevel = "INFO";

    public ServiceSettings() {
    }

    public short getPort() {
        return port;
    }

    public void setPort(short port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public boolean isCheckForUpdatesAtStartup() {
        return checkForUpdatesAtStartup;
    }

    public void setCheckForUpdatesAtStartup(boolean checkForUpdatesAtStartup) {
        this.checkForUpdatesAtStartup = checkForUpdatesAtStartup;
    }

    public boolean isCheckForUpdatesEach24h() {
        return checkForUpdatesEach24h;
    }

    public void setCheckForUpdatesEach24h(boolean checkForUpdatesEach24h) {
        this.checkForUpdatesEach24h = checkForUpdatesEach24h;
    }

    public boolean isCheckForPreviewUpdates() {
        return checkForPreviewUpdates;
    }

    public void setCheckForPreviewUpdates(boolean checkForPreviewUpdates) {
        this.checkForPreviewUpdates = checkForPreviewUpdates;
    }

    public String getLoglevel() {
        return loglevel;
    }

    public void setLoglevel(String loglevel) {
        this.loglevel = loglevel;
    }
}
