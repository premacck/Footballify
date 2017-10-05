package life.plank.juna.zone.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.User;
import life.plank.juna.zone.data.network.model.ValidationResult;
import life.plank.juna.zone.util.ValidationUtil;
import life.plank.juna.zone.util.helper.RxHelper;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SignUpActivity extends AppCompatActivity {

    @Inject
    Retrofit retrofit;

    private static final String TAG = SignUpActivity.class.getSimpleName();
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
    @BindView(R.id.signup_relative_layout)
    RelativeLayout relativeLayout;

    private Subscription subscription;
    private RestApi restApi;
    private String passwordText;
    private Boolean validUserDetails = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signup_activity);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getColor(R.color.Orange));
        }
        validateUserDetails();
        ((ZoneApplication) getApplication()).getRegisterNetworkComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
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
            registerUserCredentials(SignUpActivity.this, userName.getText().toString().trim(), password.getText().toString().trim());
        }
    }

    private void registerUserCredentials(Context context, String userName, String password) {
        subscription = restApi.registerUser(User.getInstance().withUsername(userName).withPassword(password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "Error Registering user: " + e.getMessage());
                    }

                    @Override
                    public void onNext(Response<Void> response) {
                        if (response.code() == HttpURLConnection.HTTP_CREATED) {
                            Log.d(TAG, "Registration Successful");
                            startActivity(new Intent(context, LoginActivity.class));
                        } //Todo: Change this response code to 409 when backend issue - JUNA-921 is fixed
                        else if (response.code() == 422) {
                            Snackbar.make(relativeLayout, "User name already exists", Snackbar.LENGTH_LONG).show();
                        } else
                            Snackbar.make(relativeLayout, "Signup failed, please retry", Snackbar.LENGTH_LONG).show();
                    }
                });
    }


    @OnClick(R.id.text_sign_in)
    public void signInHere() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void validateUserDetails() {
        Observable<Boolean> userNameObservable = RxHelper.getTextWatcherObservable(userName)
                .debounce(getResources().getInteger(R.integer.debounce_time), TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(s -> {
                    ValidationResult result = validateUserName(s);
                    userNameTextInput.setError(result.getReason());
                    return result.isValid();
                });

        Observable<Boolean> firstNameObservable = RxHelper.getTextWatcherObservable(firstName)
                .debounce(getResources().getInteger(R.integer.debounce_time), TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(s -> {
                    ValidationResult result = validateName(s);
                    firstNameTextInput.setError(result.getReason());
                    return result.isValid();
                });

        Observable<Boolean> lastNameObservable = RxHelper.getTextWatcherObservable(lastName)
                .debounce(getResources().getInteger(R.integer.debounce_time), TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(s -> {
                    ValidationResult result = validateName(s);
                    lastNameTextInput.setError(result.getReason());
                    return result.isValid();
                });

        Observable<Boolean> passwordObservable = RxHelper.getTextWatcherObservable(password)
                .debounce(getResources().getInteger(R.integer.debounce_time), TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(s -> {
                    ValidationResult result = validatePassword(s);
                    passwordTextInput.setError(result.getReason());
                    return result.isValid();
                });

        Observable<Boolean> confirmPasswordObservable = RxHelper.getTextWatcherObservable(confirmPassword)
                .debounce(getResources().getInteger(R.integer.debounce_time), TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(s -> {
                    passwordText = password.getText().toString().trim();
                    ValidationResult result = validateConfirmPassword(s);
                    confirmPasswordTextInput.setError(result.getReason());
                    return result.isValid();
                });

        subscription = Observable.combineLatest(userNameObservable, firstNameObservable, lastNameObservable, passwordObservable, confirmPasswordObservable, (validUserName, validFirstName, validLastName, validPassword, validConfirmPassword) -> {
            Log.i(TAG, "username: " + validUserName + ", firstname: " + validFirstName + ", lastname: " + validLastName + ", password: " + validPassword + ",confirmpassword: " + validConfirmPassword);
            return validUserName && validFirstName && validLastName && validPassword && validConfirmPassword;
        }).subscribe(aBoolean -> {
            signUpButton.setEnabled(aBoolean);
            validUserDetails = aBoolean;
        }, throwable -> Log.e(TAG, throwable.getMessage()));
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
