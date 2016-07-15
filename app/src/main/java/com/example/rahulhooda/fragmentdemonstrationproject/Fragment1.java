package com.example.rahulhooda.fragmentdemonstrationproject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by rahul.hooda on 7/15/16.
 */
public class Fragment1 extends Fragment {
    public onFragment1ClickListener mOnFragmentClickListener;
    public MainActivity mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1,container,false);

        view.findViewById(R.id.btn_fragment1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnFragmentClickListener.onFragment1Clicked(true);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        mContext = (MainActivity) context;
        mOnFragmentClickListener = (onFragment1ClickListener) mContext;
    }

    public interface onFragment1ClickListener{
        public void onFragment1Clicked(boolean value);
    }
}
