package fr.jerome.mario.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

import fr.jerome.mario.model.Mario;
import fr.jerome.mario.model.World;
import fr.jerome.mario.model.enemies.Goomba;

/**
 * Classe qui s'occupe d'afficher graphiquement World.java
 * Created by jerome on 02/10/14.
 */
public class WorldRenderer {

    private final boolean debug = false;

    // Dimensions de la caméra
    private static final float CAMERA_WIDTH = 20;
    private static final float CAMERA_HEIGHT = 15;

    // Représentation du monde à afficher
    private World world;
    // Camera pour regarder dans le monde
    private OrthographicCamera camera;
    // Rendu traçage debug
    private ShapeRenderer debugRenderer;
    // Pour le rendu avec image
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    // Notre héro
    private Mario mario;
    private MarioRenderer marioRenderer;
    private EnemiesRenderer enemiesRenderer;

    private float stateTime = 0f;

    public WorldRenderer(World w) {

        this.world = w;
        this.mario = world.mario;

        this.debugRenderer = new ShapeRenderer();

        // Load the map
        this.tiledMapRenderer = new OrthogonalTiledMapRenderer(world.getTiledMap(), 1/16f);

        this.marioRenderer = new MarioRenderer(mario, tiledMapRenderer.getSpriteBatch());
        this.enemiesRenderer = new EnemiesRenderer(world, tiledMapRenderer.getSpriteBatch());

        // caméra orthographic de 20x15 unités
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, CAMERA_WIDTH, CAMERA_HEIGHT);
        this.camera.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
        this.camera.update();
    }

    // TODO Implémenter l'anti retour de la camera
    public void moveCamera() {

        float marioX = mario.getPos().x;
        int mapWidth  = world.getTiledMap().getProperties().get("width", Integer.class);
        // nombres d'unités d'avance pour la caméra  par rapport à mario
        int decCam = 4;

        // Suivre mario s'il avance et ne dépasse pas les dimensions de la map
        if (marioX > (CAMERA_WIDTH / 2f - decCam) && marioX < mapWidth - decCam - CAMERA_WIDTH / 2f)
            camera.position.set(marioX + decCam, camera.position.y, 0);

        camera.update();
    }

    public void render() {

        stateTime += Gdx.graphics.getDeltaTime();

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        tiledMapRenderer.getSpriteBatch().begin();
            enemiesRenderer.renderer(stateTime);
            marioRenderer.renderer(stateTime);
        tiledMapRenderer.getSpriteBatch().end();

        if (debug) drawDebug() ;

        moveCamera();
    }

    public void drawDebug() {

        // Configuration du rendu avec ShapeRenderer
        debugRenderer.setProjectionMatrix(camera.combined);

        // Récupération de la map et définir le nb de layer
        TiledMap map = world.getTiledMap();
        int nbLayer = map.getLayers().getCount();

        debugRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Boucle pour passer dans chaque layer
        for (int i = 0; i < 1; i++) {

            // Récupère layer par layer
            TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("map");

            // Couleur différente pour chaque Layer
            switch (i) {
                case 1:
                    debugRenderer.setColor(new Color(0, 0, 0, 1));  // Ciel
                case 2:
                    debugRenderer.setColor(new Color(1, 0.5f, 0.5f, 1));
                    break; // Nuages
                case 3:
                    debugRenderer.setColor(new Color(0, 0, 1, 1));
                    break; // Map
                case 4:
                    debugRenderer.setColor(new Color(0, 1, 1, 1));
                    break; // Collision
            }

            // Boucle avec test si cell existe = traçage
            for (int y = 0; y <= world.getMapHeight(); y++) {
                for (int x = 0; x <= world.getMapWidth(); x++) {
                    TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                    if (cell != null) {
                        Rectangle rect = new Rectangle(x, y, 1, 1);
                        debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
                    }
                }
            }
        }

        debugRenderer.setColor(new Color(1, 1, 1, 1));
        for (Rectangle rect : world.getCollisions())
            debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);


        // Goomba
        debugRenderer.setColor(new Color(0.8f, 0.8f, 0.1f, 1));
        for (Goomba goomba : world.getGoombas())
            debugRenderer.rect(goomba.pos.x, goomba.pos.y, goomba.rect.width, goomba.rect.height);

        // Mario
        debugRenderer.setColor(new Color(0.5f, 0.5f, 0.5f, 1));
        debugRenderer.rect(mario.getPos().x, mario.getPos().y, 1, 1);

        debugRenderer.end();
    }
}


