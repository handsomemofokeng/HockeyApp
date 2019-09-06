package com.example.android.hockeyapp;

import android.media.Rating;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.hockeyapp.PlayerListFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Player} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {

    private final List<Player> mValues;
    private final OnListFragmentInteractionListener mListener;

    public PlayerAdapter(List<Player> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.player_row_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.player = mValues.get(position);
        holder.tvPlayerNameRow.setText(mValues.get(position).toString());
        holder.tvPlayerContentRow.setText(String.format("Playing for %s", mValues.get(position).getTeamName()));
//        holder.rbPlayerRatingRow.setRating((float) mValues.get(position).getRating());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.player);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tvPlayerNameRow;
        public final TextView tvPlayerContentRow;
//        public final RatingBar rbPlayerRatingRow;
        public Player player;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvPlayerNameRow = view.findViewById(R.id.tvPlayerNameRow);
            tvPlayerContentRow = view.findViewById(R.id.tvPlayerContentRow);
//            rbPlayerRatingRow = view.findViewById(R.id.rbPlayerRatingRow);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvPlayerContentRow.getText() + "'";
        }
    }
}