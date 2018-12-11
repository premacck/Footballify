package life.plank.juna.zone.view.fragment.profile

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.popup_edit_profile.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.User
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.common.AppConstants.SINGLE_SPACE
import life.plank.juna.zone.util.common.DataUtil
import life.plank.juna.zone.util.common.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.common.errorToast
import life.plank.juna.zone.util.common.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.util.sharedpreference.PreferenceManager
import life.plank.juna.zone.view.fragment.base.BaseBlurPopup
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.net.HttpURLConnection.HTTP_NO_CONTENT
import java.text.DateFormatSymbols
import java.util.*
import javax.inject.Inject

class EditProfilePopup : BaseBlurPopup() {

    @Inject
    lateinit var restApi: RestApi
    private var dateOfBirth: String = ""

    companion object {
        val TAG: String = EditProfilePopup::class.java.simpleName
        fun newInstance() = EditProfilePopup()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.getApplication().uiComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.popup_edit_profile, container, false)

    override fun doOnStart() {
        setOnClickListeners()
        setUserDetails()
    }

    override fun getBlurLayout(): BlurLayout? = root_blur_layout

    override fun getDragHandle(): View? = drag_area

    override fun getRootView(): View? = root_card

    override fun getBackgroundLayout(): ViewGroup? = root_blur_layout

    private fun setOnClickListeners() {
        dob_edit_text.onClick { showCalendar() }
        save_button.onClick {

            updateUserDetails(User(displayName = name_edit_text.text.toString(),
                    dateOfBirth = if (isNullOrEmpty(dateOfBirth)) PreferenceManager.CurrentUser.getDob() else dateOfBirth,
                    country = PreferenceManager.CurrentUser.getCountry()
                    , city = PreferenceManager.CurrentUser.getCity()))
        }
    }

    private fun updateUserDetails(user: User) {
        restApi.updateUserDetails(PreferenceManager.Auth.getToken(), user)
                .setObserverThreadsAndSmartSubscribe({
                    Log.e("updateUserDetails()", "ERROR: ", it)
                }, {
                    when (it.code()) {
                        HTTP_NO_CONTENT -> {
                            dismiss()
                        }
                        else -> {
                            errorToast(R.string.something_went_wrong, it)
                        }
                    }
                }, this)
    }

    private fun setUserDetails() {
        name_edit_text.setText(PreferenceManager.CurrentUser.getDisplayName())
        username_edit_text.setText(PreferenceManager.CurrentUser.getHandle())
        email_edit_text.setText(PreferenceManager.CurrentUser.getUserEmail())
        location_edit_text.setText(PreferenceManager.CurrentUser.getLocation())
        val date = DataUtil.convertDate(PreferenceManager.CurrentUser.getDob(), getString(R.string.dateformat_yyyy_mm_dd))
        val dob = date.date.toString() + SINGLE_SPACE + DateFormatSymbols().shortMonths[date.month] + ", " + (date.year + 1900)
        dob_edit_text.setText(dob)
    }

    private fun showCalendar() {
        val date = DataUtil.convertDate(PreferenceManager.CurrentUser.getDob(), getString(R.string.dateformat_yyyy_mm_dd))
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(context!!, R.style.DatePickerDialogTheme,
                { _, year, monthOfYear, dayOfMonth ->
                    val dob = dayOfMonth.toString() + SINGLE_SPACE + DateFormatSymbols().months[monthOfYear] + ", " + year
                    dateOfBirth = ((year).toString() + "-" + (monthOfYear + 1).toString() + "-" + dayOfMonth)
                    dob_edit_text.setText(dob)
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

        datePickerDialog.updateDate(date.year + 1900, date.month, date.date)
        datePickerDialog.show()
    }
}
