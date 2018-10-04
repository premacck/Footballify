package life.plank.juna.zone.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Formation implements Parcelable {
    private String nickname;
    private String imagePath;
    private int number;
    private int formationNumber;
    //        TODO : change yellowcard, redcard and yellowred fields to camelCase when they are updated in backend
    private int yellowcard;
    private int redcard;
    private int yellowred;
    private int goals;
    private int substituteIn;
    private int substituteOut;

    protected Formation(Parcel in) {
        nickname = in.readString();
        imagePath = in.readString();
        number = in.readInt();
        formationNumber = in.readInt();
        yellowcard = in.readInt();
        redcard = in.readInt();
        yellowred = in.readInt();
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
        dest.writeInt(yellowcard);
        dest.writeInt(redcard);
        dest.writeInt(yellowred);
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

    public int getYellowcard() {
        return yellowcard;
    }

    public void setYellowcard(int yellowcard) {
        this.yellowcard = yellowcard;
    }

    public int getRedcard() {
        return redcard;
    }

    public void setRedcard(int redcard) {
        this.redcard = redcard;
    }

    public int getYellowred() {
        return yellowred;
    }

    public void setYellowred(int yellowred) {
        this.yellowred = yellowred;
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