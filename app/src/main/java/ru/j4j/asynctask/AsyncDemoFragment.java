package ru.j4j.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;


/**
 * Класс - фрагмент используется для запуска AsyncTask для
 * сохранения выполнения фонового потока при перезапуске
 * активности (сохраняем фрагмент в памяти на время перезапуска).
 *
 * @author Шавва Максим.
 * @version 1.0
 * @since 11.06.2019г.
 */
public class AsyncDemoFragment extends Fragment {

    /**
     * Тэг для логов.
     */
    private static final String TAG = "debug";

    /**
     * Слушатель событий, происходящих в SampleAsyncTask.
     */
    private FragmentListener mListener;

    /**
     * Ссылка на фоновое задание.
     */
    AsyncDemoFragment.SampleAsyncTask task;

    /**
     * Флаг используется для определения, запущено ли задание.
     */
    private boolean running = false;

    public boolean isRunning() {
        return running;
    }

    /**
     * Интерфейс слушателя.
     */
    public interface FragmentListener {
        void onPreExecute();
        void onProgressUpdate(Integer... values);
        void onPostExecute(String s);
    }

    /**
     * Добавляем ссылку на слушателя.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentListener) {
            mListener = (FragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentListener");
        }
    }

    /**
     * Отключаем слушателя.
     */
    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    /**
     * Устанавливаем для этого фрагмента флаг retainInstance = true.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true); // Retain this fragment across configuration changes.
    }

    /**
     * Запускаем фоновое выполнение.
     */
    public void startTask() {
        task = new SampleAsyncTask();
        task.execute(100);
    }

    /**
     * Отменяем Фоновое выполнение.
     */
    public void cancelTask() {
        task.cancel(false);
    }

    class SampleAsyncTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected void onPreExecute() {
            mListener.onPreExecute();
            running = true;
        }

        @Override
        protected String doInBackground(Integer... integers) {
            int count = 0;
            while (count++ < integers[0] && !isCancelled()) {
                publishProgress((count * 100) / integers[0]);
                Log.d(TAG, "doInBackground: " + count);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "Finish";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mListener.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled() {
            mListener.onPostExecute("Canceled");
            running = false;
        }

        @Override
        protected void onPostExecute(String s) {
            mListener.onPostExecute(s);
            running = false;
        }
    }
}