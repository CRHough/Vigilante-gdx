package com.aesophor.vigilante.component;

import com.aesophor.vigilante.component.character.*;
import com.aesophor.vigilante.component.equipment.EquipmentDataComponent;
import com.aesophor.vigilante.component.graphics.AnimationComponent;
import com.aesophor.vigilante.component.graphics.IconComponent;
import com.aesophor.vigilante.component.graphics.SpriteComponent;
import com.aesophor.vigilante.component.item.ItemDataComponent;
import com.aesophor.vigilante.component.physics.B2BodyComponent;
import com.aesophor.vigilante.component.sound.SoundComponent;
import com.badlogic.ashley.core.ComponentMapper;

public class Mappers {

    public static final ComponentMapper<CharacterStatsComponent> STATS;
    public static final ComponentMapper<CharacterAnimationsComponent> CHARACTER_ANIMATIONS;
    public static final ComponentMapper<B2BodyComponent> B2BODY;
    public static final ComponentMapper<SpriteComponent> SPRITE;
    public static final ComponentMapper<IconComponent> ICON;
    public static final ComponentMapper<CharacterStateComponent> STATE;
    public static final ComponentMapper<CombatTargetComponent> COMBAT_TARGETS;
    public static final ComponentMapper<PickupItemTargetComponent> PICKUP_ITEM_TARGETS;
    public static final ComponentMapper<PortalTargetComponent> PORTAL_TARGET;
    public static final ComponentMapper<CharacterAIComponent> CHARACTER_AI;
    public static final ComponentMapper<CharacterStatsRegenComponent> REGENERATION;
    public static final ComponentMapper<InventoryComponent> INVENTORY;
    public static final ComponentMapper<EquipmentSlotsComponent> EQUIPMENT_SLOTS;
    public static final ComponentMapper<DroppableItemsComponent> DROP_ITEMS;
    public static final ComponentMapper<ItemDataComponent> ITEM_DATA;
    public static final ComponentMapper<EquipmentDataComponent> EQUIPMENT_DATA;
    public static final ComponentMapper<CharacterDataComponent> CHARACTER_DATA;
    public static final ComponentMapper<SoundComponent> SOUNDS;
    public static final ComponentMapper<AnimationComponent> ANIMATION;

    static {
        STATS = ComponentMapper.getFor(CharacterStatsComponent.class);
        CHARACTER_ANIMATIONS = ComponentMapper.getFor(CharacterAnimationsComponent.class);
        B2BODY = ComponentMapper.getFor(B2BodyComponent.class);
        SPRITE = ComponentMapper.getFor(SpriteComponent.class);
        ICON = ComponentMapper.getFor(IconComponent.class);
        STATE = ComponentMapper.getFor(CharacterStateComponent.class);
        COMBAT_TARGETS = ComponentMapper.getFor(CombatTargetComponent.class);
        PICKUP_ITEM_TARGETS = ComponentMapper.getFor(PickupItemTargetComponent.class);
        PORTAL_TARGET = ComponentMapper.getFor(PortalTargetComponent.class);
        CHARACTER_AI = ComponentMapper.getFor(CharacterAIComponent.class);
        REGENERATION = ComponentMapper.getFor(CharacterStatsRegenComponent.class);
        INVENTORY = ComponentMapper.getFor(InventoryComponent.class);
        EQUIPMENT_SLOTS = ComponentMapper.getFor(EquipmentSlotsComponent.class);
        DROP_ITEMS = ComponentMapper.getFor(DroppableItemsComponent.class);
        ITEM_DATA = ComponentMapper.getFor(ItemDataComponent.class);
        EQUIPMENT_DATA = ComponentMapper.getFor(EquipmentDataComponent.class);
        CHARACTER_DATA = ComponentMapper.getFor(CharacterDataComponent.class);
        SOUNDS = ComponentMapper.getFor(SoundComponent.class);
        ANIMATION = ComponentMapper.getFor(AnimationComponent.class);
    }

}
