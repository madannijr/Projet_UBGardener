package fr.ubx.poo.ubgarden.game.go.decor.ground;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;
import fr.ubx.poo.ubgarden.game.go.personage.Insecte;

// Porte
public class Door extends Decor {
    private boolean open;
    private final String direction; // next ou prev

    public Door(Position position, String direction) {
        super(position);
        this.direction = direction;
        this.open = false;
    }

    public String getDirection() {
        return direction;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    @Override
    public boolean walkableBy(Gardener gardener) {
        return open;
    }

    @Override
    public boolean walkableBy(Insecte insecte) {
        return insecte.canWalkOn(this);
    }
}