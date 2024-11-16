package org.poo.main;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.poo.fileio.ActionsInput;
import org.poo.fileio.CardInput;
import org.poo.fileio.DecksInput;
import org.poo.fileio.GameInput;
import org.poo.fileio.Input;

import org.poo.main.utils.JsonUtils;
import org.poo.main.utils.MagicNumbers;
import org.poo.main.utils.Table;
import org.poo.main.utils.Hero;
import org.poo.main.utils.GenericMinion;
import org.poo.main.utils.GenericCard;
import org.poo.main.utils.specificCards.Disciple;
import org.poo.main.utils.specificCards.Miraj;
import org.poo.main.utils.specificCards.TheCursedOne;
import org.poo.main.utils.specificCards.TheRipper;

import static org.poo.main.utils.MagicNumbers.TABLEROWS;
import static org.poo.main.utils.MagicNumbers.THIRDROW;
import static org.poo.main.utils.MagicNumbers.FIFTHCOLOUMN;
import static org.poo.main.utils.MagicNumbers.FIRSTCOLOUMN;


public class GameExecute {
    private boolean gameEnded;
    private Input inputData;
    private ArrayNode outputData;
    private int startingPlayer;
    private Table table;
    private int playerOneMana;
    private int playerTwoMana;
    private int roundNumber;
    private int gamesEnded = 0;
    private int playerOneWins = 0;
    private int playerTwoWins = 0;
    public GameExecute() {
        this.gameEnded = false;
        this.table = new Table();
    }

    /**
     * Plays the game based on the input data and generates the output data.
     *
     * @param inputDataRead The input data containing the game details.
     * @param outputDataRead The output data to store the results of the game.
     */
    public final void playGame(final Input inputDataRead,
                               final ArrayNode outputDataRead) {
        this.outputData = outputDataRead;
        this.inputData = inputDataRead;

        for (GameInput game : inputData.getGames()) {
            initializeGame(game);
            processGameActions(game);
        }
    }

    private void initializeGame(final GameInput game) {
        playerOneMana = 1;
        playerTwoMana = 1;
        roundNumber = 1;
        this.gameEnded = false;
        this.table = new Table();
        DecksInput playerOneDecks = inputData.getPlayerOneDecks();
        DecksInput playerTwoDecks = inputData.getPlayerTwoDecks();

        int playerOneDeckIdx = game.getStartGame().getPlayerOneDeckIdx();
        int playerTwoDeckIdx = game.getStartGame().getPlayerTwoDeckIdx();

        ArrayList<CardInput> originalPlayerOneDeck =
                new ArrayList<>(playerOneDecks.getDecks().get(playerOneDeckIdx));
        ArrayList<CardInput> originalPlayerTwoDeck =
                new ArrayList<>(playerTwoDecks.getDecks().get(playerTwoDeckIdx));

        ArrayList<CardInput> playerOneDeck = new ArrayList<>();
        for (CardInput card : originalPlayerOneDeck) {
            playerOneDeck.add(new CardInput(card));
        }

        ArrayList<CardInput> playerTwoDeck = new ArrayList<>();
        for (CardInput card : originalPlayerTwoDeck) {
            playerTwoDeck.add(new CardInput(card));
        }

        int shuffleSeed = game.getStartGame().getShuffleSeed();
        Collections.shuffle(playerOneDeck, new Random(shuffleSeed));
        Collections.shuffle(playerTwoDeck, new Random(shuffleSeed));

        table.setPlayerDeck(playerOneDeck, 1);
        table.setPlayerDeck(playerTwoDeck, 2);

        startingPlayer = game.getStartGame().getStartingPlayer();
        table.setPlayerTurn(startingPlayer);
        table.setPlayerOneHero(new Hero(game.getStartGame().getPlayerOneHero()));
        table.setPlayerTwoHero(new Hero(game.getStartGame().getPlayerTwoHero()));

        drawCardForPlayer(1);
        drawCardForPlayer(2);
    }

    private void processGameActions(final GameInput game) {

        ArrayList<ActionsInput> actions = game.getActions();
        for (ActionsInput action : actions) {
            if (gameEnded) {
                break;
            }
            handleAction(action);
        }
    }

