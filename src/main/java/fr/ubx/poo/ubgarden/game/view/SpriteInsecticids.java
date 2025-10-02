package fr.ubx.poo.ubgarden.game.view;

import fr.ubx.poo.ubgarden.game.go.GameObject;
import javafx.scene.layout.Pane;

public class SpriteInsecticids extends Sprite {


    public SpriteInsecticids(Pane layer,  GameObject gameObject) {
        super(layer, null, gameObject);
        updateImage();
    }

    @Override
    public void updateImage() {
        setImage(ImageResourceFactory.getInstance().get(ImageResource.INSECTICIDE));
    }

}
