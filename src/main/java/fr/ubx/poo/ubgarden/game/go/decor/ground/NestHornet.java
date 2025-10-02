package fr.ubx.poo.ubgarden.game.go.decor.ground;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.engine.Timer;
import fr.ubx.poo.ubgarden.game.go.bonus.Intesecticids;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.personage.Hornet;
import fr.ubx.poo.ubgarden.game.go.personage.Insecte;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// nids de frelons
public class NestHornet extends Decor {
    private final Timer spawnTimer ;
    private final Game game ;
    private final Random random = new Random() ;
    public NestHornet(Game game,Position position) {
        super(position);
        this.game = game ;
        this.spawnTimer = new Timer(10000); // 10 secondes
        this.spawnTimer.start();
    }


    @Override
    public void update(long now) {
        spawnTimer.update(now); // mettre a jour l'etat timer
        if(!spawnTimer.isRunning()){
            spawnHornet(); // fait apparaitre un frelon
            spawnTimer.start(); // on redémarre le timer pour attendre les 10 secondes
        }
    }

    // methode de creation des Hornets
    private void spawnHornet(){
        // trouver la position du frelon
        Position spawnPosition = this.getPosition();

        // Créer le frelon
        Hornet hornet = new Hornet(game, spawnPosition);
        game.world().addMob(hornet);
        System.out.println("Un frelon est né ");

        // Placer 2 bombes insecticides à des positions aléatoires dans la carte
        int bombsPlaced = 0;
        int mapWidth = game.world().getGrid().width();
        int mapHeight = game.world().getGrid().height();

        while (bombsPlaced < 2) {
            int x = random.nextInt(mapWidth);
            int y = random.nextInt(mapHeight);
            Position bombPos = new Position(spawnPosition.level(), x, y);

            if (game.world().getGrid().inside(bombPos)
                    && game.world().getGrid().get(bombPos) instanceof Ground ground
                    && ground.getBonus() == null) {

                Intesecticids bomb = new Intesecticids(bombPos, ground);
                ground.setBonus(bomb);
                bomb.setModified(true);
                ground.setModified(true);

                System.out.println(" Bombe placée  : " + bombPos);
                bombsPlaced++;
            }
        }
    }
}
