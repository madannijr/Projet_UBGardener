package fr.ubx.poo.ubgarden.game.view;

import fr.ubx.poo.ubgarden.game.go.GameObject;
import fr.ubx.poo.ubgarden.game.go.personage.Wasp;
import fr.ubx.poo.ubgarden.game.Direction;
import javafx.scene.layout.Pane;

import static fr.ubx.poo.ubgarden.game.view.ImageResourceFactory.getInstance;
import static fr.ubx.poo.ubgarden.game.view.ImageResource.*;

public class SpriteWasp extends Sprite {

    public SpriteWasp(Pane layer, GameObject gameObject) {
        super(layer, null, gameObject);
        updateImage();
    }

    @Override
    public void updateImage() {
       Wasp wasp = (Wasp) getGameObject();
        Direction dir = wasp.getDirection();
        switch (dir) {
            case UP -> setImage(getInstance().get(WASP_UP));
            case DOWN -> setImage(getInstance().get(WASP_DOWN));
            case LEFT -> setImage(getInstance().get(WASP_LEFT));
            case RIGHT -> setImage(getInstance().get(WASP_RIGHT));
        }
    }
}
