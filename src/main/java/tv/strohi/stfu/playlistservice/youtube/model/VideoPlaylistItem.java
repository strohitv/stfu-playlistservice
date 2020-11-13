package tv.strohi.stfu.playlistservice.youtube.model;

public class VideoPlaylistItem {
    private PlaylistSnippet snippet;

    public VideoPlaylistItem() {
    }

    public VideoPlaylistItem(PlaylistSnippet snippet) {
        this.snippet = snippet;
    }

    public VideoPlaylistItem(String playlistId, String videoId) {
        this.snippet = new PlaylistSnippet(playlistId, new VideoResource(videoId));
    }

    public PlaylistSnippet getSnippet() {
        return snippet;
    }

    public void setSnippet(PlaylistSnippet snippet) {
        this.snippet = snippet;
    }

    @Override
    public String toString() {
        return "VideoPlaylistItem {" +
                "snippet=" + snippet +
                '}';
    }

    public static class PlaylistSnippet {
       private String playlistId;
       private VideoResource resourceId;

        public PlaylistSnippet() {
        }

        public PlaylistSnippet(String playlistId, VideoResource resourceId) {
            this.playlistId = playlistId;
            this.resourceId = resourceId;
        }

        public String getPlaylistId() {
            return playlistId;
        }

        public void setPlaylistId(String playlistId) {
            this.playlistId = playlistId;
        }

        public VideoResource getResourceId() {
            return resourceId;
        }

        public void setResourceId(VideoResource resourceId) {
            this.resourceId = resourceId;
        }

        @Override
        public String toString() {
            return "PlaylistSnippet {" +
                    "playlistId='" + playlistId + '\'' +
                    ", resourceId=" + resourceId +
                    '}';
        }
    }

    public static class VideoResource {
        private String kind = "youtube#video";
        private String videoId;

        public VideoResource() {
        }

        public VideoResource(String videoId) {
            this.videoId = videoId;
        }

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

        @Override
        public String toString() {
            return "VideoResource {" +
                    "kind='" + kind + '\'' +
                    ", videoId='" + videoId + '\'' +
                    '}';
        }
    }
}
