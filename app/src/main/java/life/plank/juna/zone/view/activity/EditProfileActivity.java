package life.plank.juna.zone.view.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = EditProfileActivity.class.getSimpleName();
    @BindView(R.id.dob_edit_text)
    EditText dobEditText;

    public static void launch(Context packageContext) {
        packageContext.startActivity(new Intent(packageContext, EditProfileActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);

    }

    @OnClick({R.id.dob_edit_text})
    public void dobClicked() {
        showCalendar();
    }

    private void showCalendar() {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) ->
                        dobEditText.setText(dayOfMonth + " " + new DateFormatSymbols().getMonths()[monthOfYear] + ", " + year),
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }
}
