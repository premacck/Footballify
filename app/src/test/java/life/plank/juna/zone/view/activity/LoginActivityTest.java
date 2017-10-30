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
public class LoginActivityTest {

    @InjectMocks
    LoginActivity loginActivity;

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
        assertTrue(loginActivity.validateUserName("sobia@plank.life", mockContext).isValid());
    }

    @Test
    public void checkUserNameIsInvalidWhenUsernameDoesNotMatchRegularExpression() {
        when(mockContext.getString(R.string.user_name_regex)).thenReturn("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$");
        when(mockContext.getString(R.string.enter_valid_user_name)).thenReturn("Please enter valid user name");

        ValidationResult validationResult = new ValidationResult(false, "Please enter valid user name", null);

        assertArrayEquals(validationResult.getReason().toCharArray(), loginActivity.validateUserName("username", mockContext).getReason().toCharArray());
        assertFalse(loginActivity.validateUserName("username", mockContext).isValid());
    }

    @Test
    public void checkUserNameIsInvalidForEmptyString() {
        when(mockContext.getString(R.string.user_name_regex)).thenReturn("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$");
        when(mockContext.getString(R.string.enter_valid_user_name)).thenReturn("Please enter valid user name");

        ValidationResult validationResult = new ValidationResult(false, "Please enter valid user name", null);

        assertArrayEquals(validationResult.getReason().toCharArray(), loginActivity.validateUserName(" ", mockContext).getReason().toCharArray());
        assertFalse(loginActivity.validateUserName(" ", mockContext).isValid());
    }

    @Test
    public void checkPasswordIsValid() {
        assertTrue(loginActivity.validatePassword("password", mockContext).isValid());
    }

    @Test
    public void checkPasswordIsInvalidWhenPasswordIsEmptyString() {
        when(mockContext.getString(R.string.enter_password)).thenReturn("Please enter a password");

        ValidationResult validationResult = new ValidationResult(false, "Please enter a password", null);

        assertArrayEquals(validationResult.getReason().toCharArray(), loginActivity.validatePassword(" ", mockContext).getReason().toCharArray());
        assertFalse(loginActivity.validatePassword(" ", mockContext).isValid());
    }

    @Test
    public void checkPasswordIsInvalidWhenPasswordStringContainsSpaces() {
        when(mockContext.getString(R.string.enter_password)).thenReturn("Please enter a password");

        ValidationResult validationResult = new ValidationResult(false, "Please enter a password", null);

        assertArrayEquals(validationResult.getReason().toCharArray(), loginActivity.validatePassword("pass word", mockContext).getReason().toCharArray());
        assertFalse(loginActivity.validatePassword("pass word", mockContext).isValid());
    }
}