package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.model.SignUpModel;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.RestUtilKt.errorToast;

public class SignUpActivity extends AppCompatActivity {
    String TAG = SignUpActivity.class.getCanonicalName();
    @Inject
    Retrofit retrofit;

    @BindView(R.id.username_edit_text)
    EditText userNameEditText;
    @BindView(R.id.email_edit_text)
    EditText emailEditText;
    @BindView(R.id.password_edit_text)
    EditText passwordEditText;
    @BindView(R.id.sign_up)
    ImageView signUp;
    TextWatcher signUpFieldsWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            //TODO: validate email
            if (!emailEditText.getText().toString().trim().isEmpty() && !passwordEditText.getText().toString().trim().isEmpty() &&
                    !userNameEditText.getText().toString().trim().isEmpty()) {
                signUp.setVisibility(View.VISIBLE);
            } else {
                signUp.setVisibility(View.INVISIBLE);
            }
        }
    };
    private RestApi restApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        ButterKnife.bind(this);

        emailEditText.addTextChangedListener(signUpFieldsWatcher);
        passwordEditText.addTextChangedListener(signUpFieldsWatcher);
        userNameEditText.addTextChangedListener(signUpFieldsWatcher);
    }

    @OnClick({R.id.sign_in_card, R.id.sign_up})
    public void onViewClicked(View view) {

        switch (view.getId()) {

            case R.id.sign_in_card:
                startActivity(new Intent(this, SignInActivity.class));
                finish();
                break;
            case R.id.sign_up:
                signUp();
                break;
        }
    }

    private void signUp() {
        SignUpModel signUpModel = new SignUpModel(UUID.randomUUID().toString(), userNameEditText.getText().toString().trim(),
                emailEditText.getText().toString().trim(), "USA", "Washington DC", "email", "Praneeth", "Muskula");
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
                        errorToast(R.string.something_went_wrong, e);
                    }

                    @Override
                    public void onNext(Response<SignUpModel> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_CREATED:
                                Intent intentSubmit = new Intent(SignUpActivity.this, SignInActivity.class);
                                startActivity(intentSubmit);
                                break;
                            case HttpURLConnection.HTTP_FORBIDDEN:
                                errorToast(R.string.username_exists, response);
                                break;
                            default:
                                errorToast(R.string.something_went_wrong, response);
                                Log.e(TAG, response.message());
                                break;
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }
}