    private void handleAction(final ActionsInput action) {
        ObjectNode actionOutput = null;
        switch (action.getCommand()) {
            case "getCardAtPosition":
            case "getPlayerOneWins":
            case "getPlayerTwoWins":
            case "getPlayerDeck":
            case "getPlayerHero":
            case "getTotalGamesPlayed":
            case "getPlayerTurn":
            case "getPlayerMana":
            case "getCardsInHand":
            case "getCardsOnTable":
            case "getFrozenCardsOnTable":
                actionOutput = outputData.addObject();
                actionOutput.put("command", action.getCommand());
                break;
            case "placeCard":
                placeCard(action);
                return;
            case "endPlayerTurn":
                handleEndPlayerTurn();
                return;
            case "useHeroAbility":
                handleUseHeroAbility(action);
                return;
            case "cardUsesAttack":
                handleCardUsesAttack(action);
                break;
            case "useAttackHero":
                if (table.getPlayerTurn() == 1) {
                    handleUseAttackHero(action, table.getPlayerTwoHero());
                } else {
                    handleUseAttackHero(action, table.getPlayerOneHero());
                }
                break;
            case "cardUsesAbility":
                handleCardUsesSpell(action);
                break;
            default:
                break;
        }

        if (actionOutput != null) {
            switch (action.getCommand()) {
                case "getTotalGamesPlayed":
                    actionOutput.put("output", gamesEnded);
                    break;
                case "getPlayerOneWins":
                    actionOutput.put("output", playerOneWins);
                    break;
                case "getPlayerTwoWins":
                    actionOutput.put("output", playerTwoWins);
                    break;
                case "getFrozenCardsOnTable":
                    handleGetFrozenCardsOnTable(action, actionOutput);
                    break;
                case "getCardAtPosition":
                    handleGetCardAtPosition(action, actionOutput);
                    break;
                case "getPlayerMana":
                    handleGetPlayerMana(action, actionOutput);
                    break;
                case "getPlayerDeck":
                    handleGetPlayerDeck(action, actionOutput);
                    break;
                case "getPlayerHero":
                    int playerIdx = action.getPlayerIdx();
                    if (playerIdx == 1) {
                        handleGetPlayerHero(action,
                                actionOutput,
                                table.getPlayerOneHero());
                    } else {
                        handleGetPlayerHero(action,
                                actionOutput,
                                table.getPlayerTwoHero());
                    }
                    break;
                case "getPlayerTurn":
                    handleGetPlayerTurn(actionOutput);
                    break;
                case "getCardsInHand":
                    handleGetCardsInHand(action, actionOutput);
                    break;
                case "getCardsOnTable":
                    handleGetCardsOnTable(actionOutput);
                    break;
                default:
                    break;
            }
        }
    }


    private void handleGetFrozenCardsOnTable(final ActionsInput action,
                                             final ObjectNode actionOutput) {
        actionOutput.put("command", action.getCommand());
        ArrayNode frozenCardsArray = actionOutput.putArray("output");

        for (int row = 0; row < TABLEROWS; row++) {
            ArrayList<GenericMinion> cardsInRow = table.getTableRow(row);
            for (GenericMinion minion : cardsInRow) {
                if (minion.isFrozen()) {
                    ObjectNode cardNode = frozenCardsArray.addObject();
                    JsonUtils.addMinionDetailsObjNode(cardNode, minion);
                }
            }
        }
    }

