package life.plank.juna.zone.view.fragment.base

import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import org.jetbrains.anko.support.v4.runOnUiThread

/**
 * Base class for all the dialog fragments in the app
 * Contains the functionality parts common through all the fragments.
 */
abstract class BaseDialogFragment : androidx.fragment.app.DialogFragment() {

    @Suppress("DeferredResultUnused")
    fun smartDismiss(afterDismissAction: () -> Unit) {
        dismiss()
        async {
            delay(280)
            runOnUiThread { afterDismissAction() }
        }
    }

    /**
     * Function for child dialog fragment to notify the activity whether they are ready to exit or not
     * @return false when the fragment needs to do it's own actions when back is pressed, true otherwise
     */
    open fun onBackPressed(): Boolean {
        return true
    }
}