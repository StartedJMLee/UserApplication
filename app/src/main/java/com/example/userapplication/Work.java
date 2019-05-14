package com.example.userapplication;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.List;
import java.util.Objects;

public class Work {
    private int id;
    private String name;
    private String author;
    private String account;
    private String site;
    private List<CardItem> comments;

    public Work(int id, String name, String author, String account, String site) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.account = account;
        this.site = site;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public List<CardItem> getComments() {
        return comments;
    }

    public void setComments(List<CardItem> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Work{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", account='" + account + '\'' +
                ", site='" + site + '\'' +
                ", comments=" + comments +
                '}';
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Work work = (Work) o;
        return id == work.id &&
                Objects.equals(name, work.name) &&
                Objects.equals(author, work.author) &&
                Objects.equals(account, work.account) &&
                Objects.equals(site, work.site) &&
                Objects.equals(comments, work.comments);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(id, name, author, account, site, comments);
    }
}
