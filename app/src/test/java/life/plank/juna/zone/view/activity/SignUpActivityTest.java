package life.plank.juna.zone.view.activity;

import android.test.mock.MockContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.ValidationResult;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by plank-sobia on 10/24/2017.
 */
public class SignUpActivityTest {

    @InjectMocks
    SignUpActivity signUpActivity;

    @Mock
    MockContext mockContext;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void checkUsernameIsValid() {
        when(mockContext.getString(R.string.user_name_regex)).thenReturn("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$");
        assertTrue(signUpActivity.validateUserName("sobia@plank.life", mockContext).isValid());
    }

    @Test
    public void checkUserNameIsInvalidWhenUsernameDoesNotMatchRegularExpression() {
        when(mockContext.getString(R.string.user_name_regex)).thenReturn("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$");
        when(mockContext.getString(R.string.enter_valid_user_name)).thenReturn("Please enter valid user name");

        ValidationResult validationResult = new ValidationResult(false, "Please enter valid user name", null);

        assertArrayEquals(validationResult.getReason().toCharArray(), signUpActivity.validateUserName("username", mockContext).getReason().toCharArray());
        assertFalse(signUpActivity.validateUserName("username", mockContext).isValid());
    }

    @Test
    public void checkUserNameIsInvalidForEmptyString() {
        when(mockContext.getString(R.string.user_name_regex)).thenReturn("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$");
        when(mockContext.getString(R.string.enter_valid_user_name)).thenReturn("Please enter valid user name");

        ValidationResult validationResult = new ValidationResult(false, "Please enter valid user name", null);

        assertArrayEquals(validationResult.getReason().toCharArray(), signUpActivity.validateUserName(" ", mockContext).getReason().toCharArray());
        assertFalse(signUpActivity.validateUserName(" ", mockContext).isValid());
    }

    @Test
    public void checkNameIsValid() {
        when(mockContext.getString(R.string.name_regex)).thenReturn("^[A-Za-z]*$");
        assertTrue(signUpActivity.validateName("Sobia", mockContext).isValid());
    }

    @Test
    public void checkNameIsInvalidWhenUsernameDoesNotMatchRegularExpression() {
        when(mockContext.getString(R.string.name_regex)).thenReturn("^[A-Za-z]*$");
        when(mockContext.getString(R.string.enter_valid_name)).thenReturn("Please enter valid name");

        ValidationResult validationResult = new ValidationResult(false, "Please enter valid name", null);

        assertArrayEquals(validationResult.getReason().toCharArray(), signUpActivity.validateName("Sobia123", mockContext).getReason().toCharArray());
        assertFalse(signUpActivity.validateName("Sobia123", mockContext).isValid());
    }

    @Test
    public void checkNameIsInvalidForEmptyString() {
        when(mockContext.getString(R.string.name_regex)).thenReturn("^[A-Za-z]*$");
        when(mockContext.getString(R.string.enter_valid_name)).thenReturn("Please enter valid name");

        ValidationResult validationResult = new ValidationResult(false, "Please enter valid name", null);

        assertArrayEquals(validationResult.getReason().toCharArray(), signUpActivity.validateName(" ", mockContext).getReason().toCharArray());
        assertFalse(signUpActivity.validateName(" ", mockContext).isValid());
    }

    @Test
    public void checkPasswordIsValid() {
        assertTrue(signUpActivity.validatePassword("password", mockContext).isValid());
    }

    @Test
    public void checkPasswordIsInvalidWhenPasswordIsEmptyString() {
        when(mockContext.getString(R.string.enter_password)).thenReturn("Please enter a password");

        ValidationResult validationResult = new ValidationResult(false, "Please enter a password", null);

        assertArrayEquals(validationResult.getReason().toCharArray(), signUpActivity.validatePassword("", mockContext).getReason().toCharArray());
        assertFalse(signUpActivity.validatePassword(" ", mockContext).isValid());
    }

    @Test
    public void checkPasswordIsInvalidWhenPasswordStringContainsSpaces() {
        when(mockContext.getString(R.string.enter_valid_password)).thenReturn("Please enter a valid password");

        ValidationResult validationResult = new ValidationResult(false, "Please enter a valid password", null);

        assertArrayEquals(validationResult.getReason().toCharArray(), signUpActivity.validatePassword("pass word", mockContext).getReason().toCharArray());
        assertFalse(signUpActivity.validatePassword("pass word", mockContext).isValid());
    }

    @Test
    public void checkConfirmPasswordIsValid() {
        assertTrue(signUpActivity.validateConfirmPassword("password", "password", mockContext).isValid());
    }

    @Test
    public void checkConfirmPasswordIsInvalidWhenConfirmPasswordIsEmptyString() {
        when(mockContext.getString(R.string.confirm_password)).thenReturn("Please confirm password");

        ValidationResult validationResult = new ValidationResult(false, "Please confirm password", null);

        assertArrayEquals(validationResult.getReason().toCharArray(), signUpActivity.validateConfirmPassword("", "", mockContext).getReason().toCharArray());
        assertFalse(signUpActivity.validateConfirmPassword("password", "", mockContext).isValid());
    }

    @Test
    public void checkConfirmPasswordIsInvalidWhenPasswordsMismatch() {
        when(mockContext.getString(R.string.passwords_mismatch)).thenReturn("Passwords don't match");

        ValidationResult validationResult = new ValidationResult(false, "Passwords don't match", null);

        assertArrayEquals(validationResult.getReason().toCharArray(), signUpActivity.validateConfirmPassword("password", "pass word", mockContext).getReason().toCharArray());
        assertFalse(signUpActivity.validateConfirmPassword("password", "pass word", mockContext).isValid());
    }
}