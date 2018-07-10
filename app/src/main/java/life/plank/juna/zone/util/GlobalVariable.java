package life.plank.juna.zone.util;

/**
 * Created by plank-arfaa on 26/12/17.
 */

public class GlobalVariable {
    //These constants will be used to get particular data from Display Metrics
    static final int DISPLAY_HEIGHT = 100;
    static final int DISPLAY_WIDTH = 101;

    private static GlobalVariable instance;
    private final int DISPLAY_METRICS_ERROR_STATE = -1;
    private Integer tilePosition = 0;

    private GlobalVariable() {
    }

    public static synchronized GlobalVariable getInstance() {
        if (instance == null) {
            instance = new GlobalVariable();
        }
        return instance;
    }

    public int getDisplayHeight() {
        return DISPLAY_HEIGHT;
    }

    public int getDisplayWidth() {
        return DISPLAY_WIDTH;
    }

    public int getDisplayMetricsErrorState() {
        return DISPLAY_METRICS_ERROR_STATE;
    }

    public Integer getTilePosition() {
        return tilePosition;
    }

    public void setTilePosition(Integer tilePosition) {
        this.tilePosition = tilePosition;
    }
}