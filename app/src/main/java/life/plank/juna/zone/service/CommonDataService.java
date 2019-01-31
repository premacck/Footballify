package life.plank.juna.zone.service;

import android.text.TextUtils;
import android.util.Patterns;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.IntegerRes;
import androidx.annotation.StringRes;
import life.plank.juna.zone.ZoneApplication;

import static com.prembros.facilis.util.DataUtilKt.isNullOrEmpty;

/**
 * Class intended for common data related services.
 */
public class CommonDataService {

    @NotNull
    public static String findString(@StringRes int stringRes) {
        return ZoneApplication.Companion.getAppContext().getString(stringRes);
    }

    @NotNull
    public static CharSequence findString(@StringRes int stringRes, Object... formatArgs) {
        return ZoneApplication.Companion.getAppContext().getString(stringRes, formatArgs);
    }

    @NotNull
    public static Integer findInt(@IntegerRes int integerRes) {
        return ZoneApplication.Companion.getAppContext().getResources().getInteger(integerRes);
    }

    @Contract("null -> false")
    public static boolean isValidEmail(CharSequence target) {
        return (target != null && !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static <T> void validateAndUpdateList(List<T> originalList, List<T> newList, boolean isError) {
        if (!isError) {
            if (originalList == null) {
                originalList = new ArrayList<>();
            }
            if (!isNullOrEmpty(newList)) {
                originalList.addAll(newList);
            }
        }
    }
}