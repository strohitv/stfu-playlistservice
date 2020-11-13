package tv.strohi.stfu.playlistservice.config;

import org.apache.logging.log4j.Level;

public class ServiceSettings {
    private short port = 10800;
    private String user = "";
    private String password = "";
    private boolean checkForUpdatesAtStartup = true;
    private boolean checkForUpdatesEach24h = true;
    private boolean downloadPreviewUpdates = false;
    private Level loglevelRoot = Level.INFO;
    private Level loglevelService = Level.INFO;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean checkForUpdatesAtStartup() {
        return checkForUpdatesAtStartup;
    }

    public void setCheckForUpdatesAtStartup(boolean checkForUpdatesAtStartup) {
        this.checkForUpdatesAtStartup = checkForUpdatesAtStartup;
    }

    public boolean checkForUpdatesEach24h() {
        return checkForUpdatesEach24h;
    }

    public void setCheckForUpdatesEach24h(boolean checkForUpdatesEach24h) {
        this.checkForUpdatesEach24h = checkForUpdatesEach24h;
    }

    public boolean downloadPreviewUpdates() {
        return downloadPreviewUpdates;
    }

    public void setDownloadPreviewUpdates(boolean downloadPreviewUpdates) {
        this.downloadPreviewUpdates = downloadPreviewUpdates;
    }

    public Level getLoglevelRoot() {
        return loglevelRoot;
    }

    public void setLoglevelRoot(Level loglevelRoot) {
        this.loglevelRoot = loglevelRoot;
    }

    public Level getLoglevelService() {
        return loglevelService;
    }

    public void setLoglevelService(Level loglevelService) {
        this.loglevelService = loglevelService;
    }

    @Override
    public String toString() {
        return "ServiceSettings {" +
                "port=" + port +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", checkForUpdatesAtStartup=" + checkForUpdatesAtStartup +
                ", checkForUpdatesEach24h=" + checkForUpdatesEach24h +
                ", downloadPreviewUpdates=" + downloadPreviewUpdates +
                ", loglevelRoot=" + loglevelRoot +
                ", loglevelService=" + loglevelService +
                '}';
    }
}
