package com.inu.inunity.common.editorJS;

import java.util.Map;
import java.util.UUID;

public class Block {
    private String id;
    private String type;
    private BlockData data;
    private Map<String, Object> tunes;

    public Block() {
        this.id = generateId();
        this.type = "raw";
        this.data = new BlockData();
    }

    private String generateId() {
        return UUID.randomUUID().toString().substring(0, 10);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public BlockData getData() { return data; }
    public void setData(BlockData data) { this.data = data; }
    public Map<String, Object> getTunes() { return tunes; }
    public void setTunes(Map<String, Object> tunes) { this.tunes = tunes; }
}