    /**
     * Handles the use of a hero's ability.
     *
     * @param action The action input containing the details of the hero ability usage.
     */
    private void handleUseHeroAbility(final ActionsInput action) {
        int playerIdx = table.getPlayerTurn();
        Hero hero = (playerIdx == 1) ? table.getPlayerOneHero() : table.getPlayerTwoHero();

        ObjectNode actionOutput;
        if (hero == null) {
            actionOutput = outputData.addObject();
            actionOutput.put("command", "useHeroAbility");
            actionOutput.put("error", "Invalid hero ability: Hero does not exist.");
            return;
        }

        int targetRow = action.getAffectedRow();
        boolean isEnemyRow = isEnemyCard(targetRow);
        boolean isOwnRow = !isEnemyRow;

        int requiredMana = hero.getMana();

        if ((playerIdx == 1 && playerOneMana < requiredMana)
                || (playerIdx == 2 && playerTwoMana < requiredMana)) {
            actionOutput = outputData.addObject();
            actionOutput.put("command", "useHeroAbility");
            actionOutput.put("affectedRow", targetRow);
            actionOutput.put("error", "Not enough mana to use hero's ability.");
            return;
        }

        if (hero.hasAttackedThisTurn()) {
            actionOutput = outputData.addObject();
            actionOutput.put("command", "useHeroAbility");
            actionOutput.put("affectedRow", targetRow);
            actionOutput.put("error", "Hero has already attacked this turn.");
            return;
        }

        if ((hero.getName().equals("Lord Royce") || hero.getName().equals("Empress Thorina"))
                && !isEnemyRow) {
            actionOutput = outputData.addObject();
            actionOutput.put("command", "useHeroAbility");
            actionOutput.put("affectedRow", targetRow);
            actionOutput.put("error", "Selected row does not belong to the enemy.");
            return;
        }

        if ((hero.getName().equals("King Mudface")
                || hero.getName().equals("General Kocioraw"))
                && !isOwnRow) {
            actionOutput = outputData.addObject();
            actionOutput.put("command", "useHeroAbility");
            actionOutput.put("affectedRow", targetRow);
            actionOutput.put("error", "Selected row does not belong to the current player.");
            return;
        }

        if (playerIdx == 1) {
            playerOneMana -= requiredMana;
        } else {
            playerTwoMana -= requiredMana;
        }

        hero.setAttackedCurrentRound(true);

        switch (hero.getName()) {
            case "King Mudface":
                abilityKM(action);
                break;
            case "General Kocioraw":
                abilityGK(action);
                break;
            case "Lord Royce":
                abilityLR(action);
                break;
            case "Empress Thorina":
                abilityET(action);
                break;
            default:
                break;
        }
    }
    /**
     * Handles the use of a Lord Royce.
     *
     * @param action The action input containing the details of the hero ability usage.
     */
    private void abilityLR(final ActionsInput action) {
        int targetRow = action.getAffectedRow();
        for (GenericMinion minionOnRow : table.getTableRow(targetRow)) {
            minionOnRow.setIsFrozen(true);
        }
    }
    /**
     * Handles the use of an Empress Thorina.
     *
     * @param action The action input containing the details of the hero ability usage.
     */
    private void abilityET(final ActionsInput action) {
        int targetRow = action.getAffectedRow();
        GenericMinion maxHealthMinion = null;
        for (GenericMinion minionOnRow : table.getTableRow(targetRow)) {
            if (maxHealthMinion == null || minionOnRow.getHealth() > maxHealthMinion.getHealth()) {
                maxHealthMinion = minionOnRow;
            }
        }
        if (maxHealthMinion != null) {
            table.getTableRow(targetRow).remove(maxHealthMinion);
        }
    }
    /**
     * Handles the use of a General Kocioraw.
     *
     * @param action The action input containing the details of the hero ability usage.
     */
    private void abilityGK(final ActionsInput action) {
        int targetRow = action.getAffectedRow();
        for (GenericMinion minionOnRow : table.getTableRow(targetRow)) {
            minionOnRow.setAttackDamage(minionOnRow.getAttackDamage() + 1);
        }
    }
    /**
     * Handles the use of a King Mudface.
     *
     * @param action The action input containing the details of the hero ability usage.
     */
    private void abilityKM(final ActionsInput action) {
        int targetRow = action.getAffectedRow();
        for (GenericMinion minionOnRow : table.getTableRow(targetRow)) {
            minionOnRow.setHealth(minionOnRow.getHealth() + 1);
        }
    }

