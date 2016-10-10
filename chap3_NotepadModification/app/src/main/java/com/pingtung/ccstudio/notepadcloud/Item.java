package com.pingtung.ccstudio.notepadcloud;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by crowd_000 on 10/6/2016.
 */

public class Item {
    private String title;
    private String content;

    public Item(){
    }

    public Item(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("content", content);

        return result;
    }

}
