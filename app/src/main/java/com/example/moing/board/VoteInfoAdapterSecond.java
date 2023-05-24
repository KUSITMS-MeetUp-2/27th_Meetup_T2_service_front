package com.example.moing.board;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moing.R;
import com.example.moing.Response.BoardVoteInfoResponse;

import java.util.ArrayList;
import java.util.List;

public class VoteInfoAdapterSecond extends RecyclerView.Adapter<VoteInfoAdapterSecond.VoteSecondViewHolder> {
    private List<BoardVoteInfoResponse.VoteChoice> items; // 리사이클러뷰 안에 들어갈 값 저장
    private List<List<String>> userList;

    public VoteInfoAdapterSecond(List<BoardVoteInfoResponse.VoteChoice> items, List<List<String>> userList) {
        this.items = items;
        this.userList = userList;
    }

    @NonNull
    @Override
    public VoteInfoAdapterSecond.VoteSecondViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_item_vote_second, parent, false);
        return new VoteInfoAdapterSecond.VoteSecondViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoteInfoAdapterSecond.VoteSecondViewHolder holder, int position) {
        /** 해당 위치의 사용자 데이터를 가져와 ViewHolder에 바인딩 **/
        String user = getUserForPosition(position);
        holder.name.setText(user);
    }

    /** 투표를 한 사람들의 모든 수를 반환한다. **/
    @Override
    public int getItemCount() {
        int totalItemCount = 0;
        List<String> check = new ArrayList<>();
        for (List<String> userList : userList) {
            for (String user : userList) {
                if (!check.contains(user)) {
                    check.add(user);
                    totalItemCount++;
                }
            }
        }
        return totalItemCount;
    }

    public class VoteSecondViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        public VoteSecondViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tv_recycle_name);
        }
    }

    /** 이중 List에서 position 값을 통해 사용자 하나의 데이터를 가져온다 **/
    private String getUserForPosition(int position) {
        int count = 0;
        for (List<String> users : userList) {
            if (position < count + users.size()) {
                return users.get(position - count);
            }
            count += users.size();
        }
        return null;
    }
}