    private void handleUseAttackHero(final ActionsInput action,
                                     final Hero hero) {
        ObjectNode actionOutput;
        int attackerRow = action.getCardAttacker().getX();
        int attackerCol = action.getCardAttacker().getY();
        GenericMinion attackerCard = table.getCardAtPosition(attackerRow, attackerCol);

        if (attackerCard == null || hero == null) {
            actionOutput = outputData.addObject();
            actionOutput.put("command", "useAttackHero");
            actionOutput.putObject("cardAttacker").put("x", attackerRow).put("y", attackerCol);
            actionOutput.put("error", "Invalid attack: Attacker or attacked card does not exist.");
            return;
        }

        if (attackerCard.hasAttackedThisTurn()) {
            actionOutput = outputData.addObject();
            actionOutput.put("command", "useAttackHero");
            actionOutput.putObject("cardAttacker").put("x", attackerRow).put("y", attackerCol);
            actionOutput.put("error", "Attacker card has already attacked this turn.");
            return;

        }
        if (attackerCard.isFrozen()) {
            actionOutput = outputData.addObject();
            actionOutput.put("command", "useAttackHero");
            actionOutput.putObject("cardAttacker").put("x", attackerRow).put("y", attackerCol);
            actionOutput.put("error", "Attacker card is frozen.");
            return;
        }

        if (table.hasTankOnEnemyRow(table.getPlayerTurn())) {
            actionOutput = outputData.addObject();
            actionOutput.put("command", "useAttackHero");
            actionOutput.putObject("cardAttacker").put("x", attackerRow).put("y", attackerCol);
            actionOutput.put("error", "Attacked card is not of type 'Tank'.");
            return;
        }

        attackerCard.setAttackedCurrentRound(true);
        hero.setHealth(hero.getHealth() - attackerCard.getAttackDamage());
        if (hero.getHealth() <= 0) {
            actionOutput = outputData.addObject();
            gamesEnded++;
            if (table.getPlayerTurn() == 1) {
                actionOutput.put("gameEnded", "Player one killed the enemy hero.");
                playerOneWins++;
            } else {
                actionOutput.put("gameEnded", "Player two killed the enemy hero.");
                playerTwoWins++;
            }
        }

    }

    private void handleCardUsesSpell(final ActionsInput action) {
        int attackerRow = action.getCardAttacker().getX();
        int attackedRow = action.getCardAttacked().getX();

        int attackerCol = action.getCardAttacker().getY();
        int attackedCol = action.getCardAttacked().getY();

        GenericMinion attackerCard = table.getCardAtPosition(attackerRow, attackerCol);
        GenericMinion attackedCard = table.getCardAtPosition(attackedRow, attackedCol);

        boolean canUseSpell = checkConditionsSpell(action);
        if (!canUseSpell) {
            return;
        }
        attackerCard.specialAbility(attackedCard);
        if (!attackerCard.getName().equals("Disciple")) {
            attackerCard.setAttackedCurrentRound(true);
        }
        if (attackedCard.getHealth() <= 0) {
            table.removeCardAtPosition(attackedRow, attackedCol);
        }
        if (attackerCard.getHealth() <= 0) {
            table.removeCardAtPosition(attackerRow, attackerCol);
        }

    }
    private boolean checkConditionsSpell(final ActionsInput action) {
        ObjectNode actionOutput;
        int attackerRow = action.getCardAttacker().getX();
        int attackerCol = action.getCardAttacker().getY();
        int attackedRow = action.getCardAttacked().getX();
        int attackedCol = action.getCardAttacked().getY();
        GenericMinion attackerCard = table.getCardAtPosition(attackerRow, attackerCol);
        GenericMinion attackedCard = table.getCardAtPosition(attackedRow, attackedCol);

        if (!isEnemyCard(attackedRow)
                && !attackerCard.getName().equals("Disciple")) {
            actionOutput = outputData.addObject();
            actionOutput.put("command", "cardUsesAbility");
            actionOutput.putObject("cardAttacker").put("x", attackerRow).put("y", attackerCol);
            actionOutput.putObject("cardAttacked").put("x", attackedRow).put("y", attackedCol);
            actionOutput.put("error", "Attacked card does not belong to the enemy.");
            return false;
        }

        if (isEnemyCard(attackedRow)
                && attackerCard.getName().equals("Disciple")) {
            actionOutput = outputData.addObject();
            actionOutput.put("command", "cardUsesAbility");
            actionOutput.putObject("cardAttacker").put("x", attackerRow).put("y", attackerCol);
            actionOutput.putObject("cardAttacked").put("x", attackedRow).put("y", attackedCol);
            actionOutput.put("error", "Attacked card does not belong to the current player.");
            return false;
        }

        if (attackerCard.hasAttackedThisTurn()) {
            actionOutput = outputData.addObject();
            actionOutput.put("command", "cardUsesAbility");
            actionOutput.putObject("cardAttacker").put("x", attackerRow).put("y", attackerCol);
            actionOutput.putObject("cardAttacked").put("x", attackedRow).put("y", attackedCol);
            actionOutput.put("error", "Attacker card has already attacked this turn.");
            return false;
        }

        if (attackerCard.isFrozen()) {
            actionOutput = outputData.addObject();
            actionOutput.put("command", "cardUsesAbility");
            actionOutput.putObject("cardAttacker").put("x", attackerRow).put("y", attackerCol);
            actionOutput.putObject("cardAttacked").put("x", attackedRow).put("y", attackedCol);
            actionOutput.put("error", "Attacker card is frozen.");
            return false;
        }

        if (!attackerCard.getName().equals("Disciple")
                && table.hasTankOnEnemyRow(table.getPlayerTurn())
                && !attackedCard.getIsTank()) {
            actionOutput = outputData.addObject();
            actionOutput.put("command", "cardUsesAbility");
            actionOutput.putObject("cardAttacker").put("x", attackerRow).put("y", attackerCol);
            actionOutput.putObject("cardAttacked").put("x", attackedRow).put("y", attackedCol);
            actionOutput.put("error", "Attacked card is not of type 'Tank'.");
            return false;
        }

        return true;
    }

