package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.Board;
import life.plank.juna.zone.view.activity.PrivateBoardActivity;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

import static life.plank.juna.zone.ZoneApplication.getApplication;
import static life.plank.juna.zone.util.PreferenceManager.getToken;

public class UserBoardsAdapter extends RecyclerView.Adapter<UserBoardsAdapter.UserBoardsViewHolder> {
    private static final String TAG = UserBoardsAdapter.class.getSimpleName();
    @Inject
    @Named("default")
    Retrofit retrofit;
    @Inject
    Gson gson;
    private List<Board> boardList;
    private Context context;
    private RestApi restApi;
    private Picasso picasso;

    public UserBoardsAdapter(List<Board> boardList, Context context, Picasso picasso) {
        this.context = context;
        this.boardList = boardList;
        this.picasso = picasso;
    }

    @Override
    public UserBoardsAdapter.UserBoardsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        getApplication().getUiComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        return new UserBoardsAdapter.UserBoardsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_and_title, parent, false));
    }

    @Override
    public void onBindViewHolder(UserBoardsAdapter.UserBoardsViewHolder holder, int position) {
        holder.boardTitle.setText(boardList.get(position).getName());
        picasso.load(boardList.get(position).getBoardIcon().getUrl()).into(holder.boardIcon);
        holder.boardIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToBoard(boardList.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return boardList.size();
    }

    public void update(List<Board> users) {
        boardList.clear();
        this.boardList.addAll(users);
        notifyDataSetChanged();
    }

    public class UserBoardsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView boardIcon;
        @BindView(R.id.title)
        TextView boardTitle;

        UserBoardsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void navigateToBoard(String boardId) {
        restApi.getBoardById(boardId, getToken(context))
                .subscribeOn(rx.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<Board>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(context, R.string.could_not_navigate_to_board, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<Board> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                PrivateBoardActivity.launch(context, gson.toJson(response.body()));
                                break;
                            default:
                                Toast.makeText(context, R.string.could_not_navigate_to_board, Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
    }
}

