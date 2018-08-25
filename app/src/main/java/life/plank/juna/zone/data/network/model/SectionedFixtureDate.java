package life.plank.juna.zone.data.network.model;

import java.util.List;

import life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection;
import lombok.Data;

@Data
public class SectionedFixtureDate {
    private String date;
    private FixtureSection section;
    private List<ScoreFixture> scoreFixtureList;

    private SectionedFixtureDate(String date, FixtureSection section, List<ScoreFixture> scoreFixtureList) {
        this.date = date;
        this.section = section;
        this.scoreFixtureList = scoreFixtureList;
    }

    public void add(SectionedFixtureDate sectionedFixtureDate) {
        this.date = sectionedFixtureDate.date;
        this.scoreFixtureList = sectionedFixtureDate.scoreFixtureList;
    }

    public static SectionedFixtureDate getFrom(String date, FixtureSection section, List<ScoreFixture> scoreFixtureList) {
        return new SectionedFixtureDate(date, section, scoreFixtureList);
    }
}