package fr.ubx.poo.ubgarden.game.go.personage;

import fr.ubx.poo.ubgarden.game.Direction;
import fr.ubx.poo.ubgarden.game.Game;
import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.bonus.Bonus;
import fr.ubx.poo.ubgarden.game.go.bonus.Intesecticids;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;

public class Hornet extends Insecte {

    private int bombCounter = 0; // Compteur pour les bombes insecticides touchées

    public Hornet(Game game, Position position) {
        super(game, position, 30, 2, game.configuration().hornetMoveFrequency()); // 30 de dégâts, 2 piqûres possibles
        getMoveTimer().start();
    }

    @Override
    public boolean walkableBy(Gardener gardener) {
        return true; // Le jardinier peut marcher sur le frelon
    }

    @Override
    public boolean walkableBy(Insecte insecte) {
        return insecte.walkableBy(this);
    }

    // methode pour piquer le jardinier
    @Override
    public void sting(Gardener gardener) {
        // Si le frelon est déjà supprimé il fait rien
        if (this.isDeleted()) return;

        // Si le jardinier peut tuer le frelon
        if (peutEtreTuerPar(gardener)) return;

        int damage;
        // Si le frelon a été touché au moins une fois par une bombe, les dégâts sont réduits de moitié
        if (bombCounter > 0) {
            damage = getStingDamage() / 2;
        } else {
            damage = getStingDamage();
        }

        // Appliquer les dégâts au jardinier
        gardener.hurt(damage);
        System.out.println("Le frelon a piqué le jardinier : ");

        // Réduire le nombre de piqûres restantes du frelon
        setRemainingStings(getRemainingStings() - 1);

        // Si le frelon a utilisé toutes ses piqûres
        if (getRemainingStings() == 0) {
            this.remove();
            System.out.println("Le frelon est mort après avoir piqué.");
        }
    }




    @Override
    public int nbreBombskill() {
        return 2 ; // 2 bombs pour tuer le frelon
    }

    @Override
    public Position move(Direction direction) {
        // Effectue le mouvement standard défini dans la classe mère (Insecte)
        Position nextPos = super.move(direction);

        // Récupère le décor sur la case d'arrivée
        Decor nextDecor = getGame().world().getGrid().get(nextPos);

        if (nextDecor != null) {
            // Vérifie si la case contient un bonus insecticide
            Bonus bonus = nextDecor.getBonus();
            if (bonus instanceof Intesecticids) {
                // Supprimer la bombe après passage
                bonus.remove();
                nextDecor.setModified(true); // Signale que la case doit être re-rendue
                bombCounter++;
                System.out.println("Le frelon a été touché par une bombe insecticide");

                // Si le frelon a été touché deux fois, il meurt
                if (bombCounter >= 2) {
                    this.remove();
                    System.out.println("Le frelon est tué par 2 bombes insecticides");
                }
            }
        }

        return nextPos;
    }


}
