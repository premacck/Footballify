package life.plank.juna.zone.view.fragment.camera

import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_custom_gallery.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.util.camera.PermissionHandler.*
import life.plank.juna.zone.view.adapter.gallery.GalleryAdapter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.util.*

class CustomGalleryFragment : Fragment() {

    companion object {

        private val TAG = CustomGalleryFragment::class.java.simpleName

        fun newInstance(isForImage: Boolean): CustomGalleryFragment {
            val fragment = CustomGalleryFragment()
            val args = Bundle()
            args.putBoolean(ZoneApplication.getContext().getString(R.string.intent_is_camera_for_image), isForImage)
            fragment.arguments = args
            return fragment
        }
    }

    private var isForImage: Boolean = false

    private lateinit var adapter: GalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        if (args != null) {
            isForImage = args.getBoolean(getString(R.string.intent_is_camera_for_image))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_custom_gallery, container, false)
        ButterKnife.bind(this, rootView)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = GalleryAdapter(Glide.with(this))
        gallery_recycler_view.adapter = adapter
    }

    @AfterPermissionGranted(STORAGE_PERMISSION_REQUEST_CODE_GALLERY)
    override fun onResume() {
        super.onResume()
        if (EasyPermissions.hasPermissions(ZoneApplication.getContext(), *STORAGE_PERMISSIONS)) {
            getGalleryItems()
        } else requestStoragePermissionsForGallery(activity)
    }

    private fun getGalleryItems() {
        try {
            doAsync {
                val galleryItems = ArrayList<String>()
                val cursor: Cursor?
                val projection = arrayOf(MediaStore.MediaColumns.DATA)
                val orderByDateTaken = MediaStore.Video.Media.DATE_TAKEN

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
                    uiThread {
                        adapter.update(galleryItems)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "getGalleryItems(): ", e)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}