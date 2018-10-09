package life.plank.juna.zone.data.model;

import android.arch.persistence.room.Ignore;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Helper class for nested list parsing through parcels
 */
public class FormationList extends ArrayList<Formation> implements Parcelable {

    @Ignore
    protected FormationList(Parcel in) {
        in.readTypedList(this, Formation.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FormationList> CREATOR = new Creator<FormationList>() {
        @Override
        public FormationList createFromParcel(Parcel in) {
            return new FormationList(in);
        }

        @Override
        public FormationList[] newArray(int size) {
            return new FormationList[size];
        }
    };
}
