package life.plank.juna.zone.view.notification

import android.content.*
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import com.prembros.facilis.util.*
import kotlinx.android.synthetic.main.shimmer_notification.*
import kotlinx.android.synthetic.main.user_notification.*
import life.plank.juna.zone.*
import life.plank.juna.zone.data.api.*
import life.plank.juna.zone.sharedpreference.CurrentUser
import life.plank.juna.zone.view.base.BaseJunaCardActivity
import retrofit2.Response
import java.net.HttpURLConnection.*
import javax.inject.Inject

class UserNotificationActivity : BaseJunaCardActivity() {

    @Inject
    lateinit var restApi: RestApi

    private lateinit var adapter: NotificationAdapter

    companion object {
        private val TAG = UserNotificationActivity::class.java.simpleName

        fun launch(packageContext: Context) {
            packageContext.startActivity(Intent(packageContext, UserNotificationActivity::class.java))
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_notification)
        ZoneApplication.getApplication().uiComponent.inject(this)

        toolbar.title = getString(R.string.notification)
        toolbar.setProfilePic(CurrentUser.profilePicUrl)

        initRecyclerView()
        getNotifications(false)

        clear_text.onDebouncingClick { clearAllNotifications() }

        swipe_refresh_layout.setOnRefreshListener { getNotifications(true) }
    }

    private fun initRecyclerView() {
        adapter = NotificationAdapter(this)
        notification_recycler_view.adapter = adapter
    }

    private fun getNotifications(isRefreshing: Boolean) {
        onContentLoading()
        restApi.getNotifications()
                .onTerminate { if (isRefreshing) swipe_refresh_layout.isRefreshing = false }
                .setObserverThreadsAndSmartSubscribe({
                    Log.e(TAG, it.message, it)
                    onContentLoaded(false, R.string.failed_to_get_notifications)
                }, {
                    when (it.code()) {
                        HTTP_OK -> it.body()?.run {
                            onContentLoaded(isNotEmpty())
                            adapter.update(this)
                        }
                        HTTP_NOT_FOUND -> onContentLoaded(false)
                        else -> onContentLoaded(false, R.string.failed_to_get_notifications, it)
                    }
                }, this)
    }

    private fun clearAllNotifications() {
        if (!isNullOrEmpty(adapter.notificationList)) {
            onContentLoaded(false)
            restApi.setAllNotificationsAsRead(adapter.notificationList[0].id).execute(this)
            adapter.clear()
        }
    }

    private fun onContentLoading() {
        shimmer_notification.visibility = View.VISIBLE
        shimmer_notification.startShimmerAnimation()
        notification_recycler_view.visibility = View.INVISIBLE
        no_notification.visibility = View.GONE
    }

    private fun onContentLoaded(isSuccessful: Boolean, @StringRes message: Int = R.string.all_caught_up, response: Response<*>? = null) {
        shimmer_notification.visibility = View.GONE
        shimmer_notification.stopShimmerAnimation()
        notification_recycler_view.visibility = if (isSuccessful) View.VISIBLE else View.INVISIBLE
        no_notification.visibility = if (isSuccessful) View.INVISIBLE else View.VISIBLE
        val noNotificationMessage: String =
                if (response != null) {
                    "${getString(message)} : ${response.code()}"
                } else {
                    getString(message)
                }
        if (!isSuccessful) no_notification.text = noNotificationMessage
    }

    override fun restApi(): RestApi? = restApi

    override fun getFragmentContainer(): Int = R.id.notification_fragment_container
}