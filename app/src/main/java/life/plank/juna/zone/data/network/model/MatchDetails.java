package life.plank.juna.zone.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class MatchDetails {
    private Integer id;
    @SerializedName("foreignId")
    @Expose
    private Long matchId;
    private FootballTeam homeTeam;
    private FootballTeam awayTeam;
    private Integer matchDay;
    private Integer homeGoals;
    private Integer awayGoals;
    private String hometeamFormation;
    private String awayteamFormation;
    private Integer homeTeamScore;
    private Integer awayTeamScore;
    private Integer homeTeamPenaltyScore;
    private Integer awayTeamPenaltyScore;
    private String halfTimeScore;
    private String fullTimeScore;
    private String timeStatus;
    private Integer minute;
    private Integer extraMinute;
    private Integer injuryTime;
    private Date matchStartTime;

    private League league;
    private Stadium venue;
    private List<Highlights> highlights;
    private List<MatchEvent> matchEvents;
    private List<Commentary> commentary;
    @SerializedName("commentaries")
    @Expose
    private boolean isCommentaryAvailable;
    private MatchStats matchStats;
    private Lineups lineups;
    private List<StandingModel> standingsList;
    private List<TeamStatsModel> teamStatsList;
    private List<ScrubberData> scrubberDataList;
}
