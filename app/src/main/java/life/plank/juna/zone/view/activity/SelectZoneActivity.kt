package life.plank.juna.zone.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_select_zone.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.Zones
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.common.errorToast
import life.plank.juna.zone.util.common.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.Auth.getToken
import life.plank.juna.zone.view.activity.home.HomeActivity
import life.plank.juna.zone.view.adapter.onboarding.SelectZoneAdapter
import java.net.HttpURLConnection
import java.util.*
import javax.inject.Inject

class SelectZoneActivity : AppCompatActivity() {

    @Inject
    lateinit var restApi: RestApi

    private lateinit var selectZoneAdapter: SelectZoneAdapter
    private val zonesList = ArrayList<Zones>()
    internal var zone = Zones()

    companion object {
        private val TAG = SelectZoneActivity::class.java.simpleName
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_zone)
        (applicationContext as ZoneApplication).uiComponent.inject(this)

        retrieveZones()
        initRecyclerView()

        follow_button.onDebouncingClick {
            zone.zoneIds = selectZoneAdapter.selectedZoneNames
            followZones(zone)
        }
    }

    private fun initRecyclerView() {
        selectZoneAdapter = SelectZoneAdapter(zonesList, Glide.with(this))
        val gridLayoutManager = GridLayoutManager(this, 2, VERTICAL, false)
        board_recycler_view.layoutManager = gridLayoutManager
        board_recycler_view.adapter = selectZoneAdapter
    }

    private fun retrieveZones() {
        restApi.retrieveZones().setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "retrieveZones() : onError() $it")
        }, {
            when (it.code()) {
                HttpURLConnection.HTTP_OK -> setUpAdapterWithNewData(it.body())
                HttpURLConnection.HTTP_NOT_FOUND -> errorToast(R.string.failed_to_retrieve_zones, it)
                else -> errorToast(R.string.something_went_wrong, it)
            }
        })
    }

    private fun followZones(zones: Zones) {
        restApi.followZones(getToken(), zones).setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "onError: $it")
        }, {
            when (it.code()) {
                HttpURLConnection.HTTP_OK -> {
                    startActivity(Intent(this@SelectZoneActivity, HomeActivity::class.java))
                    finish()
                }
                else -> errorToast(R.string.something_went_wrong, it)
            }
        })
    }

    private fun setUpAdapterWithNewData(zonesList: List<Zones>?) {
        this.zonesList.clear()
        this.zonesList.addAll(zonesList!!)
        selectZoneAdapter.notifyDataSetChanged()
    }
}