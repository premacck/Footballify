package life.plank.juna.zone.data.network.model;

import java.util.List;

import life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection;
import lombok.Data;

@Data
public class SectionedFixture {
    FixtureSection section;
    String matchday;
    List<ScoreFixtureModel> scoreFixtureModelList;

    private SectionedFixture(FixtureSection section, List<ScoreFixtureModel> scoreFixtureModelList) {
        this.section = section;
        this.scoreFixtureModelList = scoreFixtureModelList;
    }

    private SectionedFixture(String matchday, FixtureSection section, List<ScoreFixtureModel> scoreFixtureModelList) {
        this.matchday = matchday;
        this.section = section;
        this.scoreFixtureModelList = scoreFixtureModelList;
    }

    public static SectionedFixture getFrom(FixtureSection classification, List<ScoreFixtureModel> scoreFixtureModelList) {
        return new SectionedFixture(classification, scoreFixtureModelList);
    }

    public static SectionedFixture getFrom(String matchday, FixtureSection section, List<ScoreFixtureModel> scoreFixtureModelList) {
        return new SectionedFixture(matchday, section, scoreFixtureModelList);
    }
}