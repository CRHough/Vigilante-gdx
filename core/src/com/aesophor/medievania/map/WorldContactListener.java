package com.aesophor.medievania.map;

import com.aesophor.medievania.entity.character.Enemy;
import com.aesophor.medievania.entity.item.Item;
import com.aesophor.medievania.util.CategoryBits;
import com.aesophor.medievania.entity.character.Character;
import com.aesophor.medievania.entity.character.Player;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class WorldContactListener implements ContactListener {

    public WorldContactListener() {

    }


    @Override
    public void beginContact(Contact contact) {
        // Some variables that might get reused for several times.
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
                character.setIsJumping(false);
                break;

            // When a character lands on a platform, make following changes.
            case CategoryBits.FEET | CategoryBits.PLATFORM:
                character = (Character) getTargetFixture(CategoryBits.FEET, fixtureA, fixtureB).getUserData();
                character.setIsJumping(false);
                character.setIsOnPlatform(true);
                break;

            // When player gets close to a portal, register the portal to the player.
            case CategoryBits.PLAYER | CategoryBits.PORTAL:
                player = (Player) getTargetFixture(CategoryBits.PLAYER, fixtureA, fixtureB).getUserData();
                portal = (Portal) getTargetFixture(CategoryBits.PORTAL, fixtureA, fixtureB).getUserData();
                player.setCurrentPortal(portal);
                break;


            // When a player bumps into an enemy, the enemy will inflict damage and knockback to the player.
            case CategoryBits.PLAYER | CategoryBits.ENEMY:
                player = (Player) getTargetFixture(CategoryBits.PLAYER, fixtureA, fixtureB).getUserData();
                enemy = (Enemy) getTargetFixture(CategoryBits.ENEMY, fixtureA, fixtureB).getUserData();
                enemy.inflictDamage(player, 25);
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
                player.addInRangeTarget(enemy);
                break;

            // Set player as enemy's current target (so enemy can inflict damage to player).
            case CategoryBits.MELEE_WEAPON | CategoryBits.PLAYER:
                player = (Player) getTargetFixture(CategoryBits.PLAYER, fixtureA, fixtureB).getUserData();
                enemy = (Enemy) getTargetFixture(CategoryBits.MELEE_WEAPON, fixtureA, fixtureB).getUserData();
                enemy.addInRangeTarget(player);
                break;

            case CategoryBits.PLAYER | CategoryBits.ITEM:
                player = (Player) getTargetFixture(CategoryBits.PLAYER, fixtureA, fixtureB).getUserData();
                item = (Item) getTargetFixture(CategoryBits.ITEM, fixtureA, fixtureB).getUserData();
                player.getPickupItemTargetComponent().addInRangeItems(item);
                break;
                
            default:
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
        // Some variables that might get reused for several times.
        Character character;
        Player player;
        Enemy enemy;
        Portal portal;
        Item item;

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        int cDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;
        
        switch (cDef) {
            // When a character leaves the ground, make following changes.
            case CategoryBits.FEET | CategoryBits.GROUND:
                character = (Character) getTargetFixture(CategoryBits.FEET, fixtureA, fixtureB).getUserData();
                if (character.getB2Body().getLinearVelocity().y > .5f) {
                    character.setIsJumping(true);
                }
                break;

            // When a character leaves the platform, make following changes.
            case CategoryBits.FEET | CategoryBits.PLATFORM:
                character = (Character) getTargetFixture(CategoryBits.FEET, fixtureA, fixtureB).getUserData();
                if (character.getB2Body().getLinearVelocity().y < -.5f) {
                    character.setIsJumping(true);
                    character.setIsOnPlatform(false);
                }
                break;

            // When player leaves the portal, unregister the portal from the player.
            case CategoryBits.PLAYER | CategoryBits.PORTAL:
                player = (Player) getTargetFixture(CategoryBits.PLAYER, fixtureA, fixtureB).getUserData();
                player.setCurrentPortal(null);
                break;


            // Clear player's current target (so player cannot inflict damage to enemy from a distance).
            case CategoryBits.MELEE_WEAPON | CategoryBits.ENEMY:
                player = (Player) getTargetFixture(CategoryBits.MELEE_WEAPON, fixtureA, fixtureB).getUserData();
                enemy = (Enemy) getTargetFixture(CategoryBits.ENEMY, fixtureA, fixtureB).getUserData();
                player.removeInRangeTarget(enemy);
                break;

            // Clear enemy's current target (so enemy cannot inflict damage to player from a distance).
            case CategoryBits.MELEE_WEAPON | CategoryBits.PLAYER:
                enemy = (Enemy) getTargetFixture(CategoryBits.MELEE_WEAPON, fixtureA, fixtureB).getUserData();
                player = (Player) getTargetFixture(CategoryBits.PLAYER, fixtureA, fixtureB).getUserData();
                enemy.removeInRangeTarget(player);
                break;

            case CategoryBits.PLAYER | CategoryBits.ITEM:
                player = (Player) getTargetFixture(CategoryBits.PLAYER, fixtureA, fixtureB).getUserData();
                item = (Item) getTargetFixture(CategoryBits.ITEM, fixtureA, fixtureB).getUserData();
                player.getPickupItemTargetComponent().removeInRangeItems(item);
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
            case CategoryBits.PLAYER | CategoryBits.PLATFORM:
                Fixture playerBody = getTargetFixture(CategoryBits.PLAYER, fixtureA, fixtureB);
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
    public static Fixture getTargetFixture(short targetCategoryBits, Fixture fixtureA, Fixture fixtureB) {
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

}