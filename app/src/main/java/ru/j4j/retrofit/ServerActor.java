package ru.j4j.retrofit;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Класс обрабатвает запрос GET на сервер с использованием
 * библиотеки Retrofit.
 *
 * @author Шавва Максим (masyam@mail.ru).
 * @version 1.0
 * @project Job4jRest
 * @since 14.06.2019.
 */
public class ServerActor {

    private static final String TAG = "debug";
    InteractView context;

    /**
     * API доступа к серверу.
     */
    JsonPlaceHolderApi jsonPlaceHolderApi;

    /**
     * Интерфейс для передачи результатов зсароса в главную активность.
     */
    public interface InteractView {
        void onSetPostsView(List<Post> posts);
        void onSetCommentView(List<Comment> comments);
        void onResponseReceive(int code);
        void onError(String textMessage);
    }

    /**
     * Реализуем серверные API в своём классе jsonPlaceHolderApi
     * используя retrofit.create()
     * В билдере указываем базовый УРЛ.
     */
    ServerActor(Context context) {
        if (context instanceof InteractView) {
            this.context = (InteractView) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement InteractView");
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
    }

    /**
     * Вызывается из главной активности при клике на кнопку POSTS.
     * Асинхронно вызываем метод .getPosts(), результат обрабатываем
     * в колбэке.
     */
    void onGetPostsButtonClick() {
        Call<List<Post>> call = jsonPlaceHolderApi.getPosts();
        call.enqueue(new myCallback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    context.onError("Server error code: " + response.code());
                    return;
                }
                context.onSetPostsView(response.body());
            }
        });
    }

    /**
     * Вызывается из главной активности при клике на кнопку COMMENTS.
     * Асинхронно вызываем метод .getComments(), результат обрабатываем
     * в колбэке.
     */
    void onCommentButtonClick() {
        Call<List<Comment>> call = jsonPlaceHolderApi.getComments();
        call.enqueue(new myCallback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (!response.isSuccessful()) {
                    context.onError("Server error code: " + response.code());
                    return;
                }
                context.onSetCommentView(response.body());
            }
        });
    }

    /**
     * Вызывается из главной активности при клике на кнопку POST,
     * когда нужно отправить Json-данные на сервер.
     */
    void onPostJSonClick(Post post) {
        Call<Post> call = jsonPlaceHolderApi.createPost(post);
        call.enqueue(new myCallback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    List<Post> posts = new ArrayList<>();
                    posts.add(response.body());
                    context.onSetPostsView(posts);
                }
            }
        });
    }

    /**
     * Вызывается из главной активности при клике на кнопку POST URL,
     * когда нужно отправить данные в параметрах URL на сервер.
     */
    void onPostURLClick() {
        Call<Post> call = jsonPlaceHolderApi.createPostURL(1, "title1", "text1");
        call.enqueue(new myCallback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    List<Post> posts = new ArrayList<>();
                    posts.add(response.body());
                    context.onSetPostsView(posts);
                }
            }
        });
    }

    /**
     * Вызывается из главной активности при клике на кнопку POST MAP,
     * когда нужно отправить данные в параметрах URL на сервер.
     */
    void onPostMapClick() {
        Map<String, String> fields = new HashMap<>();
        fields.put("userId", "1");
        fields.put("title", "Title1");
        fields.put("body", "Text1");
        Call<Post> call = jsonPlaceHolderApi.createPostMap(fields);
        call.enqueue(new myCallback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    List<Post> posts = new ArrayList<>();
                    posts.add(response.body());
                    context.onSetPostsView(posts);
                }
            }
        });
    }

    /**
     * Вызывается из главной активности при клике на кнопку PUT,
     * когда нужно отправить данные в параметрах JSon на сервер для замены старых.
     * PUT заменяет все данные поста.
     */
    void onPutClick() {
        final Post post = new Post(1, "new title", "new text");
        Call<Post> call = jsonPlaceHolderApi.putPost(1, post);
        call.enqueue(new myCallback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    List<Post> posts = new ArrayList<>();
                    posts.add(response.body());
                    context.onSetPostsView(posts);
                }
            }
        });
    }

    /**
     * Вызывается из главной активности при клике на кнопку PATCH,
     * когда нужно отправить данные в параметрах JSon на сервер для замены старых.
     * PATCH заменяет только ненулевые данные в посте.
     */
    void onPatchClick() {
        final Post post = new Post(1, null, "patched text");
        Call<Post> call = jsonPlaceHolderApi.patchPost(1, post);
        call.enqueue(new myCallback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    List<Post> posts = new ArrayList<>();
                    posts.add(response.body());
                    context.onSetPostsView(posts);
                }
            }
        });
    }

    void onDeleteClick() {
        Call<Void> call = jsonPlaceHolderApi.deletePost(101);
        call.enqueue(new myCallback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    context.onResponseReceive(response.code());
                    //Log.d(TAG, "Response code (must be 200): " + response.code());
                }
            }
        });
    }

    /**
     * Наследуемся от интерфейса Callback во избежание дублирования кода.
     */
    private abstract class myCallback<T> implements Callback<T> {
        @Override
        public void onFailure(Call<T> call, Throwable t) {
            context.onError(t.getMessage());
        }
    }
}