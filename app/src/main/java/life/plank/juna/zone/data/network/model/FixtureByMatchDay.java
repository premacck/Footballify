package life.plank.juna.zone.data.network.model;

import java.util.List;

import lombok.Data;

@Data
public class FixtureByMatchDay {
    private String daySection;
    private int matchDay;
    private List<FixtureByDate> fixtureByDateList;

    private FixtureByMatchDay(String daySection, int matchDay, List<FixtureByDate> fixtureByDateList) {
        this.daySection = daySection;
        this.matchDay = matchDay;
        this.fixtureByDateList = fixtureByDateList;
    }
}