package agh.oop.darwin_world.model.worlds;


import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.world_elements.Water;
import agh.oop.darwin_world.model.world_elements.WorldElement;
import agh.oop.darwin_world.presenter.UserConfigurationRecord;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class FlowsAndDrains extends AbstractWorldMap {

    Map<Vector2d, WorldElement> waterPlaces = new HashMap<Vector2d, WorldElement>();

    public FlowsAndDrains(UserConfigurationRecord config) {
        super(config);
        generateRandomWaterPosition();


    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return super.canMoveTo(position) && !waterPlaces.containsKey(position); // and no water on the map
    }

    @Override
    public WorldElement objectAt(Vector2d position) { //ważne do wyświetlania elementów mapy
        if (waterPlaces.containsKey(position)) {
            return waterPlaces.get(position);
        }
        return super.objectAt(position);
    }

    public void generateRandomWaterPosition() {
        SecureRandom random = new SecureRandom();
        int x = random.nextInt(boundary.upperRight().getX());
        int y = random.nextInt(boundary.upperRight().getY());
        Vector2d position = new Vector2d(x, y);
        Water water = new Water(position);
        waterPlaces.put(position, water);
    }


}
