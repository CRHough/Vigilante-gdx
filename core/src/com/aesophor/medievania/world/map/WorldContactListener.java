package com.aesophor.medievania.world.map;

import com.aesophor.medievania.constants.CategoryBits;
import com.aesophor.medievania.world.character.Character;
import com.aesophor.medievania.world.character.Player;
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
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        
        int cDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;
        
        switch (cDef) {
            case CategoryBits.PLAYER | CategoryBits.GROUND:
                if (fixtureA.getFilterData().categoryBits == CategoryBits.PLAYER) {
                    ((Player) fixtureA.getUserData()).setIsJumping(false);
                } else {
                    ((Player) fixtureB.getUserData()).setIsJumping(false);
                }
                break;
                
            case CategoryBits.PLAYER | CategoryBits.PLATFORM:
                if (fixtureA.getFilterData().categoryBits == CategoryBits.PLAYER) {
                    ((Player) fixtureA.getUserData()).setIsJumping(false);
                } else {
                    ((Player) fixtureB.getUserData()).setIsJumping(false);
                }
                break;
                
            case CategoryBits.ENEMY | CategoryBits.GROUND:
                if (fixtureA.getFilterData().categoryBits == CategoryBits.ENEMY) {
                    ((Character) fixtureA.getUserData()).setIsJumping(false);
                } else {
                    ((Character) fixtureB.getUserData()).setIsJumping(false);
                }
                break;
                
            case CategoryBits.ENEMY | CategoryBits.PLATFORM:
                if (fixtureA.getFilterData().categoryBits == CategoryBits.ENEMY) {
                    ((Character) fixtureA.getUserData()).setIsJumping(false);
                } else {
                    ((Character) fixtureB.getUserData()).setIsJumping(false);
                }
                break;
                
                
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
                    ((Character) fixtureA.getUserData()).reverseMovement();
                } else {
                    ((Character) fixtureB.getUserData()).reverseMovement();
                }
                System.out.println("hit!");
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
            case CategoryBits.PLAYER | CategoryBits.GROUND:
                if (fixtureA.getFilterData().categoryBits == CategoryBits.PLAYER) {
                    if (((Player) fixtureA.getUserData()).getB2Body().getLinearVelocity().y < -.5f) {
                        ((Player) fixtureA.getUserData()).setIsJumping(true);
                    }
                } else {
                    if (((Player) fixtureB.getUserData()).getB2Body().getLinearVelocity().y < -.5f) {
                        ((Player) fixtureB.getUserData()).setIsJumping(true);
                    }
                }
                break;
                
            case CategoryBits.PLAYER | CategoryBits.PLATFORM:
                if (fixtureA.getFilterData().categoryBits == CategoryBits.PLAYER) {
                    if (((Player) fixtureA.getUserData()).getB2Body().getLinearVelocity().y < -.5f) {
                        ((Player) fixtureA.getUserData()).setIsJumping(true);
                    }
                } else {
                    if (((Player) fixtureB.getUserData()).getB2Body().getLinearVelocity().y < -.5f) {
                        ((Player) fixtureB.getUserData()).setIsJumping(true);
                    }
                }
                break;
                
            case CategoryBits.ENEMY | CategoryBits.GROUND:
                if (fixtureA.getFilterData().categoryBits == CategoryBits.ENEMY) {
                    ((Character) fixtureA.getUserData()).setIsJumping(true);
                } else {
                    ((Character) fixtureB.getUserData()).setIsJumping(true);
                }
                break;
                
            case CategoryBits.ENEMY | CategoryBits.PLATFORM:
                if (fixtureA.getFilterData().categoryBits == CategoryBits.ENEMY) {
                    ((Character) fixtureA.getUserData()).setIsJumping(true);
                } else {
                    ((Character) fixtureB.getUserData()).setIsJumping(true);
                }
                break;
                
            case CategoryBits.MELEE_WEAPON | CategoryBits.ENEMY:
                Player player;
                // Clear player's in range target when contact ends.
                if (fixtureA.getFilterData().categoryBits == CategoryBits.ENEMY) {
                    player = (Player) (fixtureB.getUserData());
                    player.setInRangeTarget(null);
                } else {
                    player = (Player) (fixtureA.getUserData());
                    player.setInRangeTarget(null);
                }
                break;
                
            case CategoryBits.MELEE_WEAPON | CategoryBits.PLAYER:
                Character t;
                // Clear target's in range target when contact ends.
                if (fixtureA.getFilterData().categoryBits == CategoryBits.PLAYER) {
                    t = (Character) (fixtureB.getUserData());
                    t.setInRangeTarget(null);
                } else {
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
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        
        int cDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;
        
        switch (cDef) {
            // Allow player to pass through platforms and collide on the way down.
            case CategoryBits.PLAYER | CategoryBits.PLATFORM:
                float playerY;
                float platformY;
                
                if (fixtureA.getFilterData().categoryBits == CategoryBits.PLAYER) {
                    playerY = fixtureA.getBody().getPosition().y;
                    platformY = fixtureB.getBody().getPosition().y;
                } else {
                    playerY = fixtureB.getBody().getPosition().y;
                    platformY = fixtureA.getBody().getPosition().y;
                }
                
                if (playerY > platformY + .15f) { // Player is about to land on the platform.
                    contact.setEnabled(true);
                } else {
                    contact.setEnabled(false);
                }
                
                break;
                
            default:
                break;
        }
        
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        
    }

}