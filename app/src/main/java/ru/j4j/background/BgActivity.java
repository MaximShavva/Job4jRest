package ru.j4j.background;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

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

    /**
     * Используем этот объект для связи главного потока с соновым.
     */
    private Handler mainHandler = new Handler();

    /**
     * Выводит информацию о достигнутых 50%
     */
    private TextView reach;

    /**
     * Сюда выведем какую-либо картинку из интернета.
     */
    private ImageView picture;

    /**
     * УРЛ картинки.
     */
    private final String url = "https://kotiki.net/wp-content/uploads/2019/05" +
            "/%D0%94%D0%9E%D0%98%D0%93%D0%A0%D0%90%D0%9B%D0%98%D0%A1%D0%AC-310x165.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bg);
        reach = findViewById(R.id.textView);
        picture = findViewById(R.id.imageView);
    }

    /**
     * Колбэк кнопки startButton.
     * Запускает новый поток.
     * Разные варианты обновления IU, кроме runOnUiThread():
     *
     * reach.post(() -> reach.setText("Достигли 50%"));
     *
     * или:
     * Handler threadHandler = new Handler(Looper.getMainLooper());
     * threadHandler.post(() -> reach.setText("Достигли 50%"));
     *
     * или
     * mainHandler.post(() -> reach.setText("Достигли 50%"));
     */
    public void startThread(View view) {
        active = true;
        reach.setText(R.string.wait_fifty);
        new Thread(() -> {
            int count = 0;
            while (active && count != 11) {
                Log.d(BgActivity.TAG, "startThread: " + count);
                if (count == 5) {
                    runOnUiThread(() -> reach.setText("Достигли 50%"));
                }
                count++;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Bitmap bitmap = loadImageFromNetwork(url);
            mainHandler.post(() -> picture.setImageBitmap(bitmap));
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
     * Грузим картинку из интернета в фоне.
     * Log: In Thread: Thread-173
     */
    private Bitmap loadImageFromNetwork(String url) {
        Log.d(BgActivity.TAG, "In Thread: " + Thread.currentThread().getName());
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new URL(url).openStream());
        } catch (IOException e) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.not_found);
        }
        return bitmap;
    }
}