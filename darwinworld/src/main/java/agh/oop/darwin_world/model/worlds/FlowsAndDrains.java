package agh.oop.darwin_world.model.worlds;


import agh.oop.darwin_world.model.utils.SortedLinkedList;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.world_elements.Animal;
import agh.oop.darwin_world.model.world_elements.Lake;
import agh.oop.darwin_world.model.world_elements.Water;
import agh.oop.darwin_world.model.world_elements.WorldElement;
import agh.oop.darwin_world.presenter.UserConfigurationRecord;

import java.security.SecureRandom;
import java.util.*;

public class FlowsAndDrains extends AbstractWorldMap {

    List<Lake> lakes = new ArrayList<>();

    public FlowsAndDrains(UserConfigurationRecord config) {
        super(config);
        Lake lake = new Lake(config);
        lakes.add(lake);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        for (Lake lake : lakes) {
            if(lake.isLake(position)) {return false;}
        }
        return super.canMoveTo(position);
    }

    @Override
    public WorldElement returnObjectAt(Vector2d position) { //ważne do wyświetlania elementów mapy
        for (Lake lake : lakes) {
            if (lake.isLake(position)) {
                return lake;
            }
        }
            return super.returnObjectAt(position);
    }

    @Override
    public void generateEnvironment ( int grassCount, int day){
        super.generateEnvironment(grassCount, day);
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
                    if (lake.isLake(position)) {
                        SortedLinkedList<Animal> animals = animalsAtPositions.get(position);
                        for (Animal animal : animals) {
                            animal.setEnergy(0); //nieładne to chyba
                        }
                    }
                }
            }
        }


        }
    }