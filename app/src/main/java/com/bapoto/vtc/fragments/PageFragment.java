package com.bapoto.vtc.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bapoto.bapoto.R;

public class PageFragment extends Fragment {

    // Key for the Bundle
    private static final String KEY_POSITION="position";
    private static final String KEY_COLOR="color";



    // Required empty public constructor
    public PageFragment() {}

    //2 Method that will create a new instance of PageFragment and add data to its bundle
    public static PageFragment newInstance(int position, int color) {
        //2.1 create new fragment
        PageFragment frag = new PageFragment();
        //2.2 Create bundle and add it some data
        Bundle args = new Bundle();
        args.putInt("position",position);
        args.putInt("color",color);
        frag.setArguments(args);

        return (frag);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //3 get Layout of PageFragment
        View result =  inflater.inflate(R.layout.fragment_page, container, false);

        //4 get widgets from layout and serialise it
        LinearLayout rootView = (LinearLayout) result.findViewById(R.id.fragment_page_rootview);
        TextView textView = (TextView) result.findViewById(R.id.fragment_page_title);

        //5 get data from bundle(created in new Instance Method)
        int position = getArguments().getInt(KEY_POSITION,-1);
        int color = getArguments().getInt(KEY_COLOR,-1);

        //6 update widgets with data's

        rootView.setBackgroundColor(color);
        textView.setText("Page numéro "+position);

        return result;
    }
}