package life.plank.juna.zone.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.user_notification.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.util.PreferenceManager
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.view.adapter.common.NotificationAdapter
import java.util.*


class UserNotificationActivity : AppCompatActivity() {

    private lateinit var notificationAdapter: NotificationAdapter
    private val notificationArray = ArrayList<String>()

    //TODO: Remove after integrating with the backend
    private fun populateNotificationArray() {
        notificationArray.add("PikaPerfect has accepted your invite.")
        notificationArray.add("AnnaBeautiful and 36 others has replied to your comment.")
        notificationArray.add("CoolRahlf and 5 others has liked your post.")
        notificationArray.add("PrettyPopo has replied to your comment")
        notificationArray.add("Xuan Xii  Cheng and 12 others has liked your comment.")
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.user_notification)
        (applicationContext as ZoneApplication).uiComponent.inject(this)

        toolbar!!.title = getString(R.string.notification)
        toolbar!!.setProfilePic(PreferenceManager.CurrentUser.getProfilePicUrl())
        populateNotificationArray()
        initRecyclerView()

        clear_text.onDebouncingClick {
            notificationAdapter.clear()
            //TODO: Send request to backend to mark all notification items as read
        }
    }

    private fun initRecyclerView() {
        notificationAdapter = NotificationAdapter(notificationArray)
        notification_recycler_view.layoutManager = LinearLayoutManager(this)
        notification_recycler_view.adapter = notificationAdapter
    }

    companion object {
        private val TAG = UserNotificationActivity::class.java.simpleName

        fun launch(packageContext: Context) {
            packageContext.startActivity(Intent(packageContext, UserNotificationActivity::class.java))
        }
    }
}