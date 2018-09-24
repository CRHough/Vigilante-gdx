package com.aesophor.medievania.component.item;

import com.badlogic.ashley.core.Component;

public class ItemDataComponent implements Component {

    private int type;
    private String image;
    private String name;
    private String desc;


    public ItemType getType() {
        return ItemType.values()[type];
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

}