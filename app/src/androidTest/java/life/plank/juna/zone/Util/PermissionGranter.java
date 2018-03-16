package life.plank.juna.zone.Util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.IdlingResource;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.v4.content.ContextCompat;

import java.util.concurrent.TimeUnit;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

/**
 * Created by plank-hasan on 3/16/2018.
 */

public class PermissionGranter {
    //TODO: needs better solution for Idling the resources
    //TODO: try catch will be removed once a better solution is found
    private static final int PERMISSIONS_DIALOG_DELAY = 300;
    private static final int GRANT_BUTTON_INDEX = 1;
    private int waitingTime = 10;
    private static IdlingResource idlingResource;

    public static void allowPermissionsIfNeeded(String permissionNeeded) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !hasNeededPermission(permissionNeeded)) {
           // sleep(PERMISSIONS_DIALOG_DELAY);
            IdlingPolicies.setMasterPolicyTimeout(60, TimeUnit.SECONDS);
            IdlingPolicies.setIdlingResourceTimeout(26, TimeUnit.SECONDS);
            idlingResource = new ElapsedTimeIdlingResource(PERMISSIONS_DIALOG_DELAY);
            try {
                Espresso.registerIdlingResources(idlingResource);
                UiDevice device = UiDevice.getInstance(getInstrumentation());
                UiObject allowPermissions = device.findObject(new UiSelector()
                        .clickable(true)
                        .checkable(false)
                        .index(GRANT_BUTTON_INDEX));
                if (allowPermissions.exists()) {
                    allowPermissions.click();
                }
                Espresso.unregisterIdlingResources(idlingResource);
            } catch (UiObjectNotFoundException e) {
                e.printStackTrace();
                Espresso.unregisterIdlingResources(idlingResource);
            }
        }
    }

    private static boolean hasNeededPermission(String permissionNeeded) {
        Context context = InstrumentationRegistry.getTargetContext();
        int permissionStatus = ContextCompat.checkSelfPermission(context, permissionNeeded);
        return permissionStatus == PackageManager.PERMISSION_GRANTED;
    }

    private static void sleep(long millis) {
        IdlingPolicies.setMasterPolicyTimeout(60, TimeUnit.SECONDS);
        IdlingPolicies.setIdlingResourceTimeout(26, TimeUnit.SECONDS);
        idlingResource = new ElapsedTimeIdlingResource(millis);
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException("Cannot execute Thread.sleep()");
        }
    }
}