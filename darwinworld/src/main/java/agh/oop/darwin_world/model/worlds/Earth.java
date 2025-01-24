package agh.oop.darwin_world.model.worlds;

import agh.oop.darwin_world.presenter.UserConfigurationRecord;

public class Earth extends AbstractWorldMap{

    private static final String MAP_NAME = "Earth";


    public Earth(UserConfigurationRecord config) {
        super(config);
    }
    @Override
    public String getIdString(){
        return String.format("%s %s",MAP_NAME,id.toString());
    }




}
