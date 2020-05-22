package com.davyraitt.cvgame.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.davyraitt.cvgame.CVGame;
import com.davyraitt.cvgame.Items.Item;
import com.davyraitt.cvgame.Sprites.Enemy;
import com.davyraitt.cvgame.Sprites.InteractiveTileObject;
import com.davyraitt.cvgame.Sprites.Mario;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        if (fixA.getUserData() == ("head") || fixB.getUserData() == ("head")) {
            Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;

            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())) {
                ((InteractiveTileObject) object.getUserData()).onHeadHit();
            }
        }

        switch (cDef) {
            case CVGame.ENEMY_HEAD_BIT | CVGame.MARIO_BIT:
                if (fixA.getFilterData().categoryBits == CVGame.ENEMY_HEAD_BIT) {
                    ((Enemy) fixA.getUserData()).hitOnHead();
                } else if (fixA.getFilterData().categoryBits == CVGame.ENEMY_HEAD_BIT) {
                    ((Enemy) fixB.getUserData()).hitOnHead();
                }
                break;
            case CVGame.ENEMY_BIT | CVGame.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == CVGame.ENEMY_BIT) {
                    ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                } else {
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                }
                break;
            case CVGame.MARIO_BIT | CVGame.ENEMY_BIT:
                Gdx.app.log("Mario", "DIED");
            case CVGame.ITEM_BIT | CVGame.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == CVGame.ITEM_BIT) {
                    ((Item) fixA.getUserData()).reverseVelocity(true, false);
                } else {
                }
                break;

            case CVGame.ITEM_BIT | CVGame.MARIO_BIT:
                if (fixA.getFilterData().categoryBits == CVGame.ITEM_BIT) {
                    ((Item) fixA.getUserData()).use((Mario) fixB.getUserData());
                } else {
                    ((Item) fixB.getUserData()).use((Mario) fixA.getUserData());
                }
                break;


        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
