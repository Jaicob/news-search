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
import com.jaicob.newssearch.adapters.viewholders.ImageViewHolder;
import com.jaicob.newssearch.adapters.viewholders.TextViewHolder;
import com.jaicob.newssearch.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Jaicob on 7/27/16.
 */
public class ArticleArrayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Article> articles;
    private Context context;
    private static OnItemClickListener listener;
    private final int IMAGE = 0, TEXT = 1;

    public interface OnItemClickListener{
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public ArticleArrayAdapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }


    public Context getContext() {
        return context;
    }


    @Override
    public int getItemViewType(int position) {
        String thumbnail = articles.get(position).getThumbnail();
        if (thumbnail.isEmpty()){
            return TEXT;
        } else {
            return IMAGE;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);

        switch (viewType) {
            case IMAGE:
                View v2 = inflater.inflate(R.layout.item_article_result, viewGroup, false);
                viewHolder = new ImageViewHolder(v2,listener);
                break;
            default:
                View v1 = inflater.inflate(R.layout.item_article_result_text, viewGroup, false);
                viewHolder = new TextViewHolder(v1,listener);
                break;
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case IMAGE:
                ImageViewHolder vh2 = (ImageViewHolder) viewHolder;
                configureImageViewHolder(vh2,position);
                break;
            default:
                TextViewHolder vh1 = (TextViewHolder) viewHolder;
                configureTextViewHolder(vh1, position);
                break;
        }
    }


    private void configureTextViewHolder(TextViewHolder viewHolder, int position){
        Article article = articles.get(position);
        TextView title = viewHolder.tvTitle;
        TextView readingTime = viewHolder.tvReadingTime;
        TextView date = viewHolder.tvPubDate;
        title.setText(article.getHeadline());
        title.setMaxLines(3);
        readingTime.setText(article.getReadTime());
        date.setText(article.getPubDate());
    }


    private void configureImageViewHolder(ImageViewHolder viewHolder, int position){
        Article article = articles.get(position);
        TextView textView = viewHolder.tvTitle;
        TextView readingTime = viewHolder.tvReadingTime;
        TextView date = viewHolder.tvPubDate;
        textView.setText(article.getHeadline());
        textView.setMaxLines(2);

        readingTime.setText(article.getReadTime());
        date.setText(article.getPubDate());

        ImageView imageView = viewHolder.ivImage;
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
