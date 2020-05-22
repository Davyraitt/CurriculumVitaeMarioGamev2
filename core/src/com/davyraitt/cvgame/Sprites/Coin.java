package com.davyraitt.cvgame.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.davyraitt.cvgame.CVGame;
import com.davyraitt.cvgame.Items.ItemDef;
import com.davyraitt.cvgame.Items.Mushroom;
import com.davyraitt.cvgame.Scenes.Hud;
import com.davyraitt.cvgame.Screens.PlayScreen;

public class Coin extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 3290;

    public Coin(PlayScreen screen, MapObject object) {
        super(screen, object);
        tileSet = map.getTileSets().getTileSet("NES - Super Mario Bros - World 1-1");
        fixture.setUserData(this);
        setCategoryFilter(CVGame.COIN_BIT);


    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Coin", "Collision");


        if (getCell().getTile().getId() == BLANK_COIN) {
            CVGame.manager.get("Audio/Sounds/bump.wav", Sound.class).play();
        } else {
            if (object.getProperties().containsKey("mushroom")) {
                screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / CVGame.PPM),
                        Mushroom.class));
                CVGame.manager.get("Audio/Sounds/powerup_spawn.wav", Sound.class).play();
            }

            else {
                CVGame.manager.get("Audio/Sounds/coin.wav", Sound.class).play();
            }


            Hud.addScore(2000);

        }

        getCell().setTile(tileSet.getTile(BLANK_COIN));


    }
}
