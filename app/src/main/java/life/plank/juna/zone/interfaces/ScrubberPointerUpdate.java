package life.plank.juna.zone.interfaces;

import android.view.View;

/**
 * Created by plank-niraj on 20-02-2018.
 */

public interface ScrubberPointerUpdate {

    void moveScrubberPointer(View view, int position);

    void addCommentary(int position);
}
