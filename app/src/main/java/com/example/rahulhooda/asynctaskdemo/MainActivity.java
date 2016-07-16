package com.example.rahulhooda.asynctaskdemo;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private EditText time;
    private TextView finalResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        time = (EditText) findViewById(R.id.et_time);
        button = (Button) findViewById(R.id.btn_do_it);
        finalResult = (TextView) findViewById(R.id.tv_result);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SleepTask sleepTask = new SleepTask();
                final String sleepTime = time.getText().toString();
                sleepTask.execute(sleepTime);

                /**
                 * All these Async Tasks are executed in a sequential manner by default.
                 * If you wish to execute Async tasks in parallel then use executeOnExecutor() and pass
                 * AysncTask.THREAD_POOL_EXECUTOR as first parameter
                 * */
                new SleepTask().execute(sleepTime);
                new SleepTask().execute(sleepTime);

                //Executing Async tasks in parallel using AsyncTask.THREAD_POOL_EXECUTOR
                new SleepTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,sleepTime);
                new SleepTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,sleepTime);

            }
        });
    }

    class SleepTask extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String response;
            publishProgress("Sleeping...");
            int sleepTime = Integer.parseInt(params[0]);
            try {
                Thread.sleep(sleepTime);
                response = "Slept for "+sleepTime+" milliseconds";
            } catch (InterruptedException e) {
                e.printStackTrace();
                response = e.getMessage();
            }
            catch (Exception e){
                e.printStackTrace();
                response = e.getMessage();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            finalResult.setText(result);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            finalResult.setText(values[0]);
        }
    }
}
