package com.aesophor.medievania.component;

import com.badlogic.ashley.core.ComponentMapper;

public class Mappers {

    public static final ComponentMapper<StatsComponent> CHARACTER_STATS;
    public static final ComponentMapper<AnimationComponent> ANIMATION;
    public static final ComponentMapper<B2BodyComponent> B2BODY;
    public static final ComponentMapper<SpriteComponent> SPRITE;
    public static final ComponentMapper<StateComponent> STATE;
    public static final ComponentMapper<CombatTargetComponent> COMBAT_TARGET;
    public static final ComponentMapper<CharacterAIComponent> CHARACTER_AI;
    public static final ComponentMapper<StatsRegenerationComponent> REGENERATION;
    public static final ComponentMapper<InventoryComponent> INVENTORY;
    public static final ComponentMapper<DroppableItemsComponent> DROP_ITEMS;
    public static final ComponentMapper<ItemDataComponent> ITEM_DATA;

    static {
        CHARACTER_STATS = ComponentMapper.getFor(StatsComponent.class);
        ANIMATION = ComponentMapper.getFor(AnimationComponent.class);
        B2BODY = ComponentMapper.getFor(B2BodyComponent.class);
        SPRITE = ComponentMapper.getFor(SpriteComponent.class);
        STATE = ComponentMapper.getFor(StateComponent.class);
        COMBAT_TARGET = ComponentMapper.getFor(CombatTargetComponent.class);
        CHARACTER_AI = ComponentMapper.getFor(CharacterAIComponent.class);
        REGENERATION = ComponentMapper.getFor(StatsRegenerationComponent.class);
        INVENTORY = ComponentMapper.getFor(InventoryComponent.class);
        DROP_ITEMS = ComponentMapper.getFor(DroppableItemsComponent.class);
        ITEM_DATA = ComponentMapper.getFor(ItemDataComponent.class);
    }

}
