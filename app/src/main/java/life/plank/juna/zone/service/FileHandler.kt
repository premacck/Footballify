package life.plank.juna.zone.service

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.os.Environment.*
import android.util.Log
import android.view.View
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication.Companion.appContext
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
            val fileName = activityTag + appContext.getString(R.string.screenshot_background_suffix)
            intent.putExtra(appContext.getString(R.string.intent_activity_name), activityTag)
            try {
                val fileOutputStream = appContext.openFileOutput(fileName, Context.MODE_PRIVATE)
                val screenshot = getScreenshot(screenshotView)
                screenshot.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                fileOutputStream.close()
                screenshot.recycle()
            } catch (e: Exception) {
                Log.e(fileName, "saveScreenshot() : ", e)
            }

        }

        fun getSavedScreenshot(intent: Intent): Bitmap? {
            val fileName = intent.getStringExtra(appContext.getString(R.string.intent_activity_name)) + appContext.getString(R.string.screenshot_background_suffix)
            return try {
                val fileInputStream = appContext.openFileInput(fileName)
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
                    appContext.getString(R.string.app_name)
            )
            if (!mediaFolder.exists()) {
                mediaFolder.mkdirs()
            }
            return mediaFolder
        }

        private fun getFilePrefix(isForImage: Boolean): String {
            return (if (isForImage) IMAGE else VIDEO) + UNDERSCORE + SimpleDateFormat(appContext.getString(R.string.media_file_prefix_pattern), Locale.getDefault()).format(Date())
        }

        /**
         * Method to create a new image file in the media folder
         */
        @Throws(IOException::class)
        fun saveImageFile(mediaFolder: File = createMediaFolderIfNotExists(true), bitmap: Bitmap): String {
            val mediaFile = File.createTempFile(getFilePrefix(true), appContext.getString(R.string.jpg_extension), mediaFolder)
            saveBitmap(bitmap, mediaFile)
            updateMediaStoreDatabase(mediaFile.absolutePath)
            return mediaFile.absolutePath
        }

        /**
         * Method to create a new video file in the media folder
         */
        @Throws(IOException::class)
        fun saveVideoFile(isForImage: Boolean, mediaFolder: File, videoFile: File): String {
            val timeStamp = SimpleDateFormat(appContext.getString(R.string.media_file_prefix_pattern), Locale.getDefault()).format(Date())
            val prefix = (if (isForImage) IMAGE else VIDEO) + UNDERSCORE + timeStamp
            val mediaFile = File.createTempFile(prefix, appContext.getString(if (isForImage) R.string.jpg_extension else R.string.mp4_extension), mediaFolder)
            saveFile(videoFile, mediaFile, true)
            updateMediaStoreDatabase(mediaFile.absolutePath)
            return mediaFile.absolutePath
        }

        /**
         * Method for updating the media store database of the device, to make the other apps aware of the new media file
         */
        private fun updateMediaStoreDatabase(videoPath: String) {
            appContext.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).setData(Uri.fromFile(File(videoPath))))
        }
    }
}