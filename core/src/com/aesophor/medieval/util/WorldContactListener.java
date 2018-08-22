package com.aesophor.medieval.util;

import com.aesophor.medieval.Medieval;
import com.aesophor.medieval.sprites.Enemy;
import com.aesophor.medieval.sprites.Player;
import com.badlogic.gdx.Gdx;
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
        
        /*
        if ("head".equals(fixtureA.getUserData()) || "head".equals(fixtureB.getUserData())) {
            Fixture head = (fixtureA.getUserData().equals("head")) ? fixtureA : fixtureB;
            Fixture object = (fixtureA.getUserData().equals("head")) ? fixtureB : fixtureA;
            
            if (object.getUserData() instanceof InteractiveTileObject) {
                ((InteractiveTileObject) object.getUserData()).onHeadHit();
            }
        }
        */
        
        if ("body".equals(fixtureA.getUserData()) || "body".equals(fixtureB.getUserData())) {
            Fixture body = ("body".equals(fixtureA.getUserData())) ? fixtureA : fixtureB;
            player.setIsJumping(false);
        }
        
        
        switch (cDef) {
            case Medieval.PLAYER_BIT | Medieval.ENEMY_BIT:
                if (fixtureA.getFilterData().categoryBits == Medieval.PLAYER_BIT) {
                    ((Player) fixtureA.getUserData()).inflictDamage(25);
                } else {
                    ((Player) fixtureB.getUserData()).inflictDamage(25);
                }
                Gdx.app.log("Player", "received 25 damage.");
                break;
                
            case Medieval.MELEE_WEAPON_BIT | Medieval.ENEMY_BIT:
                Player player;
                Enemy enemy;
                // Set enemy as player's current target (so he can inflict damage to it).
                if (fixtureA.getFilterData().categoryBits == Medieval.ENEMY_BIT) {
                    enemy = (Enemy) (fixtureA.getUserData());
                    player = (Player) (fixtureB.getUserData());
                    
                    player.setTargetEnemy(enemy);
                } else {
                    enemy = (Enemy) (fixtureB.getUserData());
                    player = (Player) (fixtureA.getUserData());
                    
                    player.setTargetEnemy(enemy);
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
        
        if ("body".equals(fixtureA.getUserData()) || "body".equals(fixtureB.getUserData())) {
            Fixture body = ("body".equals(fixtureA.getUserData())) ? fixtureA : fixtureB;
            player.setIsJumping(true);
        }
        
        int cDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;
        
        switch (cDef) {
            case Medieval.MELEE_WEAPON_BIT | Medieval.ENEMY_BIT:
                Player player;
                // Set enemy as player's current target (so he can inflict damage to it).
                if (fixtureA.getFilterData().categoryBits == Medieval.ENEMY_BIT) {
                    player = (Player) (fixtureB.getUserData());
                    player.setTargetEnemy(null);
                } else {
                    player = (Player) (fixtureA.getUserData());
                    player.setTargetEnemy(null);
                }
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