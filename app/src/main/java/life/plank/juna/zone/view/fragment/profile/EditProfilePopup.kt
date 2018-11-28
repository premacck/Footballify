package life.plank.juna.zone.view.fragment.profile

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.popup_edit_profile.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.PreferenceManager
import life.plank.juna.zone.view.fragment.base.BaseBlurPopup
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.text.DateFormatSymbols
import java.util.*
import javax.inject.Inject

class EditProfilePopup : BaseBlurPopup() {

    @Inject
    lateinit var restApi: RestApi

    companion object {
        val TAG: String = EditProfilePopup::class.java.simpleName
        fun newInstance() = EditProfilePopup()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.getApplication().uiComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.popup_edit_profile, container, false)

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
        save_button.onClick { dismiss() }
    }

    private fun setUserDetails() {
        name_edit_text.setText(PreferenceManager.CurrentUser.getDisplayName())
        username_edit_text.setText(PreferenceManager.CurrentUser.getHandle())
        email_edit_text.setText(PreferenceManager.CurrentUser.getUserEmail())
        location_edit_text.setText(PreferenceManager.CurrentUser.getLocation())
    }

    private fun showCalendar() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(context!!,
                { _, year, monthOfYear, dayOfMonth ->
                    val dob = dayOfMonth.toString() + " " + DateFormatSymbols().months[monthOfYear] + ", " + year
                    dob_edit_text.setText(dob)
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }

}