    private void handleCardUsesAttack(final ActionsInput action) {
        ObjectNode actionOutput;
        int attackerRow = action.getCardAttacker().getX();
        int attackerCol = action.getCardAttacker().getY();
        int attackedRow = action.getCardAttacked().getX();
        int attackedCol = action.getCardAttacked().getY();

        if (attackerRow < 0 || attackerRow >= MagicNumbers.TABLEROWS || attackedRow < 0
                || attackedRow >= MagicNumbers.TABLEROWS) {
            actionOutput = outputData.addObject();
            actionOutput.put("command", "cardUsesAttack");
            actionOutput.putObject("cardAttacker").put("x", attackerRow).put("y", attackerCol);
            actionOutput.putObject("cardAttacked").put("x", attackedRow).put("y", attackedCol);
            actionOutput.put("error", "Invalid row: Attacker or attacked row is out of bounds.");
            return;
        }

        GenericMinion attackerCard = table.getCardAtPosition(attackerRow, attackerCol);
        GenericMinion attackedCard = table.getCardAtPosition(attackedRow, attackedCol);

        if (attackerCard == null || attackedCard == null) {
            actionOutput = outputData.addObject();
            actionOutput.put("command", "cardUsesAttack");
            actionOutput.putObject("cardAttacker").put("x", attackerRow).put("y", attackerCol);
            actionOutput.putObject("cardAttacked").put("x", attackedRow).put("y", attackedCol);
            actionOutput.put("error", "Invalid attack: Attacker or attacked card does not exist.");
            return;
        }
        if (!isEnemyCard(attackedRow)) {
            actionOutput = outputData.addObject();
            actionOutput.put("command", "cardUsesAttack");
            actionOutput.putObject("cardAttacker").put("x", attackerRow).put("y", attackerCol);
            actionOutput.putObject("cardAttacked").put("x", attackedRow).put("y", attackedCol);
            actionOutput.put("error", "Attacked card does not belong to the enemy.");
            return;
        }

        if (attackerCard.hasAttackedThisTurn()) {
            actionOutput = outputData.addObject();
            actionOutput.put("command", "cardUsesAttack");
            actionOutput.putObject("cardAttacker").put("x", attackerRow).put("y", attackerCol);
            actionOutput.putObject("cardAttacked").put("x", attackedRow).put("y", attackedCol);
            actionOutput.put("error", "Attacker card has already attacked this turn.");
            return;
        }

        if (attackerCard.isFrozen()) {
            actionOutput = outputData.addObject();
            actionOutput.put("command", "cardUsesAttack");
            actionOutput.putObject("cardAttacker").put("x", attackerRow).put("y", attackerCol);
            actionOutput.putObject("cardAttacked").put("x", attackedRow).put("y", attackedCol);
            actionOutput.put("error", "Attacker card is frozen.");
            return;
        }

        if (table.hasTankOnEnemyRow(table.getPlayerTurn())) {
            if (!attackedCard.getIsTank()) {
                actionOutput = outputData.addObject();
                actionOutput.put("command", "cardUsesAttack");
                actionOutput.putObject("cardAttacker").put("x", attackerRow).put("y", attackerCol);
                actionOutput.putObject("cardAttacked").put("x", attackedRow).put("y", attackedCol);
                actionOutput.put("error", "Attacked card is not of type 'Tank'.");
                return;
            }
        }

        attackerCard.setAttackedCurrentRound(true);

        attackedCard.setHealth(attackedCard.getHealth() - attackerCard.getAttackDamage());

        if (attackedCard.getHealth() <= 0) {
            table.removeCardAtPosition(attackedRow, attackedCol);
        }
    }

