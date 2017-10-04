package life.plank.juna.zone.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

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
import life.plank.juna.zone.util.PreferenceManager;
import life.plank.juna.zone.util.ValidationUtil;
import life.plank.juna.zone.util.helper.RxHelper;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    @Inject
    Retrofit retrofit;

    private static final String TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.text_input_user_name)
    TextInputLayout userNameTextInput;
    @BindView(R.id.text_input_password)
    TextInputLayout passwordTextInput;
    @BindView(R.id.input_user_name)
    EditText userName;
    @BindView(R.id.input_password)
    EditText password;
    @BindView(R.id.button_sign_in)
    Button signInButton;

    private Subscription subscription;
    private RestApi restApi;
    private Boolean validUserDetails = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login_activity);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getColor(R.color.Orange));
        }
        validateLoginDetails();
        ((ZoneApplication) getApplication()).getLoginNetworkComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    @OnClick(R.id.button_sign_in)
    public void signIn() {
        if (validUserDetails) {
            PreferenceManager prefManager = new PreferenceManager(this);
            prefManager.saveString(getString(R.string.shared_pref_username), userName.getText().toString().trim());
            loginUser(LoginActivity.this, userName.getText().toString().trim(), password.getText().toString().trim());
        }
    }

    private void loginUser(Context context, String userName, String password) {
        subscription = restApi.loginUser(User.getInstance().withUsername(userName).withPassword(password))
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
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            Log.d(TAG, "Login Successful. Status Code =" + response.code());
                            startActivity(new Intent(context, ZoneHomeActivity.class));
                        } else
                            Log.d(TAG, "Registration Failed. Status Code:" + response.code());
                    }
                });

    }

    @OnClick(R.id.text_sign_up)
    public void signUpHere() {
        startActivity(new Intent(this, SocialLoginActivity.class));
    }

    private void validateLoginDetails() {
        Observable<Boolean> userNameObservable = RxHelper.getTextWatcherObservable(userName)
                .debounce(getResources().getInteger(R.integer.debounce_time), TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(s -> {
                    ValidationResult result = validateUserName(s);
                    userNameTextInput.setError(result.getReason());
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

        subscription = Observable.combineLatest(userNameObservable, passwordObservable,
                (validUserName, validPassword) -> {
                    Log.i(TAG, "username: " + validUserName + ", password: " + validPassword);
                    return validUserName && validPassword;
                }).subscribe(aBoolean -> {
            signInButton.setEnabled(aBoolean);
            validUserDetails = aBoolean;
        }, throwable -> Log.e(TAG, throwable.getMessage()));
    }

    private ValidationResult validateUserName(@NonNull String userName) {
        return ValidationUtil.isValidUsername(userName, getApplicationContext());
    }

    private ValidationResult validatePassword(@NonNull String password) {
        return ValidationUtil.isValidPassword(password, getApplicationContext());
    }
}