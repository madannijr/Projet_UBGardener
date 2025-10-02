package fr.ubx.poo.ubgarden.game.view;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.go.GameObject;
import fr.ubx.poo.ubgarden.game.go.personage.Hornet;
import javafx.scene.layout.Pane;

import static fr.ubx.poo.ubgarden.game.view.ImageResource.*;
import static fr.ubx.poo.ubgarden.game.view.ImageResourceFactory.getInstance;

public class SpriteHornet extends Sprite{

    public SpriteHornet(Pane layer, GameObject gameObject) {
        super(layer,null, gameObject);
        updateImage();
    }

    @Override
    public void updateImage() {
        Hornet hornet = (Hornet) getGameObject();
        Direction direction = hornet.getDirection();
        switch (direction){
            case UP -> setImage(getInstance().get(HORNET_UP));
            case LEFT -> setImage(getInstance().get(HORNET_LEFT));
            case DOWN -> setImage(getInstance().get(HORNET_DOWN));
            case RIGHT -> setImage(getInstance().get(HORNET_RIGHT));

        }
    }
}
