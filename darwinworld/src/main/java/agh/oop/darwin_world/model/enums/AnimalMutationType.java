package agh.oop.darwin_world.model.enums;

import agh.oop.darwin_world.model.mutation.AbstractMutation;
import agh.oop.darwin_world.model.mutation.LightCorrectionMutation;
import agh.oop.darwin_world.model.mutation.RandomMutation;

public enum AnimalMutationType
{
    RANDOM_MUTATION,
    LIGHT_CORRECTION_MUTATION;

    public AbstractMutation enumToMutation(int min,int max)
    {
        return switch (this){
            case RANDOM_MUTATION -> new RandomMutation(min,max);
            case LIGHT_CORRECTION_MUTATION -> new LightCorrectionMutation(min,max);
        };
    }

    public static AnimalMutationType stringToEnum(String str)
    {
        return switch (str){
            case "Random Mutation" ->RANDOM_MUTATION;
            case "Light Correction Mutation" -> LIGHT_CORRECTION_MUTATION;
            default -> throw new IllegalStateException("Unexpected value: " + str);
        };
    }

    @Override
    public String toString() {
    return switch (this){
        case RANDOM_MUTATION -> "Random Mutation";
        case LIGHT_CORRECTION_MUTATION -> "Light Correction Mutation";
        };
    }
}
