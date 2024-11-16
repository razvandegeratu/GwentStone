package org.poo.main.utils;

import org.poo.fileio.CardInput;
import java.util.ArrayList;

public class Table {
    private final ArrayList<GenericMinion>[] table = new ArrayList[MagicNumbers.TABLEROWS];
    private final ArrayList<GenericMinion> playerOneHand = new ArrayList<>();
    private final ArrayList<GenericMinion> playerTwoHand = new ArrayList<>();

    private int playerTurn;
    private final ArrayList<GenericMinion> playerOneDeck = new ArrayList<>();
    private final ArrayList<GenericMinion> playerTwoDeck = new ArrayList<>();

    public Table() {
        // Initialize each row in the table
        for (int i = 0; i < table.length; i++) {
            table[i] = new ArrayList<>();
        }
    }
    /**
     * Method to return a player's deck
     * @param idx is player's index
     */
    public final ArrayList<GenericMinion> getPlayerDeck(final int idx) {
        return idx == 1 ? playerOneDeck : playerTwoDeck;
    }

    /**
     * Method to check if a player has a tank placed
     * @param playerToBeCheckedTurn is player's index
     */
    public boolean hasTankOnEnemyRow(final int playerToBeCheckedTurn) {
        int enemyFrontRow = (playerToBeCheckedTurn == 1) ? MagicNumbers.SECONDROW
                                                            : MagicNumbers.THIRDROW;
        int enemyBackRow = (playerToBeCheckedTurn == 1) ? MagicNumbers.FIRSTROW
                                                            : MagicNumbers.FOURTHROW;

        // Check if there are any tanks on the enemy front row
        for (GenericMinion card : table[enemyFrontRow]) {
            if (card.getIsTank()) {
                return true;
            }
        }

        // Check if there are any tanks on the enemy back row - redundant check,
        // But you never know when the game might change
        for (GenericMinion card : table[enemyBackRow]) {
            if (card.getIsTank()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to return a player's hand
     * @param idx is player's index
     */
    public final ArrayList<GenericMinion> getPlayerHand(final int idx) {
        return idx == 1 ? playerOneHand : playerTwoHand;
    }

    /** Method to set the player whose turn it is
     * @param playerTurn is the index of the player whose turn it is
     */
    public final  void setPlayerTurn(final int playerTurn) {
        this.playerTurn = playerTurn;
    }
    /** Method returns the index of the player whose turn it is
     **/
    public final int getPlayerTurn() {
        return playerTurn;
    }

    /**
     * Method to set a player's deck
     * @param cardInputs is the list of cards
     * @param playerIdx is player's index
     */
    public void setPlayerDeck(final ArrayList<CardInput> cardInputs, final int playerIdx) {
        ArrayList<GenericMinion> targetDeck = (playerIdx == 1) ? playerOneDeck : playerTwoDeck;
        targetDeck.clear();

        for (CardInput cardInput : cardInputs) {
            targetDeck.add(new GenericMinion(cardInput));
        }

    }

    /**
     * Method to get a card to the table
     * @param row is the row number
     * @param col is the column number
     */
    public GenericMinion getCardAtPosition(final int row, final int col) {
        ArrayList<GenericMinion> rowList = getTableRow(row);

        if (rowList == null || col < MagicNumbers.FIRSTCOLOUMN || col >= rowList.size()) {
            return null;
        }
        return rowList.get(col);
    }
    /**
     * Method remove card from given position
     * @param row is the row number
     * @param col is the column number
     */
    public void removeCardAtPosition(final int row, final int col) {
        ArrayList<GenericMinion> rowList = getTableRow(row);
        if (col >= 0 && col < rowList.size()) {
            rowList.remove(col);
        } else {
            System.out.println("No minion found at row " + row + ", col " + col);
        }
    }

    /**
     * Returns the row of the table
     * @param row is the row number
     */
    public ArrayList<GenericMinion> getTableRow(final int row) {
        return table[row];
    }

    private Hero playerOneHero;
    private Hero playerTwoHero;

    /** Returns the Hero for player 2
     */
    public Hero getPlayerOneHero() {
        return playerOneHero;
    }

    /** Returns the Hero for player 2
     */
    public Hero getPlayerTwoHero() {
        return playerTwoHero;
    }

    /** Sets the Hero for player 1
     * @param hero is the Hero object for player 1
     */
    public void setPlayerOneHero(final Hero hero) {
        this.playerOneHero = hero;
    }

    /** Sets the Hero for player 2
     * @param hero is the Hero object for player 2
     */
    public void setPlayerTwoHero(final Hero hero) {
        this.playerTwoHero = hero;
    }


}
