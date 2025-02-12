package agh.oop.darwin_world.model.worlds;


import agh.oop.darwin_world.World;
import agh.oop.darwin_world.model.utils.SortedLinkedList;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.world_elements.Animal;
import agh.oop.darwin_world.model.world_elements.Lake;
import agh.oop.darwin_world.model.world_elements.Plant;
import agh.oop.darwin_world.model.world_elements.WorldElement;
import agh.oop.darwin_world.presenter.UserConfigurationRecord;

import java.util.*;

public class FlowsAndDrains extends AbstractWorldMap {

    private List<Lake> lakes;

    public FlowsAndDrains(UserConfigurationRecord config) {
        super(config);
        this.lakes=new ArrayList<>();
        for(int i=0;i<countNumberOfLakes(config);i++){
            lakes.add(new Lake(config));
        }

   }

   private int countNumberOfLakes(UserConfigurationRecord config) {
        System.out.println(Math.min(config.mapBoundary().upperRight().x(), config.mapBoundary().upperRight().y())/10);
        return Math.min(config.mapBoundary().upperRight().x(), config.mapBoundary().upperRight().y())/10;

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
    public boolean plantCanBePlaced(Vector2d position) {
        if((lakes != null) && !lakes.isEmpty()) {
            for (Lake lake : lakes) {
                if (lake.occupiedByLake(position)) {
                    return false;
                }
            }
        }

        return super.plantCanBePlaced(position);
    }


    @Override
    public void generateEnvironment (int plantsCount, int day){
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


                List<Vector2d> plantsToRemovePositions= new ArrayList<>();
                for(Vector2d position : plants.keySet()){
                    if (lake.occupiedByLake(position)) {
                        plantsToRemovePositions.add(position);
                    }
                }
                for (Vector2d position : plantsToRemovePositions) {
                    plants.remove(position);
                }

            }
        }
        super.generateEnvironment(plantsCount, day);
        }
    }