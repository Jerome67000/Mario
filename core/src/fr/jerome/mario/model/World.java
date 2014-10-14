package fr.jerome.mario.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;

/**
 * Représente le monde (un niveau)
 * Created by jerome on 01/10/14.
 */
public class World {

    public Mario mario;
    private TiledMap tiledMap;
    private TiledMapTileLayer groundLayer;

    public World() {

        mario = new Mario(new Vector2(3, 2), this);
        tiledMap = new TmxMapLoader().load("Maps/map_test.tmx");
        groundLayer = (TiledMapTileLayer)tiledMap.getLayers().get("obstacles");
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public TiledMapTileLayer getGroundLayer() {
        return groundLayer;
    }
}
