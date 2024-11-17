package org.poo.main.utils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.cards.GenericCard;
import org.poo.main.cards.GenericMinion;
import org.poo.main.cards.Hero;

public class JsonUtils {

    /**
     * Adds the details of a hero to the given ObjectNode.
     *
     * @param cardNode The ObjectNode to which the hero's details will be added.
     * @param hero The hero whose details are to be added.
     */
    public static void addHeroDetails(final ObjectNode cardNode, final Hero hero) {
        cardNode.put("mana", hero.getMana());

        cardNode.put("description", hero.getDescription());

        ArrayNode colorsArray = cardNode.putArray("colors");
        for (String color : hero.getColors()) {
            colorsArray.add(color);
        }
        cardNode.put("name", hero.getName());
        cardNode.put("health", hero.getHealth());
    }

    /**
     * Adds the details of a minion to the given ObjectNode.
     *
     * @param cardNode The ObjectNode to which the minion's details will be added.
     * @param minion The minion whose details are to be added.
     */
    public static void addMinionDetailsObjNode(final ObjectNode cardNode,
                                               final GenericMinion minion) {
        cardNode.put("mana", minion.getMana());
        cardNode.put("attackDamage", minion.getAttackDamage());
        cardNode.put("health", minion.getHealth());
        cardNode.put("description", minion.getDescription());

        ArrayNode colorsArray = cardNode.putArray("colors");
        for (String color : minion.getColors()) {
            colorsArray.add(color);
        }
        cardNode.put("name", minion.getName());
    }

    /**
     * Adds the details of a minion to the given ArrayNode.
     *
     * @param handArray The ArrayNode to which the minion's details will be added.
     * @param card The minion whose details are to be added.
     */
    public static void addMinionDetailsArrNode(final ArrayNode handArray,
                                               final GenericCard card) {
        final ObjectNode cardNode = handArray.addObject();
        cardNode.put("mana", card.getMana());
        cardNode.put("attackDamage", card.getAttackDamage());
        cardNode.put("health", card.getHealth());
        cardNode.put("description", card.getDescription());
        ArrayNode colorsArray = cardNode.putArray("colors");
        for (String color : card.getColors()) {
            colorsArray.add(color);
        }
        cardNode.put("name", card.getName());
    }
}
