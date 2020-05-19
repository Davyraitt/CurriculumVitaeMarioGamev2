package com.davyraitt.cvgame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.davyraitt.cvgame.CVGame;
import com.davyraitt.cvgame.Scenes.Hud;
import com.davyraitt.cvgame.Sprites.Mario;
import com.davyraitt.cvgame.Tools.B2WorldCreator;
import com.davyraitt.cvgame.Tools.WorldContactListener;

import javax.xml.soap.Text;

public class PlayScreen implements Screen {


    //textures and sprites and the game class
    private CVGame game;
    private TextureAtlas atlas;

    //camera's and views
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;


    //variables for loading the tiled map
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //variables vor Box2d (object collision)
    private World world;
    private Box2DDebugRenderer b2dr;

    //creating the player
    private Mario player;


    public PlayScreen(CVGame game) {
        atlas = new TextureAtlas("Mario_And_Enemies.pack");

        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(CVGame.V_WIDTH / CVGame.PPM, CVGame.V_HEIGHT / CVGame.PPM, gameCam);
        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("Map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / CVGame.PPM);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();


        //create player
        player = new Mario(world, this);

        world.setContactListener(new WorldContactListener());

        new B2WorldCreator(world, map);
    }

    public TextureAtlas getAtlas () {
        return atlas;
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2 || Gdx.input.isKeyPressed(Input.Keys.D  ) && player.b2body.getLinearVelocity().x <= 2) {
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true );
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2 || Gdx.input.isKeyPressed(Input.Keys.A  ) && player.b2body.getLinearVelocity().x <= 2) {
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true );
        }
    }

    public void update(float dt) {
        handleInput(dt);

        world.step(1 / 60f, 6, 2);

        player.update(dt);
        hud.update(dt);

        gameCam.position.x = player.b2body.getPosition().x;

        gameCam.update();
    }

    @Override
    public void render(float delta) {

        //Update logic
        update(delta);
        renderer.setView(gameCam);


        //Clear the game screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Render our game map
        renderer.render();

        //Render our BOX2DDebugLines
        b2dr.render(world, gameCam.combined);

        //Render mario
        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();


        // Draw what the HUD camera sees
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
