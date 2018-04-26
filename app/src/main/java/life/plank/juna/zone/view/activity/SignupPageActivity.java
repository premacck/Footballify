package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;

public class SignupPageActivity extends AppCompatActivity {

    @BindView(R.id.username_text_input_layout)
    TextInputLayout usernameInputLayout;
    @BindView(R.id.password_input_layout)
    TextInputLayout passwordInputLayout;
    @BindView(R.id.email_input_layout)
    TextInputLayout emailInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        ButterKnife.bind(this);
        setCollapsedHintMiddle(usernameInputLayout);
        setCollapsedHintMiddle(emailInputLayout);
        setCollapsedHintMiddle(passwordInputLayout);
    }

    private void setCollapsedHintMiddle(TextInputLayout textInputLayout) {
        try {
            Field helperField = TextInputLayout.class.getDeclaredField(getString(R.string.decalred_field_parameter));
            helperField.setAccessible(true);
            Object helper = helperField.get(textInputLayout);

            Method setterMethod = helper.getClass().getDeclaredMethod(getString(R.string.declared_method_parameter), int.class);
            setterMethod.setAccessible(true);
            setterMethod.invoke(helper, Gravity.TOP | Gravity.LEFT);
        } catch (NoSuchFieldException e) {
            // TODO
        } catch (IllegalAccessException e) {
            // TODO
        } catch (NoSuchMethodException e) {
            // TODO
        } catch (InvocationTargetException e) {
            // TODO
        }
    }
}