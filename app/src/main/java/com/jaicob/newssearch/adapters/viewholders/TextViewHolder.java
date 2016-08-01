package com.jaicob.newssearch.adapters.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jaicob.newssearch.R;
import com.jaicob.newssearch.adapters.ArticleArrayAdapter;

/**
 * Created by Jaicob on 7/31/16.
 */
public class TextViewHolder extends RecyclerView.ViewHolder {
    public TextView tvTitle;
    public TextView tvReadingTime;
    public TextView tvPubDate;

    public TextViewHolder(final View itemView, final ArticleArrayAdapter.OnItemClickListener listener) {
        super(itemView);
        this.tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        this.tvReadingTime = (TextView) itemView.findViewById(R.id.tvReadTime);
        this.tvPubDate = (TextView) itemView.findViewById(R.id.tvDate);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Triggers click upwards to the adapter on click
                if (listener != null)
                    listener.onItemClick(itemView, getLayoutPosition());
            }
        });
    }

    public TextView getTvReadingTime() {
        return tvReadingTime;
    }

    public void setTvReadingTime(TextView tvReadingTime) {
        this.tvReadingTime = tvReadingTime;
    }

    public TextView getTvPubDate() {
        return tvPubDate;
    }

    public void setTvPubDate(TextView tvPubDate) {
        this.tvPubDate = tvPubDate;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(TextView tvTitle) {
        this.tvTitle = tvTitle;
    }

}