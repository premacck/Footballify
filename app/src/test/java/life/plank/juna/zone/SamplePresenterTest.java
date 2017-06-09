package life.plank.juna.zone;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import life.plank.juna.zone.data.network.model.SampleResponseModel;
import life.plank.juna.zone.data.SampleInterface;
import life.plank.juna.zone.data.SamplePresenter;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by dhamini-poorna-chandra on 9/6/2017.
 */

public class SamplePresenterTest {

    @Mock
    private RestApi restApi;

    @Mock
    private SampleInterface.View view;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void fetchValidDataShouldLoadIntoView() {

        SampleResponseModel sampleResponseModel = new SampleResponseModel(0, null, null);

        when(restApi.getCharacters())
                .thenReturn(Observable.just(sampleResponseModel));

        SamplePresenter samplePresenter = new SamplePresenter(
                this.restApi,
                Schedulers.immediate(),
                Schedulers.immediate(),
                this.view
        );

        samplePresenter.loadData();

        InOrder inOrder = Mockito.inOrder(view);
        inOrder.verify(view, times(1)).onFetchDataStarted();
        inOrder.verify(view, times(1)).onFetchDataSuccess(sampleResponseModel);
        inOrder.verify(view, times(1)).onFetchDataCompleted();
    }

    @Test
    public void fetchErrorShouldReturnErrorToView() {

        Exception exception = new Exception();

        when(restApi.getCharacters())
                .thenReturn(Observable.<SampleResponseModel>error(exception));

        SamplePresenter samplePresenter = new SamplePresenter(
                this.restApi,
                Schedulers.immediate(),
                Schedulers.immediate(),
                this.view
        );

        samplePresenter.loadData();

        InOrder inOrder = Mockito.inOrder(view);
        inOrder.verify(view, times(1)).onFetchDataStarted();
        inOrder.verify(view, times(1)).onFetchDataError(exception);
        verify(view, never()).onFetchDataCompleted();
    }

}
