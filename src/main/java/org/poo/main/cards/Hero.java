package org.poo.main.cards;

import org.poo.fileio.CardInput;

public class Hero extends GenericMinion {
    private static final int HERO_DEFAULT_HEALTH = 30;

    public Hero(final CardInput cardInput) {
        super(cardInput);
        super.setHealth(HERO_DEFAULT_HEALTH); // Set default health to 30 for heroes
    }

}
