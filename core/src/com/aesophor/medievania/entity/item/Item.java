package com.aesophor.medievania.entity.item;

import com.aesophor.medievania.component.sound.SoundComponent;
import com.aesophor.medievania.component.graphics.SpriteComponent;
import com.aesophor.medievania.component.item.ItemDataComponent;
import com.aesophor.medievania.component.item.ItemType;
import com.aesophor.medievania.component.physics.B2BodyComponent;
import com.aesophor.medievania.util.CategoryBits;
import com.aesophor.medievania.util.Constants;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

public class Item extends Entity implements Disposable {

    protected static final int itemWidth = 16;
    protected static final int itemHeight = 16;

    protected final ItemDataComponent itemData;
    protected final SpriteComponent sprite;
    protected final B2BodyComponent b2body;
    protected final SoundComponent sounds;
    protected final ItemType type;

    public Item(String itemName, World world, Float x, Float y) {
        itemData = ItemDataManager.getInstance().get(itemName);
        sprite = new SpriteComponent(new Texture(itemData.getImage()), x * Constants.PPM, y * Constants.PPM);
        b2body = new B2BodyComponent(world);
        sounds = new SoundComponent();
        type = itemData.getType();

        add(itemData);
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
     * Builds body fixture based on this itemData's body width and height.
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


    public ItemType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Rustic Axe";
    }

    @Override
    public void dispose() {
        sprite.dispose();
    }

}