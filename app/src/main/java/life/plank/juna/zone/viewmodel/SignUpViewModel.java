package life.plank.juna.zone.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import life.plank.juna.zone.data.network.builder.JunaUserBuilder;
import life.plank.juna.zone.data.network.model.ValidationResult;
import life.plank.juna.zone.domain.service.AuthenticationService;
import life.plank.juna.zone.util.ValidationUtil;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by plank-sobia on 11/13/2017.
 */

public class SignUpViewModel {

    private static final String TAG = SignUpViewModel.class.getSimpleName();
    private Observable<ValidationResult> userNameObservable;
    private Observable<ValidationResult> firstNameObservable;
    private Observable<ValidationResult> lastNameObservable;
    private Observable<ValidationResult> passwordObservable;
    private Observable<ValidationResult> confirmPasswordObservable;
    private AuthenticationService authenticationService;
    private Context context;
    private String passwordText;

    public SignUpViewModel(Context context, AuthenticationService authenticationService) {
        this.context = context;
        this.authenticationService = authenticationService;
    }

    public Observable<Boolean> validateUserDetails(Observable<String> userName, Observable<String> firstName, Observable<String> lastName, Observable<String> password, Observable<String> confirmPassword) {
        userNameObservable = userName.observeOn(AndroidSchedulers.mainThread())
                .map(s -> validateUserName(s, context));

        firstNameObservable = firstName.observeOn(AndroidSchedulers.mainThread())
                .map(s -> validateName(s, context));

        lastNameObservable = lastName.observeOn(AndroidSchedulers.mainThread())
                .map(s -> validateName(s, context));

        passwordObservable = password.observeOn(AndroidSchedulers.mainThread())
                .map(s -> validatePassword(s, context));


        password.subscribe(s -> passwordText = s);
        confirmPasswordObservable = confirmPassword.observeOn(AndroidSchedulers.mainThread())
                .map(s -> validateConfirmPassword(passwordText, s, context));

        return Observable.combineLatest(userNameObservable, firstNameObservable, lastNameObservable, passwordObservable, confirmPasswordObservable, (validUserName, validFirstName, validLastName, validPassword, validConfirmPassword) -> {
            Log.i(TAG, "username: " + validUserName + ", firstname: " + validFirstName + ", lastname: " + validLastName + ", password: " + validPassword + ",confirmpassword: " + validConfirmPassword);
            return validUserName.isValid() && validFirstName.isValid() && validLastName.isValid() && validPassword.isValid() && validConfirmPassword.isValid();
        });
    }

    public Observable<Response<Void>> registerUser(String userName, String password) {
        return authenticationService.register(JunaUserBuilder.getInstance()
                .withUserName(userName)
                .withPassword(password)
                .build());
    }

    public ValidationResult validateUserName(@NonNull String username, Context context) {
        return ValidationUtil.isValidUsername(username, context);
    }

    public ValidationResult validateName(@NonNull String name, Context context) {
        return ValidationUtil.isValidName(name, context);
    }

    public ValidationResult validatePassword(@NonNull String password, Context context) {
        return ValidationUtil.isValidPassword(password, context);
    }

    public ValidationResult validateConfirmPassword(String passwordText, @NonNull String confirmPassword, Context context) {
        return ValidationUtil.isValidConfirmPassword(passwordText, confirmPassword, context);
    }

    public Observable<ValidationResult> getUserNameObservable() {
        return userNameObservable;
    }

    public Observable<ValidationResult> getFirstNameObservable() {
        return firstNameObservable;
    }

    public Observable<ValidationResult> getLastNameObservable() {
        return lastNameObservable;
    }

    public Observable<ValidationResult> getPasswordObservable() {
        return passwordObservable;
    }

    public Observable<ValidationResult> getConfirmPasswordObservable() {
        return confirmPasswordObservable;
    }
}
