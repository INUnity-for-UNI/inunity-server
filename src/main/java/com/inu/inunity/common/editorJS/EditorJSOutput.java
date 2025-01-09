package com.inu.inunity.common.editorJS;

import java.util.ArrayList;
import java.util.List;

public class EditorJSOutput {
    private String version;
    private long time;
    private List<Block> blocks;

    public EditorJSOutput() {
        this.blocks = new ArrayList<>();
        this.time = System.currentTimeMillis();
        this.version = "2.30.7";
    }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public long getTime() { return time; }
    public void setTime(long time) { this.time = time; }
    public List<Block> getBlocks() { return blocks; }
    public void setBlocks(List<Block> blocks) { this.blocks = blocks; }
}
