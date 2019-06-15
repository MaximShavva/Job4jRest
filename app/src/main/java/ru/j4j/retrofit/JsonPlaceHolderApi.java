package ru.j4j.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author Шавва Максим (masyam@mail.ru).
 * @version 1.0
 * @project Job4jRest
 * @since 13.06.2019.
 */
public interface JsonPlaceHolderApi {

    /**
     * В аннотации указываем конечную точку запроса на сервер
     * (Может быть название метода, или файла на сервере).
     */
    @GET("posts")
    Call<List<Post>> getPosts();

    @GET("comments?postId=1")
    Call<List<Comment>> getComments();
}