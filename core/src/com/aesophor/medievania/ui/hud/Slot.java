package com.aesophor.medievania.ui.hud;

import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.entity.item.Item;
import com.aesophor.medievania.ui.theme.LabelStyles;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Slot extends Table {

    private final Image slotBackgroundImage;
    private final Image itemIconImage;

    private Image itemNameBackgroundImage;
    private Label itemNameLabel;

    public Slot(TextureRegion slotBackground, float x, float y) {
        this.slotBackgroundImage = new Image(slotBackground);
        this.itemIconImage = new Image();
        itemIconImage.setScale(1.5f);

        top().left();
        setPosition(x, y);
        setFillParent(true);

        HorizontalGroup group = new HorizontalGroup();
        group.padTop(6f).padLeft(4f);
        group.addActor(itemIconImage);
        add(new Stack(slotBackgroundImage, group));
    }

    public Slot(TextureRegion slotBackground, TextureRegion descBackground, float x, float y) {
        this(slotBackground, x, y);
        this.itemNameBackgroundImage = new Image(descBackground);
        this.itemNameLabel = new Label("", LabelStyles.WHITE_REGULAR);

        HorizontalGroup itemName = new HorizontalGroup();
        itemName.padLeft(3f);
        itemName.addActor(itemNameLabel);
        add(new Stack(itemNameBackgroundImage, itemName)).padLeft(2f).padTop(20f);
    }


    public void update(Item item) {
        // Update item icon.
        if (item != null) {
            Texture texture = Mappers.SPRITE.get(item).getTexture();
            item.reloadTexture();
            itemIconImage.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
        } else {
            itemIconImage.setDrawable(null);
        }

        // Update item name label if it exists.
        if (itemNameLabel != null) {
            itemNameLabel.setText((item != null) ? Mappers.ITEM_DATA.get(item).getName() : "");
        }
    }

}