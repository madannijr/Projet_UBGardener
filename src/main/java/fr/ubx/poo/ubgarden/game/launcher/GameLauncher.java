package fr.ubx.poo.ubgarden.game.launcher;

import fr.ubx.poo.ubgarden.game.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class GameLauncher {

    private GameLauncher() {
    }

    public static GameLauncher getInstance() {
        return LoadSingleton.INSTANCE;
    }

    private int integerProperty(Properties properties, String name, int defaultValue) {
        return Integer.parseInt(properties.getProperty(name, Integer.toString(defaultValue)));
    }

    private boolean booleanProperty(Properties properties, String name, boolean defaultValue) {
        return Boolean.parseBoolean(properties.getProperty(name, Boolean.toString(defaultValue)));
    }

    private Configuration getConfiguration(Properties properties) {

        // Load parameters
        int waspMoveFrequency = integerProperty(properties, "waspMoveFrequency", 2);
        int hornetMoveFrequency = integerProperty(properties, "hornetMoveFrequency", 1);

        int gardenerEnergy = integerProperty(properties, "gardenerEnergy", 100);
        int energyBoost = integerProperty(properties, "energyBoost", 50);
        long energyRecoverDuration = integerProperty(properties, "energyRecoverDuration", 1_000);
        long diseaseDuration = integerProperty(properties, "diseaseDuration", 5_000);

        return new Configuration(gardenerEnergy, energyBoost, energyRecoverDuration, diseaseDuration, waspMoveFrequency, hornetMoveFrequency);
    }

    private String decompress(String compressed) {
        StringBuilder decompressed = new StringBuilder();
        int i = 0;
        while (i < compressed.length()) {
            char current = compressed.charAt(i);
            i++;
            StringBuilder countStr = new StringBuilder();
            while (i < compressed.length() && Character.isDigit(compressed.charAt(i))) {
                countStr.append(compressed.charAt(i));
                i++;
            }
            int count = !countStr.isEmpty() ? Integer.parseInt(countStr.toString()) : 1;
            decompressed.append(String.valueOf(current).repeat(count));
        }
        return decompressed.toString();
    }

    public Game load(File file) {
        Properties props = new Properties();

        try (FileInputStream fis = new FileInputStream(file)) {
            props.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la lecture du fichier : " + e.getMessage());
        }

        Configuration configuration = getConfiguration(props);
        boolean compression = Boolean.parseBoolean(props.getProperty("compression", "false"));

        int levelsCount = Integer.parseInt(props.getProperty("levels", "1"));
        World world = new World(levelsCount);

        Position gardenerPosition = null;
        MapLevel[] mapLevels = new MapLevel[levelsCount]; // pour stocker tous les MapLevel

        for (int i = 1; i <= levelsCount; i++) {
            String levelString = props.getProperty("level" + i);
            if (levelString == null)
                throw new RuntimeException("Missing level" + i + " property!");

            if (compression) {
                levelString = decompress(levelString);
            }

            String[] rows = levelString.split("x");
            int height = rows.length;
            int width = 0;
            for (String row : rows) {
                width = Math.max(width, row.length());
            }

            MapLevel mapLevel = new MapLevel(width, height);

            for (int y = 0; y < height; y++) {
                String row = rows[y];
                for (int x = 0; x < row.length(); x++) {
                    char code = row.charAt(x);
                    MapEntity entity;
                    try {
                        entity = MapEntity.fromCode(code);
                    } catch (MapException e) {
                        throw new RuntimeException("Caractère invalide '" + code + "' à (" + x + "," + y + ")");
                    }
                    mapLevel.set(x, y, entity);
                }
            }

            // Pour level1 seulement : trouver la position du jardinier
            if (i == 1) {
                gardenerPosition = mapLevel.getGardenerPosition();
                if (gardenerPosition == null)
                    throw new RuntimeException("No gardener found in level1");
            }

            mapLevels[i - 1] = mapLevel; // Stocker pour plus tard
        }

        // Maintenant on a tous les MapLevels -> créer le Game
        if (gardenerPosition == null) {
            throw new RuntimeException("Gardener position not found !");
        }

        Game game = new Game(world, configuration, gardenerPosition);

        // Maintenant créer les Levels avec Game non-null
        for (int i = 1; i <= levelsCount; i++) {
            MapLevel mapLevel = mapLevels[i - 1];
            Level level = new Level(game, i, mapLevel);
            world.put(i, level);
        }

        return game;
    }


    public Game load() {
        Properties emptyConfig = new Properties();
        MapLevel mapLevel = new MapLevelDefaultStart();
        Position gardenerPosition = mapLevel.getGardenerPosition();
        if (gardenerPosition == null)
            throw new RuntimeException("Gardener not found");
        Configuration configuration = getConfiguration(emptyConfig);
        World world = new World(1);
        Game game = new Game(world, configuration, gardenerPosition);
        Map level = new Level(game, 1, mapLevel);
        world.put(1, level);
        return game;
    }

    private static class LoadSingleton {
        static final GameLauncher INSTANCE = new GameLauncher();
    }

}

