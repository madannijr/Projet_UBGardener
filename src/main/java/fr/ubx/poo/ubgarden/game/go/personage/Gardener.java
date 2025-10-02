/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubgarden.game.go.personage;

import fr.ubx.poo.ubgarden.game.*;
import fr.ubx.poo.ubgarden.game.engine.Timer;
import fr.ubx.poo.ubgarden.game.go.GameObject;
import fr.ubx.poo.ubgarden.game.go.Movable;
import fr.ubx.poo.ubgarden.game.go.PickupVisitor;
import fr.ubx.poo.ubgarden.game.go.WalkVisitor;
import fr.ubx.poo.ubgarden.game.go.bonus.Carrots;
import fr.ubx.poo.ubgarden.game.go.bonus.EnergyBoost;
import fr.ubx.poo.ubgarden.game.go.bonus.Intesecticids;
import fr.ubx.poo.ubgarden.game.go.bonus.PoisonedApple;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.decor.ground.Door;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Gardener extends GameObject implements Movable, PickupVisitor, WalkVisitor {

    private int energy ;
    private Direction direction;
    private boolean moveRequested = false;
    private int diseasesLevel = 1;
    private int insecticideCount = 0 ;
    private final Timer energyRecoveryTimer ;
    private final List<Timer> activeDiseaseTimers = new ArrayList<>();


    public Gardener(Game game, Position position) {

        super(game, position);
        this.direction = Direction.DOWN;
        this.energy = game.configuration().gardenerEnergy();
        this.energyRecoveryTimer = new Timer(game.configuration().energyRecoverDuration());
    }

    @Override
    public void pickUp(EnergyBoost energyBoost) {
        energyBoost.remove();
        // ajout de l'energie
        this.energy +=  game.configuration().energyBoost() ;
        // limitter de l'energie a energie max definie
        int maxEnergy = game.configuration().gardenerEnergy();
        if (this.energy > maxEnergy) {
            this.energy = maxEnergy ;
        }
        // Guerrir de toute maladie
        diseasesLevel = 1 ;
    }

    @Override
    public void pickUp(Carrots carrots) {
        carrots.remove();
        game.decrementCarrots();

        // Ouverture des Portes
        if (game.getCounteurCarrots() == 0) {
            // Toutes les carottes sont ramassées, ouvrir les portes
            for (Map map : game.world().values()) {
                for (Decor decor : ((Level) map).values()) {
                    if (decor instanceof Door door) {
                        door.setOpen(true);
                        door.setModified(true);

                        System.out.println("Ouverture de la porte à : " + door.getPosition());
                    }
                }
            }
        }
    }
    @Override
    public void pickUp(PoisonedApple poisonedApple) {
        poisonedApple.remove(); //  retire  la pomme empoisonnée  de l'herbe

        this.diseasesLevel++; //  Augumente  le niveau  de diseaseLevel
        // Démarre un timer  pour  gérer la  durée  de diseaseLevel
        long diseaseDuration=  game.configuration().diseaseDuration();
        Timer diseaseTimer = new Timer(diseaseDuration);
        diseaseTimer.start();
        // Ajoute ce timer à la liste des timers actifs
        activeDiseaseTimers.add(diseaseTimer);
    }

    @Override
    public void pickUp(Intesecticids intesecticids) {
      intesecticids.remove();
      this.insecticideCount ++;
        System.out.println("Bombs Ramassee " + this.insecticideCount);

    }

    public int getEnergy() {
        return this.energy;
    }

    public int getInsecticideCount() {
        return insecticideCount;
    }

    public void setInsecticideCount(int insecticideCount) {
        this.insecticideCount = insecticideCount;
    }

    public int getDiseasesLevel() {
        return diseasesLevel;
    }

    public Timer getEnergyRecoveryTimer() {
        return energyRecoveryTimer;
    }

    public void setDiseasesLevel(int diseasesLevel) {
        this.diseasesLevel = diseasesLevel;
    }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
            setModified(true);
        }
        moveRequested = true;
    }

    @Override
    public final boolean canMove(Direction direction) {

        Position nextPos = direction.nextPosition(getPosition());

        // Vérifie si la position est en dehors des limites de la carte
        if (nextPos.x() < 0 || nextPos.x() >= game.world().getGrid().width() ||
                nextPos.y() < 0 || nextPos.y() >= game.world().getGrid().height()) {
            return false;
        }

        // Vérifie s'il y a un obstacle infranchissable
        Decor nextDecor = game.world().getGrid().get(nextPos);
        if (nextDecor != null && !nextDecor.walkableBy(this)) {
            return false;
        }

        return true;
    }

    @Override
    public Position move(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Decor next = game.world().getGrid().get(nextPos);
        setPosition(nextPos);

        if (next != null) {
            this.hurt(next.energyConsumptionWalk() * diseasesLevel);
            next.pickUpBy(this);
        }

        return nextPos;
    }

    public void update(long now) {
        // mettre a jour l'etat du timer
        energyRecoveryTimer.update(now);
        // Parcourt les timers actifs pour les trognons pourris
        for (Iterator<Timer> iterator = activeDiseaseTimers.iterator(); iterator.hasNext();) {
            Timer diseaseTimer = iterator.next();
            diseaseTimer.update(now);
            // Si le timer a expiré
            if (!diseaseTimer.isRunning()) {
                // Diminue le diseaseLevel (en tenant compte des timers actifs)
                this.diseasesLevel = Math.max(1, this.diseasesLevel - 1);
                System.out.println("Poison apple end");
                // Retire ce timer de la liste des timers actifs
                iterator.remove();
            }
        }

        if (moveRequested) {
            if (canMove(direction)) {
                move(direction);
            }
            // on redemare le timer quand le joueur bouge
            energyRecoveryTimer.start();
        } else{
            if (!energyRecoveryTimer.isRunning() && energy < game.configuration().gardenerEnergy()){
                energy++ ; // le joueur recuere un point d'energy
                energyRecoveryTimer.start();
            }
        }
        moveRequested = false;
    }

    public void hurt(int damage) {
        this.energy -= damage;
    }


    public void hurt() {
        hurt(1);
    }

    public Direction getDirection() {
        return direction;
    }

}
