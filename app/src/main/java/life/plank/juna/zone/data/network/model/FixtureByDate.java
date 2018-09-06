package life.plank.juna.zone.data.network.model;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class FixtureByDate {
    private Date date;
    private List<ScoreFixture> fixtures;

    private FixtureByDate(Date date, List<ScoreFixture> fixtures) {
        this.date = date;
        this.fixtures = fixtures;
    }

    public static FixtureByDate getFrom(Date date, List<ScoreFixture> scoreFixtureList) {
        return new FixtureByDate(date, scoreFixtureList);
    }
}