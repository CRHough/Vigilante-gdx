package com.aesophor.medievania.world.map;

import com.aesophor.medievania.constant.Constants;
import com.aesophor.medievania.world.object.character.Character;
import com.aesophor.medievania.world.object.character.Player;
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
            case Constants.PLAYER_BIT | Constants.ENEMY_BIT:
                if (fixtureA.getFilterData().categoryBits == Constants.PLAYER_BIT) {
                    ((Player) fixtureA.getUserData()).receiveDamage(25);
                } else {
                    ((Player) fixtureB.getUserData()).receiveDamage(25);
                }
                Gdx.app.log("Player", "received 25 damage.");
                break;
                
            case Constants.MELEE_WEAPON_BIT | Constants.ENEMY_BIT:
                Player player;
                Character target;
                // Set enemy as player's current target (so he can inflict damage to it).
                if (fixtureA.getFilterData().categoryBits == Constants.ENEMY_BIT) {
                    target = (Character) (fixtureA.getUserData());
                    player = (Player) (fixtureB.getUserData());
                    
                    player.setTargetEnemy(target);
                } else {
                    target = (Character) (fixtureB.getUserData());
                    player = (Player) (fixtureA.getUserData());
                    
                    player.setTargetEnemy(target);
                }
                break;
                
            case Constants.MELEE_WEAPON_BIT | Constants.PLAYER_BIT:
                Player p;
                Character t;
                System.out.println("enemy got a target");
                // Set enemy as player's current target (so he can inflict damage to it).
                if (fixtureA.getFilterData().categoryBits == Constants.PLAYER_BIT) {
                    p = (Player) (fixtureA.getUserData());
                    t = (Character) (fixtureB.getUserData());
                    
                    t.setTargetEnemy(p);
                } else {
                    p = (Player) (fixtureB.getUserData());
                    t = (Character) (fixtureA.getUserData());
                    
                    t.setTargetEnemy(p);
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
            case Constants.MELEE_WEAPON_BIT | Constants.ENEMY_BIT:
                Player player;
                // Unset player's current target when contact ends.
                if (fixtureA.getFilterData().categoryBits == Constants.ENEMY_BIT) {
                    player = (Player) (fixtureB.getUserData());
                    player.setTargetEnemy(null);
                } else {
                    player = (Player) (fixtureA.getUserData());
                    player.setTargetEnemy(null);
                }
                break;
                
            case Constants.MELEE_WEAPON_BIT | Constants.PLAYER_BIT:
                Player p;
                Character t;
                // Set enemy as player's current target (so he can inflict damage to it).
                if (fixtureA.getFilterData().categoryBits == Constants.PLAYER_BIT) {
                    p = (Player) (fixtureA.getUserData());
                    t = (Character) (fixtureB.getUserData());
                    
                    t.setTargetEnemy(null);
                } else {
                    p = (Player) (fixtureB.getUserData());
                    t = (Character) (fixtureA.getUserData());
                    
                    t.setTargetEnemy(null);
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