    private boolean isEnemyCard(final int attackedRow) {
        return (table.getPlayerTurn() == 1 && attackedRow < 2
                || table.getPlayerTurn() == 2 && attackedRow >= 2);
    }

    private void handleGetCardAtPosition(final ActionsInput action,
                                         final ObjectNode actionOutput) {
        int row = action.getX();
        int col = action.getY();
        actionOutput.put("command", "getCardAtPosition");

        ArrayList<GenericMinion> rowList = table.getTableRow(row);

        if (col < 0 || col >= rowList.size()) {
            actionOutput.put("x", row);
            actionOutput.put("y", col);
            actionOutput.put("output", "No card available at that position.");
            return;
        }

        GenericMinion card = rowList.get(col);
        actionOutput.put("x", row);
        actionOutput.put("y", col);

        ObjectNode cardOutput = actionOutput.putObject("output");
        JsonUtils.addMinionDetailsObjNode(cardOutput, card);
    }

    private void handleGetPlayerMana(final ActionsInput action, final ObjectNode actionOutput) {
        int playerIdx = action.getPlayerIdx();
        int playerMana = (playerIdx == 1) ? playerOneMana : playerTwoMana;

        actionOutput.put("playerIdx", playerIdx);
        actionOutput.put("output", playerMana);
    }

    private void placeCard(final ActionsInput action) {
        int handIdx = action.getHandIdx();
        int playerIdx = table.getPlayerTurn();
        ArrayList<GenericMinion> playerHand = table.getPlayerHand(playerIdx);

        ObjectNode actionOutput = null;

        if (handIdx < 0 || handIdx >= playerHand.size()) {
            actionOutput = outputData.addObject();
            actionOutput.put("command", "placeCard");
            actionOutput.put("error", "invalid hand index");
        } else {
            GenericMinion cardToPlace = playerHand.get(handIdx);
            switch (cardToPlace.getName()) {
                case "The Ripper":
                    cardToPlace = new TheRipper(cardToPlace.getCardInput());
                    break;
                case "Miraj":
                    cardToPlace = new Miraj(cardToPlace.getCardInput());
                    break;
                case "The Cursed One":
                    cardToPlace = new TheCursedOne(cardToPlace.getCardInput());
                    break;
                case "Disciple":
                    cardToPlace = new Disciple(cardToPlace.getCardInput());
                    break;
                case "Goliath":
                case "Warden":
                    cardToPlace.setIsTank(true);
                    break;
                default:
                    cardToPlace.setIsTank(false);
                    break;
            }

            int row = -1;
            switch (cardToPlace.getName()) {
                case "Disciple":
                case "The Cursed One":
                case "Sentinel":
                case "Berserker":
                    row = (playerIdx == 1) ? MagicNumbers.FOURTHROW : MagicNumbers.FIRSTROW;
                    break;
                case "Miraj":
                case "The Ripper":
                case "Goliath":
                case "Warden":
                    row = (playerIdx == 1) ? MagicNumbers.THIRDROW : MagicNumbers.SECONDROW;
                    break;
                default:
                    actionOutput = outputData.addObject();
                    actionOutput.put("command", "placeCard");
                    actionOutput.put("error", "invalid card type");
                    break;
            }

            if (actionOutput == null && table.getPlayerTurn() != playerIdx) {
                actionOutput = outputData.addObject();
                actionOutput.put("command", "placeCard");
                actionOutput.put("error", "not your turn");
            }

            int playerMana = (playerIdx == 1) ? playerOneMana : playerTwoMana;
            if (actionOutput == null && cardToPlace.getMana() > playerMana) {
                actionOutput = outputData.addObject();
                actionOutput.put("command", "placeCard");
                actionOutput.put("handIdx", action.getHandIdx());
                actionOutput.put("error", "Not enough mana to place card on table.");
            }

            if (actionOutput == null) {
                if (playerIdx == 1) {
                    playerOneMana -= cardToPlace.getMana();
                } else {
                    playerTwoMana -= cardToPlace.getMana();
                }

                table.getTableRow(row).add(cardToPlace);
                playerHand.remove(handIdx);
            }
        }
    }

