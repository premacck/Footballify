package life.plank.juna.zone.view.activity.base;

import life.plank.juna.zone.view.fragment.base.BaseCard;

public abstract class BaseCardActivity extends BaseActivity {

    protected int cardStackCount;

    public abstract void pushCard(BaseCard card);

    public abstract void popCard(BaseCard card);
}