package com.aesophor.vigilante.system.graphics;

import com.aesophor.vigilante.component.Mappers;
import com.aesophor.vigilante.component.character.CharacterAnimationsComponent;
import com.aesophor.vigilante.component.character.CharacterDataComponent;
import com.aesophor.vigilante.component.character.EquipmentSlotsComponent;
import com.aesophor.vigilante.component.graphics.SpriteComponent;
import com.aesophor.vigilante.component.physics.B2BodyComponent;
import com.aesophor.vigilante.util.Constants;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;

/**
 * EquipmentRendererSystem is responsible for rendering character's equipment atop their bodies.
 */
public class EquipmentRendererSystem extends CharacterRendererSystem {

    public EquipmentRendererSystem(Batch batch, Camera camera, World world) {
        super(batch, camera, world);
    }


    @Override
    public void processEntity(Entity entity, float delta) {
        CharacterDataComponent characterData = Mappers.CHARACTER_DATA.get(entity);
        SpriteComponent sprite = Mappers.SPRITE.get(entity);
        B2BodyComponent b2body = Mappers.B2BODY.get(entity);

        // TODO: Equipment overlay precedence.
        EquipmentSlotsComponent slots = Mappers.EQUIPMENT_SLOTS.get(entity);
        slots.getEquipment().forEach(((equipmentType, item) -> {
            if (item != null) {
                CharacterAnimationsComponent equipmentAnimations = Mappers.CHARACTER_ANIMATIONS.get(item);
                SpriteComponent sp = Mappers.SPRITE.get(item);

                float textureOffsetX = characterData.getSpriteOffsetX();
                float textureOffsetY = characterData.getSpriteOffsetY();

                float textureX = b2body.getBody().getPosition().x - sprite.getWidth() / 2 + (textureOffsetX / Constants.PPM);
                float textureY = b2body.getBody().getPosition().y - sprite.getHeight() / 2 + (textureOffsetY / Constants.PPM);

                sp.setRegion(getFrame(entity, equipmentAnimations, delta));
                sp.setPosition(textureX, textureY);
                sp.draw(batch);
            }
        }));
    }

}