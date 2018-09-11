package com.aesophor.medievania.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteComponent extends Sprite implements Component {

    public SpriteComponent(Texture texture, float x, float y) {
        super(texture);
        setPosition(x, y);
    }

}