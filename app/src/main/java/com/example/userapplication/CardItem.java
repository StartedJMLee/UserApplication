package com.example.userapplication;

import java.util.Objects;

public class CardItem {
    private String name;
    private String contents;

    public CardItem(String name, String contents) {
        this.name = name;
        this.contents = contents;
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

    @Override
    public String toString() {
        return "CardItem{" +
                "name='" + name + '\'' +
                ", contents='" + contents + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardItem cardItem = (CardItem) o;
        return Objects.equals(name, cardItem.name) &&
                Objects.equals(contents, cardItem.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, contents);
    }
}
