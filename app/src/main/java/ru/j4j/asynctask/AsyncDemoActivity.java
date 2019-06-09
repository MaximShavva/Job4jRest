package ru.j4j.asynctask;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import ru.j4j.R;

/**
 * Класс демонстрирует использование AsyncTask для
 * "тяжелых" задачь.
 *
 * @author Шавва Максим.
 * @version 1.0
 * @since 09.06.2019г.
 */
public class AsyncDemoActivity extends AppCompatActivity {

    private static final String TAG = "debug";

    Button start;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_demo);
        start = findViewById(R.id.button_start);
        bar = findViewById(R.id.progressBar);
        Log.d(TAG, "AsyncDemoActivity hash:" + this.hashCode());
    }

    public void startAcyncTask(View view) {
        SampleAsyncTask task = new SampleAsyncTask(this);
        task.execute(100);
    }

    static class SampleAsyncTask extends AsyncTask<Integer, Integer, String> {

        private WeakReference<AsyncDemoActivity> activityWeakReference;

        SampleAsyncTask(AsyncDemoActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AsyncDemoActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Integer... integers) {
            int count = 0;
            while (count++ < integers[0]) {
                publishProgress((count * 100) / integers[0]);
                try {
                    Thread.sleep(1000);
                    Log.d(TAG, "Activity link is null? " + (activityWeakReference.get() == null)
                            + ";     Activity finished? " + activityWeakReference.get().isFinishing());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "Finish";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            AsyncDemoActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.bar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            AsyncDemoActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            Toast.makeText(activity.getBaseContext(), s, Toast.LENGTH_SHORT).show();
            activity.bar.setProgress(0);
            activity.bar.setVisibility(View.INVISIBLE);
        }
    }
}