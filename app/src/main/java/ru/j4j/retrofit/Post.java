package ru.j4j.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * @author Шавва Максим (masyam@mail.ru).
 * @version 1.0
 * @project Job4jRest
 * @since 13.06.2019.
 */
public class Post {

    private int userId;
    private int id;
    private String title;

    @SerializedName("body")
    private String text;

    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        if (userId != post.userId) return false;
        if (id != post.id) return false;
        if (!title.equals(post.title)) return false;
        return text.equals(post.text);
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + id;
        result = 31 * result + title.hashCode();
        result = 31 * result + text.hashCode();
        return result;
    }
}