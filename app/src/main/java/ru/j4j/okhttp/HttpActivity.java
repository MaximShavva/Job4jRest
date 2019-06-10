package ru.j4j.okhttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ru.j4j.R;

public class HttpActivity extends AppCompatActivity {

    private static final String TAG = "debug";

    private MediaType JSON;

    /**
     * json для передачи логина/пароля в Track Studio.
     */
    private final String jsonParams =
            "{'method':'login'," +
                    "'login':'maxim.shavva'," +
                    "'password':'asdljhqw223jh'}";

    private final String TRACKER = "https://job4j.ru/TrackStudio/LoginAction.do";

    private final String URL = "https://jsonplaceholder.typicode.com/posts";

    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);
        result = findViewById(R.id.text_view_result);
        JSON = MediaType.parse("application/json; charset=utf-8");
        Log.d(TAG, "MediaType created: " + (JSON != null));
    }

    /**
     * client.newCall(request).enqueue() - постановка запроса в очередь в отдельный поток.
     * client.newCall(request).execute() - запускать синхронно, блокируется главный поток.
     */
    public void httpCall(View view) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String strResponse = response.body().string();
                    result.post(() -> result.setText(strResponse));
                }
            }
        });
    }

    /**
     * Посылаем запрос-логин на сайт TrackStudio.
     * Ответ-html выводим в ScrollView -> TextView.
     */
    public void httpLogin(View view) {
        OkHttpClient client = getUnsafeOkHttpClient();
        RequestBody body = RequestBody.create(JSON, jsonParams);
        Log.d(TAG, "RequestBody created: " + (body.contentType()));
        Request request = new Request.Builder()
                .url(TRACKER)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String strResponse = response.body().string();
                    result.post(() -> result.setText(strResponse));
                }
            }
        });
    }

    /**
     * Получаем клиент OkHttpClient, устойчивый к ошибкам SSL.
     * (Источник https://gist.github.com/chalup/8706740 - работает до версии OkHttp 3.1.1)
     * Метод работает для версии com.squareup.okhttp3:okhttp:3.9.1
     * (Версии okHttp выше 3.9.1 требуют Android API >= 21. В моём случае - API 19)
     */
    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain
                                , String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain
                                , String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[0];
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier((hostname, session) -> true)
                    .build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}