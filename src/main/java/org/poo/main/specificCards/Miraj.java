package org.poo.main.specificCards;

import org.poo.fileio.CardInput;
import org.poo.main.cards.GenericMinion;

public class Miraj extends GenericMinion {

    public Miraj(final CardInput cardInput) {

        super(cardInput);
    }
    /**
     * The special ability of Miraj is to swap the health and attack damage of the attacked minion
     * @param attackedMinion is the minion that is attacked by Miraj
     */
    @Override
    public void specialAbility(final GenericMinion attackedMinion) {
        int aux = attackedMinion.getHealth();
        attackedMinion.setHealth(this.getHealth());
        this.setHealth(aux);
    }
}
