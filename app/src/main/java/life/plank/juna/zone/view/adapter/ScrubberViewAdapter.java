package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daasuu.bl.BubbleLayout;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.ScrubberViewData;
import life.plank.juna.zone.interfaces.ScrubberPointerUpdate;
import life.plank.juna.zone.util.GlobalVariable;
import life.plank.juna.zone.util.ScrubberConstants;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.util.helper.ItemTouchHelperInterface;
import life.plank.juna.zone.util.helper.PopUpWindowHelper;

/**
 * Created by plank-niraj on 26-01-2018.
 */
public class ScrubberViewAdapter extends RecyclerView.Adapter<ScrubberViewAdapter.ScrubberViewHolder> implements ItemTouchHelperInterface {
    public boolean trigger = false;
    public PopupWindow popupWindow, popupWindowPointer;
    ScrubberPointerUpdate scrubberPointerUpdate;
    //Temp data
    HashMap<Integer, String> detailedData;
    HashMap<Integer, ScrubberViewData> scrubberViewDataHolder;
    ArrayList<Integer> scrubberProgressData;
    int matchNumber;
    @BindView(R.id.commentary_text)
    TextView commentaryTextView;
    private int viewHeight = 0;
    private BubbleLayout bubbleLayout;
    private RelativeLayout pointerImageRelativeLayout;
    private int totalWidth;
    private int viewWidth;
    private Context context;
    // TODO: 26-01-2018 use server data.
    private ItemTouchHelper itemTouchHelper;


    public ScrubberViewAdapter(Context context,
                               ArrayList<Integer> data,
                               HashMap<Integer, ScrubberViewData> scrubberViewDataHolder,
                               ScrubberPointerUpdate scrubberPointerUpdate, int matchNumber) {
        this.context = context;
        this.scrubberViewDataHolder = scrubberViewDataHolder;
        this.scrubberPointerUpdate = scrubberPointerUpdate;
        detailedData = new HashMap<>();
        this.scrubberProgressData = data;
        this.matchNumber = matchNumber;
        calculateViewWidth();
        addHardCodedData();
    }

    private void calculateViewWidth() {
        totalWidth = UIDisplayUtil.getDisplayMetricsData(context, GlobalVariable.getInstance().getDisplayWidth());
        viewWidth = totalWidth / ScrubberConstants.getScrubberViewTotalWindow();
    }

    private void addHardCodedData() {
        ScrubberConstants.getHighLightsMatchOne(scrubberViewDataHolder);
    }

