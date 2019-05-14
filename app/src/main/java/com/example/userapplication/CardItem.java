package com.example.userapplication;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Objects;

public class CardItem {
    private String name;
    private String contents;
    private int type; //관람객0, 작가1

    public CardItem(String name, String contents, int type) {
        this.name = name;
        this.contents = contents;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardItem cardItem = (CardItem) o;
        return type == cardItem.type &&
                Objects.equals(name, cardItem.name) &&
                Objects.equals(contents, cardItem.contents);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(name, contents, type);
    }

    @Override
    public String toString() {
        return "CardItem{" +
                "name='" + name + '\'' +
                ", contents='" + contents + '\'' +
                ", type=" + type +
                '}';
    }
}
