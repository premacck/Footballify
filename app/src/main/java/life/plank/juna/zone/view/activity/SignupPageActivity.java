package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.HttpURLConnection;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.SignupModel;
import life.plank.juna.zone.util.ActivityUtil;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SignupPageActivity extends AppCompatActivity {
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
    Button imageButtton;
    @BindView(R.id.password_editext)
    EditText passwordEditText;
    private RestApi restApi;
    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        ((ZoneApplication) getApplication()).getSignupUserNetworkComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        ButterKnife.bind(this);
        ActivityUtil.setCollapsedHintMiddle(usernameInputLayout,this);
        ActivityUtil.setCollapsedHintMiddle(emailInputLayout,this);
        ActivityUtil.setCollapsedHintMiddle(passwordInputLayout,this);
        String emailText = emailEditText.getText().toString();
        String passwordText = passwordEditText.getText().toString();
        String userNameText = userNameEditText.getText().toString();
        imageButtton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userNameText.isEmpty())
                {
                    userNameEditText.setError( "User Name Can not be Empty");
                }
                else if(emailText.isEmpty())
                {
                    emailEditText.setError( "Email Can not be Empty");
                }
                else {
                    signup();
                }
            }
        } );
    }

    private void signup() {
        SignupModel signupModel = new SignupModel( "969f1c52-92c0-4591-b9fd-14d4406efafc","Praneeth Muskula","praneeth@plank.life","USA","Washington DC","email","Praneeth", "Muskula");
        restApi.getSignup(signupModel)
                    .subscribeOn( Schedulers.io() )
                    .observeOn( AndroidSchedulers.mainThread() )
                    .subscribe( new Subscriber<Response<SignupModel>>() {
                        @Override
                        public void onCompleted() {
                            Log.e( "", "onCompleted: " );
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e( "", "onError: " + e );

                        }

                        @Override
                        public void onNext(Response<SignupModel> signupModel) {
                                Log.e( "", "onNext: "+signupModel );

                        }

                    } );
    }
}