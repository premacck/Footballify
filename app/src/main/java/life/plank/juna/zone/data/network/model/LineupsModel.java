package life.plank.juna.zone.data.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineupsModel {
    private List<List<Formation>> homeTeamFormation = null;
    private List<List<Formation>> awayTeamFormation = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public List<List<Formation>> getHomeTeamFormation() {
        return homeTeamFormation;
    }

    public void setHomeTeamFormation(List<List<Formation>> homeTeamFormation) {
        this.homeTeamFormation = homeTeamFormation;
    }

    public List<List<Formation>> getAwayTeamFormation() {
        return awayTeamFormation;
    }

    public void setAwayTeamFormation(List<List<Formation>> awayTeamFormation) {
        this.awayTeamFormation = awayTeamFormation;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put( name, value );
    }

    public class Formation {
        private String fullName;
        private Integer number;

        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

    }
}
