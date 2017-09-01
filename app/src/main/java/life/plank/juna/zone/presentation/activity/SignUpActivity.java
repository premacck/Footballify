package life.plank.juna.zone.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.ValidationResult;
import life.plank.juna.zone.util.ValidationUtil;
import life.plank.juna.zone.util.helper.RxHelper;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func5;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    @BindView(R.id.text_input_user_name)
    TextInputLayout userNameTextInput;
    @BindView(R.id.text_input_first_name)
    TextInputLayout firstNameTextInput;
    @BindView(R.id.text_input_last_name)
    TextInputLayout lastNameTextInput;
    @BindView(R.id.text_input_password)
    TextInputLayout passwordTextInput;
    @BindView(R.id.text_input_confirm_password)
    TextInputLayout confirmPasswordTextInput;
    @BindView(R.id.input_user_name)
    EditText userName;
    @BindView(R.id.input_first_name)
    EditText firstName;
    @BindView(R.id.input_last_name)
    EditText lastName;
    @BindView(R.id.input_password)
    EditText password;
    @BindView(R.id.input_confirm_password)
    EditText confirmPassword;
    @BindView(R.id.button_sign_up)
    Button signUpButton;

    private Subscription subscription;
    private String passwordText;
    private Boolean validUserDetails = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signup_activity);
        ButterKnife.bind(this);
        validateUserDetails();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    @OnClick(R.id.button_sign_up)
    public void signUp() {
        if (validUserDetails) {
            Toast.makeText(this, getString(R.string.signup_successful), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.text_sign_in)
    public void signInHere() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void validateUserDetails() {
        Observable<Boolean> userNameObservable = RxHelper.getTextWatcherObservable(userName)
                .debounce(getResources().getInteger(R.integer.debounce_time), TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        ValidationResult result = validateUserName(s);
                        userNameTextInput.setError(result.getReason());
                        return result.isValid();
                    }
                });

        Observable<Boolean> firstNameObservable = RxHelper.getTextWatcherObservable(firstName)
                .debounce(getResources().getInteger(R.integer.debounce_time), TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        ValidationResult result = validateName(s);
                        firstNameTextInput.setError(result.getReason());
                        return result.isValid();
                    }
                });

        Observable<Boolean> lastNameObservable = RxHelper.getTextWatcherObservable(lastName)
                .debounce(getResources().getInteger(R.integer.debounce_time), TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        ValidationResult result = validateName(s);
                        lastNameTextInput.setError(result.getReason());
                        return result.isValid();
                    }
                });

        Observable<Boolean> passwordObservable = RxHelper.getTextWatcherObservable(password)
                .debounce(getResources().getInteger(R.integer.debounce_time), TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        ValidationResult result = validatePassword(s);
                        passwordTextInput.setError(result.getReason());
                        return result.isValid();
                    }
                });

        Observable<Boolean> confirmPasswordObservable = RxHelper.getTextWatcherObservable(confirmPassword)
                .debounce(getResources().getInteger(R.integer.debounce_time), TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        passwordText = password.getText().toString().trim();
                        ValidationResult result = validateConfirmPassword(s);
                        confirmPasswordTextInput.setError(result.getReason());
                        return result.isValid();
                    }
                });

        subscription = Observable.combineLatest(userNameObservable, firstNameObservable, lastNameObservable, passwordObservable, confirmPasswordObservable, new Func5<Boolean, Boolean, Boolean, Boolean, Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean validUserName, Boolean validFirstName, Boolean validLastName, Boolean validPassword, Boolean validConfirmPassword) {
                Log.i(TAG, "username: " + validUserName + ", firstname: " + validFirstName + ", lastname: " + validLastName + ", password: " + validPassword + ",confirmpassword: " + validConfirmPassword);
                return validUserName && validFirstName && validLastName && validPassword && validConfirmPassword;
            }
        }).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                signUpButton.setEnabled(aBoolean);
                validUserDetails = true;
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(TAG, throwable.getMessage());
            }
        });
    }

    private ValidationResult validateUserName(@NonNull String username) {
        return ValidationUtil.isValidUsername(username, getApplicationContext());
    }

    private ValidationResult validateName(@NonNull String name) {
        return ValidationUtil.isValidName(name, getApplicationContext());
    }

    private ValidationResult validatePassword(@NonNull String password) {
        return ValidationUtil.isValidPassword(password, getApplicationContext());
    }

    private ValidationResult validateConfirmPassword(@NonNull String confirmPassword) {
        return ValidationUtil.isValidConfirmPassword(passwordText, confirmPassword, getApplicationContext());
    }
}
