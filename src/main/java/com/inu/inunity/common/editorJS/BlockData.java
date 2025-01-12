package com.inu.inunity.common.editorJS;

import java.util.List;

public class BlockData {
    private String text;
    private String html;
    private List<ListItem> items;

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getHtml() { return html; }
    public void setHtml(String html) { this.html = html; }
    public List<ListItem> getItems() { return items; }
    public void setItems(List<ListItem> items) { this.items = items; }
}

