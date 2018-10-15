package life.plank.juna.zone.view.activity.camera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.util.customview.VerticalViewPager;
import life.plank.juna.zone.view.activity.base.StackableCardActivity;
import life.plank.juna.zone.view.fragment.camera.CameraFragment;
import life.plank.juna.zone.view.fragment.camera.CustomGalleryFragment;

import static life.plank.juna.zone.util.UIDisplayUtil.setupSwipeGesture;

public class CustomCameraActivity extends StackableCardActivity {

    @BindView(R.id.drag_area)
    TextView dragArea;
    @BindView(R.id.root_card)
    CardView rootCard;
    @BindView(R.id.faded_card)
    CardView fadedCard;
    @BindView(R.id.blur_background_image_view)
    ImageView blurBackgroundImageView;
    @BindView(R.id.camera_view_pager)
    VerticalViewPager cameraViewPager;

    private boolean isForImage;
    private CameraPagerAdapter pagerAdapter;

    public static void launch(Activity from, boolean isForImage) {
        Intent intent = new Intent(from, CustomCameraActivity.class);
        intent.putExtra(ZoneApplication.getContext().getString(R.string.intent_is_camera_for_image), isForImage);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_camera);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        isForImage = intent.getBooleanExtra(getString(R.string.intent_is_camera_for_image), true);

        setupSwipeGesture(this, dragArea, rootCard, fadedCard);

        setupViewPager();
    }

    private void setupViewPager() {
        pagerAdapter = new CameraPagerAdapter(getSupportFragmentManager(), this);
        cameraViewPager.setAdapter(pagerAdapter);
    }

    static class CameraPagerAdapter extends FragmentPagerAdapter {

        private Fragment currentFragment;
        private final WeakReference<CustomCameraActivity> ref;

        CameraPagerAdapter(FragmentManager fm, CustomCameraActivity activity) {
            super(fm);
            ref = new WeakReference<>(activity);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return CameraFragment.newInstance(ref.get().isForImage);
                default:
                    return CustomGalleryFragment.Companion.newInstance(ref.get().isForImage);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            if (getCurrentFragment() != object) {
                currentFragment = (Fragment) object;
            }
            super.setPrimaryItem(container, position, object);
        }

        Fragment getCurrentFragment() {
            return currentFragment;
        }
    }

    @Override
    public void onBackPressed() {
        if (pagerAdapter.getCurrentFragment() instanceof CustomGalleryFragment) {
            cameraViewPager.setCurrentItem(0);
        } else {
            super.onBackPressed();
        }
    }
}