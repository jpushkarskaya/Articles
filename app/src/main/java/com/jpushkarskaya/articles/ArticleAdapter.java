package com.jpushkarskaya.articles;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by epushkarskaya on 10/20/16.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private List<Article> articles;
    private Context context;

    public ArticleAdapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    private Context getContext() {
        return this.context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View articleView = inflater.inflate(R.layout.item_article, parent, false);
        return new ViewHolder(articleView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article article = articles.get(position);

        String thumbNailURL = article.getThumbNail();
        if (!thumbNailURL.isEmpty()) {
            Picasso.with(getContext()).load(article.getThumbNail())
                    .placeholder(R.drawable.news)
                    .into(holder.imgThumbnail);
            // todo: create real placeholder img
        }
        holder.tvHeadline.setText(article.getHeadline());
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgThumbnail;
        TextView tvHeadline;

        public ViewHolder(View view) {
            super(view);
            this.imgThumbnail = (ImageView) view.findViewById(R.id.imgThumbnail);
            this.tvHeadline = (TextView) view.findViewById(R.id.tvHeadline);
        }

    }

}
