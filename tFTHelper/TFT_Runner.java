package tFTHelper;

import java.util.Scanner;
import json_Converter.*;

public class TFT_Runner {
	
	private static Scanner keyboard = new Scanner(System.in);
	private static TFT_Team currentTeam;
	
//===========================================================================================================
	
	public static void main (String[] args){  //notFullyImplemented
		
		Json_Converter.main(args);
		boolean playAnother = true;
		
		System.out.println("Welcome to TFT Helper - Good luck in Soloqueue!");
		System.out.println("-----------------------------------------------");
		System.out.println("When entering champ names, put at least the first 3 characters");
		
		while (playAnother == true) {
			
			currentTeam = new TFT_Team();
			gameStart(); //Adds starting champ + item to board
			currentTeam.fillHand();
			playAnother = optionSelect(); //Method for adding/removing items & champs, and for ending games.
										  //If true returned, user will play another game.
			
		}
		
		System.out.println("\nA good time to take a break. See you next time!");
		
		System.out.println(currentTeam.toStringRecursive(currentTeam.champRegistry));
	}
	
	private static void gameStart() { //method complete. Adds starting champ and item to board.
		
		String userInput = "";
		String startChamp;
		boolean validInput = false;
		
		
		while (validInput == false) {
			System.out.println("What champion did you pick first?");
			userInput = keyboard.nextLine();
			if (userInput.equalsIgnoreCase("help")) {
				help();
			} else {			
				validInput = currentTeam.buyChampTeam(userInput,true);
			}
		}		

		currentTeam.addGold(1);
		currentTeam.addXP(2);
		//currentTeam.addChampBoard(currentTeam.boardChamps.get(0).getChampName());
		actionLog("Started with" + toString(userInput));
		
		//addItem();
		
		
		/*startChamp = userInput;
		userInput = "help";

		while (userInput.equalsIgnoreCase("help")) { 
			
			System.out.println("What item did they have?");
			userInput = keyboard.nextLine();
			if (userInput.equalsIgnoreCase("help")) {
				help();
			}
		}
		
		addItemChamp(userInput,startChamp); //need to add checks for bad entries in method*/
		
	}
	
	private static boolean optionSelect () {  //Should be complete-------------------------------------------
		
		System.out.println("Available commands:");
		help();	
		
		System.out.println(currentTeam.toString());
		System.out.println("What's the first move, captain?");		

		currentTeam.generateAdvice();
		
		String userInput = keyboard.nextLine();
		boolean keepGoing = true;
				
		while (keepGoing == true) {	
		
			switch (userInput.toLowerCase()) {
			
				case "a":
				case"advice":
					currentTeam.generateAdvice();
					break;
			
				case "b":
				case "buy":					
					buyChamp();
					break;
					
				case "bo":
				case "board":
					boardChamp();
					break;
					
				case "be":
				case "bench":
					benchChamp();
					break;
					
				case "s":
				case "sell":
					sellChamp();
					break;
				
				case "i":
				case "item":
					addItem();
				
				case "n":
				case "next":
				case "next round":
					currentTeam.newHand(false);
					break;
					
				case "rr":
				case "re":
				case "reroll":
					currentTeam.newHand(true);
					break;
				
				case "xp":
				case "experience":
					currentTeam.addXP(4);
					break;
				
				case "won game":
				case "lost game":
				case "lose game":
				case "ragequit":
					return(gameResult(userInput));
					
				case "l":
					currentTeam.levelUp();
									
				case "help":
					help();
				break;
				
				default:
					System.out.println("Sorry, \"" + userInput + "\" is not a valid command. Please try again.\n");				
				break;					
			
			}
			
			actionLog(toString(userInput));
			//optimalMoveCalc();			
			
			System.out.println(currentTeam.toString());
			System.out.println("Gold = " + currentTeam.gold + ", " + 
					"level: " + currentTeam.teamLevel + ", max on board: " + 
					currentTeam.maxChamps + ", current on board: " + 
					currentTeam.numOnBoard); /////////////////////////////////////THING LEFT OFF ON
			//System.out.println(currentTeam.toStringBench());
			//System.out.println(currentTeam.toStringField());
			
			if(userInput.equalsIgnoreCase("a") != true || userInput.equalsIgnoreCase("advice") != true)
				currentTeam.generateAdvice();
			
			System.out.println("Alright, what's next?");
			userInput = keyboard.nextLine();
			
		}
		
		 return false;
	}

	
	private static void buyChamp() {  //-complete-------------------------------------------
		
		String champBuy;
		
		System.out.println("Buy which champ?");
		champBuy = keyboard.nextLine();
		currentTeam.buyChampTeam(champBuy, false);
		actionLog("Bought " + toString(champBuy) + ".");
		
		return;
		
	}
	
