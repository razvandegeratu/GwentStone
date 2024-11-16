package org.poo.main.specificCards;

import org.poo.fileio.CardInput;
import org.poo.main.cards.GenericMinion;

public class TheRipper extends GenericMinion {
    public TheRipper(final CardInput card) {
        super(card);
    }

    /**
     * The special ability of TheRipper is to decrease the attack damage of attacked minion by 2.
     * @param attackedMinion is the minion that is attacked by TheRipper
     */
    @Override
    public void specialAbility(final GenericMinion attackedMinion) {
        attackedMinion.setAttackDamage(attackedMinion.getAttackDamage() - 2);
        if (attackedMinion.getAttackDamage() < 0) {
            attackedMinion.setAttackDamage(0);
        }
    }

}
