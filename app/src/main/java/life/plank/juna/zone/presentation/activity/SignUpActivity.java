package life.plank.juna.zone.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.text_input_user_name)
    TextInputLayout mTextInput_UserName;
    @BindView(R.id.text_input_first_name)
    TextInputLayout mTextInput_FirstName;
    @BindView(R.id.text_input_last_name)
    TextInputLayout mTextInput_LastName;
    @BindView(R.id.text_input_password)
    TextInputLayout mTextInput_Password;
    @BindView(R.id.text_input_confirm_password)
    TextInputLayout mTextInput_ConfirmPassword;
    @BindView(R.id.input_user_name)
    EditText mUserName;
    @BindView(R.id.input_first_name)
    EditText mFirstName;
    @BindView(R.id.input_last_name)
    EditText mLastName;
    @BindView(R.id.input_password)
    EditText mPassword;
    @BindView(R.id.input_confirm_password)
    EditText mConfirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signup_activity);
        ButterKnife.bind(this);
        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                setPasswordsErrorToEmpty();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mTextInput_ConfirmPassword.setError("Please confirm password");
                if (mPassword.getText().toString().isEmpty()) {
                    setPasswordsErrorToEmpty();
                }
            }
        });


        mConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mConfirmPassword.getText().toString().trim().equals(mPassword.getText().toString().trim())) {
                    mTextInput_ConfirmPassword.setError(null);
                } else if (mConfirmPassword.getText().toString().isEmpty()) {
                    mTextInput_ConfirmPassword.setError("Please confirm password");
                } else {
                    mTextInput_ConfirmPassword.setError("Passwords don't match");
                }
            }
        });
    }

    private void setPasswordsErrorToEmpty() {
        mTextInput_Password.setError(null);
        mTextInput_ConfirmPassword.setError(null);
    }

    @OnClick(R.id.button_sign_up)
    public void signUp() {
        if (validateSignUp(mTextInput_UserName, mTextInput_FirstName, mTextInput_LastName, mTextInput_Password, mTextInput_ConfirmPassword)) {
            Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.text_sign_in)
    public void signInHere() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    private boolean validateSignUp(TextInputLayout mTextInput_userName, TextInputLayout mTextInput_firstName, TextInputLayout mTextInput_lastName, TextInputLayout mTextInput_password, TextInputLayout mTextInput_confirmPassword) {
        boolean isSuccess = false;
        String userNameVal = mUserName.getText().toString().trim();
        String firstNameVal = mFirstName.getText().toString().trim();
        String lastNameVal = mLastName.getText().toString().trim();
        String passwordVal = mPassword.getText().toString().trim();
        String confirmPasswordVal = mConfirmPassword.getText().toString().trim();

        mTextInput_userName.setError(null);
        mTextInput_firstName.setError(null);
        mTextInput_lastName.setError(null);
        mTextInput_password.setError(null);
        mTextInput_confirmPassword.setError(null);
        if (userNameVal.isEmpty()) {
            mTextInput_userName.setError("Please enter valid user name");
            isSuccess = false;
        } else if (firstNameVal.isEmpty() || !firstNameVal.matches("^[A-Za-z]*$")) {
            mTextInput_firstName.setError("Please enter valid name");
            isSuccess = false;
        } else if (lastNameVal.isEmpty() || !lastNameVal.matches("^[A-Za-z]*$")) {
            mTextInput_lastName.setError("Please enter valid name");
            isSuccess = false;
        } else if (passwordVal.isEmpty()) {
            mTextInput_password.setError("Please enter a password");
            isSuccess = false;
        } else if (confirmPasswordVal.isEmpty()) {
            mTextInput_confirmPassword.setError("Please confirm password");
            isSuccess = false;
        } else {
            isSuccess = true;
        }
        return isSuccess;
    }
}
