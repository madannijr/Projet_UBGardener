package fr.ubx.poo.ubgarden.game;

import fr.ubx.poo.ubgarden.game.go.personage.Insecte;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class World {
    private final java.util.Map<Integer, Map> grids = new HashMap<>();
    private int currentLevel = 1;
    private final ArrayList<Insecte> insectes = new ArrayList<>(); // Liste pour stocker les Insectes

    public World(int levels) {
        if (levels < 1) throw new IllegalArgumentException("Levels must be greater than 1");
    }


    public int currentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public Map getGrid(int level) {
        return grids.get(level);
    }

    public Map getGrid() {
        return getGrid(currentLevel);
    }

    public void put(int level, Map grid) {
        this.grids.put(level, grid);
    }

   public Collection<Map> values() {
        return grids.values();
    }

    public void addMob(Insecte insecte){
        insectes.add(insecte);
    }

    public List<Insecte> getInsectes(){
        return insectes ;
    }

}
