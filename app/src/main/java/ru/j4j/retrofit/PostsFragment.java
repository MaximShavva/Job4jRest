package ru.j4j.retrofit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.j4j.R;

/**
 * Фрагмент отображения записей с сервера в RecyclerView.
 *
 * @author Шавва Максим (masyam@mail.ru).
 * @version 1.0
 * @project Job4jRest
 * @since 14.06.2019.
 */
public class PostsFragment extends Fragment {

    /**
     * Ссылка на RecyclerView.Adapter. Сохраняется при повороте экрана.
     */
    private PostAdapter adapter;

    /**
     * Выставляем флаг удержания фрагмента в памяти при повороте.
     * Создаём адаптер RecyclerView здесь для того, чтобы он не
     * пересоздавался при повороте.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.adapter = new PostAdapter();
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler, container, false);
        RecyclerView recycler = view.findViewById(R.id.posts_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter);
        return view;
    }

    /**
     * Метод вызывается из главной активности при изменении данных,
     * полученных с сервера.
     */
    public void onContentChange(List<Post> posts) {
        adapter.setPosts(posts);
        adapter.notifyDataSetChanged();
    }

    /**
     * Содержит view одного элемента списка RecyclerView.
     */
    private class ItemHolder extends RecyclerView.ViewHolder {
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    /**
     * Список RecyclerView записей с сервера.
     */
    private class PostAdapter extends RecyclerView.Adapter<ItemHolder> {

        List<Post> posts = new ArrayList<>();

        public void setPosts(List<Post> posts) {
            this.posts = posts;
        }

        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.layout_post, viewGroup, false);
            return new ItemHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder itemHolder, int i) {
            TextView userid = itemHolder.itemView.findViewById(R.id.user_id_view);
            TextView id = itemHolder.itemView.findViewById(R.id.id_view);
            TextView title = itemHolder.itemView.findViewById(R.id.title_view);
            TextView text = itemHolder.itemView.findViewById(R.id.text_view);
            String userText = "" + posts.get(i).getUserId();
            String idText = "" + posts.get(i).getId();
            userid.setText(userText);
            id.setText(idText);
            title.setText(posts.get(i).getTitle());
            text.setText(posts.get(i).getText());
        }

        @Override
        public int getItemCount() {
            return posts.size();
        }
    }
}