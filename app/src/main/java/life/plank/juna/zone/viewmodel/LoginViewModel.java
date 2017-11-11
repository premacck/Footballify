package life.plank.juna.zone.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

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

    public LoginViewModel(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public Observable<Boolean> validateUserDetails(Context context, Observable<String> userName, Observable<String> password) {
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
}
