package life.plank.juna.zone.ui.board.fragment.fixture.extra

import android.animation.*
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import life.plank.juna.zone.R
import life.plank.juna.zone.component.customview.CircularTextView

fun ImageView.throwDart(dart_remaining_count_text_view: CircularTextView, remainingDarts: Int) {
    when (id) {
        R.id.thrown_dart_1, R.id.thrown_dart_4 -> dartAnimation(-500f, 600f).start()
        R.id.thrown_dart_2, R.id.thrown_dart_5 -> dartAnimation(500f, 500f).start()
        R.id.thrown_dart_3, R.id.thrown_dart_6 -> dartAnimation(800f, 200f).start()
    }
    dart_remaining_count_text_view.text = (remainingDarts - 1).toString()
}

fun ImageView.dartAnimation(fromX: Float, fromY: Float): ObjectAnimator {
    val dartAnimation = ObjectAnimator.ofPropertyValuesHolder(
            this,
            PropertyValuesHolder.ofFloat(View.TRANSLATION_X, fromX, 0f),
            PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, fromY, 0f)
    ).setDuration(400)
    dartAnimation.interpolator = AccelerateInterpolator()
    visibility = View.VISIBLE
    return dartAnimation
}
