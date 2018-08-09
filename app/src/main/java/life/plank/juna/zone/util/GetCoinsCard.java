package life.plank.juna.zone.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import life.plank.juna.zone.R;

import static life.plank.juna.zone.util.UIDisplayUtil.getDp;

public class GetCoinsCard extends RelativeLayout {

    TextView coinAmountView;
    ObliqueStrikeTextView earlierPriceView;
    TextView currentPriceView;

    public GetCoinsCard(Context context) {
        this(context, null);
    }

    public GetCoinsCard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GetCoinsCard(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public GetCoinsCard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.get_coins_card, this);
        coinAmountView = findViewById(R.id.coin_amount);
        earlierPriceView = findViewById(R.id.earlier_price);
        currentPriceView = findViewById(R.id.current_price);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GetCoinsCard);
        try {
            setCoinAmountView(array.getString(R.styleable.GetCoinsCard_coinAmount));
            setEarlierPriceView(array.getString(R.styleable.GetCoinsCard_earlierPrice));
            setCurrentPriceView(array.getString(R.styleable.GetCoinsCard_currentPrice));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (array != null) array.recycle();
        }
    }

    public String getCoinAmountView() {
        return coinAmountView.getText().toString();
    }

    /**
     * To set the amount of coins
     * @param coinAmount the amount of coins to set. To make this field disappear, set coin amount to zero.
     */
    public void setCoinAmountView(String coinAmount) {
        if (coinAmount != null && !coinAmount.isEmpty()) {
            String realCoinAmount = "  " + coinAmount;
            coinAmountView.setText(realCoinAmount);
        } else {
            coinAmountView.setText(null);
        }
    }

    public String getEarlierPriceView() {
        return earlierPriceView.getText().toString();
    }

    /**
     * To set the earlier price of coins, that should come in strikethrough text.
     * @param earlierPrice the earlier price of coins to set. To make this field disappear, set earlierPrice to null.
     */
    public void setEarlierPriceView(String earlierPrice) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.coinAmountView.getLayoutParams();
        if (earlierPrice != null && !earlierPrice.isEmpty()) {
            String realEarlierPrice = earlierPrice + "    ";
            earlierPriceView.setText(realEarlierPrice);
            params.leftMargin = (int) getDp(getContext(), 4);
        } else {
            earlierPriceView.setText(null);
            params.leftMargin = (int) getDp(getContext(), 0);
        }
    }

    public String getCurrentPriceView() {
        return currentPriceView.getText().toString();
    }

    /**
     * To set the currentPrice price of coins.
     * @param currentPrice the earlier price of coins to set. To make this field disappear, set earlierPrice to null.
     */
    public void setCurrentPriceView(String currentPrice) {
        currentPriceView.setText(currentPrice);
    }
}