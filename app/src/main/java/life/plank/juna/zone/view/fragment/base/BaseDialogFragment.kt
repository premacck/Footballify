package life.plank.juna.zone.view.fragment.base

import android.support.v4.app.DialogFragment

/**
 * Base class for all the dialog fragments in the app
 * Contains the functionality parts common through all the fragments.
 */
abstract class BaseDialogFragment : DialogFragment() {

    /**
     * Function for child dialog fragment to notify the activity whether they are ready to exit or not
     * @return false when the fragment needs to do it's own actions when back is pressed, true otherwise
     */
    open fun onBackPressed(): Boolean {
        return true
    }
}