package life.plank.juna.zone.data.network.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import life.plank.juna.zone.R;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by plank-sharath on 4/10/2018.
 */

@Data
public class MatchScore {
    private String header;
    private String rounds;
    private String homeTeam;
    private String visitingTeam;
    private String matchTime;
    private String tag;
    private String homeTeamScore;
    private String visitingTeamScore;

    public MatchScore(String tag, String header, String rounds, String homeTeam, String visitingTeam, String matchTime, String homeTeamScore, String visitingTeamScore) {
        this.header = header;
        this.rounds = rounds;
        this.homeTeam = homeTeam;
        this.visitingTeam = visitingTeam;
        this.matchTime = matchTime;
        this.tag = tag;
        this.homeTeamScore = homeTeamScore;
        this.visitingTeamScore = visitingTeamScore;
    }

    public static List<MatchScore> getScores(Context context) {
        List<MatchScore> scoresModelList = new ArrayList<>();
        scoresModelList.add(new MatchScore("header", context.getString(R.string.header_fa_cup), context.getString(R.string.round_fa_cup), context.getString(R.string.team_one_fa_cup), context.getString(R.string.team_two_fa_cup), "" + context.getString(R.string.full_match_time), "0", "1"));
        scoresModelList.add(new MatchScore("body", context.getString(R.string.header_fa_cup), context.getString(R.string.round_fa_cup), context.getString(R.string.team_one_fa_cup), context.getString(R.string.team_two_fa_cup), "" + context.getString(R.string.full_match_time), "2", "4"));
        scoresModelList.add(new MatchScore("header", context.getString(R.string.header_league), " ", context.getString(R.string.team_one_league), context.getString(R.string.team_two_league), context.getString(R.string.full_match_time), "1", "0"));
        scoresModelList.add(new MatchScore("body", context.getString(R.string.header_league), " ", context.getString(R.string.team_one_league), context.getString(R.string.team_two_league), context.getString(R.string.half_match_time), "3", "0"));
        scoresModelList.add(new MatchScore("body", context.getString(R.string.header_south), " ", context.getString(R.string.team_one_south), context.getString(R.string.team_two_south), context.getString(R.string.full_match_time), "2", "0"));
        scoresModelList.add(new MatchScore("header", context.getString(R.string.header_fa_cup), context.getString(R.string.round_fa_cup), context.getString(R.string.team_one_fa_cup), context.getString(R.string.team_two_fa_cup), "" + context.getString(R.string.full_match_time), "1", "3"));
        scoresModelList.add(new MatchScore("body", context.getString(R.string.header_league), " ", context.getString(R.string.team_one_league), context.getString(R.string.team_two_league), context.getString(R.string.full_match_time), "0", "1"));
        scoresModelList.add(new MatchScore("body", context.getString(R.string.header_south), " ", context.getString(R.string.team_one_south), context.getString(R.string.team_two_south), context.getString(R.string.full_match_time), "2", "3"));
        return scoresModelList;
    }

    protected boolean canEqual(Object other) {
        return other instanceof MatchScore;
    }

}
