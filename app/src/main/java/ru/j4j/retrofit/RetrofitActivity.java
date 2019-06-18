package ru.j4j.retrofit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
public class RetrofitActivity extends AppCompatActivity
        implements ServerActor.InteractView
        , NavigationView.OnNavigationItemSelectedListener, SendPostDialog.DataDialogReceiver {

    private String TAG = "debug";
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
        initViews();
        actor = new ServerActor(this);
        fm = getSupportFragmentManager();
        postsFragment = (PostsFragment) fm.findFragmentByTag(POST_TAG);
        commentFragment = (CommentFragment) fm.findFragmentByTag(COMMENT_TAG);
        if (postsFragment == null) postsFragment = new PostsFragment();
        if (commentFragment == null) commentFragment = new CommentFragment();
    }

    /**
     * Задаём активности экшн-бар и помещаем на него кнопку выдвижения
     * навигационной панелии.
     * Задаём сативность слушателем кнопок навигации(GET, POST, DELETE...).
     */
    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this
                , drawer
                , toolbar
                , R.string.nav_open_drawer
                , R.string.nav_close_drawer);
        toggle.syncState(); //синхронизирует кнопку toggle с состоянием панели.
        drawer.addDrawerListener(toggle);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     *  Выбор действий в ответ на клик по пунктам меню.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        switch (menuItem.getItemId()) {
            case R.id.nav_post_json:
                SendPostDialog dialog = new SendPostDialog();
                dialog.show(fm, "send_dialog");
                postFragmentShow();
                break;
            case R.id.nav_post_url:
                actor.onPostURLClick();
                postFragmentShow();
                break;
            case R.id.nav_post_map:
                actor.onPostMapClick();
                postFragmentShow();
                break;
            case R.id.nav_get_post:
                actor.onGetPostsButtonClick();
                postFragmentShow();
                break;
            case R.id.nav_get_comment:
                actor.onCommentButtonClick();
                commentFragmentShow();
                break;
            case R.id.nav_put:
                actor.onPutClick();
                postFragmentShow();
                break;
            case R.id.nav_patch:
                actor.onPatchClick();
                postFragmentShow();
                break;
            case R.id.nav_delete:
                actor.onDeleteClick();
        }
        drawer.closeDrawer(GravityCompat.START); //задвигаем меню к началу экрана
        return true;
    }

    /**
     * Вызывается при клике меню GET posts из метода this.onNavigationItemSelected.
     */
    private void postFragmentShow() {
        if (fm.findFragmentById(R.id.response_content) == null) {
            fm.beginTransaction()
                    .add(R.id.response_content, postsFragment, POST_TAG)
                    .commit();
        } else {
            if (COMMENT_TAG.equals(fm.findFragmentById(R.id.response_content).getTag())) {
                fm.beginTransaction()
                        .replace(R.id.response_content, postsFragment, POST_TAG)
                        .commit();
            }
        }
    }

    /**
     * Вызывается при клике меню GET comments из метода this.onNavigationItemSelected.
     */
    private void commentFragmentShow() {
        if (fm.findFragmentById(R.id.response_content) == null) {
            fm.beginTransaction()
                    .add(R.id.response_content, commentFragment, COMMENT_TAG)
                    .commit();
        } else {
            if (POST_TAG.equals(fm.findFragmentById(R.id.response_content).getTag())) {
                fm.beginTransaction()
                        .replace(R.id.response_content, commentFragment, COMMENT_TAG)
                        .commit();
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

    @Override
    public void onResponseReceive(int code) {
        DeleteDialog dialog = new DeleteDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("message", code);
        dialog.setArguments(bundle);
        dialog.show(fm, "dialog_tag");
    }

    /**
     * Вызывается провайдером actor в случае ошибки.
     */
    @Override
    public void onError(String textMessage) {
        Toast.makeText(this, textMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDataCollected(Post post) {
        actor.onPostJSonClick(post);
    }

    @Override
    protected void onDestroy() {
        actor = null;
        super.onDestroy();
    }
}