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
 * Created by plank-sharath on 2/21/2018.
 */
@Data
public class ScoreFixture {
    private String header;
    private String rounds;
    private String teamOne;
    private String teamTwo;
    private String time;
    private String tag;

    public ScoreFixture(String tag, String header, String rounds, String teamOne, String teamTwo, String time) {
        this.header = header;
        this.rounds = rounds;
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;
        this.time = time;
        this.tag = tag;
    }

    public static List<ScoreFixture> getFixture(Context context) {
        List<ScoreFixture> fixtureModelList = new ArrayList<>();
        fixtureModelList.add(new ScoreFixture("header", context.getString(R.string.header_fa_cup), context.getString(R.string.round_fa_cup), context.getString(R.string.team_one_fa_cup), context.getString(R.string.team_two_fa_cup), "" + context.getString(R.string.time_fa_cup)));
        fixtureModelList.add(new ScoreFixture("body", context.getString(R.string.header_fa_cup), context.getString(R.string.round_fa_cup), context.getString(R.string.team_one_fa_cup), context.getString(R.string.team_two_fa_cup), "" + context.getString(R.string.time_fa_cup)));
        fixtureModelList.add(new ScoreFixture("header", context.getString(R.string.header_league), " ", context.getString(R.string.team_one_league), context.getString(R.string.team_two_league), context.getString(R.string.time_league)));
        fixtureModelList.add(new ScoreFixture("body", context.getString(R.string.header_league), " ", context.getString(R.string.team_one_league), context.getString(R.string.team_two_league), context.getString(R.string.time_league)));
        fixtureModelList.add(new ScoreFixture("body", context.getString(R.string.header_south), " ", context.getString(R.string.team_one_south), context.getString(R.string.team_two_south), context.getString(R.string.time_south)));
        fixtureModelList.add(new ScoreFixture("header", context.getString(R.string.header_fa_cup), context.getString(R.string.round_fa_cup), context.getString(R.string.team_one_fa_cup), context.getString(R.string.team_two_fa_cup), "" + context.getString(R.string.time_fa_cup)));
        fixtureModelList.add(new ScoreFixture("body", context.getString(R.string.header_league), " ", context.getString(R.string.team_one_league), context.getString(R.string.team_two_league), context.getString(R.string.time_league)));
        fixtureModelList.add(new ScoreFixture("body", context.getString(R.string.header_south), " ", context.getString(R.string.team_one_south), context.getString(R.string.team_two_south), context.getString(R.string.time_south)));
        return fixtureModelList;
    }

}
