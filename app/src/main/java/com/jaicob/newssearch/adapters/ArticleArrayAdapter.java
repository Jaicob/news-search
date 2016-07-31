package com.jaicob.newssearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaicob.newssearch.R;
import com.jaicob.newssearch.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Jaicob on 7/27/16.
 */
public class ArticleArrayAdapter extends RecyclerView.Adapter<ArticleArrayAdapter.ViewHolder> {
    private List<Article> articles;
    private Context context;
    private static OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivImage;
        public TextView tvTitle;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            this.tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null)
                        listener.onItemClick(itemView, getLayoutPosition());
                }
            });
        }
    }


    public ArticleArrayAdapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }


    public Context getContext() {
        return context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View articleView = inflater.inflate(R.layout.item_article_result, parent, false);

        ViewHolder viewHolder = new ViewHolder(articleView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article article = articles.get(position);

        TextView textView = holder.tvTitle;
        textView.setText(article.getHeadline());

        ImageView imageView = holder.ivImage;
        imageView.setImageResource(0);
        String thumbnail = article.getThumbnail();

        if (!TextUtils.isEmpty(thumbnail)) {
            Picasso.with(this.context).load(thumbnail).into(imageView);
        }
    }


    @Override
    public int getItemCount() {
        return articles.size();
    }

}
