package fr.ubx.poo.ubgarden.game.go.decor.ground;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.engine.Timer;
import fr.ubx.poo.ubgarden.game.go.bonus.Intesecticids;
import fr.ubx.poo.ubgarden.game.go.personage.Insecte;
import fr.ubx.poo.ubgarden.game.go.personage.Wasp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

// Nid de guêpes : génère une guêpe et une bombe insecticide toutes les 5 secondes
public class NestWasp extends Ground {
    private final Timer spawnTimer;
    private final Game game;
    private final Random random = new Random();

    public NestWasp(Game game, Position position) {
        super(position);
        this.game = game;
        this.spawnTimer = new Timer(5000); // toutes les 5 secondes
        this.spawnTimer.start();
    }

    @Override
    public boolean walkableBy(Insecte insecte) {
        return insecte.canWalkOn(this);
    }

    @Override
    public void update(long now) {
        spawnTimer.update(now);
        if (!spawnTimer.isRunning()) {
            spawnWasp();
            spawnTimer.start();
        }
    }

    private void spawnWasp() {
        Position spawnPosition = this.getPosition();

        // Créer la guêpe
        Wasp wasp = new Wasp(game, spawnPosition);
        game.world().addMob(wasp);
        System.out.println("Une guêpe est née ");

        // Essayer de placer une bombe insecticide à une position éloignée aléatoire
        List<Direction> directions = new ArrayList<>(List.of(Direction.values()));
        Collections.shuffle(directions);

        for (Direction direction : directions) {
            int offset = 2 + random.nextInt(3); // entre 2 et 4 cases de distance
            Position bombPos = spawnPosition;

            for (int i = 0; i < offset; i++) {
                bombPos = direction.nextPosition(bombPos);
            }

            if (game.world().getGrid().inside(bombPos)
                    && game.world().getGrid().get(bombPos) instanceof Ground ground
                    && ground.getBonus() == null) {

                Intesecticids bonus = new Intesecticids(bombPos, ground);
                ground.setBonus(bonus);
                bonus.setModified(true);
                //pour rendre visible immédiatement
                ground.setModified(true);
                System.out.println(" Bombe placée à : " + bombPos);
                break;
            }
        }
    }
}
