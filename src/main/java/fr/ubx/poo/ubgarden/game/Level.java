package fr.ubx.poo.ubgarden.game;

import fr.ubx.poo.ubgarden.game.go.bonus.Carrots;
import fr.ubx.poo.ubgarden.game.go.bonus.EnergyBoost;
import fr.ubx.poo.ubgarden.game.go.bonus.PoisonedApple;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.decor.Tree;
import fr.ubx.poo.ubgarden.game.go.decor.ground.*;
import fr.ubx.poo.ubgarden.game.launcher.MapEntity;
import fr.ubx.poo.ubgarden.game.launcher.MapLevel;

import java.util.Collection;
import java.util.HashMap;

public class Level implements Map {

    private final int level;
    private final int width;

    private final int height;

    private final java.util.Map<Position, Decor> decors = new HashMap<>();

    public Level(Game game, int level, MapLevel entities) {
        this.level = level;
        this.width = entities.width();
        this.height = entities.height();
        int counteurCarrots= 0;

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                Position position = new Position(level, i, j);
                MapEntity mapEntity = entities.get(i, j);
                switch (mapEntity) {
                    case Grass:
                        decors.put(position, new Grass(position));
                        break;
                    case Tree:
                        decors.put(position, new Tree(position));
                        break;
                    case Apple: {
                        Decor grass = new Grass(position);
                        grass.setBonus(new EnergyBoost(position, grass));
                        decors.put(position, grass);
                        break;
                    }

                    case Hedgehog:
                        Hebgehog hedgehog = new Hebgehog(position);
                        decors.put(position, hedgehog);
                        game.setHebgehog(hedgehog);
                        break;

                    case Carrots: {
                        Decor land = new Land(position);
                        land.setBonus(new Carrots(position, land));
                        decors.put(position, land);
                        counteurCarrots++;
                        break;
                    }
                    case PoisonedApple: {
                        Decor grass = new Grass(position);
                        grass.setBonus(new PoisonedApple(position, grass));
                        decors.put(position, grass);
                        break;
                    }
                    case Flowers:
                        decors.put(position,new Flowers(position));
                        break;
                    case Land:
                        decors.put(position,new Land(position));
                        break;

                    case DoorNextClosed:
                        decors.put(position, new Door(position, "next"));
                        break;
                    case DoorNextOpened: {
                        Door door = new Door(position, "next");
                        door.setOpen(true);
                        decors.put(position, door);
                        break;
                    }
                    case DoorPrevOpened:
                        Door door = new Door(position, "prev");
                        door.setOpen(true);
                        decors.put(position, door);
                        break;

                    case NestWasp:
                        decors.put(position,new NestWasp(game,position));
                        break;

                    case NestHornet:
                        decors.put(position,new NestHornet(game,position));
                        break;
                    default:
                        throw new RuntimeException("EntityCode " + mapEntity.name() + " not processed");
                }
            }
        //met à jour le counter de carrotes
        game.setCounteurCarrots(counteurCarrots);

    }

    @Override
    public int width() {
        return this.width;
    }

    @Override
    public int height() {
        return this.height;
    }

    public Decor get(Position position) {
        return decors.get(position);
    }

    public Collection<Decor> values() {
        return decors.values();
    }


    @Override
    public boolean inside(Position position) {
        return position.x() >= 0 && position.x() < width &&
                position.y() >= 0 && position.y() < height;

    }

    @java.lang.Override
    public void put(Position position, Decor decor) {
        decors.put(position, decor);
    }
}
