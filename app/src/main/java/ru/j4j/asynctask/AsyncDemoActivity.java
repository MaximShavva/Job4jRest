package ru.j4j.asynctask;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import ru.j4j.R;

/**
 * Класс демонстрирует использование AsyncTask для
 * "тяжелых" задачь.
 *
 * @author Шавва Максим.
 * @version 1.1
 * @since 11.06.2019г.
 */
public class AsyncDemoActivity extends AppCompatActivity
        implements AsyncDemoFragment.FragmentListener {

    /**
     * Метка, по которой будет искать фрагмент фрагментменеджер.
     */
    private static final String TAG_TASK_FRAGMENT = "task_fragment";

    /**
     * Тэг для логов.
     */
    private static final String TAG = "debug";

    /**
     * Ссылка на компонент "Прогрес-бар".
     */
    private ProgressBar bar;

    /**
     * Ссылка на фрагмент с заданием AsyncTask.
     */
    private AsyncDemoFragment taskFragment;

    /**
     * Так как мы использовали setRetainInstance(true) в AsyncDemoFragment,
     * можем вернуть на него ссылку при помощи фрагментменеджера
     * после перезапуска активности.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_demo);
        bar = findViewById(R.id.progressBar);
        FragmentManager fm = getSupportFragmentManager();
        taskFragment = (AsyncDemoFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);
        if (taskFragment == null) {
            taskFragment = new AsyncDemoFragment();
            fm.beginTransaction()
                    .add(taskFragment, TAG_TASK_FRAGMENT)
                    .commit();
        }
        if (taskFragment.isRunning()) {
            onPreExecute();
        }
    }

    /**
     *  Запускаем фоновое задание по клику на кнопку "Старт".
     *  Если оно уже было запущено, отменяем его.
     */
    public void startAsyncTask(View view) {
        if (taskFragment.isRunning()) {
            taskFragment.cancelTask();
        } else {
            taskFragment.startTask();
        }
    }

    /**
     * Колбэк вызывается из SampleAsyncTask и делает видимым прогресс-бар.
     */
    @Override
    public void onPreExecute() {
        bar.setVisibility(View.VISIBLE);
    }

    /**
     * Колбэк вызывается из SampleAsyncTask и обновляет прогресс-бар.
     */
    @Override
    public void onProgressUpdate(Integer... values) {
        bar.setProgress(values[0]);
    }

    /**
     * Использую один и тот же колбэк-вызов для SampleAsyncTask.onPostExecute()
     * и для SampleAsyncTask.onCancelled() поскольку действия одни и те же:
     * Обнулить прогресс-бар и вывести тост об отмене или окончании.
     */
    @Override
    public void onPostExecute(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        bar.setProgress(0);
        bar.setVisibility(View.INVISIBLE);
    }
}