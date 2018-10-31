package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.RequestManager;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.model.Board;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.view.activity.PrivateBoardActivity;
import life.plank.juna.zone.view.activity.board.CreateBoardActivity;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

import static life.plank.juna.zone.ZoneApplication.getApplication;
import static life.plank.juna.zone.util.PreferenceManager.getSharedPrefs;
import static life.plank.juna.zone.util.PreferenceManager.getToken;

public class UserBoardsAdapter extends RecyclerView.Adapter<UserBoardsAdapter.UserBoardsViewHolder> {
    private static final String TAG = UserBoardsAdapter.class.getSimpleName();
    private List<Board> boardList;
    private Context context;
    private RestApi restApi;
    private RequestManager glide;

    public UserBoardsAdapter(Context context, RestApi restApi, RequestManager glide) {
        this.context = context;
        this.restApi = restApi;
        this.glide = glide;
        this.boardList = new ArrayList<>();
    }

    @NonNull
    @Override
    public UserBoardsAdapter.UserBoardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        getApplication().getUiComponent().inject(this);
        return new UserBoardsAdapter.UserBoardsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_and_title, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserBoardsAdapter.UserBoardsViewHolder holder, int position) {
        try {
            if (Objects.equals(boardList.get(position).getName(), context.getString(R.string.new_))) {
                holder.boardTitle.setText(boardList.get(position).getName());
                holder.boardIcon.setImageDrawable(context.getDrawable(R.drawable.new_board_circle));
                holder.boardIcon.setBorderColor(context.getColor(R.color.white));
                holder.boardIcon.setOnClickListener(view -> navigateToBoard(boardList.get(position).getId(), boardList.get(position).getName()));
            } else {
                holder.boardTitle.setText(boardList.get(position).getName());
                glide.load(boardList.get(position).getBoardIconUrl()).into(holder.boardIcon);
                holder.boardIcon.setBorderColor(Color.parseColor(boardList.get(position).getColor()));
                holder.boardIcon.setOnClickListener(view -> navigateToBoard(boardList.get(position).getId(), boardList.get(position).getName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return boardList.size();
    }

    public void setUserBoards(List<Board> boards) {
        boardList.clear();
        this.boardList.addAll(boards);
        notifyDataSetChanged();
    }

    static class UserBoardsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        CircleImageView boardIcon;
        @BindView(R.id.title)
        TextView boardTitle;

        UserBoardsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void launchBoardMaker() {
        SharedPreferences editor = getSharedPrefs(context.getString(R.string.pref_user_details));
        String username = editor.getString(context.getString(R.string.pref_display_name), context.getString(R.string.na));
        CreateBoardActivity.Companion.launch(context, username);
    }

    private void navigateToBoard(String boardId, String boardName) {
        if (boardName.equals(context.getString(R.string.new_))) {
            launchBoardMaker();
        } else {
            restApi.getBoardById(boardId, getToken())
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
                                    PrivateBoardActivity.launch(context, response.body());
                                    break;
                                default:
                                    Toast.makeText(context, R.string.could_not_navigate_to_board, Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    });
        }
    }
}

