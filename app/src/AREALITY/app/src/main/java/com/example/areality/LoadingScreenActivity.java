package com.example.areality;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class LoadingScreenActivity extends Activity
{
    private Context context = this;

    private ProgressDialog progressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        new LoadViewTask().execute();

    }

    private class LoadViewTask extends AsyncTask<Void, Integer, Void>
    {
        @Override
        protected void onPreExecute()
        {
            ProgressDialog.show(LoadingScreenActivity.this,"Loading...",
                    "Initializing AREALITY...", false, false);
        }

        @Override
        protected Void doInBackground(Void... params)
        {

            try
            {
                synchronized (this)
                {
                    int counter = 0;
                    while(counter <= 4)
                    {
                        this.wait(850);
                        counter++;
                        publishProgress(counter*25);
                    }
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result)
        {
            progressDialog.dismiss();
            Intent intent = new Intent(context, MapsActivity.class);
            startActivity(intent);
        }
    }
}