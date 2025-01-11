package agh.oop.darwin_world;

import agh.oop.darwin_world.model.enums.MapDirection;

import java.util.ArrayList;
import java.util.List;

public class OptionsParser {

    private final static String ILLEGAL_MOVE_MESSAGE = "- illegal move";
    public static List<MapDirection> parse(String[] args)
    {
        List<MapDirection> moves = new ArrayList<>();
        for(String arg : args)
            switch(arg) {
                case "N"-> moves.add(MapDirection.N);
                case "S"-> moves.add(MapDirection.S);
                case "E"-> moves.add(MapDirection.E);
                case "W"-> moves.add(MapDirection.W);
                case "NE" -> moves.add(MapDirection.NE);
                case "SE" -> moves.add(MapDirection.SE);
                case "SW" -> moves.add(MapDirection.SW);
                case "NW" -> moves.add(MapDirection.NW);
                default -> throw new IllegalArgumentException(String.format("%s %s",arg,ILLEGAL_MOVE_MESSAGE));
            }
        return moves;
    }
}