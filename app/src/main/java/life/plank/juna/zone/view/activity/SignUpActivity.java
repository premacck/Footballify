package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.SignUpModel;
import life.plank.juna.zone.util.ActivityUtil;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SignUpActivity extends AppCompatActivity {
    String TAG = SignUpActivity.class.getCanonicalName();
    @Inject
    @Named("default")
    Retrofit retrofit;
    @BindView(R.id.username_text_input_layout)
    TextInputLayout usernameInputLayout;
    @BindView(R.id.password_input_layout)
    TextInputLayout passwordInputLayout;
    @BindView(R.id.email_input_layout)
    TextInputLayout emailInputLayout;
    @BindView(R.id.username_editext)
    EditText userNameEditText;
    @BindView(R.id.email_editext)
    EditText emailEditText;
    @BindView(R.id.image_button)
    ImageView imageButton;
    @BindView(R.id.password_editext)
    EditText passwordEditText;
    private RestApi restApi;
    String emailText, passwordText, userNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        ((ZoneApplication) getApplication()).getSignUpUserNetworkComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        ButterKnife.bind(this);
        ActivityUtil.setCollapsedHintMiddle(usernameInputLayout, this);
        ActivityUtil.setCollapsedHintMiddle(emailInputLayout, this);
        ActivityUtil.setCollapsedHintMiddle(passwordInputLayout, this);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailText = emailEditText.getText().toString();
                passwordText = passwordEditText.getText().toString();
                userNameText = userNameEditText.getText().toString();
                if (userNameText.isEmpty()) {
                    userNameEditText.setError(getString(R.string.username_empty));
                } else if (emailText.isEmpty()) {
                    emailEditText.setError(getString(R.string.email_empty));
                } else if (passwordText.isEmpty()) {
                    passwordEditText.setError(getString(R.string.password_empty));
                } else {
                    signUp();
                }
            }
        });
    }

    private void signUp() {
        SignUpModel signUpModel = new SignUpModel(UUID.randomUUID().toString(), userNameText, emailText, "USA", "Washington DC", "email", "Praneeth", "Muskula");
        restApi.createUser(signUpModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<SignUpModel>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e);
                        Toast.makeText(getApplicationContext(), R.string.something_went_wrong_500, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<SignUpModel> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_CREATED:
                                Intent intentSubmit = new Intent(SignUpActivity.this, SignInActivity.class);
                                startActivity(intentSubmit);
                                break;
                            case HttpURLConnection.HTTP_FORBIDDEN:
                                Toast.makeText(getApplicationContext(), R.string.username_exists, Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Log.e(TAG, response.message());
                                break;
                        }
                    }
                });
    }
}