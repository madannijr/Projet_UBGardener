package fr.ubx.poo.ubgarden.game.go.decor.ground;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.personage.Insecte;

public class Land  extends  Ground{
    public Land(Position position) {
        super(position);
    }

    @Override
    public boolean walkableBy(Insecte insecte) {
        return insecte.canWalkOn(this);
    }
}
