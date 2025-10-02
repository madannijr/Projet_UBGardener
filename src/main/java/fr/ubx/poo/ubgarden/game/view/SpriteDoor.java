package fr.ubx.poo.ubgarden.game.view;

import fr.ubx.poo.ubgarden.game.go.GameObject;
import fr.ubx.poo.ubgarden.game.go.decor.ground.Door;
import javafx.scene.layout.Pane;

import static fr.ubx.poo.ubgarden.game.view.ImageResource.DOOR_CLOSED;
import static fr.ubx.poo.ubgarden.game.view.ImageResource.DOOR_OPENED;
import static fr.ubx.poo.ubgarden.game.view.ImageResourceFactory.getInstance;

public class SpriteDoor extends Sprite {

    public SpriteDoor(Pane layer, GameObject gameObject) {
        super(layer, null, gameObject);
        updateImage();
    }


    @Override
    public void updateImage() {
        Door door = (Door) getGameObject(); // Caster l'objet vers la classe Door
        if (door.isOpen()) {
            setImage(getInstance().get(DOOR_OPENED));
            System.out.println("Porte ouverte");
        } else {
            setImage(getInstance().get(DOOR_CLOSED));
            System.out.println("Porte fermée");
        }
    }
}
