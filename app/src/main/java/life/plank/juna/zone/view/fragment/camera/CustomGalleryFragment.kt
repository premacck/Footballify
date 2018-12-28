package life.plank.juna.zone.view.fragment.camera

import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import com.prembros.facilis.util.onClick
import kotlinx.android.synthetic.main.fragment_custom_gallery.*
import life.plank.juna.zone.*
import life.plank.juna.zone.R
import life.plank.juna.zone.util.camera.PermissionHandler
import life.plank.juna.zone.util.camera.PermissionHandler.STORAGE_PERMISSION_REQUEST_CODE_GALLERY
import life.plank.juna.zone.util.view.UIDisplayUtil.toggleZone
import life.plank.juna.zone.view.activity.camera.CustomCameraActivity
import life.plank.juna.zone.view.adapter.gallery.GalleryAdapter
import org.jetbrains.anko.*
import pub.devrel.easypermissions.*
import java.util.*

class CustomGalleryFragment : Fragment() {

    companion object {
        private val TAG = CustomGalleryFragment::class.java.simpleName
        fun newInstance() = CustomGalleryFragment()
    }

    private var galleryItems: MutableList<String> = ArrayList()
    private var isForImage: Boolean = true
    private lateinit var galleryOptions: Array<ToggleButton>
    private var isGalleryLoading: Boolean = false

    private lateinit var adapter: GalleryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_custom_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = GalleryAdapter(activity as CustomCameraActivity, isForImage)
        gallery_recycler_view.adapter = adapter

        galleryOptions = arrayOf(action_image, action_video)
        galleryOptions.onClick { toggleView(it as ToggleButton) }
        (if (isForImage) action_image else action_video).performClick()
    }

    override fun onResume() {
        super.onResume()
        loadGallery()
    }

    @AfterPermissionGranted(STORAGE_PERMISSION_REQUEST_CODE_GALLERY)
    private fun loadGallery() {
        if (!isGalleryLoading) {
            if (EasyPermissions.hasPermissions(ZoneApplication.getContext(), *PermissionHandler.STORAGE_PERMISSIONS)) {
                getGalleryItems()
            } else PermissionHandler.requestStoragePermissionsForGallery(activity)
        }
    }

    private fun toggleView(view: ToggleButton) {
        for (galleryOption in galleryOptions) {
            if (galleryOption.id == view.id) {
                if (galleryOption.isChecked) continue

                toggleZone(activity, galleryOption, !galleryOption.isChecked)
                continue
            }
            toggleZone(activity, galleryOption, false)
        }
        isForImage = view.id == R.id.action_image
        loadGallery()
    }

    private fun getGalleryItems() {
        if (isGalleryLoading) return

        isGalleryLoading = true
        doAsync {
            try {
                galleryItems.clear()
                val cursor: Cursor?
                val projection = arrayOf(MediaStore.MediaColumns.DATA)
                val orderByDateTaken = if (isForImage) MediaStore.Images.Media.DATE_TAKEN else MediaStore.Video.Media.DATE_TAKEN

                cursor = ZoneApplication.getContext().contentResolver.query(
                        if (isForImage) MediaStore.Images.Media.EXTERNAL_CONTENT_URI else MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        projection, null, null, "$orderByDateTaken DESC")
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        try {
                            //                        Add Image/Video path in the galleryItem List
                            galleryItems.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)))
                        } catch (e: Exception) {
                            Log.e(TAG, "getGalleryItems(): cursor operation", e)
                        }
                    }
                    cursor.close()
                }
                uiThread {
                    adapter.update(galleryItems, isForImage)
                    isGalleryLoading = false
                }
            } catch (e: Exception) {
                Log.e(TAG, "getGalleryItems(): ", e)
                isGalleryLoading = false
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}