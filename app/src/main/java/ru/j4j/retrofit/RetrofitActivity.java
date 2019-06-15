package ru.j4j.retrofit;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import ru.j4j.R;

/**
 * Класс - главная активность, содержит 2 кнопки для запросов
 * информации с сервера. Информация при нажатии на эти кнопки
 * выводится во фрагменте под ними в список RecyclerView.
 * При повороте экрана информация на экране сохраняется.
 *
 * @author Шавва Максим (masyam@mail.ru).
 * @version 1.0
 * @project Job4jRest
 * @since 14.06.2019.
 */
public class RetrofitActivity extends AppCompatActivity implements ServerActor.InteractView {

    /**
     * Тэги используем при смене фрагментов в актионости.
     */
    private final String POST_TAG = "posts";
    private final String COMMENT_TAG = "comments";

    /**
     * Ссылка на обработчик запросов на сервер.
     */
    private ServerActor actor;

    /**
     * Менеджер фрагментов.
     */
    private FragmentManager fm;

    /**
     * Чтобы не пересоздавать фрагменты, храним на них ссылки.
     */
    private PostsFragment postsFragment;
    private CommentFragment commentFragment;

    /**
     * В этом методе восстанавливаем фрагмент, который был в активности
     * до изменения состояния (поворота экрана).
     * Другой фрагмент создаём заново.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        actor = new ServerActor(this);
        fm = getSupportFragmentManager();
        postsFragment = (PostsFragment) fm.findFragmentByTag(POST_TAG);
        commentFragment = (CommentFragment) fm.findFragmentByTag(COMMENT_TAG);
        if (postsFragment == null) postsFragment = new PostsFragment();
        if (commentFragment == null) commentFragment = new CommentFragment();
    }

    /**
     * Колбэк вызывается при клике на кнопку POSTS.
     */
    public void postShow(View view) {
        if (fm.findFragmentById(R.id.response_content) == null) {
            fm.beginTransaction()
                    .add(R.id.response_content, postsFragment, POST_TAG)
                    .commit();
            actor.onPostButtonClick();
        } else {
            if (COMMENT_TAG.equals(fm.findFragmentById(R.id.response_content).getTag())) {
                fm.beginTransaction()
                        .replace(R.id.response_content, postsFragment, POST_TAG)
                        .commit();
                actor.onPostButtonClick();
            }
        }
    }

    /**
     * Колбэк вызывается при клике на кнопку COMMENTS.
     */
    public void commentShow(View view) {
        if (fm.findFragmentById(R.id.response_content) == null) {
            fm.beginTransaction()
                    .add(R.id.response_content, commentFragment, COMMENT_TAG)
                    .commit();
            actor.onCommentButtonClick();
        } else {
            if (POST_TAG.equals(fm.findFragmentById(R.id.response_content).getTag())){
                fm.beginTransaction()
                        .replace(R.id.response_content, commentFragment, COMMENT_TAG)
                        .commit();
                actor.onCommentButtonClick();
            }
        }
    }

    /**
     * Вызывается провайдером actor, когда он получил данные от сервера.
     */
    @Override
    public void onSetPostsView(List<Post> posts) {
        postsFragment.onContentChange(posts);
    }

    /**
     * Вызывается провайдером actor, когда он получил данные от сервера.
     */
    @Override
    public void onSetCommentView(List<Comment> comments) {
        commentFragment.onContentChange(comments);
    }

    /**
     * Вызывается провайдером actor в случае ошибки.
     */
    @Override
    public void onError(String textMessage) {
        Toast.makeText(this, textMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        actor = null;
        super.onDestroy();
    }
}