    @Override
    public ScrubberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ScrubberConstants.SCRUBBER_VIEW_HALF_TIME:
                return new ScrubberViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.scrubber_view_events_half_time, parent, false));

            case ScrubberConstants.SCRUBBER_VIEW_GOAL:
                return new ScrubberViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.scrubber_view_events_goal, parent, false));

            case ScrubberConstants.SCRUBBER_VIEW_CURSOR:
                return new ScrubberViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.scrubber_view_events_pointer, parent, false));

            case ScrubberConstants.SCRUBBER_VIEW_CARDS:
                return new ScrubberViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.scrubber_view_events_cards, parent, false));

            case ScrubberConstants.SCRUBBER_VIEW_SUBSTITUTE:
                return new ScrubberViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.scrubber_view_events_substitions, parent, false));

            case ScrubberConstants.SCRUBBER_POST_MATCH:
                return new ScrubberViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.scrubber_view_events_post_match, parent, false));

            default:
                return new ScrubberViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.scrubber_view_events_progress, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return scrubberProgressData.get(position);
    }

    @Override
    public void onBindViewHolder(final ScrubberViewHolder holder, final int position) {
        holder.view.getLayoutParams().width = viewWidth;
        if (viewHeight == 0)
            holder.view.post(() -> viewHeight = holder.view.getHeight());
        //To start the drag on touch and swipe.
        holder.view.setOnTouchListener((v, event) -> {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                itemTouchHelper.startDrag(holder);
            }
            return false;
        });
        if (position == scrubberProgressData.size() - 1 && scrubberViewDataHolder.containsKey(position) &&
                scrubberViewDataHolder.get(position).isTriggerEvents()) {
            displayTooltip(holder.view, scrubberViewDataHolder.get(position).getMessage());
        }
        holder.view.setOnClickListener(itemView -> {
            if (scrubberViewDataHolder.containsKey(position) && scrubberViewDataHolder.get(position).isTriggerEvents()) {
                displayTooltip(itemView, scrubberViewDataHolder.get(position).getMessage());
            }
        });
        if (scrubberViewDataHolder.containsKey(position) && scrubberViewDataHolder.get(position).isTriggerEvents()) {
            scrubberPointerUpdate.updateRecentEvents(position);
        }
        if (position == scrubberProgressData.size() - 1) {
            displayPointerImage(holder.view);
        }
    }

    private void displayPointerImage(View view) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int[] itemViewXYLocation = new int[2];
                view.getLocationInWindow(itemViewXYLocation);
                if (popupWindowPointer == null || !popupWindowPointer.isShowing()) {
                    pointerImageRelativeLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.scrubber_view_pointer_image, null);
                    ButterKnife.bind(this, pointerImageRelativeLayout);
                    PopUpWindowHelper<RelativeLayout> popUpWindowHelper = new PopUpWindowHelper<>();
                    popUpWindowHelper.setView(pointerImageRelativeLayout);
                    setUpPopupWindow(ScrubberConstants.getPointerImageDimen(),
                            itemViewXYLocation[0],
                            itemViewXYLocation[1] + viewHeight - ScrubberConstants.getPointerImageDimen()/4,
                            view, popUpWindowHelper);
                    popupWindowPointer = popUpWindowHelper.genericPopUpWindow(context);
                    popupWindowPointer.setFocusable(false);
                    popupWindowPointer.setOutsideTouchable(false);
                } else if (popupWindowPointer != null) {
                    popupWindowPointer.update(itemViewXYLocation[0] - ScrubberConstants.getPointerImageDimen()/2 + viewWidth/2 ,
                            itemViewXYLocation[1] + viewHeight - ScrubberConstants.getPointerImageDimen()/2,
                            ScrubberConstants.getPointerImageDimen(),
                            ScrubberConstants.getPointerImageDimen());
                }
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    /**
     * @param view:  Display on top of view
     * @param status : String to display
     */
    private void displayTooltip(View view, String status) {
        SimpleTooltip simpleTooltip = new SimpleTooltip.Builder(context)
                .anchorView(view)
                .text(status)
                .backgroundColor(ContextCompat.getColor(context, R.color.orange))
                .arrowColor(ContextCompat.getColor(context, R.color.orange))
                .highlightShape(R.drawable.shadow_tooltip)
                .transparentOverlay(true)
                .gravity(Gravity.TOP)
                .build();
        simpleTooltip.show();
        Handler handler = new Handler();
        handler.postDelayed(simpleTooltip::dismiss, 2000);
    }

    @Override
    public int getItemCount() {
        return scrubberProgressData.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition, RecyclerView.ViewHolder target) {
        //TODO: Remove hardcoded code
        trigger = true;
        if (scrubberViewDataHolder.containsKey(target.getLayoutPosition()) &&
                scrubberViewDataHolder.get(target.getLayoutPosition()).isTriggerEvents()) {
            displayTooltipAboveEvent(target.itemView, scrubberViewDataHolder.get(target.getLayoutPosition()).getMessage());
        }
        return false;
    }

    @Override
    public void clearView(int adapterPosition) {
        trigger = false;
    }

    private void displayTooltipAboveEvent(View itemView, String message) {
        int[] itemViewXYLocation = new int[2];
        itemView.getLocationInWindow(itemViewXYLocation);
        displayPointerImage(itemView);
        if (popupWindow == null || !popupWindow.isShowing()) {
            bubbleLayout = (BubbleLayout) LayoutInflater.from(context).inflate(R.layout.custom_tooltip, null);
            ButterKnife.bind(this, bubbleLayout);
            PopUpWindowHelper<BubbleLayout> popUpWindowHelper = new PopUpWindowHelper<>();
            popUpWindowHelper.setView(bubbleLayout);
            setUpPopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT,
                    itemViewXYLocation[0] - ScrubberConstants.getPointerPositionOffset(),
                    itemViewXYLocation[1] - ScrubberConstants.getPopupHeight(),
                    itemView,
                    popUpWindowHelper
            );
            setPopUpTextAndLocation(message, itemViewXYLocation[0]);
            popupWindow = popUpWindowHelper.genericPopUpWindow(context);
        } else if (popupWindow != null) {
            setPopUpTextAndLocation(message, itemViewXYLocation[0]);
            popupWindow.update(itemViewXYLocation[0] - ScrubberConstants.getPointerPositionOffset(),
                    itemViewXYLocation[1] - ScrubberConstants.getPopupHeight(),
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ScrubberConstants.getPopupHeight());
        }
    }

    private void setUpPopupWindow(int width, int viewPositionX, int viewPositionY, View parentView, PopUpWindowHelper<?> popUpWindowHelper) {
        popUpWindowHelper.setPopUpHeight(width);
        popUpWindowHelper.setPopUpWidth(width);
        popUpWindowHelper.setPopUpLocationX(viewPositionX);
        popUpWindowHelper.setPopUpLocationY(viewPositionY);
        popUpWindowHelper.setParentView(parentView);
    }

    private void setPopUpTextAndLocation(String message, int itemViewXLocation) {
        commentaryTextView.setText(message);
        bubbleLayout.setArrowPosition(getPointerPosition(itemViewXLocation));
    }

    public void setItemTouchHelper(ItemTouchHelper itemTouchHelper) {
        this.itemTouchHelper = itemTouchHelper;
    }

    private float getPointerPosition(int itemViewXLocation) {
        return itemViewXLocation + getPopUpWidth() > totalWidth ?
                (itemViewXLocation + getPopUpWidth()) - totalWidth : ScrubberConstants.getPointerPositionOffset();
    }

    private int getPopUpWidth() {
        bubbleLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        return bubbleLayout.getWidth();
    }

    class ScrubberViewHolder extends RecyclerView.ViewHolder {
        View view;

        ScrubberViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.scrubber_view);
        }
    }
}