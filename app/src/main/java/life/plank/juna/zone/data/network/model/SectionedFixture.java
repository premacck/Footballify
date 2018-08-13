package life.plank.juna.zone.data.network.model;

import java.util.List;

import life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection;
import lombok.Data;

@Data
public class SectionedFixture {
    FixtureSection classification;
    List<ScoreFixtureModel> scoreFixtureModelList;

    private SectionedFixture(FixtureSection classification, List<ScoreFixtureModel> scoreFixtureModelList) {
        this.classification = classification;
        this.scoreFixtureModelList = scoreFixtureModelList;
    }

    public static SectionedFixture getFrom(FixtureSection classification, List<ScoreFixtureModel> scoreFixtureModelList) {
        return new SectionedFixture(classification, scoreFixtureModelList);
    }
}