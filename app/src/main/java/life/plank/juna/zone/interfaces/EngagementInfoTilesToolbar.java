package life.plank.juna.zone.interfaces;

import android.support.annotation.DrawableRes;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

public interface EngagementInfoTilesToolbar {

    void setLeagueLogo(String logoUrl);

    void setLeagueLogo(@DrawableRes int resource);

    void setPeopleCount(String peopleCount);

    void setCommentCount(String commentsCount);

    void setBoardTitle(String boardTitle);

    void showLock(boolean showLock);

    boolean isFollowing();

    void setFollowing(boolean isFollowing);

    TabLayout getInfoTilesTabLayout();

    void setupWithViewPager(ViewPager viewPager);
}