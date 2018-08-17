package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class OverviewFragment extends Fragment {
    String i;

    public OverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_overview, container, false);
        TextView tv_overview = view.findViewById(R.id.overView_tv);
        Details movieDetails = ((MovieDetailsActivity)getActivity()).getToSendData();
        String overView = movieDetails.getOverView();
        tv_overview.setText(overView);
       return view;
    }

}
