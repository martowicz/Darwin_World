package agh.ics.oop;

import agh.ics.oop.model.Grass;
import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Vector2d;

public class World {
    public static void main(String[] args) {

        MapDirection mv = MapDirection.NE;
        MapDirection rotate = mv.rotate(7);
        System.out.println(rotate);
        Grass grass = new Grass(new Vector2d(2,2));


    }
}
