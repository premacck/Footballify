package life.plank.juna.zone.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Formation implements Parcelable {
    private String nickname;
    private String imagePath;
    private int number;
    private int formationNumber;
    private int yellowCard;
    private int redCard;
    private int yellowRed;
    private int goals;
    private int substituteIn;
    private int substituteOut;

    protected Formation(Parcel in) {
        nickname = in.readString();
        imagePath = in.readString();
        number = in.readInt();
        formationNumber = in.readInt();
        yellowCard = in.readInt();
        redCard = in.readInt();
        yellowRed = in.readInt();
        goals = in.readInt();
        substituteIn = in.readInt();
        substituteOut = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nickname);
        dest.writeString(imagePath);
        dest.writeInt(number);
        dest.writeInt(formationNumber);
        dest.writeInt(yellowCard);
        dest.writeInt(redCard);
        dest.writeInt(yellowRed);
        dest.writeInt(goals);
        dest.writeInt(substituteIn);
        dest.writeInt(substituteOut);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Formation> CREATOR = new Creator<Formation>() {
        @Override
        public Formation createFromParcel(Parcel in) {
            return new Formation(in);
        }

        @Override
        public Formation[] newArray(int size) {
            return new Formation[size];
        }
    };

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getFormationNumber() {
        return formationNumber;
    }

    public void setFormationNumber(int formationNumber) {
        this.formationNumber = formationNumber;
    }

    public int getYellowCard() {
        return yellowCard;
    }

    public void setYellowCard(int yellowCard) {
        this.yellowCard = yellowCard;
    }

    public int getRedCard() {
        return redCard;
    }

    public void setRedCard(int redCard) {
        this.redCard = redCard;
    }

    public int getYellowRed() {
        return yellowRed;
    }

    public void setYellowRed(int yellowRed) {
        this.yellowRed = yellowRed;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getSubstituteIn() {
        return substituteIn;
    }

    public void setSubstituteIn(int substituteIn) {
        this.substituteIn = substituteIn;
    }

    public int getSubstituteOut() {
        return substituteOut;
    }

    public void setSubstituteOut(int substituteOut) {
        this.substituteOut = substituteOut;
    }
}