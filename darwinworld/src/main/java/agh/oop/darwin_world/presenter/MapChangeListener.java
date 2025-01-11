package agh.oop.darwin_world.presenter;

import agh.oop.darwin_world.model.worlds.WorldMap;

public interface MapChangeListener {
    void mapChanged(WorldMap worldMap, String message);
}