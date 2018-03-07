package life.plank.juna.zone.view.adapter;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import life.plank.juna.zone.BaseUnitTest;
import life.plank.juna.zone.R;
import life.plank.juna.zone.viewmodel.ChatModel;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * Created by plank-hasan on 2/28/2018.
 */

public class ChatAdapterTest extends BaseUnitTest {
    private final int ITEM_VIEW_INCOMING = 0;
    private final int ITEM_VIEW_OUTGOING = 1;
    @InjectMocks
    private ChatAdapter chatAdapter;
    @Mock
    private Context context;
    @Mock
    private List<ChatModel> chatModels;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        chatAdapter = mock(ChatAdapter.class);
        chatModels = new ArrayList<>();
    }

    @Test
    public void creatingChatAdapterInstanceShouldIncrementTheAdapterItemCount() {
        //In chat adapter constructor data is fed from the ChatModel class which is having 12 items.
        //so equality is checked for 12
        chatAdapter = spy(new ChatAdapter(context));
        assertThat(chatAdapter.getItemCount() == 12, is(true));
    }

    @Test
    public void getItemViewTypeShouldReturnIncomingChatView() {
        //at 0th position of 12 items, isMyMessage flag is set false, so it should return incoming view
        chatAdapter = spy(new ChatAdapter(context));
        assertThat(chatAdapter.getItemViewType(0) == ITEM_VIEW_INCOMING, is(true));
    }

    @Test
    public void getItemViewTypeShouldReturnOutgoingChatView() {
        //at 1st position of 12 items, isMyMessage flag is set true, so it should return outgoing view
        chatAdapter = spy(new ChatAdapter(context));
        assertThat(chatAdapter.getItemViewType(1) == ITEM_VIEW_OUTGOING, is(true));
    }

    @Test
    public void verifyIfTheMediaTypeIsText(){
        chatModels.add(new ChatModel("text", context.getString(R.string.lorem_ipsum_text),"",false));
            assertThat(chatModels.get(0).getTag(), is("text"));
    }
}
