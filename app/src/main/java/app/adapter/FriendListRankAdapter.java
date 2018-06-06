package app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.topmission.gomipoi.R;

import java.util.List;

import app.data.http.GomipoiFriendsRankResponse;

/**
 * Created by jerro on 06/02/2018.
 */

public class FriendListRankAdapter extends RecyclerView.Adapter<FriendListRankAdapter.ViewHolder> {

    Context mContext;
    List<GomipoiFriendsRankResponse> mAdapterList;
    public FriendListRankAdapter(Context context, List<GomipoiFriendsRankResponse> listData) {
        mContext = context;
        mAdapterList = listData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_friend_list_rank, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GomipoiFriendsRankResponse item = mAdapterList.get(position);
        holder.tvRankNumber.setText(String.format(mContext.getString(R.string.friend_list_rank_rank), item.getRanking()));
        holder.tvPoints.setText(String.format(mContext.getString(R.string.friend_list_rank_points), item.getPoint()));
        holder.tvName.setText(String.format(mContext.getString(R.string.friend_list_rank_after_name), item.getNickname()));
        holder.tvExtraString.setText(item.getExtraText());
    }

    @Override
    public int getItemCount() {
        return mAdapterList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvRankNumber;
        TextView tvName;
        TextView tvPoints;
        TextView tvExtraString;

        public ViewHolder(View itemView) {
            super(itemView);

            tvRankNumber = itemView.findViewById(R.id.tv_rank_no);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPoints = itemView.findViewById(R.id.tv_points);
            tvExtraString = itemView.findViewById(R.id.tv_extra_string);
        }
    }
}
