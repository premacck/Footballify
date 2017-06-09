package life.plank.juna.zone.data;

import android.support.annotation.NonNull;

import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.SampleResponseModel;
import rx.Observer;
import rx.Scheduler;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dhamini-poorna-chandra on 6/9/2017.
 */

public class SamplePresenter implements SampleInterface.Presenter {

    @NonNull
    private RestApi restApi;

    @NonNull
    private Scheduler backgroundScheduler;

    @NonNull
    private Scheduler mainScheduler;

    @NonNull
    private CompositeSubscription subscriptions;

    private SampleInterface.View view;

    public SamplePresenter(
        @NonNull RestApi restApi,
        @NonNull Scheduler backgroundScheduler,
        @NonNull Scheduler mainScheduler,
        SampleInterface.View view) {
        this.restApi = restApi;
        this.backgroundScheduler = backgroundScheduler;
        this.mainScheduler = mainScheduler;
        this.view = view;
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void loadData() {
        view.onFetchDataStarted();
        subscriptions.clear();

        Subscription subscription = restApi
                .getCharacters()
                .subscribeOn(backgroundScheduler)
                .observeOn(mainScheduler)
                .subscribe(new Observer<SampleResponseModel>() {
                    @Override
                    public void onCompleted() {
                        view.onFetchDataCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onFetchDataError(e);
                    }

                    @Override
                    public void onNext(SampleResponseModel rootModel) {
                        view.onFetchDataSuccess(rootModel);
                    }
                });

        subscriptions.add(subscription);

    }
}
