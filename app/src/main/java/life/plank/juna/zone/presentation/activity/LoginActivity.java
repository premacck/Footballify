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
import rx.functions.Func2;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
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
    private Boolean validUserDetails = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login_activity);
        ButterKnife.bind(this);
        validateLoginDetails();
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
            Toast.makeText(this, getString(R.string.login_successful), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.text_sign_up)
    public void signUpHere() {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    private void validateLoginDetails() {
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

        subscription = Observable.combineLatest(userNameObservable, passwordObservable, new Func2<Boolean, Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean validUserName, Boolean validPassword) {
                Log.i(TAG, "username: " + validUserName + ", password: " + validPassword);
                return validUserName && validPassword;
            }
        }).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                signInButton.setEnabled(aBoolean);
                validUserDetails = true;
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(TAG, throwable.getMessage());
            }
        });
    }

    private ValidationResult validateUserName(@NonNull String userName) {
        return ValidationUtil.isValidUsername(userName, getApplicationContext());
    }

    private ValidationResult validatePassword(@NonNull String password) {
        return ValidationUtil.isValidPassword(password, getApplicationContext());
    }
}