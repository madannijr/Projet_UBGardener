package fr.ubx.poo.ubgarden.game.view;

import fr.ubx.poo.ubgarden.game.go.GameObject;
import fr.ubx.poo.ubgarden.game.go.decor.ground.Flowers;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class SpriteFlowers extends Sprite {
    public SpriteFlowers(Pane layer, Image image, GameObject gameObject) {
        super(layer, image, gameObject);
    }

    @Override
    public void updateImage() {
      Flowers flowerBeds= (Flowers) getGameObject() ;
    }
}
