package org.poo.main.utils;

import org.poo.fileio.CardInput;

import java.util.ArrayList;

public class GenericCard {
    private final CardInput cardInput;

    public GenericCard(final CardInput cardInput) {
        this.cardInput = cardInput;
    }

    /** Returns the description of a card
     *
     */
    public String getDescription() {
        return cardInput.getDescription();
    }

    /** Returns the name of a card
     *
     */
    public String getName() {
        return cardInput.getName();
    }

    /** Returns the card
     *
     */
    public CardInput getCardInput() {
        return cardInput;
    }

    /** Returns the colors of a card
     *
     */
    public ArrayList<String> getColors() {
        return cardInput.getColors();
    }

    /** Returns the Health of a card
     *
     */
    public int getHealth() {
        return cardInput.getHealth();
    }

    /** Sets the Health of a card
     */
    public void setHealth(final int health) {
        cardInput.setHealth(health);
    }

    /** Returns the Mana of a card
     *
     */
    public int getMana() {
        return cardInput.getMana();
    }

    /** Returns the attack damage of a card
     *
     */
    public int getAttackDamage() {
        return cardInput.getAttackDamage();
    }

    /** Returns the current card
     *
     */
    protected GenericCard getcardInput() {
        return this;
    }
}
