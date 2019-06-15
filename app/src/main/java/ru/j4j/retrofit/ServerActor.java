package ru.j4j.retrofit;

import android.content.Context;

import java.util.List;

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
    void onPostButtonClick() {
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
     * Наследуемся от интерфейса Callback во избежание дублирования кода.
     */
    private abstract class myCallback<T> implements Callback<T> {
        @Override
        public void onFailure(Call<T> call, Throwable t) {
            context.onError(t.getMessage());
        }
    }
}