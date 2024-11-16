package org.poo.main.utils.specificCards;

import org.poo.fileio.CardInput;
import org.poo.main.utils.GenericMinion;

public class Disciple extends GenericMinion {
    public Disciple(final CardInput cardInput) {
        super(cardInput);
    }

    /**
     * The special ability of Disciple is to increase the health of the allied minion by 2.
     * @param alliedMinion is the minion that is allied with Disciple
     */
    @Override
    public void specialAbility(final GenericMinion alliedMinion) {
        alliedMinion.setHealth(alliedMinion.getHealth() + 2);
    }
}
