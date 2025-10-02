    /*
     * Copyright (c) 2020. Laurent Réveillère
     */

    package fr.ubx.poo.ubgarden.game.engine;

    import fr.ubx.poo.ubgarden.game.Direction;
    import fr.ubx.poo.ubgarden.game.Game;
    import fr.ubx.poo.ubgarden.game.Position;
    import fr.ubx.poo.ubgarden.game.go.GameObject;
    import fr.ubx.poo.ubgarden.game.go.bonus.Bonus;
    import fr.ubx.poo.ubgarden.game.go.bonus.Carrots;
    import fr.ubx.poo.ubgarden.game.go.decor.Decor;
    import fr.ubx.poo.ubgarden.game.go.decor.ground.Door;
    import fr.ubx.poo.ubgarden.game.go.personage.Gardener;
    import fr.ubx.poo.ubgarden.game.go.personage.Insecte;
    import fr.ubx.poo.ubgarden.game.view.ImageResource;
    import fr.ubx.poo.ubgarden.game.view.Sprite;
    import fr.ubx.poo.ubgarden.game.view.SpriteFactory;
    import fr.ubx.poo.ubgarden.game.view.SpriteGardener;
    import javafx.animation.AnimationTimer;
    import javafx.application.Platform;
    import javafx.scene.Group;
    import javafx.scene.Scene;
    import javafx.scene.layout.Pane;
    import javafx.scene.layout.StackPane;
    import javafx.scene.paint.Color;
    import javafx.scene.text.Font;
    import javafx.scene.text.Text;
    import javafx.scene.text.TextAlignment;

    import java.util.*;


    public final class GameEngine {

        private static AnimationTimer gameLoop;
        private final Game game;
        private final Gardener gardener;
        private final List<Sprite> sprites = new LinkedList<>(); // Liste pour les Sprites
        private final Set<Sprite> cleanUpSprites = new HashSet<>();
        private final List<Insecte> affiches = new LinkedList<>();
        private final Set<GameObject> affichesBonus = new HashSet<>(); // Nouvelle Liste pour bonus
        private final Scene scene;
        private StatusBar statusBar;
        private final Pane rootPane = new Pane();
        private final Group root = new Group();
        private final Pane layer = new Pane();
        private Input input;

        public GameEngine(Game game, Scene scene) {
            this.game = game;
            this.scene = scene;
            this.gardener = game.getGardener();
            initialize();
            buildAndSetGameLoop();
        }

        public Pane getRoot() {
            return rootPane;
        }

        private void initialize() {
            int height = game.world().getGrid().height();
            int width = game.world().getGrid().width();
            int sceneWidth = width * ImageResource.size;
            int sceneHeight = height * ImageResource.size;
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/application.css")).toExternalForm());
            input = new Input(scene);

            root.getChildren().clear();
            root.getChildren().add(layer);
            statusBar = new StatusBar(root, sceneWidth, sceneHeight);

            rootPane.getChildren().clear();
            rootPane.setPrefSize(sceneWidth, sceneHeight + StatusBar.height);
            rootPane.getChildren().add(root);

            // Création des sprites initiaux
            for (Decor decor : game.world().getGrid().values()) {
                sprites.add(SpriteFactory.create(layer, decor));
                decor.setModified(true);
                Bonus bonus = decor.getBonus();
                if (bonus != null) {
                    sprites.add(SpriteFactory.create(layer, bonus));
                    bonus.setModified(true);
                    affichesBonus.add(bonus); //Ajoute à la liste de bonus affichés
                }
            }

            sprites.add(new SpriteGardener(layer, gardener));
            resizeScene(sceneWidth, sceneHeight);
        }

        void buildAndSetGameLoop() {
            gameLoop = new AnimationTimer() {
                public void handle(long now) {
                    checkLevel();
                    processInput();
                    update(now);
                    checkCollision();
                    cleanupSprites();
                    render();
                    statusBar.update(game);
                }
            };
        }
        // Vérifie si un changement de niveau est demandé
        private void checkLevel() {
            if (game.isSwitchLevelRequested()) {
                int newLevel = game.getSwitchLevel();
                String comingFrom = game.getSwitchLevelDirection();
                game.world().setCurrentLevel(newLevel);
                // Recompte les carottes à ramasser
                int countCarrots = 0;
                for (Decor decor : game.world().getGrid().values()) {
                    if (decor.getBonus() instanceof Carrots) {
                        countCarrots++;
                    }
                }
                game.setCounteurCarrots(countCarrots);
                // Trouver la porte par laquelle on arrive
                Position doorPos = null;
                for (Decor decor : game.world().getGrid().values()) {
                    if (decor instanceof Door door && door.isOpen()) {
                        if ((comingFrom.equals("next") && "prev".equals(door.getDirection())) ||
                                (comingFrom.equals("prev") && "next".equals(door.getDirection()))) {
                            doorPos = door.getPosition();
                            break;
                        }
                    }
                }
                    // Positionner le jardinier à l'entrée
                if (doorPos != null) {
                    Position positionReapparition = new Position(newLevel, doorPos.x(), doorPos.y() + 1);
                    gardener.setPosition(positionReapparition);
                }

                // Réinitialisation de la scène
                sprites.clear();
                affiches.clear();
                affichesBonus.clear(); //On réinitialise les bonus
                cleanupSprites();
                initialize();
                game.clearSwitchLevel();
            }
        }
        // Détection des collisions entre jardinier et insectes
        private void checkCollision() {
            Position pos = gardener.getPosition();

            for (Insecte insecte : game.world().getInsectes()) {

                if (insecte.getPosition().equals(pos)) {
                    if (!insecte.isDeleted()) {
                        if (insecte.peutEtreTuerPar(gardener)) {
                            gardener.setInsecticideCount(gardener.getInsecticideCount() - insecte.nbreBombskill());
                            insecte.remove();
                            System.out.println(" Insecte tué par le jardinier ");
                        } else {
                            insecte.sting(gardener);
                        }
                    }
                }
            }
        }


        private void processInput() {
            if (input.isExit()) {
                gameLoop.stop();
                Platform.exit();
                System.exit(0);
            } else if (input.isMoveDown()) {
                gardener.requestMove(Direction.DOWN);
            } else if (input.isMoveLeft()) {
                gardener.requestMove(Direction.LEFT);
            } else if (input.isMoveRight()) {
                gardener.requestMove(Direction.RIGHT);
            } else if (input.isMoveUp()) {
                gardener.requestMove(Direction.UP);
            }
            input.clear();
        }

        private void showMessage(String msg, Color color) {
            Text message = new Text(msg);
            message.setTextAlignment(TextAlignment.CENTER);
            message.setFont(new Font(60));
            message.setFill(color);

            StackPane pane = new StackPane(message);
            pane.setPrefSize(rootPane.getWidth(), rootPane.getHeight());
            rootPane.getChildren().clear();
            rootPane.getChildren().add(pane);

            new AnimationTimer() {
                public void handle(long now) {
                    processInput();
                }
            }.start();
        }
        // Met a jour toute la logique du jeu
        private void update(long now) {
            game.world().getGrid().values().forEach(decor -> decor.update(now));
            gardener.update(now);

            // Ajout des nouveaux bonus (insecticides)
            for (Decor decor : game.world().getGrid().values()) {
                Bonus bonus = decor.getBonus();
                if (bonus != null && !affichesBonus.contains(bonus)) {
                    sprites.add(SpriteFactory.create(layer, bonus));
                    affichesBonus.add(bonus);
                    bonus.setModified(true);
                }
            }

              int currentLevel1 = game.world().currentLevel();

                // Met à jour uniquement les insectes du niveau courant
            game.world().getInsectes().stream()
                    .filter(insecte -> insecte.getPosition().level() == currentLevel1)
                    .forEach(insecte -> {
                        insecte.update(now);
                        if (!affiches.contains(insecte)) {
                            sprites.add(SpriteFactory.create(layer, insecte));
                            affiches.add(insecte);
                        }
                    });

            // Passage de niveau via la porte
            Position pos = gardener.getPosition();
            Decor decor = game.world().getGrid().get(pos);
            if (decor instanceof Door door && door.isOpen()) {
                if (pos.equals(door.getPosition())) {
                    int currentLevel = game.world().currentLevel();
                    String dir = door.getDirection();
                    if ("prev".equals(dir) && currentLevel > 1) {
                        game.requestSwitchLevel(currentLevel - 1, "prev");
                    } else if ("next".equals(dir) && currentLevel < 3) {
                        game.requestSwitchLevel(currentLevel + 1, "next");
                    }
                }
            }

            if (game.defeat()) {
                gameLoop.stop();
                showMessage("Perdu!", Color.RED);
            }

            if (game.victory()) {
                gameLoop.stop();
                showMessage("Victoire !", Color.GREEN);
            }
        }

        public void cleanupSprites() {
            sprites.forEach(sprite -> {
                if (sprite.getGameObject().isDeleted()) {
                    cleanUpSprites.add(sprite);
                }
            });
            cleanUpSprites.forEach(Sprite::remove);
            sprites.removeAll(cleanUpSprites);
            cleanUpSprites.clear();
        }

        private void render() {
            sprites.forEach(Sprite::render);
        }

        public void start() {
            gameLoop.start();
        }

        private void resizeScene(int width, int height) {
            rootPane.setPrefSize(width, height + StatusBar.height);
            layer.setPrefSize(width, height);
            Platform.runLater(() -> scene.getWindow().sizeToScene());
        }
    }