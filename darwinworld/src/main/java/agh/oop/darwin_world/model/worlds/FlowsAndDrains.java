package agh.oop.darwin_world.model.worlds;


import agh.oop.darwin_world.World;
import agh.oop.darwin_world.model.utils.SortedLinkedList;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.world_elements.Animal;
import agh.oop.darwin_world.model.world_elements.Lake;
import agh.oop.darwin_world.model.world_elements.WorldElement;
import agh.oop.darwin_world.presenter.UserConfigurationRecord;

import java.util.*;

public class FlowsAndDrains extends AbstractWorldMap {

    private List<Lake> lakes = new ArrayList<>();

    public FlowsAndDrains(UserConfigurationRecord config) {
        super(config);
        Lake lake = new Lake(config);
        lakes.add(lake);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        for (Lake lake : lakes) {
            if(lake.occupiedByLake(position)) {
                return false;
            }
        }
        return super.canMoveTo(position);
    }

    @Override
    public WorldElement returnObjectAt(Vector2d position) { //ważne do wyświetlania elementów mapy
        for (Lake lake : lakes) {
            if (lake.occupiedByLake(position)) {
                return lake;
            }
        }
            return super.returnObjectAt(position);
    }

    @Override
    public WorldElement returnEnvironmentAt(Vector2d position) {
        for (Lake lake : lakes) {
            if (lake.occupiedByLake(position)) {
                return lake;
            }
        }
        return super.returnEnvironmentAt(position);
    }



    @Override
    public void generateEnvironment (int plantsCount, int day){
        super.generateEnvironment(plantsCount, day);
        if (day % 5 == 1) {
            Random rand = new Random();
            for (Lake lake : lakes) {
                int s = rand.nextInt(2);
                if (s == 0) {
                    lake.reduceLake();
                } else {
                    lake.extendLake();
                }
                for (Vector2d position : animalsAtPositions.keySet()) {
                    if (lake.occupiedByLake(position)) {
                        SortedLinkedList<Animal> animals = animalsAtPositions.get(position);
                        for (Animal animal : animals) {
                            animal.setDyingForOtherCause();
                            //System.out.println("Animal dying because of water");
                        }
                    }
                }

            }
        }


        }
    }