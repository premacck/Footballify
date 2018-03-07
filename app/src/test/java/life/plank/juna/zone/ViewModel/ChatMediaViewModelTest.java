package life.plank.juna.zone.ViewModel;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentProvider;

import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import life.plank.juna.zone.data.network.model.ChatMediaViewData;
import life.plank.juna.zone.viewmodel.ChatMediaViewModel;

import static org.mockito.Mockito.doNothing;

/**
 * Created by plank-niraj on 26-02-2018.
 */

public class ChatMediaViewModelTest{

    @Mock
    Context context;

    @Test
    public void testCases(){

        ArrayList<ChatMediaViewData> media = new ArrayList<>();
        ChatMediaViewModel chatMediaViewModel = new ChatMediaViewModel(context);
        doNothing().when(chatMediaViewModel).getDataFromProvider();
        chatMediaViewModel.getAllMedia(media);

    }
}

/*
class OneQueryMockContentProvider extends MockContentProvider {
    private Cursor queryResult;

    public void addQueryResult(Cursor expectedResult) {
        this.queryResult = expectedResult;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return this.queryResult;
    }
}*/
