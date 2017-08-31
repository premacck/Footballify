package life.plank.juna.zone.data.network.model;

import android.support.annotation.NonNull;

/**
 * Created by plank-sobia on 8/29/2017.
 */

public class ValidationResult<Data> {

    private boolean valid;
    private String reason;
    private Data data;

    public ValidationResult(boolean valid, String reason, Data data) {
        this.valid = valid;
        this.reason = reason;
        this.data = data;
    }

    public static <Data> ValidationResult<Data> success(Data data) {
        return new ValidationResult<>(true, null, data);
    }

    public static <Data> ValidationResult<Data> failure(@NonNull String reason, Data data) {
        return new ValidationResult<>(false, reason, data);
    }

    public boolean isValid() {
        return valid;
    }

    @NonNull
    public String getReason() {
        return reason;
    }

    public Data getData() {
        return data;
    }
}
