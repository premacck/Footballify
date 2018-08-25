package life.plank.juna.zone.data.network.model;

import java.util.List;

import lombok.Data;

@Data
public class SectionedFixtureMatchDay {
    String matchday;
    List<SectionedFixtureDate> sectionedFixtureDateList;

    private SectionedFixtureMatchDay(String matchday, List<SectionedFixtureDate> sectionedFixtureDateList) {
        this.matchday = matchday;
        this.sectionedFixtureDateList = sectionedFixtureDateList;
    }

    public static SectionedFixtureMatchDay getFrom(String matchday, List<SectionedFixtureDate> sectionedFixtureDateList) {
        return new SectionedFixtureMatchDay(matchday, sectionedFixtureDateList);
    }
}