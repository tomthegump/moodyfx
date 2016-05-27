package sample.data;

import java.util.Date;

/**
 * Created by Ingo on 21.05.2016.
 */
public class Vote {

    private final Date timestamp;
    private final int surveyId;
    private final int votedPoints;
    private final String site;

    public Vote(int surveyId, int votedPoints, String site) {
        this(surveyId, votedPoints, site, new Date());
    }

    public Vote(int surveyId, int votedPoints, String site, Date timestamp) {
        this.timestamp = timestamp;
        this.surveyId = surveyId;
        this.votedPoints = votedPoints;
        this.site = site;
    }

    public String getSite() {
        return site;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public int getVotedPoints() {
        return votedPoints;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "timestamp=" + timestamp +
                ", surveyId=" + surveyId +
                ", votedPoints=" + votedPoints +
                ", site='" + site + '\'' +
                '}';
    }
}