    private void handleEndPlayerTurn() {

        if (table.getPlayerTurn() == 1) {
            for (int row = 2; row < TABLEROWS; row++) {
                for (GenericMinion card : table.getTableRow(row)) {
                    card.setIsFrozen(false);
                }
            }
        } else {
            for (int row = 0; row < THIRDROW; row++) {
                for (GenericMinion card : table.getTableRow(row)) {
                    card.setIsFrozen(false);
                }
            }
        }
        if (table.getPlayerTurn() != startingPlayer) {
            endRound();
        }
        if (table.getPlayerTurn() == 1) {
            table.setPlayerTurn(2);
        } else {
            table.setPlayerTurn(1);
        }
    }

    private void endRound() {
        roundNumber++;
        int manaToAdd = Math.min(MagicNumbers.MAXMANATOADD, roundNumber);
        playerOneMana += manaToAdd;
        playerTwoMana += manaToAdd;

        drawCardForPlayer(1);
        drawCardForPlayer(2);

        for (int row = 0; row < MagicNumbers.TABLEROWS; row++) {
            ArrayList<GenericMinion> cardsInRow = table.getTableRow(row);
            for (GenericMinion card : cardsInRow) {
                card.setAttackedCurrentRound(false);
            }

        }
        table.getPlayerOneHero().setAttackedCurrentRound(false);
        table.getPlayerTwoHero().setAttackedCurrentRound(false);
    }

    private void drawCardForPlayer(final int playerIdx) {
        ArrayList<GenericMinion> playerDeck;
        if (playerIdx == 1) {
            playerDeck = table.getPlayerDeck(1);
        } else {
            playerDeck = table.getPlayerDeck(2);
        }
        if (!table.getPlayerDeck(playerIdx).isEmpty()) {
            table.getPlayerHand(playerIdx).add(new GenericMinion(playerDeck.getFirst()));
        }
        if (!playerDeck.isEmpty()) {
            playerDeck.removeFirst();
        }
    }

    private void handleGetCardsInHand(final ActionsInput action, final ObjectNode actionOutput) {
        int playerIdx = action.getPlayerIdx();
        ArrayList<GenericMinion> hand = table.getPlayerHand(playerIdx);

        actionOutput.put("playerIdx", playerIdx);
        ArrayNode handArray = actionOutput.putArray("output");

        for (GenericCard card : hand) {
            JsonUtils.addMinionDetailsArrNode(handArray, card);
        }
    }

    private void handleGetPlayerHero(final ActionsInput action,
                                     final ObjectNode actionOutput,
                                     final Hero hero) {
        int playerIdx = action.getPlayerIdx();
        actionOutput.put("command", action.getCommand());
        actionOutput.put("playerIdx", playerIdx);

        ObjectNode heroOutput = actionOutput.putObject("output");

        JsonUtils.addHeroDetails(heroOutput, hero);
    }

    private void handleGetPlayerDeck(final ActionsInput action, final ObjectNode actionOutput) {

        int playerIdx = action.getPlayerIdx();
        ArrayList<GenericMinion> playerDeck = (playerIdx == 1) ? table.getPlayerDeck(1)
                                                                 : table.getPlayerDeck(2);

        actionOutput.put("playerIdx", playerIdx);
        ArrayNode deckArray = actionOutput.putArray("output");
        for (GenericMinion card : playerDeck) {
            JsonUtils.addMinionDetailsArrNode(deckArray, card);
        }
    }

    private void handleGetCardsOnTable(final ObjectNode actionOutput) {
        ArrayNode tableArray = actionOutput.putArray("output");

        for (int row = 0; row < TABLEROWS; row++) {
            ArrayNode rowArray = tableArray.addArray();
            ArrayList<GenericMinion> cardsInRow = table.getTableRow(row);
            for (int col = FIRSTCOLOUMN; col <= FIFTHCOLOUMN; col++) {
                if (col < cardsInRow.size()) {
                    GenericMinion minion = cardsInRow.get(col);
                    JsonUtils.addMinionDetailsArrNode(rowArray, minion);
                }
            }
        }
    }

    private void handleGetPlayerTurn(final ObjectNode actionOutput) {
        actionOutput.put("output", table.getPlayerTurn());
    }
}
