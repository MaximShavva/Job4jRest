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
public class CommentFragment extends Fragment {

    /**
     * Ссылка на RecyclerView.Adapter. Сохраняется при повороте экрана.
     */
    private CommentAdapter adapter;

    /**
     * Выставляем флаг удержания фрагмента в памяти при повороте.
     * Создаём адаптер RecyclerView здесь для того, чтобы он не
     * пересоздавался при повороте.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.adapter = new CommentAdapter();
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
    public void onContentChange(List<Comment> comments) {
        adapter.setComments(comments);
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
    private class CommentAdapter extends RecyclerView.Adapter<ItemHolder> {

        List<Comment> comments = new ArrayList<>();

        public void setComments(List<Comment> comments) {
            this.comments = comments;
        }

        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.layout_comment, viewGroup, false);
            return new ItemHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder itemHolder, int i) {
            TextView postId = itemHolder.itemView.findViewById(R.id.post_id_view);
            TextView id = itemHolder.itemView.findViewById(R.id.mere_id_view);
            TextView name = itemHolder.itemView.findViewById(R.id.name_view);
            TextView eMail = itemHolder.itemView.findViewById(R.id.email_view);
            TextView text = itemHolder.itemView.findViewById(R.id.full_text_view);
            String postText = "" + comments.get(i).getPost();
            String idText = "" + comments.get(i).getId();
            postId.setText(postText);
            id.setText(idText);
            name.setText(comments.get(i).getName());
            eMail.setText(comments.get(i).getEmail());
            text.setText(comments.get(i).getText());
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }
    }
}