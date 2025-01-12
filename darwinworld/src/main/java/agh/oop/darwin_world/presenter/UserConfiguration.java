package agh.oop.darwin_world.presenter;

import agh.oop.darwin_world.model.enums.AnimalMutationType;
import agh.oop.darwin_world.model.enums.MapDirection;
import agh.oop.darwin_world.model.enums.WorldMapType;
import agh.oop.darwin_world.model.mutation.AbstractMutation;
import agh.oop.darwin_world.model.utils.MapVisualizer;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.worlds.Boundary;

public class UserConfiguration
{
//    wariant wzrostu roślin (wyjaśnione w sekcji poniżej),
//    energia konieczna, by uznać zwierzaka za najedzonego (i gotowego do rozmnażania),
//    energia rodziców zużywana by stworzyć potomka,
//    wariant zachowania zwierzaków (wyjaśnione w sekcji poniżej).

    private Boundary mapBoundary = null;
    private WorldMapType mapType = WorldMapType.ROUND_WORLD;
    private int plantNumber = 0;
    private int energyFromPlant = 0;
    private int plantsGrowingDaily = 0;
    private int animalsCountAtStart = 0;
    private int animalsEnergyAtStart = 0;
    private int minimalMutations = 0;
    private int maximalMutations = 0;
    private int genomLength = 0;
    private AnimalMutationType mutationType = AnimalMutationType.RANDOM_MUTATION;

    private AbstractMutation mutation;

    public WorldMapType getMapType() {
        return mapType;
    }

    public void setMapType(WorldMapType mapType) {
        this.mapType = mapType;
    }

    public int getPlantNumber() {
        return plantNumber;
    }

    public void setPlantNumber(int plantNumber) {
        this.plantNumber = plantNumber;
    }

    public AnimalMutationType getMutationType() {
        return mutationType;
    }

    public void setMutationType(AnimalMutationType mutationType) {
        this.mutationType = mutationType;
    }

    public int getGenomLength() {
        return genomLength;
    }

    public void setGenomLength(int genomLength) {
        this.genomLength = genomLength;
    }

    public int getMaximalMutations() {
        return maximalMutations;
    }

    public void setMaximalMutations(int maximalMutations) {
        this.maximalMutations = maximalMutations;
    }

    public int getMinimalMutations() {
        return minimalMutations;
    }

    public void setMinimalMutations(int minimalMutations) {
        this.minimalMutations = minimalMutations;
    }

    public int getAnimalsEnergyAtStart() {
        return animalsEnergyAtStart;
    }

    public void setAnimalsEnergyAtStart(int animalsEnergyAtStart) {
        this.animalsEnergyAtStart = animalsEnergyAtStart;
    }

    public int getAnimalsCountAtStart() {
        return animalsCountAtStart;
    }

    public void setAnimalsCountAtStart(int animalsCountAtStart) {
        this.animalsCountAtStart = animalsCountAtStart;
    }

    public int getEnergyFromPlant() {
        return energyFromPlant;
    }

    public void setEnergyFromPlant(int energyFromPlant) {
        this.energyFromPlant = energyFromPlant;
    }

    public int getPlantsGrowingDaily() {
        return plantsGrowingDaily;
    }

    public void setPlantsGrowingDaily(int plantsGrowingDaily) {
        this.plantsGrowingDaily = plantsGrowingDaily;
    }

    public void setMapBoundary(int xmax, int ymax) {
        if(mapBoundary == null)
            this.mapBoundary = new Boundary(new Vector2d(0,0),new Vector2d(xmax,ymax));
       // else
            //można tu potem zabezpiecznie zreobić
    }

    public Boundary getMapBoundary() {
        return mapBoundary;
    }

    public void setFinalMutationTypeAndLength()
    {
        int minMutation = this.getMinimalMutations();
        int maxMutation = this.getMaximalMutations();

        //todo
        //można tu doraobić checki tylko jakie warunki
        this.mutation = getMutationType().enumToMutation(minMutation, maxMutation);
    }
    public AbstractMutation getMutation() {
        return mutation;
    }
}
