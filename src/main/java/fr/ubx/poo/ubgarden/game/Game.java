package fr.ubx.poo.ubgarden.game;

import fr.ubx.poo.ubgarden.game.go.decor.ground.Hebgehog;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;

public class Game {

    private final Configuration configuration;
    private final World world;
    private final Gardener gardener;
    private Hebgehog hebgehog; // Hérisson
    private boolean switchLevelRequested = false;
    private int switchLevel;
    private int counteurCarrots = 0;
    private String switchLevelDirection; // next ou prev

    public Game(World world, Configuration configuration, Position gardenerPosition) {
        this.configuration = configuration;
        this.world = world;
        gardener = new Gardener(this, gardenerPosition);
    }

    public Configuration configuration() {
        return configuration;
    }

    public int getCounteurCarrots() {
        return counteurCarrots;
    }

    public void setCounteurCarrots(int counteurCarrots) {
        this.counteurCarrots = counteurCarrots;
    }

    public Gardener getGardener() {
        return this.gardener;
    }

    public Hebgehog getHebgehog() {
        return hebgehog;
    }

    public void setHebgehog(Hebgehog hebgehog) {
        this.hebgehog = hebgehog;
    }

    public World world() {
        return world;
    }

    public boolean isSwitchLevelRequested() {
        return switchLevelRequested;
    }

    public int getSwitchLevel() {
        return switchLevel;
    }

    public void requestSwitchLevel(int level, String direction) {
        this.switchLevel = level;
        this.switchLevelRequested = true;
        this.switchLevelDirection = direction;
    }

    public void clearSwitchLevel() {
        this.switchLevelRequested = false;
        this.switchLevelDirection = null;
    }

    public boolean victory() {
        return hebgehog != null && gardener.getPosition().equals(hebgehog.getPosition());
    }

    public boolean defeat() {
        return gardener.getEnergy() <= 0;
    }

    public void decrementCarrots() {
        this.counteurCarrots--;
        System.out.println("Carottes restantes : " + counteurCarrots);
    }

    public String getSwitchLevelDirection() {
        return switchLevelDirection;
    }
}
