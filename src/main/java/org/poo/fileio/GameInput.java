package org.poo.fileio;

import java.util.ArrayList;

public class GameInput {
        private StartGameInput startGame;
        private ArrayList<ActionsInput> actions;

        public GameInput() {
        }

        public final StartGameInput getStartGame() {
                return startGame;
        }

        public final void setStartGame(final StartGameInput startGame) {
                this.startGame = startGame;
        }

        public final ArrayList<ActionsInput> getActions() {
                return actions;
        }

        public final void setActions(final ArrayList<ActionsInput> actions) {
                this.actions = actions;
        }

        @Override
        public final String toString() {
                return "GameInput{"
                        +  "startGame="
                        + startGame
                        + ", actions="
                        + actions
                        + '}';
        }
}
