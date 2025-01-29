package agh.oop.darwin_world.presenter;

import agh.oop.darwin_world.model.enums.AnimalMutationType;
import agh.oop.darwin_world.model.enums.WorldMapType;
import agh.oop.darwin_world.model.worlds.Boundary;

public record UserConfigurationRecord (Boundary mapBoundary,
                                      WorldMapType mapType,
                                      int startingPlantNumber,
                                      int energyFromPlant,
                                      int plantsGrowingDaily,
                                      int animalsCountAtStart,
                                      int animalsEnergyAtStart,
                                      int animalsEnergyToCopulate,
                                      int animalsEnergySpentOnCopulation,
                                      int minMutations,
                                      int maxMutations,
                                      int genomLength,
                                      AnimalMutationType mutationType) {
}
