package life.plank.juna.zone.ui.user.profile

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import com.prembros.facilis.dialog.BaseBlurPopup
import com.prembros.facilis.util.isNullOrEmpty
import com.prembros.facilis.util.onDebouncingClick
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.popup_edit_profile.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.api.RestApi
import life.plank.juna.zone.data.api.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.data.model.user.User
import life.plank.juna.zone.sharedpreference.CurrentUser
import life.plank.juna.zone.util.common.AppConstants.SINGLE_SPACE
import life.plank.juna.zone.util.common.errorToast
import life.plank.juna.zone.util.time.DateUtil.getIsoFormattedDate
import java.net.HttpURLConnection.HTTP_NO_CONTENT
import java.text.DateFormatSymbols
import java.util.*
import java.util.Calendar.*
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
        ZoneApplication.application.uiComponent.inject(this)
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
        dob_click_layout.onDebouncingClick { showCalendar() }
        save_button.onDebouncingClick {
            updateUserDetails(User(displayName = name_edit_text.text.toString(),
                    dateOfBirth = if (isNullOrEmpty(dateOfBirth)) CurrentUser.dob else dateOfBirth))
        }
    }

    private fun updateUserDetails(user: User) {
        restApi.updateUserDetails(user)
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
        name_edit_text.setText(CurrentUser.displayName)
        username_edit_text.setText(CurrentUser.handle)
        email_edit_text.setText(CurrentUser.userEmail)
        location_edit_text.setText(CurrentUser.location)
        val date = getIsoFormattedDate(CurrentUser.dob)
        val calendar = Calendar.getInstance()
        calendar.time = date
        val dob = "${calendar.get(DAY_OF_MONTH)} ${DateFormatSymbols().shortMonths[calendar.get(MONTH)]}, ${calendar.get(YEAR)}"
        dob_edit_text.setText(dob)
    }

    private fun showCalendar() {
        val date: Date? = getIsoFormattedDate(CurrentUser.dob)
        val calendar = Calendar.getInstance()
        calendar.time = (date ?: Date())
        val datePickerDialog = DatePickerDialog(context!!, R.style.DatePickerDialogTheme,
                { _, year, monthOfYear, dayOfMonth ->
                    val dob = dayOfMonth.toString() + SINGLE_SPACE + DateFormatSymbols().months[monthOfYear] + ", " + year
                    dateOfBirth = ((year).toString() + "-" + (monthOfYear + 1).toString() + "-" + dayOfMonth)
                    dob_edit_text.setText(dob)
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

        datePickerDialog.updateDate(calendar.get(YEAR), calendar.get(MONTH), calendar.get(DATE))
        datePickerDialog.show()
    }
}
