/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubgarden.game.go;


import fr.ubx.poo.ubgarden.game.go.bonus.Apple;
import fr.ubx.poo.ubgarden.game.go.bonus.Carrots;
import fr.ubx.poo.ubgarden.game.go.bonus.Intesecticids;
import fr.ubx.poo.ubgarden.game.go.bonus.PoisonedApple;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;

public interface Pickupable {
    /**
     * Called when a {@link Gardener} picks up this object.
     *
     * @param gardener the gardener who picks up the object
     */
    default void pickUpBy(Gardener gardener) {
    }

}
