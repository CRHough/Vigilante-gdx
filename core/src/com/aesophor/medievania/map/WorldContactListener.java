package com.aesophor.medievania.map;

import com.aesophor.medievania.component.Mappers;
import com.aesophor.medievania.component.character.CharacterStateComponent;
import com.aesophor.medievania.component.character.CharacterStatsComponent;
import com.aesophor.medievania.entity.Dust;
import com.aesophor.medievania.entity.character.Character;
import com.aesophor.medievania.entity.character.Enemy;
import com.aesophor.medievania.entity.character.Player;
import com.aesophor.medievania.entity.item.Item;
import com.aesophor.medievania.util.CategoryBits;
import com.aesophor.medievania.util.Constants;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class WorldContactListener implements ContactListener {

    private final PooledEngine engine;
    private final AssetManager assets;

    public WorldContactListener(PooledEngine engine, AssetManager assets) {
        this.engine = engine;
        this.assets = assets;
    }


    @Override
    public void beginContact(Contact contact) {
        Character character;
        Player player;
        Enemy enemy;
        Portal portal;
        Item item;

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        int cDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        switch (cDef) {
            // When a character lands on the ground, make following changes.
            case CategoryBits.FEET | CategoryBits.GROUND:
                character = (Character) getTargetFixture(CategoryBits.FEET, fixtureA, fixtureB).getUserData();
                Mappers.STATE.get(character).setJumping(false);
                engine.addEntity(createDust(assets, Mappers.B2BODY.get(character).getFeetFixturePosition()));
                break;

            // When a character lands on a platform, make following changes.
            case CategoryBits.FEET | CategoryBits.PLATFORM:
                character = (Character) getTargetFixture(CategoryBits.FEET, fixtureA, fixtureB).getUserData();
                Mappers.STATE.get(character).setJumping(false);
                Mappers.STATE.get(character).setOnPlatform(true);
                engine.addEntity(createDust(assets, Mappers.B2BODY.get(character).getFeetFixturePosition()));
                break;

            // When player gets close to a portal, register the portal to the player.
            case CategoryBits.PLAYER | CategoryBits.PORTAL:
                player = (Player) getTargetFixture(CategoryBits.PLAYER, fixtureA, fixtureB).getUserData();
                portal = (Portal) getTargetFixture(CategoryBits.PORTAL, fixtureA, fixtureB).getUserData();
                Mappers.PORTAL_TARGET.get(player).setInRangePortal(portal);
                break;


            // When a player bumps into an enemy, the enemy will inflict damage to the player and knock it back.
            case CategoryBits.PLAYER | CategoryBits.ENEMY:
                player = (Player) getTargetFixture(CategoryBits.PLAYER, fixtureA, fixtureB).getUserData();
                enemy = (Enemy) getTargetFixture(CategoryBits.ENEMY, fixtureA, fixtureB).getUserData();
                if (!fixtureA.isSensor() && !fixtureB.isSensor()) {
                    enemy.inflictDamage(player, 25);

                    CharacterStateComponent playerState = Mappers.STATE.get(player);
                    CharacterStatsComponent enemyStats = Mappers.STATS.get(enemy);

                    float knockBackForceX = (playerState.isFacingRight()) ? -enemyStats.getAttackForce() : enemyStats.getAttackForce();
                    float knockBackForceY = 1f; // temporary.
                    enemy.knockBack(player, knockBackForceX, knockBackForceY);
                }
                break;

            // When an NPC hits a cliff marker, reverse the NPC's current direction.
            case CategoryBits.ENEMY | CategoryBits.CLIFF_MARKER:
                enemy = (Enemy) getTargetFixture(CategoryBits.ENEMY, fixtureA, fixtureB).getUserData();
                enemy.getAIActions().reverseDirection();
                break;

            // Set enemy as player's current target (so player can inflict damage to enemy).
            case CategoryBits.MELEE_WEAPON | CategoryBits.ENEMY:
                player = (Player) getTargetFixture(CategoryBits.MELEE_WEAPON, fixtureA, fixtureB).getUserData();
                enemy = (Enemy) getTargetFixture(CategoryBits.ENEMY, fixtureA, fixtureB).getUserData();
                Mappers.COMBAT_TARGETS.get(player).addInRangeTarget(enemy);
                break;

            // Set player as enemy's current target (so enemy can inflict damage to player).
            case CategoryBits.MELEE_WEAPON | CategoryBits.PLAYER:
                player = (Player) getTargetFixture(CategoryBits.PLAYER, fixtureA, fixtureB).getUserData();
                enemy = (Enemy) getTargetFixture(CategoryBits.MELEE_WEAPON, fixtureA, fixtureB).getUserData();
                Mappers.COMBAT_TARGETS.get(enemy).addInRangeTarget(player);
                break;

            case CategoryBits.PLAYER | CategoryBits.ITEM:
                player = (Player) getTargetFixture(CategoryBits.PLAYER, fixtureA, fixtureB).getUserData();
                item = (Item) getTargetFixture(CategoryBits.ITEM, fixtureA, fixtureB).getUserData();
                Mappers.PICKUP_ITEM_TARGETS.get(player).addInRangeItems(item);
                break;

            default:
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
        Character character;
        Player player;
        Enemy enemy;
        Item item;

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        int cDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        switch (cDef) {
            // When a character leaves the ground, make following changes.
            case CategoryBits.FEET | CategoryBits.GROUND:
                character = (Character) getTargetFixture(CategoryBits.FEET, fixtureA, fixtureB).getUserData();
                if (Mappers.B2BODY.get(character).getBody().getLinearVelocity().y > .5f) {
                    Mappers.STATE.get(character).setJumping(true);
                    engine.addEntity(createDust(assets, Mappers.B2BODY.get(character).getFeetFixturePosition()));
                }
                break;

            // When a character leaves the platform, make following changes.
            case CategoryBits.FEET | CategoryBits.PLATFORM:
                character = (Character) getTargetFixture(CategoryBits.FEET, fixtureA, fixtureB).getUserData();
                if (Mappers.B2BODY.get(character).getBody().getLinearVelocity().y < -.5f) {
                    Mappers.STATE.get(character).setJumping(true);
                    Mappers.STATE.get(character).setOnPlatform(false);
                    engine.addEntity(createDust(assets, Mappers.B2BODY.get(character).getFeetFixturePosition()));
                }
                break;

            // When player leaves the portal, unregister the portal from the player.
            case CategoryBits.PLAYER | CategoryBits.PORTAL:
                player = (Player) getTargetFixture(CategoryBits.PLAYER, fixtureA, fixtureB).getUserData();
                Mappers.PORTAL_TARGET.get(player).setInRangePortal(null);
                break;


            // Clear player's current target (so player cannot inflict damage to enemy from a distance).
            case CategoryBits.MELEE_WEAPON | CategoryBits.ENEMY:
                player = (Player) getTargetFixture(CategoryBits.MELEE_WEAPON, fixtureA, fixtureB).getUserData();
                enemy = (Enemy) getTargetFixture(CategoryBits.ENEMY, fixtureA, fixtureB).getUserData();
                Mappers.COMBAT_TARGETS.get(player).removeInRangeTarget(enemy);
                break;

            // Clear enemy's current target (so enemy cannot inflict damage to player from a distance).
            case CategoryBits.MELEE_WEAPON | CategoryBits.PLAYER:
                enemy = (Enemy) getTargetFixture(CategoryBits.MELEE_WEAPON, fixtureA, fixtureB).getUserData();
                player = (Player) getTargetFixture(CategoryBits.PLAYER, fixtureA, fixtureB).getUserData();
                Mappers.COMBAT_TARGETS.get(enemy).removeInRangeTarget(player);
                break;

            case CategoryBits.PLAYER | CategoryBits.ITEM:
                player = (Player) getTargetFixture(CategoryBits.PLAYER, fixtureA, fixtureB).getUserData();
                item = (Item) getTargetFixture(CategoryBits.ITEM, fixtureA, fixtureB).getUserData();
                Mappers.PICKUP_ITEM_TARGETS.get(player).removeInRangeItems(item);
                break;

            default:
                break;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        int cDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        switch (cDef) {
            // Allow player to pass through platforms and collide on the way down.
            case CategoryBits.FEET | CategoryBits.PLATFORM:
                Fixture playerBody = getTargetFixture(CategoryBits.FEET, fixtureA, fixtureB);
                Fixture platform = getTargetFixture(CategoryBits.PLATFORM, fixtureA, fixtureB);

                float playerY = playerBody.getBody().getPosition().y;
                float platformY = platform.getBody().getPosition().y;

                // Enable contact if the player is about to land on the platform.
                // .15f is a value that works fine in my world.
                contact.setEnabled((playerY > platformY + .15f));
                break;

            default:
                break;
        }

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }


    /**
     * Gets the target fixture which holds the specified CategoryBits from two candidates.
     * @param targetCategoryBits target category bits.
     * @param fixtureA candidate A.
     * @param fixtureB candidate B.
     * @return target fixture. If the target cannot be found, it returns null.
     */
    private static Fixture getTargetFixture(short targetCategoryBits, Fixture fixtureA, Fixture fixtureB) {
        Fixture targetFixture;

        if (fixtureA.getFilterData().categoryBits == targetCategoryBits) {
            targetFixture = fixtureA;
        } else if (fixtureB.getFilterData().categoryBits == targetCategoryBits) {
            targetFixture = fixtureB;
        } else {
            targetFixture = null;
        }

        return targetFixture;
    }

    private static Dust createDust(AssetManager assets, Vector2 feetPosition) {
        float dustX = feetPosition.x - Dust.TEXTURE_WIDTH / Constants.PPM / 2;
        float dustY = feetPosition.y - Dust.TEXTURE_HEIGHT / Constants.PPM / 2 + .065f; // .065f is a manual correction to Y.
        return new Dust(assets, dustX, dustY);
    }

}