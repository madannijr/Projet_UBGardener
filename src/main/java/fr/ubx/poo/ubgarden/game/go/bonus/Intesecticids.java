package fr.ubx.poo.ubgarden.game.go.bonus;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;

public class Intesecticids extends Bonus{

    public Intesecticids(Position position, Decor decor) {
        super(position, decor);
    }


    @Override
    public void pickUpBy(Gardener gardener) {
        gardener.pickUp(this);
    }

}
