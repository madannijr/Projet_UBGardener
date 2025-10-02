/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubgarden.game.go.decor.ground;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.WalkVisitor;
import fr.ubx.poo.ubgarden.game.go.Walkable;
import fr.ubx.poo.ubgarden.game.go.bonus.Bonus;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;
import fr.ubx.poo.ubgarden.game.go.personage.Insecte;

public abstract class Ground extends Decor implements Walkable, WalkVisitor {

    public Ground(Position position) {
        super(position);
    }

    @Override
    public void pickUpBy(Gardener gardener) {
        Bonus bonus = getBonus();
        if (bonus != null) {
            bonus.pickUpBy(gardener);
        }
    }
    @Override
    public int energyConsumptionWalk() {
        return 2 ;
    }

    @Override
    public boolean walkableBy(Insecte insecte) {
        return true ;
    }
}
