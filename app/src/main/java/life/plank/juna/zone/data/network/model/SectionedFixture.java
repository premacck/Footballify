package life.plank.juna.zone.data.network.model;

import java.util.List;

import life.plank.juna.zone.domain.service.FootballFixtureClassifierService;
import lombok.Data;

@Data
public class SectionedFixture {
    FootballFixtureClassifierService.FixtureClassification classification;
    List<ScoreFixtureModel> scoreFixtureModelList;

    public SectionedFixture(FootballFixtureClassifierService.FixtureClassification classification, List<ScoreFixtureModel> scoreFixtureModelList) {
        this.classification = classification;
        this.scoreFixtureModelList = scoreFixtureModelList;
    }
}