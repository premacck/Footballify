package life.plank.juna.zone.view.activity;

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
import life.plank.juna.zone.data.network.builder.JunaUserBuilder;
import life.plank.juna.zone.data.network.interfaces.RestApi;
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
    @BindView(R.id.login_relative_layout)
    RelativeLayout relativeLayout;

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
        subscription = restApi.loginUser(JunaUserBuilder.getInstance()
                .withUserName(userName)
                .withPassword(password)
                .build())
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
                        } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                            Snackbar.make(relativeLayout, "Invalid username and password", Snackbar.LENGTH_LONG).show();
                        } else
                            Snackbar.make(relativeLayout, "Signin failed, please retry", Snackbar.LENGTH_LONG).show();
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
                    ValidationResult result = validateUserName(s, getApplicationContext());
                    userNameTextInput.setError(result.getReason());
                    return result.isValid();
                });

        Observable<Boolean> passwordObservable = RxHelper.getTextWatcherObservable(password)
                .debounce(getResources().getInteger(R.integer.debounce_time), TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(s -> {
                    ValidationResult result = validatePassword(s, getApplicationContext());
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

    public ValidationResult validateUserName(@NonNull String userName, Context context) {
        return ValidationUtil.isValidUsername(userName, context);
    }

    public ValidationResult validatePassword(@NonNull String password, Context context) {
        return ValidationUtil.isValidPassword(password, context);
    }
}