	private static void boardChamp() {  //--complete-----------------------------------------
				
		String champBoard;

		System.out.println("Put which champ on board?");
		champBoard = keyboard.nextLine();
		if(currentTeam.addChampBoard(champBoard) == true) {
			actionLog("Put " + toString(champBoard) + " on board.");
			System.out.println(champBoard + " added to board.");
		} else {
			System.out.println("Sorry, " + champBoard + " not found. Try again.\n");
		}
				
		return;
		
	}
	
	private static void benchChamp() {  //--complete, but not in object-----------------------
		
		if (currentTeam.numOnDeck == currentTeam.deckChamps.length) {
			System.out.println("Sorry, bench is full. Sell or put champions on your field and try again.");
			return;
		}	
		
		String champBench;		
		
		System.out.println("Bench which champ?");
		champBench = keyboard.nextLine();
		if(currentTeam.removeChampBoard(champBench) == true) {
			actionLog("Put " + toString(champBench) + " on bench.");
			System.out.println(champBench + " benched.\n");
		} else {
			System.out.println("Sorry, " + champBench + " not found. Try again.\n");
		}
		
		return;
	}
	
	private static void sellChamp() { //need to figure out how to specify, champ lvl & 
		//items holding, probably specify if there are more than 1 of champ existing in team
		//notYetImplemented in object
		
		String champSell;
		boolean sellComplete;
		
		System.out.println("Sell which champ?");
		champSell = keyboard.nextLine();
		sellComplete = currentTeam.sellChamp(champSell);
		
		if(sellComplete == false) {
			System.out.println("Sale cancelled.");
		} else if(sellComplete == true) {
			actionLog("Sold" + toString(champSell) + ".");
			//System.out.println(champSell + " sold.\n");
		}
		
		return;
	}
	
	private static void addItem() {
		String itemName = "";
		BaseItem addItem = null;
		
		while (addItem == null) {
			if (itemName.equalsIgnoreCase("cancel") || itemName.equalsIgnoreCase("c")) {
				System.out.println("Cancelled.\n");
				return;
			}
			
			System.out.println("Which item?");
			itemName = keyboard.nextLine();
			addItem = currentTeam.checkItemReg(itemName);		
		}
		

		System.out.println("To a champion or to item deck?");
		itemName = keyboard.nextLine();
		itemName = itemName.toLowerCase();
		
		switch (itemName) {
			case "c":
			case "cancel":
				System.out.println("Cancelled.\n");
				return;
				
			case "champ":
			case "champion":
				
				break;
				
			case "d":
			case "i":
			case "deck":
			case "item deck":
			
				break;
		}
		
		
	}
	
	private static boolean addItemDeck (String itemName) {
		return false;
	}
	
	private static boolean addItemChamp (String itemAdd, String toChamp) {  //notYetImplemented---------------------
		
		return false;
	}
	
	private static void help () { //---------------------------------------------------------------------------
		
		System.out.println("\n(a) or (advice) for help finding optimal champions on your board.");
		System.out.println("\n(b) or (buy) to buy a new champion.");
		System.out.println("(bo) or (board) to place a champion on the board.");
		System.out.println("(be) or (bench) to remove a champion from the board and put on your bench.");
		System.out.println("(s) or (sell) to sell a champion.");

		System.out.println("\n(i) or (item) to pick up an item.\n");
		
		System.out.println("(xp) or (experience) to add XP");
		System.out.println("(n) or (next) to go to next round.");
		System.out.println("(rr) or (re) or (reroll) to reroll hand.");
		System.out.println("(won game), (lost game), or (ragequit) to end game.");
		
		System.out.println("\nType (c) or (cancel) to cancel additions/removals.\n");

		System.out.println("(help) to get the help menu/commands list.");
		System.out.println("\nSome options have sub-commands, they should be listed when relevant.");

		System.out.println("When entering champ names, put at least the first 3 characters\n");
		
		return;
		
	}
	
	private static boolean gameResult(String endCondition) {//-------------------------------------------------
		
		String anotherRound;
		boolean loop = true;
		
		
		actionLog(toString(endCondition));
		System.out.println("Play another? (y/n or yes/no)");
		anotherRound = keyboard.nextLine();		
		
		while (loop = true) {
			
			if (anotherRound.equalsIgnoreCase("yes") || anotherRound.equalsIgnoreCase("y")) {
				System.out.println("Board succesfully reset!\n");
				actionLog(toString(anotherRound));

				return true;
			} else if (anotherRound.equalsIgnoreCase("no") || anotherRound.equalsIgnoreCase("n")) {
				actionLog(toString(anotherRound));

				return false;
			} else {
				System.out.println("Sorry, invalid command, please try again.");
				anotherRound = keyboard.nextLine();
			}
			
		}
		
		return false;
	}	

	
	private static String toString(String input) {  //notYetImplemented----------------------------------------
		
		
		return "";
	}
	
	private static void actionLog(String currentAction) {  //notYetImplemented, will output to .txt file-------
		
		
		return;
	}
	
	private static void optimalMoveCalc() { //-----------------------------------------------------------------
		
	}
	
}
	

