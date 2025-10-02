/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubgarden.game.go.decor.ground;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.personage.Insecte;

public class Grass extends Ground {
    public Grass(Position position) {
        super(position);
    }


    @Override
    public boolean walkableBy(Insecte insecte) {
        return insecte.canWalkOn(this);
    }

   @Override
    public int energyConsumptionWalk() {
        return 1;
    }

}
