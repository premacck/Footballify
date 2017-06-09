package life.plank.juna.zone.data.network.model;

/**
 * Created by dhamini-poorna-chandra on 6/9/2017.
 */

public class SampleResponseModel {

    public final int count;
    public final String next;
    public final String previous;

    public SampleResponseModel(int count, String next, String previous) {
        this.count = count;
        this.next = next;
        this.previous = previous;
    }
}
