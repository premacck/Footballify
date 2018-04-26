package life.plank.juna.zone.util;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.view.Gravity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import life.plank.juna.zone.R;

/**
 * Created by plank-sharath on 4/26/2018.
 */

public class ActivityUtil {
    public static void setCollapsedHintMiddle(TextInputLayout textInputLayout, Context context) {
        try {
            Field helperField = TextInputLayout.class.getDeclaredField(context.getString(R.string.decalred_field_parameter));
            helperField.setAccessible(true);
            Object helper = helperField.get(textInputLayout);

            Method setterMethod = helper.getClass().getDeclaredMethod(context.getString(R.string.declared_method_parameter), int.class);
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

