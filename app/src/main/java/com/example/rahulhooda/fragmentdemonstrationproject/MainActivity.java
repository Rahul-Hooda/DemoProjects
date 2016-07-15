package com.example.rahulhooda.fragmentdemonstrationproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements Fragment1.onFragment1ClickListener{

    public onUpdateTextViewContentListener mOnUpdateTextViewListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onFragment1Clicked(boolean value) {
        //if button is clicked
        if(value == true){
            mOnUpdateTextViewListener.updateTextViewContent(true);
        }
    }

    public interface onUpdateTextViewContentListener{
        public void updateTextViewContent(boolean value);
    }
}
