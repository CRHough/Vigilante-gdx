package com.aesophor.medievania.entity.item;

import com.aesophor.medievania.component.B2BodyComponent;
import com.aesophor.medievania.component.ItemType;
import com.aesophor.medievania.component.SoundComponent;
import com.aesophor.medievania.component.SpriteComponent;
import com.aesophor.medievania.util.CategoryBits;
import com.aesophor.medievania.util.Constants;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

public abstract class Item extends Entity implements Disposable {

    protected static final int itemWidth = 16;
    protected static final int itemHeight = 16;

    protected final ItemType itemType;
    protected SpriteComponent sprite;
    protected B2BodyComponent b2body;
    protected SoundComponent sounds;

    public Item(ItemType itemType, Texture texture, World currentWorld, float x, float y) {
        this.itemType = itemType;

        sprite = new SpriteComponent(texture, x, y);
        b2body = new B2BodyComponent(currentWorld);
        sounds = new SoundComponent();

        add(sprite);
        add(b2body);
        add(sounds);

        short bodyCategoryBits = CategoryBits.ITEM;
        short bodyMaskBits = CategoryBits.GROUND | CategoryBits.PLATFORM;
        defineBody(bodyCategoryBits, bodyMaskBits);

        sprite.setBounds(0, 0, itemWidth / Constants.PPM, itemHeight / Constants.PPM);
    }


    protected void defineBody(short bodyCategoryBits, short bodyMaskBits) {
        Body body = b2body.getBodyBuilder().type(BodyDef.BodyType.DynamicBody)
                .position(sprite.getX(), sprite.getY(), Constants.PPM)
                .buildBody();

        b2body.setBody(body);
        createBodyFixture(bodyCategoryBits, bodyMaskBits);
    }

    /**
     * Builds body fixture based on this item's body width and height.
     * @param categoryBits category bits of body fixture.
     * @param maskBits defines which objects the body fixture can collide with.
     */
    protected void createBodyFixture(short categoryBits, short maskBits) {
        Fixture bodyFixture = b2body.getBodyBuilder().newRectangleFixture(b2body.getBody().getPosition(), itemWidth / 2, itemHeight / 2, Constants.PPM)
                .categoryBits(categoryBits)
                .maskBits((short) (maskBits | CategoryBits.PLAYER))
                .isSensor(true)
                .setUserData(this)
                .buildFixture();

        b2body.setBodyFixture(bodyFixture);

        Fixture collisionFixture = b2body.getBodyBuilder().newRectangleFixture(b2body.getBody().getPosition(), itemWidth / 2, itemHeight / 2, Constants.PPM)
                .categoryBits(categoryBits)
                .maskBits(maskBits)
                .setUserData(this)
                .buildFixture();
    }


    public ItemType getItemType() {
        return itemType;
    }

    @Override
    public String toString() {
        return "Rustic Axe";
    }

}