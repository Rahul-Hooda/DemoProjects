package com.example.rahulhooda.fragmentdemonstrationproject;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by rahul.hooda on 7/15/16.
 */
public class Fragment2 extends Fragment implements MainActivity.onUpdateTextViewContentListener {

    private TextView mTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);

        mTextView = (TextView) view.findViewById(R.id.textview_fragment2);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).mOnUpdateTextViewListener = this;
    }

    @Override
    public void updateTextViewContent(boolean value) {
            //fetch cuurent value in TextView
            int currentValue = Integer.parseInt(mTextView.getText().toString());
            //Update the current value by 1
            mTextView.setText(""+(currentValue+1));
    }
}
