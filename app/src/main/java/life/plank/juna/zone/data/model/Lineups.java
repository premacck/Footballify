package life.plank.juna.zone.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Lineups implements Parcelable {
    private List<List<Formation>> homeTeamFormation;
    private List<List<Formation>> awayTeamFormation;
    private String homeTeamName;
    private String visitingTeamName;
    private String homeManagerName;
    private String visitingManagerName;
    private int errorMessage;

    protected Lineups(Parcel in) {
        homeTeamName = in.readString();
        visitingTeamName = in.readString();
        homeManagerName = in.readString();
        visitingManagerName = in.readString();
        errorMessage = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(homeTeamName);
        dest.writeString(visitingTeamName);
        dest.writeString(homeManagerName);
        dest.writeString(visitingManagerName);
        dest.writeInt(errorMessage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Lineups> CREATOR = new Creator<Lineups>() {
        @Override
        public Lineups createFromParcel(Parcel in) {
            return new Lineups(in);
        }

        @Override
        public Lineups[] newArray(int size) {
            return new Lineups[size];
        }
    };

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

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public String getVisitingTeamName() {
        return visitingTeamName;
    }

    public void setVisitingTeamName(String visitingTeamName) {
        this.visitingTeamName = visitingTeamName;
    }

    public String getHomeManagerName() {
        return homeManagerName;
    }

    public void setHomeManagerName(String homeManagerName) {
        this.homeManagerName = homeManagerName;
    }

    public String getVisitingManagerName() {
        return visitingManagerName;
    }

    public void setVisitingManagerName(String visitingManagerName) {
        this.visitingManagerName = visitingManagerName;
    }

    public int getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(int errorMessage) {
        this.errorMessage = errorMessage;
    }
}