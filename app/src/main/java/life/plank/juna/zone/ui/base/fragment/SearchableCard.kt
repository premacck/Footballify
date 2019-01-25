package life.plank.juna.zone.ui.base.fragment

import android.os.*
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Runnable
import org.jetbrains.anko.sdk27.coroutines.textChangedListener
import rx.Subscription

/**
 * Class for handling searches efficiently - by using a specified delay in the search API call
 */
abstract class SearchableCard : BaseJunaCard() {

    private var handler: Handler = Handler()
    private var searchRunnable: Runnable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareSearchEditText()
    }

    private fun prepareSearchEditText() {
        searchView().textChangedListener {
            onTextChanged { charSequence, _, _, _ ->
                if (!charSequence.toString().isEmpty()) {
                    smartQuery { searchAction(charSequence.toString()) }
                } else {
                    searchedList().clear()
                    searchAdapter()?.notifyDataSetChanged()
                }
            }
        }
    }

    private fun smartQuery(action: () -> Unit) {
        searchRunnable = Runnable(action)
        cancelIfSubscribed()
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed(searchRunnable, searchDelay())
    }

    private fun cancelIfSubscribed() {
        if (searchSubscription()?.isUnsubscribed != false) {
//            If it is subscribed, unsubscribe to the previous call and make a new subscription
            searchSubscription()?.unsubscribe()
        }
    }

    override fun onDestroyView() {
        cancelIfSubscribed()
        handler.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }

    abstract fun searchView(): EditText

    abstract fun searchedList(): MutableList<*>

    abstract fun searchAdapter(): RecyclerView.Adapter<*>?

    abstract fun searchAction(searchString: String)

    abstract fun searchDelay(): Long

    abstract fun searchSubscription(): Subscription?
}
