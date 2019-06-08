package ru.j4j.background;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import ru.j4j.R;

/**
 * Класс демонстрирует использование фоновых потоков для выполнения
 * "тяжелых" задачь.
 *
 * @author Шавва Максим.
 * @version 1.0
 * @since 20.03.2019г.
 */
public class BgActivity extends AppCompatActivity {

    /**
     * Используется для вывода в консоль логов.
     */
    private static final String TAG = "debug";

    /**
     * Флаг активности фонового потока.
     */
    private volatile boolean active = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bg);
    }

    /**
     * Колбэк кнопки startButton.
     * Запускает новый поток, выводит счётчик в Лог.
     */
    public void startThread(View view) {
        active = true;
        new Thread(() -> {
            int count = 0;
            while (active) {
                Log.d(BgActivity.TAG, "startThread: " + count);
                count++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Колбэк кнопки stopButton.
     * Останавливает фоновый поток, выводит в UI сообщение об остановке.
     */
    public void stopThread(View view) {
        active = false;
        Toast.makeText(this, "Stopped", Toast.LENGTH_SHORT).show();
    }

    /**
     * Этот класс использовался в примере 01 - Thread, Runnable [#91382]
     * Не задействован.
     */
    private class TestThread extends Thread {

        private int times;

        public TestThread(int times) {
            this.times = times;
        }

        @Override
        public void run() {
            int count = 0;
            while (count != times) {
                Log.d(BgActivity.TAG, "startThread: " + count);
                count++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Этот класс использовался в примере 01 - Thread, Runnable [#91382]
     * Не задействован.
     */
    private class TestRunnable implements Runnable {

        private int times;

        public TestRunnable(int times) {
            this.times = times;
        }

        @Override
        public void run() {
            int count = 0;
            while (count != times) {
                Log.d(BgActivity.TAG, "startThread: " + count);
                count++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}