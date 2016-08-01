package com.jaicob.newssearch.adapters.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaicob.newssearch.R;
import com.jaicob.newssearch.adapters.ArticleArrayAdapter;

public class ImageViewHolder extends RecyclerView.ViewHolder {
    public ImageView ivImage;
    public TextView tvTitle;
    public TextView tvReadingTime;
    public TextView tvPubDate;

    public ImageViewHolder(final View itemView, final ArticleArrayAdapter.OnItemClickListener listener) {
        super(itemView);
        this.ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
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

    public ImageView getIvImage() {
        return ivImage;
    }

    public void setIvImage(ImageView ivImage) {
        this.ivImage = ivImage;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(TextView tvTitle) {
        this.tvTitle = tvTitle;
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
}