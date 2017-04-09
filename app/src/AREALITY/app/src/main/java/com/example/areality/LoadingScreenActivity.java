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
    //A ProgressDialog object
    private ProgressDialog progressDialog;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Initialize a LoadViewTask object and call the execute() method
        new LoadViewTask().execute();

    }

    //To use the AsyncTask, it must be subclassed
    private class LoadViewTask extends AsyncTask<Void, Integer, Void>
    {
        //Before running code in separate thread
        @Override
        protected void onPreExecute()
        {
            progressDialog = ProgressDialog.show(LoadingScreenActivity.this,"Loading...",
                    "Loading application View, please wait...", false, false);
//            //Create a new progress dialog
//            progressDialog = new ProgressDialog(LoadingScreenActivity.this);
//            //Set the progress dialog to display a horizontal progress bar
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            //Set the dialog title to 'Loading...'
//            progressDialog.setTitle("Loading...");
//            //Set the dialog message to 'Loading application View, please wait...'
//            progressDialog.setMessage("Loading application View, please wait...");
//            //This dialog can't be canceled by pressing the back key
//            progressDialog.setCancelable(false);
//            //This dialog isn't indeterminate
//            progressDialog.setIndeterminate(false);
//            //The maximum number of items is 100
//            progressDialog.setMax(100);
//            //Set the current progress to zero
//            progressDialog.setProgress(0);
//            //Display the progress dialog
//            progressDialog.show();
        }

        //The code to be executed in a background thread.
        @Override
        protected Void doInBackground(Void... params)
        {
			/* This is just a code that delays the thread execution 4 times,
			 * during 850 milliseconds and updates the current progress. This
			 * is where the code that is going to be executed on a background
			 * thread must be placed.
			 */
            try
            {
                //Get the current thread's token
                synchronized (this)
                {
                    //Initialize an integer (that will act as a counter) to zero
                    int counter = 0;
                    //While the counter is smaller than four
                    while(counter <= 4)
                    {
                        //Wait 850 milliseconds
                        this.wait(850);
                        //Increment the counter
                        counter++;
                        //Set the current progress.
                        //This value is going to be passed to the onProgressUpdate() method.
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

        //Update the progress
        @Override
        protected void onProgressUpdate(Integer... values)
        {
            //set the current progress of the progress dialog
            progressDialog.setProgress(values[0]);
        }

        //after executing the code in the thread
        @Override
        protected void onPostExecute(Void result)
        {
            //close the progress dialog
            progressDialog.dismiss();
            //initialize the View
            Intent intent = new Intent(context, MapsActivity.class);
            startActivity(intent);
        }
    }
}