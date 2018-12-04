package com.jge.topratedmovies.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jge.topratedmovies.Models.Trailer;
import com.jge.topratedmovies.R;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder>{
    private List<Trailer> trailers;
    private final TrailerAdapter.ListItemClickListener mOnClickListener;


    public TrailerAdapter(TrailerAdapter.ListItemClickListener mOnClickListener){
        this.mOnClickListener = mOnClickListener;
    }

    public interface ListItemClickListener{
        void onListItemClick(Trailer trailerNameIndexClicked);
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView trailerNameTextView;

        public TrailerAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            trailerNameTextView = itemView.findViewById(R.id.trailer_name_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mOnClickListener.onListItemClick(trailers.get(position));
        }
    }


    @Override
    public TrailerAdapter.TrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);

        return new TrailerAdapter.TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.TrailerAdapterViewHolder trailerAdapterViewHolder, int i) {
        Trailer trailer = trailers.get(i);
        trailerAdapterViewHolder.trailerNameTextView.setText(trailer.getName());

    }

    @Override
    public int getItemCount() {
        if(trailers == null) return 0;
        return trailers.size();
    }

    public void setTrailerData(List<Trailer> trailerData){
        trailers = trailerData;
        notifyDataSetChanged();
    }
}
