package ru.j4j.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * @author Шавва Максим (masyam@mail.ru).
 * @version 1.0
 * @project Job4jRest
 * @since 14.06.2019.
 */
public class Comment {

    @SerializedName("postId")
    private int post;

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("body")
    private String text;

    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        if (post != comment.post) return false;
        if (id != comment.id) return false;
        if (!name.equals(comment.name)) return false;
        if (!email.equals(comment.email)) return false;
        return text.equals(comment.text);
    }

    @Override
    public int hashCode() {
        int result = post;
        result = 31 * result + id;
        result = 31 * result + name.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + text.hashCode();
        return result;
    }
}