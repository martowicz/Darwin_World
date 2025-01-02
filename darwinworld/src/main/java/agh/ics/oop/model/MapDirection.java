package agh.ics.oop.model;

import java.util.Random;

public enum MapDirection {
    N, NE, E, SE, S, SW, W, NW;

    private final static MapDirection[] values = values();


    public MapDirection rotate(int rotation){
        return values[(rotation + this.ordinal()) % values.length];
    }

    public Vector2d toUnitVector() {
        return switch (this){
            case N -> new Vector2d(0, 1);
            case E -> new Vector2d(1, 0);
            case S -> new Vector2d(0, -1);
            case W -> new Vector2d(-1, 0);
            case NE -> new Vector2d(1, 1);
            case SW -> new Vector2d(-1, -1);
            case NW -> new Vector2d(-1, 1);
            case SE -> new Vector2d(1, -1);
        };
    }

    @Override
    public String toString() {
        return switch (this){
            case N -> "N";
            case E -> "E";
            case S -> "S";
            case W -> "W";
            case NE -> "NE";
            case SW -> "SW";
            case NW -> "NW";
            case SE -> "SE";
        };
    }

    public MapDirection reverse() {
        return switch (this){
            case N -> S;
            case E -> W;
            case S -> N;
            case W -> E;
            case NE -> SW;
            case NW -> SE;
            case SE -> NW;
            case SW -> NE;
        };
    }

    public MapDirection createRandom(){
        return values[new Random().nextInt(8)]; //będzie potrzebne przy tworzeniu nowego zwierzaka
    }

}
