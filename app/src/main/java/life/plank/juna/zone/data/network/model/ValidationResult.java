package life.plank.juna.zone.data.network.model;

import android.support.annotation.NonNull;

/**
 * Created by plank-sobia on 8/29/2017.
 */

public class ValidationResult<T> {

    private boolean valid;
    private String reason;
    private T data;

    public ValidationResult(boolean valid, String reason, T t) {
        this.valid = valid;
        this.reason = reason;
        this.data = t;
    }

    public static <T> ValidationResult<T> success(T t) {
        return new ValidationResult<>(true, null, t);
    }

    public static <T> ValidationResult<T> failure(@NonNull String reason, T t) {
        return new ValidationResult<>(false, reason, t);
    }

    public boolean isValid() {
        return valid;
    }

    @NonNull
    public String getReason() {
        return reason;
    }

    public T getData() {
        return data;
    }
}
