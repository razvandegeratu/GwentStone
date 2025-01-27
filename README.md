# 323CA Degeratu Razvan-Andrei
# GwentStone

## Description
	This project simulates a PvP card game combining elements from
	HearthStone and Gwent. The two players use a various ammount of cards
	and heroes that have special abilities to fight against each other.

## Project Structure:

### Packages: 
	1. package "cards" contains GenericCard, GenericMinion,
   	   Hero and the "specific" package
	2. package "specific" contains all the specific cards:
       Disciple, Miraj, TheCursedOne, TheRipper
	3. package "utils" contains utility classes:
       JsonUtils, MagicNumbers, CustomPrettyPrinter

### Classes:
	1. GameExecute handles the flow of the game, uses methods
   	   in order to simulate the game:
		1. initializeGame - initializes the game, initializes the players and the decks, the mana,
			draws one card for each player and shuffles the Decks
			based on the seed
		2. processGameActions - parses the commands and handles
           them with specific handle methods.
	2. Table contains the table of the game, the hands, the decks and the cards on the table, 
	3. MagicNumbers contains the constants used throughout the project.
	4. CustomPrettyPrinter changes the way Json is printed in order to debug more easily.
	5. GenericCard contains the general characteristics of Minions and Heroes.
	6. GenericMinion contains the characteristics of Minions.
	7. Hero extends Minion and adds the specific health of a Hero: 30.
	8. One class for every special minion that overrides the specialAbility method.
### OOP Concepts used:
	1. Inheritance
	2. Polymorphism
	3. Encapsulation
	4. Composition
	5. Aggregation
	6. Packages
	7. Overriding
	8. Overloading
	9. Final Methods/Variables

## Ways to be improved
	1. Remove duplicate code by creating a new class that handles error output
	2. Add a class for Player,
	3. Move from the functionality of GameExecute to improve
   	    Single Responsability Principle


## Resources used:
	1. http://bit.ly/3V5E6hc - baeldung.com - Jackson
	2. https://ocw.cs.pub.ro/courses/poo-ca-cd
	3. https://refactoring.guru/design-patterns
	4. https://www.w3schools.com/java
	5. https://www.geeksforgeeks.org/
