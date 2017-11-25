package life.plank.juna.zone.data.network.model.instagram_model_class;

/**
 * Created by plank-sobia on 11/24/2017.
 */

public class InstagramResponse {
    private Data data;
    private Meta meta;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
