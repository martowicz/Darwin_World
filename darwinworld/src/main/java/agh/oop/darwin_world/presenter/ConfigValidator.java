package agh.oop.darwin_world.presenter;

import agh.oop.darwin_world.model.utils.InvalidParamenterException;

import java.io.ObjectInputFilter;

public class ConfigValidator
{
    public static void validate(UserConfigurationRecord config) throws InvalidParamenterException {
        if(config.minMutations()>config.maxMutations())
            throw new InvalidParamenterException("Minimum mutation number should be less than maximum mutation");
        if(config.genomLength()<config.maxMutations())
            throw new InvalidParamenterException("Genom length should be more than maximum mutation");
        if(config.animalsEnergyToCopulate()<config.animalsEnergySpentOnCopulation())
            throw new InvalidParamenterException("Energy given to child should be less than spent on copulation");
    }

}
