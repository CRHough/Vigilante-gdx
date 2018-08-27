package com.aesophor.medievania.world.map;

import com.aesophor.medievania.constants.CategoryBits;
import com.aesophor.medievania.constants.Constants;
import com.aesophor.medievania.world.object.character.Character;
import com.aesophor.medievania.world.object.character.Player;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class WorldContactListener implements ContactListener {
    
    private Player player;
    
    public WorldContactListener(Player player) {
        this.player = player;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        
        int cDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;
        
        switch (cDef) {
            case CategoryBits.PLAYER | CategoryBits.ENEMY:
                if (fixtureA.getFilterData().categoryBits == CategoryBits.PLAYER) {
                    ((Player) fixtureA.getUserData()).receiveDamage(25);
                    ((Player) fixtureA.getUserData()).pushedBackward(1f);
                } else {
                    ((Player) fixtureB.getUserData()).receiveDamage(25);
                    ((Player) fixtureB.getUserData()).pushedBackward(1f);
                }
                break;
                
            case CategoryBits.ENEMY | CategoryBits.CLIFF_MARKER:
                if (fixtureA.getFilterData().categoryBits == CategoryBits.ENEMY) {
                    ((Character) fixtureA.getUserData()).jump();
                } else {
                    ((Character) fixtureB.getUserData()).jump();
                }
                break;
                
            case CategoryBits.MELEE_WEAPON | CategoryBits.ENEMY:
                Player player;
                Character target;
                // Set enemy as player's current target (so he can inflict damage to it).
                if (fixtureA.getFilterData().categoryBits == CategoryBits.ENEMY) {
                    target = (Character) (fixtureA.getUserData());
                    player = (Player) (fixtureB.getUserData());
                    
                    player.setInRangeTarget(target);
                } else {
                    target = (Character) (fixtureB.getUserData());
                    player = (Player) (fixtureA.getUserData());
                    
                    player.setInRangeTarget(target);
                }
                break;
                
            case CategoryBits.MELEE_WEAPON | CategoryBits.PLAYER:
                Player p;
                Character t;
                // Set enemy as player's current target (so he can inflict damage to it).
                if (fixtureA.getFilterData().categoryBits == CategoryBits.PLAYER) {
                    p = (Player) (fixtureA.getUserData());
                    t = (Character) (fixtureB.getUserData());
                    
                    t.setInRangeTarget(p);
                } else {
                    p = (Player) (fixtureB.getUserData());
                    t = (Character) (fixtureA.getUserData());
                    
                    t.setInRangeTarget(p);
                }
                break;
                
            default:
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        
        int cDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;
        
        switch (cDef) {
            case CategoryBits.MELEE_WEAPON | CategoryBits.ENEMY:
                Player player;
                // Unset player's current target when contact ends.
                if (fixtureA.getFilterData().categoryBits == CategoryBits.ENEMY) {
                    player = (Player) (fixtureB.getUserData());
                    player.setInRangeTarget(null);
                } else {
                    player = (Player) (fixtureA.getUserData());
                    player.setInRangeTarget(null);
                }
                break;
                
            case CategoryBits.MELEE_WEAPON | CategoryBits.PLAYER:
                Player p;
                Character t;
                // Set enemy as player's current target (so he can inflict damage to it).
                if (fixtureA.getFilterData().categoryBits == CategoryBits.PLAYER) {
                    p = (Player) (fixtureA.getUserData());
                    t = (Character) (fixtureB.getUserData());
                    
                    t.setInRangeTarget(null);
                } else {
                    p = (Player) (fixtureB.getUserData());
                    t = (Character) (fixtureA.getUserData());
                    
                    t.setInRangeTarget(null);
                }
                break;
                
            default:
                break;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // TODO Auto-generated method stub
        
    }

}