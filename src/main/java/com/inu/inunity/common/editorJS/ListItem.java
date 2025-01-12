package com.inu.inunity.common.editorJS;

import java.util.List;

public class ListItem {
    private String content;
    private List<ListItem> items;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<ListItem> getItems() {
        return items;
    }

    public void setItems(List<ListItem> items) {
        this.items = items;
    }
}

