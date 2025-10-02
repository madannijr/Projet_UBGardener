package fr.ubx.poo.ubgarden.game.go.decor.ground;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.personage.Insecte;

// Herrison
public class Hebgehog extends Ground{
    public Hebgehog(Position position) {
        super(position);
    }

    @Override
    public boolean walkableBy(Insecte insecte) {
        return false;
    }
}
