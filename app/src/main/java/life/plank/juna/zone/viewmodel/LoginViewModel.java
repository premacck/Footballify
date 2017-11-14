package life.plank.juna.zone.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.builder.JunaUserBuilder;
import life.plank.juna.zone.data.network.model.ValidationResult;
import life.plank.juna.zone.domain.service.AuthenticationService;
import life.plank.juna.zone.util.ValidationUtil;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by plank-sobia on 11/8/2017.
 */

public class LoginViewModel {

    private static final String TAG = LoginViewModel.class.getSimpleName();
    private Observable<ValidationResult> usernameObservable;
    private Observable<ValidationResult> passwordObservable;
    private AuthenticationService authenticationService;
    private SharedPreferences.Editor loginPreferenceEditor;
    private Context context;

    public LoginViewModel(Context context, AuthenticationService authenticationService) {
        this.context = context;
        this.authenticationService = authenticationService;
    }

    public Observable<Boolean> validateUserDetails(Observable<String> userName, Observable<String> password) {
        usernameObservable = userName.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(s -> validateUserName(s, context));

        passwordObservable = password.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(s -> validatePassword(s, context));

        return Observable.combineLatest(usernameObservable, passwordObservable,
                (validUserName, validPassword) -> {
                    Log.i(TAG, "username: " + validUserName + ", password: " + validPassword);
                    return validUserName.isValid() && validPassword.isValid();
                });
    }

    public Observable<Response<Void>> loginUser(String userNameText, String passwordText) {
        return authenticationService.login(JunaUserBuilder.getInstance()
                .withUserName(userNameText)
                .withPassword(passwordText)
                .build());
    }

    public ValidationResult validateUserName(@NonNull String userName, Context context) {
        return ValidationUtil.isValidUsername(userName, context);
    }

    public ValidationResult validatePassword(@NonNull String password, Context context) {
        return ValidationUtil.isValidPassword(password, context);
    }

    public Observable<ValidationResult> getUsernameObservable() {
        return usernameObservable;
    }

    public Observable<ValidationResult> getPasswordObservable() {
        return passwordObservable;
    }

    public void saveLoginDetails(String userNameText, String passwordText) {
        loginPreferenceEditor = context.getSharedPreferences(context.getString(R.string.login_pref), Context.MODE_PRIVATE).edit();
        loginPreferenceEditor.putString(context.getString(R.string.shared_pref_username), userNameText.trim())
                .putString(context.getString(R.string.shared_pref_password), passwordText.trim())
                .apply();
    }

    public void clearLoginDetailsSharedPref() {
        loginPreferenceEditor = context.getSharedPreferences(context.getString(R.string.login_pref), Context.MODE_PRIVATE).edit();
        loginPreferenceEditor.clear()
                .apply();
    }

    public void enableRememberMe() {
        loginPreferenceEditor = context.getSharedPreferences(context.getString(R.string.login_pref), Context.MODE_PRIVATE).edit();
        loginPreferenceEditor.putBoolean(context.getString(R.string.shared_pref_save_login), true).apply();
    }
}
