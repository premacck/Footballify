package life.plank.juna.zone.data;

import life.plank.juna.zone.data.network.model.SampleResponseModel;

/**
 * Created by dhamini-poorna-chandra on 6/9/2017.
 */

public interface SampleInterface {

    interface View {

        void onFetchDataStarted();

        void onFetchDataCompleted();

        void onFetchDataSuccess(SampleResponseModel sampleResponseModel);

        void onFetchDataError(Throwable e);
    }

    interface Presenter {

        void loadData();

    }
}
