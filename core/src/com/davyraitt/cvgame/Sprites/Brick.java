package com.davyraitt.cvgame.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.davyraitt.cvgame.CVGame;
import com.davyraitt.cvgame.Scenes.Hud;

public class Brick extends InteractiveTileObject {


    public Brick(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        fixture.setUserData(this);
        setCategoryFilter(CVGame.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Brick", "Collision");
        setCategoryFilter(CVGame.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.addScore(3141);
    }
}
