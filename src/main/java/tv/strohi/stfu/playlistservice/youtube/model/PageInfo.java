package tv.strohi.stfu.playlistservice.youtube.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PageInfo {
    private int totalResult;
    private int resultsPerPage;

    public PageInfo() {
    }

    public PageInfo(int totalResult, int resultsPerPage) {
        this.totalResult = totalResult;
        this.resultsPerPage = resultsPerPage;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    public int getResultsPerPage() {
        return resultsPerPage;
    }

    public void setResultsPerPage(int resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }

    @Override
    public String toString() {
        return "PageInfo {" +
                "totalResult=" + totalResult +
                ", resultsPerPage=" + resultsPerPage +
                '}';
    }
}
