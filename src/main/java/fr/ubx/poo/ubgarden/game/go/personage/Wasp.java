package fr.ubx.poo.ubgarden.game.go.personage;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.bonus.Bonus;
import fr.ubx.poo.ubgarden.game.go.bonus.Intesecticids;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Wasp extends Insecte {


    public Wasp(Game game, Position position) {
        super(game, position, 20, 1, game.configuration().waspMoveFrequency());
        getMoveTimer().start();
    }


    @Override
    public boolean walkableBy(Gardener gardener) {
        return true;
    }

    @Override
    public boolean walkableBy(Insecte insecte) {
        return insecte.canWalkOn(this);
    }

    @Override
    public void sting(Gardener gardener) {
        if (this.isDeleted()) return; // L'insecte est déjà mort
        if (peutEtreTuerPar(gardener)) return; // Le jardinier a tué l'insecte avec des bombes

        gardener.hurt(getStingDamage());
        setRemainingStings(getRemainingStings() - 1);

        if (getRemainingStings() == 0) {
            this.remove();
            System.out.println("L'insecte est mort après avoir piqué.");
        }

    }

    @Override
    public int nbreBombskill() {
        return 1 ;
    }

    @Override
    public Position move(Direction direction) {
        Position nextPos = super.move(direction);

        // Vérifie la case
        Decor nextDecor = getGame().world().getGrid().get(nextPos);
        if (nextDecor != null) {
            Bonus bonus = nextDecor.getBonus();
            if (bonus instanceof Intesecticids) {
                bonus.remove(); // appelle bien setBonus(null)
                nextDecor.setModified(true);
                this.remove();
                System.out.println("La guêpe a été tuée par un insecticide.");
            }
        }

        return nextPos;
    }

}
