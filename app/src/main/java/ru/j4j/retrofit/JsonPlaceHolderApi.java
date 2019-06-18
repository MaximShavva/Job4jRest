package ru.j4j.retrofit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

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

    @POST("posts")
    Call<Post> createPost(@Body Post post);

    /**
     * В данном пост-методе данные передаются в параметрах URL.
     */
    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPostURL(
            @Field("userId") int userId
            , @Field("title") String title
            , @Field("body") String text);

    /**
     * В данном пост-методе данные передаются в параметрах URL,
     * Принимаем параметры в метод через Map<String, String>
     */
    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPostMap(@FieldMap Map<String, String> fields);

    /**
     * Замещаем один пост другим с данным id.
     */
    @PUT("posts/{id}")
    Call<Post> putPost(@Path("id") int id, @Body Post post);

    /**
     * Замещаем ненулевые данные в посте с данным id.
     */
    @PATCH("posts/{id}")
    Call<Post> patchPost(@Path("id") int id, @Body Post post);

    @DELETE("posts/{id}")
    Call<Void> deletePost(@Path("id") int id);
}