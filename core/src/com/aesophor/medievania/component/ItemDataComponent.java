package com.aesophor.medievania.component;

import com.badlogic.ashley.core.Component;

public class ItemDataComponent implements Component {

    private Integer type;
    private String name;
    private String desc;
    private String image;


    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getImage() {
        return image;
    }

}