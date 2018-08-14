package life.plank.juna.zone.data.network.model;

import java.util.List;

import life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection;
import lombok.Data;

@Data
public class SectionedFixture {
    FixtureSection section;
    List<ScoreFixtureModel> scoreFixtureModelList;

    private SectionedFixture(FixtureSection section, List<ScoreFixtureModel> scoreFixtureModelList) {
        this.section = section;
        this.scoreFixtureModelList = scoreFixtureModelList;
    }

    public static SectionedFixture getFrom(FixtureSection classification, List<ScoreFixtureModel> scoreFixtureModelList) {
        return new SectionedFixture(classification, scoreFixtureModelList);
    }
}