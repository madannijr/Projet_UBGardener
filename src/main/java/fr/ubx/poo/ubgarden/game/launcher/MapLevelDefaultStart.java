package fr.ubx.poo.ubgarden.game.launcher;


import static fr.ubx.poo.ubgarden.game.launcher.MapEntity.*;

public class MapLevelDefaultStart extends MapLevel {


    private final static int width = 18;
    private final static int height = 8;
    private final MapEntity[][] level1 = {
            {Grass, Grass,Flowers,Flowers,Flowers, Grass, Grass, Grass, Grass, Grass, Grass, Carrots,Carrots,Carrots, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, DoorNextClosed},
            {Grass, Gardener, Flowers,Flowers,Flowers,Grass,DoorNextClosed, Grass, Grass, Grass, Grass, Grass,Carrots,Carrots,Carrots, Apple, Grass, Tree, Grass, Grass, Tree, Tree, Grass, Grass, Grass, Grass},
            {Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass,NestWasp , Grass, Land,Land,Land, Grass, Grass,  Tree, Grass, Grass, Grass, Grass},
            {Grass, Grass, Grass, Grass, Grass, Grass, Grass, NestHornet , Grass, Grass, Grass, Grass,  Land, Land,Land,Grass,  Grass, Tree, Grass, Grass, Grass, Grass},
            {Grass,  PoisonedApple,Tree, Grass,Apple, Tree,Grass, Grass, Grass, Grass, Grass, Grass, Flowers,Flowers,Flowers, Grass, Grass, Grass, Tree, Grass, Grass, Apple, Grass},
            {Grass, Tree, Tree, Tree, PoisonedApple, Grass,  Carrots,Carrots,Carrots,Grass, Grass, Grass, Grass, Grass, Grass,Flowers,Flowers,Flowers, Grass, Grass, Grass, Grass, Grass, Grass, Grass},
            {Grass, Grass, Grass, Grass, PoisonedApple,Grass, Carrots,Carrots,Carrots, Grass, Grass, Grass, Grass, Grass, Flowers,Flowers,Flowers,Grass, Grass, Grass, Grass, Grass, Grass, Grass, Grass},
            {Grass, Apple, Tree, Apple, Grass, Carrots,Carrots,Carrots, Tree, Grass, Grass, Grass, Grass, Grass, Grass, Grass,Hedgehog,Grass, Grass, Grass, Grass, Grass, Grass, Grass}
    };

    public MapLevelDefaultStart() {
        super(width, height);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                set(i, j, level1[j][i]);
    }


}
