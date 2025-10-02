package fr.ubx.poo.ubgarden.game.go.personage;

import fr.ubx.poo.ubgarden.game.*;
import fr.ubx.poo.ubgarden.game.engine.Timer;
import fr.ubx.poo.ubgarden.game.go.GameObject;
import fr.ubx.poo.ubgarden.game.go.Movable;
import fr.ubx.poo.ubgarden.game.go.WalkVisitor;
import fr.ubx.poo.ubgarden.game.go.Walkable;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Insecte extends GameObject implements Movable, WalkVisitor, Walkable {
    private final Game game;
    private final Timer moveTimer;
    private int remainingStings;
    private final int stingDamage;
    private Direction direction;

    public Insecte(Game game, Position position, int stingDamage, int remainingStings, int moveFrequencyPerSecond) {
        super(game, position);
        this.game = game;
        this.stingDamage = stingDamage;
        this.remainingStings = remainingStings;
        this.direction = Direction.random() ;
        long moveIntervalMs = 1000L / moveFrequencyPerSecond; // déplacement toutes X millisecondes
        this.moveTimer = new Timer(moveIntervalMs);
        this.moveTimer.start();
    }

    public Direction getDirection() {
        return direction;
    }
    public void setDirection(Direction direction){
        this.direction = direction ;
    }

    public Timer getMoveTimer() {
        return moveTimer;
    }

    public int getRemainingStings() {
        return remainingStings;
    }

    public void setRemainingStings(int remainingStings) {
        this.remainingStings = remainingStings;
    }

    public int getStingDamage() {
        return stingDamage;
    }

    public Game getGame() {
        return game;
    }

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());

        if (!game.world().getGrid().inside(nextPos)) {
            return false;
        }

        Decor nextDecor = game.world().getGrid().get(nextPos);
        return nextDecor == null || nextDecor.walkableBy(this);
    }

    @Override
    public Position move(Direction direction) {
        // Si le timer est encore en cours, l'insecte ne peut pas bouger
        if (moveTimer.isRunning()) {
            return getPosition();
        }

        if (!canMove(direction)) {  // Si l'insecte ne peut pas se déplacer dans la direction
           moveTimer.start();  // Redémarre le timer même si aucun mouvement n'a eu lieu
            return getPosition();
        }
        // Calcule la prochaine position selon la direction
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);     // Met à jour la position de l'insecte
       moveTimer.start();
        return nextPos;
    }
    @Override
    public void update(long now) {
        getMoveTimer().update(now);     // Met à jour le timer vérifie s’il a expiré
        if (!getMoveTimer().isRunning()) {     // Si le timer n'est plus actif, l'insecte peut tenter un déplacement
            // Liste de toutes les directions mélangées aléatoirement
            List<Direction> directions = new ArrayList<>(List.of(Direction.values()));
            Collections.shuffle(directions); // Melanger les directions
            // On essaie les directions dans un ordre aléatoire
            for (Direction dir : directions) {
                if (canMove(dir)) {
                    move(dir);
                    setDirection(dir);
                    break; // S'arrête après le premier déplacement valide
                }

            }
            // Redémarre le timer pour attendre avant le prochain déplacement
            getMoveTimer().start();
        }
    }


// Elle définit comment l'insecte pique le jardinier
    public abstract void sting(Gardener gardener);


// Elle indique combien de bombes insecticides sont nécessaires pour tuer cet insecte
    public abstract int nbreBombskill();

    // Méthode concrète utilisée pour vérifier si le jardinier peut tuer l'insecte
// Elle compare le nombre de bombes disponibles chez le jardinier avec le nombre requis
    public boolean peutEtreTuerPar(Gardener gardener) {
        return gardener.getInsecticideCount() >= nbreBombskill();
    }

}
