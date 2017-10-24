package life.plank.juna.zone.util;

import android.content.Context;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.ValidationResult;

/**
 * Created by plank-sobia on 8/29/2017.
 */

public class ValidationUtil {

    public static ValidationResult<String> isValidUsername(String username, Context context) {
        if (username.isEmpty()) {
            return ValidationResult.failure(context.getString(R.string.enter_valid_user_name), username);
        }

        if (username.contains(" ")) {
            return ValidationResult.failure(context.getString(R.string.enter_valid_user_name), username);
        }

        Pattern pattern = Pattern.compile(context.getString(R.string.user_name_regex));
        Matcher matcher = pattern.matcher(username);
        boolean isValid = matcher.find();

        if (isValid) {
            return ValidationResult.success(username);
        }

        return ValidationResult.failure(context.getString(R.string.enter_valid_user_name), username);
    }

    public static ValidationResult<String> isValidName(String name, Context context) {
        if (name.isEmpty()) {
            return ValidationResult.failure(context.getString(R.string.enter_valid_name), name);
        }

        if (name.contains(" ")) {
            return ValidationResult.failure(context.getString(R.string.enter_valid_name), name);
        }

        Pattern pattern = Pattern.compile(context.getString(R.string.name_regex));
        Matcher matcher = pattern.matcher(name);
        boolean isValid = matcher.find();

        if (isValid) {
            return ValidationResult.success(name);
        }

        return ValidationResult.failure(context.getString(R.string.enter_valid_name), name);
    }

    public static ValidationResult<String> isValidPassword(String password, Context context) {
        if (password.isEmpty()) {
            return ValidationResult.failure(context.getString(R.string.enter_password), password);
        }

        if (password.contains(" ")) {
            return ValidationResult.failure(context.getString(R.string.enter_password), password);
        }

        return ValidationResult.success(password);
    }

    public static ValidationResult<String> isValidConfirmPassword(String password, String confirmPassword, Context context) {
        if (confirmPassword.isEmpty()) {
            return ValidationResult.failure(context.getString(R.string.confirm_password), confirmPassword);
        } else if (!confirmPassword.equals(password)) {
            return ValidationResult.failure(context.getString(R.string.passwords_mismatch), confirmPassword);
        } else return ValidationResult.success(confirmPassword);
    }
}

