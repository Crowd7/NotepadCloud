package com.pingtung.ccstudio.notepadcloud;

/**
 * Created by crowd_000 on 10/6/2016.
 */

public class ItemPlus extends Item {
    private String key;

	public ItemPlus() {
    }
	
    public ItemPlus(Item item, String key) {
        super(item.getTitle(),item.getContent());
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
