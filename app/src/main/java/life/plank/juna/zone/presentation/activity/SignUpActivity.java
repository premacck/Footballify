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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signup_activity);
        ButterKnife.bind(this);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                setPasswordsErrorToEmpty();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                confirmPasswordTextInput.setError(getString(R.string.confirm_password));
                if (password.getText().toString().isEmpty()) {
                    setPasswordsErrorToEmpty();
                }
            }
        });


        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (confirmPassword.getText().toString().trim().equals(password.getText().toString().trim())) {
                    confirmPasswordTextInput.setError(null);
                } else if (confirmPassword.getText().toString().isEmpty()) {
                    confirmPasswordTextInput.setError(getString(R.string.confirm_password));
                } else {
                    confirmPasswordTextInput.setError(getString(R.string.passwords_mismtch));
                }
            }
        });
    }

    private void setPasswordsErrorToEmpty() {
        passwordTextInput.setError(null);
        confirmPasswordTextInput.setError(null);
    }

    @OnClick(R.id.button_sign_up)
    public void signUp() {
        if (validateUserDetails(userNameTextInput, firstNameTextInput, lastNameTextInput, passwordTextInput, confirmPasswordTextInput)) {
            Toast.makeText(this, getString(R.string.login_successful), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.text_sign_in)
    public void signInHere() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    private boolean validateUserDetails(TextInputLayout mTextInput_userName, TextInputLayout mTextInput_firstName, TextInputLayout mTextInput_lastName, TextInputLayout mTextInput_password, TextInputLayout mTextInput_confirmPassword) {
        boolean isSuccess = false;
        String userNameText = this.userName.getText().toString().trim();
        String firstNameText = firstName.getText().toString().trim();
        String lastNameText = lastName.getText().toString().trim();
        String passwordText = password.getText().toString().trim();
        String confirmPasswordText = confirmPassword.getText().toString().trim();

        mTextInput_userName.setError(null);
        mTextInput_firstName.setError(null);
        mTextInput_lastName.setError(null);
        mTextInput_password.setError(null);
        mTextInput_confirmPassword.setError(null);
        if (userNameText.isEmpty()) {
            mTextInput_userName.setError(getString(R.string.enter_valid_user_name));
            isSuccess = false;
            return isSuccess;
        } else if (firstNameText.isEmpty() || !firstNameText.matches(getString(R.string.name_regex))) {
            mTextInput_firstName.setError(getString(R.string.enter_valid_name));
            isSuccess = false;
            return isSuccess;
        } else if (lastNameText.isEmpty() || !lastNameText.matches(getString(R.string.name_regex))) {
            mTextInput_lastName.setError(getString(R.string.enter_valid_name));
            isSuccess = false;
            return isSuccess;
        } else if (passwordText.isEmpty()) {
            mTextInput_password.setError(getString(R.string.enter_password));
            isSuccess = false;
            return isSuccess;
        } else if (confirmPasswordText.isEmpty()) {
            mTextInput_confirmPassword.setError(getString(R.string.confirm_password));
            isSuccess = false;
            return isSuccess;
        } else {
            isSuccess = true;
            return isSuccess;
        }
    }
}
