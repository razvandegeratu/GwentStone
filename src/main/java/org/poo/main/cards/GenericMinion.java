package org.poo.main.cards;

import org.poo.fileio.CardInput;

public class GenericMinion extends GenericCard {
    private int damageDealt;
    private boolean attackedCurrentRound;
    private boolean specialMinion;
    private boolean isFrozen;
    private  boolean isTank;

    /** Dummy SpecialAbility that is used by every special minon
     * @param dummyMinion is the minion that is supposed to use the ability
     */
    public void specialAbility(final GenericMinion dummyMinion) {
        // I could make this an error, but it's not in the scope of the project
        System.out.println("This minion does not have a special ability");
    }
    public GenericMinion(final GenericMinion genericMinion) {
        super(genericMinion.getCardInput());
        this.damageDealt = genericMinion.getAttackDamage();
        this.isTank = genericMinion.getcardInput().getName().equals("Goliath")
                                    || getcardInput().getName().equals("Warden");
        this.attackedCurrentRound = genericMinion.attackedCurrentRound;
        this.isFrozen = genericMinion.isFrozen();
        this.specialMinion = genericMinion.isSpecialMinion();
    }
    /** Sets the attack damage of the minion
     *
     */
    public void setAttackDamage(final int attackDamage) {
        this.damageDealt = attackDamage;
    }

    /** Returns true if minion is tank, false otherwise
     *
     */
    public boolean getIsTank() {
        return isTank;
    }

    /** Checks if a minion is special
     */
    public boolean isSpecialMinion() {
        return specialMinion;
    }

    /** Sets the bool attackedCurrentRound
     *
     * @param attackedCurrentRound is the bool that is set
     */
    public void setAttackedCurrentRound(final boolean attackedCurrentRound) {
        this.attackedCurrentRound = attackedCurrentRound;
    }

    public GenericMinion(final CardInput cardInput) {
        super(cardInput);
        this.damageDealt = cardInput.getAttackDamage();
        this.isTank = cardInput.getName().equals("Goliath") || cardInput.getName().equals("Warden");
    }

    /** Returns the attack damage of the minion
     *
     */
    public int getAttackDamage() {
        return damageDealt;
    }

    /** Returns true if minion has attacked, false otherwise
     *
     */
    public boolean hasAttackedThisTurn() {
        return attackedCurrentRound;
    }
    /** Returns the Frozen status of the minion
     *
     */
    public boolean isFrozen() {
        return isFrozen;
    }

    /**
     * This method is used to set a minion as tank
     * @param isTank is the tank status
     */
    public void setIsTank(final boolean isTank) {
        this.isTank = isTank;
    }

    /**
     * This method is used to (un)freeze a minion
     * @param status is the freeze status
     */
    public void setIsFrozen(final boolean status) {
        isFrozen = status;
    }


}
