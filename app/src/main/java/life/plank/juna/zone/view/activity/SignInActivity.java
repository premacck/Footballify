package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;

public class SignInActivity extends AppCompatActivity {
    @BindView(R.id.username_text_input_layout)
    TextInputLayout usernameInputLayout;
    @BindView(R.id.password_input_layout)
    TextInputLayout passwordInputLayout;
    @BindView(R.id.submit_button)
    ImageView submitImageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        setCollapsedHintMiddle(usernameInputLayout);
        setCollapsedHintMiddle(passwordInputLayout);
    }

    @OnClick({R.id.forgot_password,R.id.submit_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forgot_password:
                Intent intent = new Intent(SignInActivity.this, AuthForgotPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.submit_button:
                Intent intentSubmit = new Intent(SignInActivity.this, SwipePageActivity.class);
                startActivity(intentSubmit);
                break;
        }
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
