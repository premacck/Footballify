package life.plank.juna.zone.ui.feed.media

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.*
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_record_audio.*
import life.plank.juna.zone.R
import java.io.IOException

class RecordAudioActivity : AppCompatActivity(), View.OnTouchListener {

    private var permissionToRecordAccepted = false
    private var recorder: MediaRecorder? = null
    private val permissions = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    companion object {
        private val TAG = RecordAudioActivity::class.java.simpleName
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
        private var fileName: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_audio)

        audio_image_view.setBackgroundResource(R.drawable.mic_red)
        start_image_button.setOnTouchListener(this)
        chronometer_text_view.base = SystemClock.elapsedRealtime()
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
        fileName = externalCacheDir!!.absolutePath
        fileName += "/audiorecordtest.3gp"
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_RECORD_AUDIO_PERMISSION -> permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
        }
        if (!permissionToRecordAccepted) finish()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                time_relative_layout.visibility = View.VISIBLE
                tap_to_hold_text_view.visibility = View.INVISIBLE
                startRecording()
                chronometer_text_view.base = SystemClock.elapsedRealtime()
                chronometer_text_view.start()
                return true
            }
            MotionEvent.ACTION_UP -> {
                stopRecording()
                time_relative_layout.visibility = View.INVISIBLE
                tap_to_hold_text_view.visibility = View.VISIBLE
                chronometer_text_view.stop()
                val `in` = Intent(this, PostRecordedAudioActivity::class.java)
                `in`.putExtra(getString(R.string.intent_audio_path), fileName)
                startActivity(`in`)
                finish()
                return true
            }
        }
        return false
    }

    private fun startRecording() {
        recorder = MediaRecorder()
        recorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        recorder?.setOutputFile(fileName)
        recorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        try {
            recorder?.prepare()
            recorder?.start()
            audio_image_view.setBackgroundResource(R.drawable.mic_red_color)
        } catch (e: IOException) {
            Log.e(TAG, "prepare() failed")
        }
    }

    private fun stopRecording() {
        try {
            recorder?.stop()
            recorder?.release()
            recorder = null
            audio_image_view.setBackgroundResource(R.drawable.mic_red)
        } catch (e: Exception) {
            Log.e(TAG, "release() failed")
        }

    }
}