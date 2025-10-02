package fr.ubx.poo.ubgarden.game.go;

import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.decor.Tree;
import fr.ubx.poo.ubgarden.game.go.decor.ground.*;
import fr.ubx.poo.ubgarden.game.go.personage.Insecte;

public interface WalkVisitor {

    /**
     * Determines whether the visitor can walk on the given {@link Decor}.
     *
     * @param decor the decor to evaluate
     * @return true if the visitor can walk on the decor, false by default
     */
    default boolean canWalkOn(Decor decor) {
        return true;
    }

    /**
     * Determines whether the visitor can walk on the given {@link Tree}.
     *
     * @param tree the tree to evaluate
     * @return true if the visitor can walk on the tree, false by default
     */
    default boolean canWalkOn(Tree tree) {
        return false;
    }

    // TODO

    default boolean canWalkOn(Grass grass) {
        return true;
    }

    default boolean canWalkOn(Land land) {
        return true;
    }

    default boolean canWalkOn(Flowers flowers) {
        return true;
    }
    default boolean canWalkOn(NestWasp nestWasp) {
        return true;
    }
    default boolean canWalkOn(Door door) {
        return false;
    }

    default boolean canWalkOn(Insecte insecte) {
        return false;
    }


}