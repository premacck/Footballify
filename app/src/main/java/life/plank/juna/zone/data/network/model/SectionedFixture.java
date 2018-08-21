package life.plank.juna.zone.data.network.model;

import java.util.List;

import life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection;
import lombok.Data;

@Data
public class SectionedFixture {
    FixtureSection section;
    String matchday;
    List<ScoreFixture> scoreFixtureList;

    private SectionedFixture(FixtureSection section, List<ScoreFixture> scoreFixtureList) {
        this.section = section;
        this.scoreFixtureList = scoreFixtureList;
    }

    private SectionedFixture(String matchday, FixtureSection section, List<ScoreFixture> scoreFixtureList) {
        this.matchday = matchday;
        this.section = section;
        this.scoreFixtureList = scoreFixtureList;
    }

    public static SectionedFixture getFrom(FixtureSection classification, List<ScoreFixture> scoreFixtureList) {
        return new SectionedFixture(classification, scoreFixtureList);
    }

    public static SectionedFixture getFrom(String matchday, FixtureSection section, List<ScoreFixture> scoreFixtureList) {
        return new SectionedFixture(matchday, section, scoreFixtureList);
    }
}