package com.example.android.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.List;


public class CustomReviewAdapter extends ArrayAdapter<Reviews>{
    private LayoutInflater inflater;
    private List<Reviews> mReviews;

    CustomReviewAdapter(Context context, ArrayList<Reviews> reviews) {
        super(context,R.layout.reviews_list,reviews);
        this.mReviews = reviews;
        inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.reviews_list, parent, false);
        }
        TextView authorName = convertView.findViewById(R.id.author_tv);
        authorName.setText(mReviews.get(position).getAuthor());
        ExpandableTextView textView = convertView.findViewById(R.id.expand_text_view);
        textView.setText(mReviews.get(position).getReview());
        return convertView;
    }
    }

