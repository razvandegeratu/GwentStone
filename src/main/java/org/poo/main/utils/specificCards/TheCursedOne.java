package org.poo.main.utils.specificCards;

import org.poo.fileio.CardInput;
import org.poo.main.utils.GenericMinion;

public class TheCursedOne extends GenericMinion {
    public TheCursedOne(final CardInput cardInput) {
        super(cardInput);
    }

    /**
     * The special ability of TheCursedOne is to swap health and attack damage of attacked minion.
     * @param attackedMinion is the minion that is attacked by TheCursedOne
     */
    @Override
    public void specialAbility(final GenericMinion attackedMinion) {
        int aux = attackedMinion.getAttackDamage();
        attackedMinion.setAttackDamage(attackedMinion.getHealth());
        attackedMinion.setHealth(aux);
    }
}
