package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.HttpURLConnection;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.SignInModel;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.util.helper.StackAnimation;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SignInActivity extends AppCompatActivity {
    String TAG = SignInActivity.class.getCanonicalName();
    @Inject
    @Named("default")
    Retrofit retrofit;
    @Nullable
    @BindView(R.id.login)
    ImageView login;
    @BindView(R.id.email_edit_text)
    EditText emailEditText;
    @BindView(R.id.password_edit_text)
    EditText passwordEditText;

    StackAnimation stackAnimation;
    String emailText;
    private RestApi restApi;

    TextWatcher loginFieldsWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            //TODO: validate email
            if (!emailEditText.getText().toString().trim().isEmpty() && !passwordEditText.getText().toString().trim().isEmpty()) {
                login.setVisibility(View.VISIBLE);
            } else {
                login.setVisibility(View.INVISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ((ZoneApplication) getApplication()).getSignInUserNetworkComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        ButterKnife.bind(this);
        initStackAnimation();

        emailEditText.addTextChangedListener(loginFieldsWatcher);
        passwordEditText.addTextChangedListener(loginFieldsWatcher);

    }

    private void initStackAnimation() {
        stackAnimation = new StackAnimation(AppConstants.ANIMATION_DURATION,
                AppConstants.ANIMATION_START_SCALE,
                AppConstants.ANIMATION_PIVOT_VALUE);
    }

    @OnClick({R.id.login, R.id.forgot_password_text_view, R.id.sign_up_card})
    public void onViewClicked(View view) {

        switch (view.getId()) {

            case R.id.login:
                emailText = emailEditText.getText().toString();
                if (emailText.isEmpty()) {
                    emailEditText.setError(getString(R.string.username_empty));
                } else {
                    getSignInResponse(emailText);
                }
                break;
            case R.id.forgot_password_text_view:
                Intent intent = new Intent(this, AuthForgotPasswordActivity.class);
                startActivity(intent);
                break;

            case R.id.sign_up_card:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
        }
    }

    private void getSignInResponse(String emailAddress) {
        restApi.getUser(emailAddress)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<SignInModel>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e);
                        Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<SignInModel> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                //TODO: Investigate why the response.body is saved
                                UIDisplayUtil.saveSignInUserDetails(SignInActivity.this, response.body());
                                Intent intentSubmit = new Intent(SignInActivity.this, SwipePageActivity.class);
                                startActivity(intentSubmit);
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                Toast.makeText(getApplicationContext(), R.string.user_name_not_found, Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Log.e(TAG, response.message());
                                break;
                        }
                    }
                });
    }

}
