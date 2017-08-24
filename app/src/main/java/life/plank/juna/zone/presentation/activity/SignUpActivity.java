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
        userName.addTextChangedListener(textWatcher);
        firstName.addTextChangedListener(textWatcher);
        lastName.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
        confirmPassword.addTextChangedListener(textWatcher);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            userNameTextInput.setError(null);
            firstNameTextInput.setError(null);
            lastNameTextInput.setError(null);
            passwordTextInput.setError(null);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (password.getText().toString().isEmpty()) {
                setPasswordsErrorToEmpty();
            } else if (confirmPassword.getText().toString().trim().equals(password.getText().toString().trim())) {
                confirmPasswordTextInput.setError(null);
            } else if (confirmPassword.getText().toString().isEmpty()) {
                confirmPasswordTextInput.setError(getString(R.string.confirm_password));
            } else {
                confirmPasswordTextInput.setError(getString(R.string.passwords_mismtch));
            }
        }
    };

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

    private boolean validateUserDetails(TextInputLayout userNameTextInput, TextInputLayout firstNameTextInput, TextInputLayout lastNameTextInput, TextInputLayout passwordTextInput, TextInputLayout confirmPasswordTextInput) {
        String userNameText = this.userName.getText().toString().trim();
        String firstNameText = firstName.getText().toString().trim();
        String lastNameText = lastName.getText().toString().trim();
        String passwordText = password.getText().toString().trim();
        String confirmPasswordText = confirmPassword.getText().toString().trim();

        if (userNameText.isEmpty()) {
            userNameTextInput.setError(getString(R.string.enter_valid_user_name));
            return false;
        } else if (firstNameText.isEmpty() || !firstNameText.matches(getString(R.string.name_regex))) {
            firstNameTextInput.setError(getString(R.string.enter_valid_name));
            return false;
        } else if (lastNameText.isEmpty() || !lastNameText.matches(getString(R.string.name_regex))) {
            lastNameTextInput.setError(getString(R.string.enter_valid_name));
            return false;
        } else if (passwordText.isEmpty()) {
            passwordTextInput.setError(getString(R.string.enter_password));
            return false;
        } else if (confirmPasswordText.isEmpty()) {
            confirmPasswordTextInput.setError(getString(R.string.confirm_password));
            return false;
        } else if (!confirmPasswordText.equals(password.getText().toString().trim())) {
            confirmPasswordTextInput.setError(getString(R.string.passwords_mismtch));
            return false;
        } else {
            return true;
        }
    }
}
