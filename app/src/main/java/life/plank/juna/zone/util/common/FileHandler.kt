package life.plank.juna.zone.util.common

import android.content.*
import android.graphics.*
import android.net.Uri
import android.os.Environment
import android.os.Environment.*
import android.util.Log
import android.view.View
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication.getContext
import life.plank.juna.zone.util.common.AppConstants.*
import life.plank.juna.zone.util.view.UIDisplayUtil.getScreenshot
import org.apache.commons.io.FileUtils
import org.jetbrains.anko.doAsync
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Class for handling file management, i.e., saving and retrieving a file/image from the app's private storage
 */
class FileHandler {
    companion object {
        fun saveScreenshot(activityTag: String, screenshotView: View, intent: Intent) {
            val fileName = activityTag + getContext().getString(R.string.screenshot_background_suffix)
            intent.putExtra(getContext().getString(R.string.intent_activity_name), activityTag)
            try {
                val fileOutputStream = getContext().openFileOutput(fileName, Context.MODE_PRIVATE)
                val screenshot = getScreenshot(screenshotView)
                screenshot.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                fileOutputStream.close()
                screenshot.recycle()
            } catch (e: Exception) {
                Log.e(fileName, "saveScreenshot() : ", e)
            }

        }

        fun getSavedScreenshot(intent: Intent): Bitmap? {
            val fileName = intent.getStringExtra(getContext().getString(R.string.intent_activity_name)) + getContext().getString(R.string.screenshot_background_suffix)
            return try {
                val fileInputStream = getContext().openFileInput(fileName)
                val bitmap = BitmapFactory.decodeStream(fileInputStream)
                fileInputStream.close()
                bitmap
            } catch (e: Exception) {
                Log.e(fileName, "getSavedBitmap() : ", e)
                null
            }
        }

        @Throws(IOException::class)
        private fun saveBitmap(bitmap: Bitmap, directory: File) {
            val fileOutputStream = FileOutputStream(directory)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.close()
        }

        @Throws(IOException::class)
        private fun saveFile(sourceFile: File, destinationDirectory: File, isDeleteSourceFile: Boolean) {
            doAsync {
                FileUtils.copyFileToDirectory(sourceFile, destinationDirectory)
                if (isDeleteSourceFile) {
                    if (!sourceFile.delete()) {
                        Log.e("DELETE FILE", "Couldn't delete source file")
                    }
                }
            }
        }

        /**
         * Method to create the media folder for this app, if it does not exists
         */
        fun createMediaFolderIfNotExists(isForImage: Boolean): File {
            val mediaFolder = File(
                    Environment.getExternalStoragePublicDirectory(if (isForImage) DIRECTORY_PICTURES else DIRECTORY_MOVIES),
                    getContext().getString(R.string.app_name)
            )
            if (!mediaFolder.exists()) {
                mediaFolder.mkdirs()
            }
            return mediaFolder
        }

        private fun getFilePrefix(isForImage: Boolean): String {
            return (if (isForImage) IMAGE else VIDEO) + UNDERSCORE + SimpleDateFormat(getContext().getString(R.string.media_file_prefix_pattern), Locale.getDefault()).format(Date())
        }

        /**
         * Method to create a new image file in the media folder
         */
        @Throws(IOException::class)
        fun saveImageFile(mediaFolder: File = createMediaFolderIfNotExists(true), bitmap: Bitmap): String {
            val mediaFile = File.createTempFile(getFilePrefix(true), getContext().getString(R.string.jpg_extension), mediaFolder)
            saveBitmap(bitmap, mediaFile)
            updateMediaStoreDatabase(mediaFile.absolutePath)
            return mediaFile.absolutePath
        }

        /**
         * Method to create a new video file in the media folder
         */
        @Throws(IOException::class)
        fun saveVideoFile(isForImage: Boolean, mediaFolder: File, videoFile: File): String {
            val timeStamp = SimpleDateFormat(getContext().getString(R.string.media_file_prefix_pattern), Locale.getDefault()).format(Date())
            val prefix = (if (isForImage) IMAGE else VIDEO) + UNDERSCORE + timeStamp
            val mediaFile = File.createTempFile(prefix, getContext().getString(if (isForImage) R.string.jpg_extension else R.string.mp4_extension), mediaFolder)
            saveFile(videoFile, mediaFile, true)
            updateMediaStoreDatabase(mediaFile.absolutePath)
            return mediaFile.absolutePath
        }

        /**
         * Method for updating the media store database of the device, to make the other apps aware of the new media file
         */
        private fun updateMediaStoreDatabase(videoPath: String) {
            getContext().sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).setData(Uri.fromFile(File(videoPath))))
        }
    }
}