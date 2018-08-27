package life.plank.juna.zone.data.network.model;

import java.util.List;

import life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection;
import lombok.Data;

@Data
public class SectionedFixtureMatchDay {
    private FixtureSection section;
    private String matchday;
    private List<SectionedFixtureDate> sectionedFixtureDateList;

    private SectionedFixtureMatchDay(FixtureSection section, String matchday, List<SectionedFixtureDate> sectionedFixtureDateList) {
        this.section = section;
        this.matchday = matchday;
        this.sectionedFixtureDateList = sectionedFixtureDateList;
    }

    public static SectionedFixtureMatchDay getFrom(FixtureSection section, String matchday, List<SectionedFixtureDate> sectionedFixtureDateList) {
        return new SectionedFixtureMatchDay(section, matchday, sectionedFixtureDateList);
    }
}