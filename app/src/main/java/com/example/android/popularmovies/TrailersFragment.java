package com.example.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class TrailersFragment extends Fragment {
    public TrailersFragment(){

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trailers,container, false);

        ListView lv = view.findViewById(R.id.trailer_list_id);
        TextView tv = view.findViewById(R.id.tv_trailer_no);
        Details movieDetails = ((MovieDetailsActivity)getActivity()).getToSendData();
        List<String> trailers = movieDetails.getTrailer();
        if(trailers.size() == 0){
            lv.setVisibility(View.GONE);
            tv.setVisibility(View.VISIBLE);
        }else {
            displayTrailer(lv,trailers);
            lv.setVisibility(View.VISIBLE);
            tv.setVisibility(View.GONE);

        }


        return view;
    }

    private void displayTrailer(ListView view,final List<String> trailerKey){
        List<String> names = generateTextForTrailer(trailerKey);
        ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.trailer_list, R.id.trailer_text_list_tv, names);
        view.setAdapter(mArrayAdapter);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.youtube.com/watch?v="+ trailerKey.get(position)));
                if(intent.resolveActivity(getActivity().getPackageManager())!=null){
                    startActivity(intent);
                }

            }
        });


    }
    private List<String> generateTextForTrailer(List<String> key){
        List<String> names = new ArrayList<>();
        for(int i=0;i<key.size();i++){
            names.add("Trailer "+(i+1));
        }
        return names;
    }

}
