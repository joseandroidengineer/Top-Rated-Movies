package com.jge.topratedmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class UserReviewAdapter extends RecyclerView.Adapter<UserReviewAdapter.UserReviewAdapterViewHolder>{
    private List<UserReview> userReviews;
    private final UserReviewAdapter.ListItemClickListener mOnClickListener;


    public UserReviewAdapter(UserReviewAdapter.ListItemClickListener mOnClickListener){
        this.mOnClickListener = mOnClickListener;
        }

    public interface ListItemClickListener {
        void onListItemClick(UserReview userReviewNameIndexClicked);
    }

    public class UserReviewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView userReviewAuthorTextView;
    public TextView userReviewContentTextView;

    public UserReviewAdapterViewHolder(@NonNull View itemView) {
        super(itemView);
        userReviewAuthorTextView = itemView.findViewById(R.id.author_tv);
        userReviewContentTextView = itemView.findViewById(R.id.content_tv);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int position = getAdapterPosition();
        mOnClickListener.onListItemClick(userReviews.get(position));
    }

}


    @Override
    public UserReviewAdapter.UserReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.user_reviews_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);

        return new UserReviewAdapter.UserReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserReviewAdapter.UserReviewAdapterViewHolder userReviewAdapterViewHolder, int i) {
        UserReview userReview = userReviews.get(i);
        userReviewAdapterViewHolder.userReviewAuthorTextView.setText(userReview.getAuthor());
        userReviewAdapterViewHolder.userReviewContentTextView.setText(userReview.getContent());
    }

    @Override
    public int getItemCount() {
        if (userReviews == null) return 0;
        return userReviews.size();
    }

    public void setUserReviewData(List<UserReview> userReviewData) {
        userReviews = userReviewData;
        notifyDataSetChanged();
    }
}
