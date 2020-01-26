package tFTHelper;

import java.util.Scanner;
import java.util.ArrayList;

import json_Converter.GoldEfficiency;
import json_Converter.Json_Converter;



public class TFT_Team {

	private static final int HAND_SIZE = 5;
	private static final int BENCH_SIZE = 8;
	private static final int ITEM_BENCH = 9;
	private static final int XP_PER_ROUND = 2;
	
	Scanner keyboard = new Scanner(System.in);
	
//==============================================================================================
	
	Object[] overallRegistry;
	BankChamp champRegistry;	
	BankItem itemRegistry;
	int itemNumber;

	
	ArrayList<GeneralChampion> boardChamps = new ArrayList<GeneralChampion>(9);			
	GeneralChampion [] deckChamps = new GeneralChampion[BENCH_SIZE]; //need to tweak size values
	GeneralChampion [] handChamps = new GeneralChampion[HAND_SIZE];
	
	ChampItemGeneral[] boardItems = new ChampItemGeneral[ITEM_BENCH];
	
	int[] expLevels= new int[11];
	
	int numOnDeck = 0,
			numInHand = 0,
			numOnBoard = 0,
			numItems = 0,
			
			gold = 0,
			experience = 0,
			winNum = 0,
			loseNum = 0;
	
	int teamLevel = 1,
			maxChamps = 1,
			roundNum = 1;
	
	int totalIncome = 0,
			pasIncome = 0,
			streak = 0,
			intr = 0;
	
	
	public TFT_Team() { //----------------------------------------------------------------------
		
		for (int i = 0; i < boardChamps.size(); i++) {			
			deckChamps[i] = null;
			handChamps[i] = null;			
		}
		
		overallRegistry =  Json_Converter.openFile();
		
		champRegistry = (BankChamp) overallRegistry[0];
		itemRegistry = (BankItem) overallRegistry[1];
		itemNumber = (int) overallRegistry[2];		
		
		addGold(4);
		pasIncome = 2;
		totalIncome = pasIncome + streak + intr;
		
		expLevels [0] = 0;//dont use lvl 0, start at 1.
		expLevels [1] = 0;
		expLevels [2] = 2;
		expLevels [3] = 4;
		expLevels [5] = 8;
		expLevels [6] = 12;
		expLevels [7] = 22;
		expLevels [8] = 34;
		expLevels [9] = 52;
		expLevels [10] = 68;	
		
	}
	
	public boolean buyChampTeam (String champBuyName, boolean firstTurn) { //-------------------
		
		BankChamp champBuy = checkChampReg(champBuyName,champRegistry);
		boolean validInput = false;
		int index = 0;
		
		if (champBuy == null) {
			return validInput;
		}
		
		if (firstTurn == false) {
			while (index < HAND_SIZE) {			
				if (champBuy.equals(handChamps[index])) {
					validInput = true;
					break;
				}			
				index++;
			}
		}
		
		if (validInput == false && firstTurn == false) {
			System.out.println("Sorry, " + champBuy.getName() + " is not currently in your shop.");
			return false;
		}
		
		validInput = false;
		
		String wherePut;
		
		while (validInput == false) {
		
			if(firstTurn == true)
				wherePut = "f";
			else {
				System.out.println("Onto Bench or onto Field?");
				System.out.println("(Type cancel or c to cancel.)");
				wherePut = keyboard.nextLine();
			}
			
			if (wherePut.equalsIgnoreCase("cancel") || wherePut.equalsIgnoreCase("c")) {
				System.out.println("Cancelled.");
				return false;
			}
			
			if (wherePut.equalsIgnoreCase("bench") || wherePut.equalsIgnoreCase("b")) {
				validInput = buyChampBench(champBuy);
				handChamps[index] = new BankChamp();
				handChamps[index].initializeEffHolder();				

			} else if (wherePut.equalsIgnoreCase("field") || wherePut.equalsIgnoreCase("f")) {
				validInput = buyChampField(champBuy);
				handChamps[index] = new BankChamp();
				handChamps[index].initializeEffHolder();
			}
		}
		
		return true;
		
	}
	
	private boolean buyChampBench(BankChamp toBench) {
		
		if (numOnDeck == deckChamps.length) {
			System.out.println("Bench is full, try again.\n");			
		} else {
			try { 
				deckChamps[numOnDeck] = (GeneralChampion)toBench.clone();
				deckChamps[numOnDeck].setGE(toBench.getChampGE());
				System.out.println(deckChamps[numOnDeck].getName() + " put on bench.");
				numOnDeck++;
				gold -= toBench.getValueChampion("cost");
				return true;
			} catch (CloneNotSupportedException e) {
				System.out.println("Error copying champion from registry (To bench).");
				return false;
			}			
		}	
		
		return false;
	}
	
	private boolean buyChampField(BankChamp toField) {
		
		/*System.out.println(numOnBoard + " on " + maxChamps + " max");
		System.out.println("Round num " + roundNum + " team level " + teamLevel);*/
		
		int indexAdd = boardChamps.size();
		
		if (numOnBoard == maxChamps) {
			System.out.println("Field is full, try again.\n");
		} else {
			
			for (int i = 0; i < boardChamps.size(); i++) {
				if (boardChamps.get(i) == null)
					indexAdd = i;
			}
			
			if(indexAdd == boardChamps.size()) //means the arraylist must grow is full but the
				boardChamps.add(new GeneralChampion()); //game still has room for another champ
			
			try {
				boardChamps.set(indexAdd,(GeneralChampion)toField.clone());
				boardChamps.get(indexAdd).setGE(toField.getChampGE());
				System.out.println(boardChamps.get(indexAdd).getName() + " put on field.");
				numOnBoard++;
				gold -= toField.getValueChampion("cost");
				return true;
			} catch (CloneNotSupportedException e) {
				System.out.println("Error copying champion from registry (To field).");
			}
		}		
		
		return false;
	}

	public boolean addChampBoard (String champBoardName) { //-----------------------------------
		
		if (numOnBoard == maxChamps) {
			System.out.println("Board is full, bench or sell champions");
			return false;
		}
		
		BankChamp champToBoard = checkChampReg(champBoardName, champRegistry);
		if (champToBoard == null) {
			System.out.println(champBoardName + " not found.");
			return false;
		}
		
		champBoardName = champToBoard.getName();
		
		for (int i = 0; i < BENCH_SIZE; i++) {
			try {				
				if (deckChamps[i].getName().equals(champBoardName)) {					
					if (numOnBoard == boardChamps.size()) { //if no null entries exist but board
						boardChamps.add(deckChamps[i]);		//isn't full (more than 9 champs on team)
					} else {					
						for (int j = 0; j < boardChamps.size(); j++) { //otherwise looks for a
							if (boardChamps.get(j) == null) {		   //blank entry and adds 
								boardChamps.set(j,deckChamps[i]);	   //champ there
							}
						}
					}					
					System.out.println(deckChamps[i].getName() + " added to board.");
				
					deckChamps[i] = null;
					adjustList(deckChamps);
					numOnBoard++;
					numOnDeck--;
					return true;				
				} 				
			} catch (NullPointerException e) {				
			}
		}
		
		System.out.println("Champ not found on bench, try again.");		
		return false;
	}
	
	public boolean newHand (boolean reroll) { //If true, rerolled, if false, round ended--------
		
		String roundResult;
		boolean validResponse = false;
		
		for (int i = 0; i < handChamps.length; i++) {
			handChamps[i] = null;
		}
		
		if (reroll == true) {			
			gold -= 2;			
		} else {
			
			if (roundNum < 5 && roundNum > 2)
				pasIncome++;
			
			while (validResponse != true) {
				System.out.println("Win or lose?");
				roundResult = keyboard.nextLine();
				
				if(roundResult.equalsIgnoreCase("w") || roundResult.equalsIgnoreCase("win")) {
					winNum++;
					loseNum = 0;
					gold += incomeCalc(true);
					validResponse = true;
				}
				
				if (roundResult.equalsIgnoreCase("l") || roundResult.equalsIgnoreCase("lose")) {
					loseNum++;
					winNum = 0;
					gold += incomeCalc(false);
					validResponse = true;
				}
			}
			
			addXP(XP_PER_ROUND);
			roundNum++;					
		}
		
		fillHand();
		
		return false;
	}
	
	public void fillHand() { //Adds a new set of champs to your hand----------------------------
		
		String champHandName;
		BankChamp champHandAdd;
		
		System.out.println("Please enter the champs in your new hand:");
		
		for (int i = 0; i < HAND_SIZE; i++) {
			champHandName = "";
			champHandAdd = null;
			
			while (champHandAdd == null) {
				System.out.println("Champ " + (i+1) + ".");
				champHandName = keyboard.nextLine();
				champHandAdd = checkChampReg(champHandName,champRegistry);
			}
			
			if (champHandAdd != null) {	
				handChamps[i] = champHandAdd;
				/*try { 
					handChamps[i] = (GeneralChampion)champHandAdd.clone();
				} catch (CloneNotSupportedException e) {
					System.out.println("Error copying champion from registry (To hand).\n");
				}*/
			}
		}
			
		//System.out.println(toStringHand());
		
		return;
	}
	
	public boolean removeChampBoard (String champRemoveName) { //Completed----------------------
		
		BankChamp champRemoveBoard = checkChampReg(champRemoveName, champRegistry);
		ArrayList<Integer> removeBoardIndexes = new ArrayList<Integer>();
		
		if (champRemoveBoard == null)
			return false;
		
		removeBoardIndexes = checkChampBoard(champRemoveBoard, removeBoardIndexes);
		
		if (removeBoardIndexes.size() == 0)
			return false;
		
		return whichChampBoard(removeBoardIndexes, champRemoveBoard);
		
	}	
	
	private boolean whichChampBoard(ArrayList<Integer> boardOptions, GeneralChampion champRemoveBoard) {
		
		String optionSelect = "";
		int optionInt;
		GeneralChampion current;
		
		if (boardOptions.size() == 1) {
			optionSelect = "1";
			
		} else {		
			System.out.println("Which one? (Only type the number)");
			for (int i = 0 ; i < boardOptions.size(); i++) {
				current = boardChamps.get(boardOptions.get(i));
				System.out.println(i + ": " + current.toString());
			}		
			optionSelect = keyboard.nextLine();		
		}
		
		while (optionSelect.equalsIgnoreCase("c") != true || optionSelect.equalsIgnoreCase("cancel") != true) {
			
			optionInt = Integer.parseInt(optionSelect);
			
			try {				
				for (int i = 0; i < deckChamps.length; i++) {
					if (deckChamps[i] == null) {
						deckChamps[i] = boardChamps.get(boardOptions.get(optionInt - 1));
						break;
					}
				}
				
				boardChamps.set(boardOptions.get(optionInt - 1),  null);
				adjustList(boardChamps);
				//boardChamps.get(boardOptions.get(optionInt - 1)).initializeEffHolder();
				
				numOnDeck++;
				numOnBoard--;
				return true;

			} catch (IndexOutOfBoundsException e) {	
				System.out.println("Option " + optionSelect + " not found, try again.\n(Type \"c\" or \"cancel\" to cancel)");
				optionSelect = keyboard.nextLine();
			} catch (NumberFormatException e) {
				//Would mean a letter is typed in, likely to cancel. Just do nothing?
			}
		}	
		
		System.out.println("Cancelled.");		
		return false;
	}
	
	public boolean sellChamp (String champSellName) { //Completed-------------------------------
		
		ArrayList<Integer> boardIndexes = new ArrayList<Integer>();
		ArrayList<Integer> deckIndexes = new ArrayList<Integer>();
		BankChamp champSell = checkChampReg(champSellName, champRegistry);

		
		if (champSell == null) //if champ name is not valid
			return false;
		
		boardIndexes = checkChampBoard(champSell, boardIndexes);
		
		deckIndexes = checkChampDeck(champSell, deckIndexes);
		
		if (boardIndexes.size() == 0 && deckIndexes.size() == 0) { //if champ is valid but not found
			System.out.println("You do not have " + champSell.getName() + "on your field or deck.");			
			return false;
		}
		
		if (boardIndexes.size() == 1 && deckIndexes.size() == 0) { //if exactly one instance, and is on board
			gold += boardChamps.get(boardIndexes.get(0)).getValueChampion("cost");
				
			
			boardChamps.set(boardIndexes.get(0),null);
			adjustList(boardChamps);			
			numOnBoard--;		
			
			/*boardChamps.set(boardIndexes.get(0),new GeneralChampion());
			boardChamps.get(boardIndexes.get(0)).initializeEffHolder();			
			numOnBoard--;*/
				
			System.out.println("Sold " + champSell.getName() + ".");
			return true;	
		}
			
		if (boardIndexes.size() == 0 && deckIndexes.size() == 1) { //if exactly one instance, and is on deck
			gold += deckChamps[deckIndexes.get(0)].getValueChampion("cost");
			
			deckChamps[deckIndexes.get(0)] = null;
			adjustList(boardChamps);
			numOnDeck--;			
			
			/*deckChamps[deckIndexes.get(0)] = new GeneralChampion();
			deckChamps[deckIndexes.get(0)].initializeEffHolder();
			numOnDeck--;*/
			
			System.out.println("Sold " + champSell.getName() + ".");		
			return true;
			
		}

		
		return (whichChampSell(boardIndexes,deckIndexes,champSell)); //if there is more than once instance
		
	}
	
	private ArrayList<Integer> checkChampBoard(BankChamp champFindBoard, ArrayList<Integer> boardIndexes) {
		
		System.out.println("Checking board for " + champFindBoard.getName() + " to sell");

		
		for (int i = 0; i < boardChamps.size(); i++) {
			try {
			
				if (boardChamps.get(i).getName().equals(champFindBoard.getName())){
					boardIndexes.add(i);
				}
			} catch (NullPointerException e) {
				
			}
		}		
		
		System.out.println(boardIndexes.size() + " found.");
		
		return boardIndexes;
	}
	
	private ArrayList<Integer> checkChampDeck(BankChamp champFindDeck, ArrayList<Integer> deckIndexes){
		
		System.out.println("Checking deck for " + champFindDeck.getName() + " to sell");
		
		for (int i = 0; i < deckChamps.length; i++) {
			try {
				//System.out.println(deckChamps[i].getName());
				
				if (deckChamps[i].getName().equals(champFindDeck.getName())){
				deckIndexes.add(i);
				}
			} catch (NullPointerException e) {
				
			}
		}		
		
		System.out.println(deckIndexes.size() + " found.");
		
		return deckIndexes;
	}
	
	private boolean whichChampSell(ArrayList<Integer> indexesBoard, ArrayList<Integer> indexesDeck, BankChamp champToSell) {
		
		ArrayList<GeneralChampion> champOptions = new ArrayList<GeneralChampion>();
		GeneralChampion currentChamp;
		String userInput = "";
		int optionSelect;
		
		for (int i = 0; i < indexesBoard.size(); i++) { //board champs first
			currentChamp = boardChamps.get(indexesBoard.get(i));
			champOptions.add(currentChamp);
		}
		
		currentChamp = null;
		
		for (int i = 0; i < indexesDeck.size(); i++) { //field champs next
			currentChamp = deckChamps[indexesDeck.get(i)];
			champOptions.add(currentChamp);
		}
		
		System.out.println("Which one? (Only type the number)");
		
		for (int i = 0; i < champOptions.size(); i++) {
			if (i < indexesBoard.size()) {
				System.out.println("Board--");
			} else {
				System.out.println("Bench--");
			}
			System.out.println("Option " + (i+1) + ": " + champOptions.get(i).toString());
		}
		
		userInput = keyboard.nextLine();
		
		while (userInput.equalsIgnoreCase("c") != true || userInput.equalsIgnoreCase("cancel") != true) {
			optionSelect = Integer.parseInt(userInput);
			
			try {
				if ((optionSelect - 1) > indexesBoard.size()) { //means it's on the deck
					optionSelect = optionSelect - indexesBoard.size();					
					currentChamp = deckChamps[indexesDeck.get(optionSelect - 1)];
					
					gold += currentChamp.getValueChampion("cost");
					deckChamps[indexesDeck.get(optionSelect - 1)] = null;
					adjustList(deckChamps);	
					numOnDeck--;
					
				} else { //means it's on the board
					currentChamp = boardChamps.get(indexesBoard.get(optionSelect - 1));
					
					gold += currentChamp.getValueChampion("cost");					
					boardChamps.set(indexesBoard.get(optionSelect - 1), null);
					adjustList(boardChamps);
					numOnBoard--;					
				}		
				
				System.out.println("Sold " + champToSell.getName());

				return true;

			} catch (IndexOutOfBoundsException e) {				
			} catch (NullPointerException e) {				
			}			
			System.out.println("Option " + userInput + " not found, try again.\n(Type \"c\" or \"cancel\" to cancel)");
			userInput = keyboard.nextLine();
		}		
		System.out.println("Sale cancelled");
		return false;		
	}
		
		/*for (int i = 0; i < compareChamps.size(); i++) {
			System.out.println((i + 1) + ": " + compareChamps.get(i).toString());
		}
		
		
		/*if (indexesBoard.size() == 0) { //if instances of champ to sell are only found on deck
			
			for(int i = 0; i < indexesDeck.size(); i++) { //gets all instances of that champion
				currentChamp = deckChamps[indexesDeck.get(i)];				
				compareChamps.add(currentChamp);
			}
			
			for (int i = 0; i < compareChamps.size(); i++) { //checks if they're all functionally the same
				currentChamp = compareChamps.get(i);		 //(have the same items or levels)
				
				for (int j = 0; j < compareChamps.size(); j++) {
					allEqual = currentChamp.equals(compareChamps.get(j));
					if (allEqual == false)
						break;
				}
				
				if (allEqual ==false)
					break;
			}
			
			if (allEqual == true) {				
				gold += deckChamps[indexesDeck.get(0)].getValueChampion("cost");
				
				deckChamps[indexesDeck.get(0)] = new GeneralChampion();
				deckChamps[indexesDeck.get(0)].initializeEffHolder();
				return true;
			}
			
			//If more than one option
			

			


		}
		
		if (indexesDeck.size() == 0) {
			
			return true;
		}
		
		//if there's 1(+) on deck and 1(+) on field
		
		return false;*/
	
	
	public void generateAdvice() {
		
		GeneralChampion bestPut = bestPutFinder();
		GeneralChampion worstTake = worstTakeFinder();
		GoldEfficiency bestPutGE, worstTakeGE;		
		
		if (worstTake == null) {
			System.out.println("\nPut " + bestPut.getName() + " on field.");
			System.out.println("------------------------------------------");
			return;
		}
		
		//System.out.println("\nComparing " + bestPut.getName() + " against " + worstTake.getName());
		
		bestPutGE = bestPut.getChampGE();
		worstTakeGE = worstTake.getChampGE();
		
		if (bestPutGE.getValueGE("overall") > worstTakeGE.getValueGE("overall")) {
			System.out.println("\nTake " + worstTake.getName() + " off and put " + bestPut.getName() +" on field.");
			System.out.println("------------------------------------------");
			return;
		}
		
		System.out.println("Current setup optimal");
		System.out.println("------------------------------------------");
		return;		
	}
	
	private GeneralChampion bestPutFinder() {
		
		GeneralChampion current;
		GoldEfficiency currentGE;
		GeneralChampion putOn = handChamps[0];
		GoldEfficiency putOnGE = putOn.getChampGE();
		
		for (int i = 0; i < deckChamps.length; i++) { //for figuring out which has best GE not on board
			if (i < handChamps.length) { //compares champs in store (hand)
				current = handChamps[i];
				currentGE = current.getChampGE();
				
				//System.out.println("Comparing " + putOn.getName() + " to " + current.getName() + " (in hand).");
				if (putOnGE.getValueGE("overall") < currentGE.getValueGE("overall")) {
					putOn = current;
					putOnGE = current.getChampGE();
				}
			}
			
			if (i < numOnDeck) { //compares champs on deck
				current = deckChamps[i];
				currentGE = current.getChampGE();
				
				//System.out.println("Comparing " + putOn.getName() + " to " + current.getName() + " (on deck).");
				if (putOnGE.getValueGE("overall") < currentGE.getValueGE("overall")) {
					putOn = current;
					putOnGE = current.getChampGE();
				}
			}			
		}
		
		return putOn;
	}
	
	private GeneralChampion worstTakeFinder() {
				
		int index = 0;
		GeneralChampion takeOff = null;
		
		GeneralChampion current;
		GoldEfficiency takeOffGE,currentGE;
		
		while(takeOff == null && index < boardChamps.size()) {
			takeOff = boardChamps.get(index);
			index++;
		}
		
		if (takeOff == null) 
			return takeOff;		


		for (int i = 0; i < boardChamps.size(); i++) {
			current = boardChamps.get(i);
			
			if (current != null) {
			
				takeOffGE = takeOff.getChampGE();
				currentGE = current.getChampGE();
			
				if (currentGE.getValueGE("overall") < takeOffGE.getValueGE("overall")) {
					takeOff = current;
					takeOffGE = currentGE;
				}
			}
		}
		
		return takeOff;
	}
	
	private int incomeCalc(boolean roundWin) { //Should be done----------------------------
		totalIncome = pasIncome;
		
		if (gold < 50) {
			totalIncome += ((int)(gold * 0.1));
		} else {
			totalIncome += 5;
		}
		
		if (winNum > 1 || loseNum > 1)
			totalIncome++;
		if (winNum > 3 || loseNum > 3)
			totalIncome++;
		if (winNum > 6 || loseNum > 6)
			totalIncome++;
		
		if (roundWin == true && roundNum > 3 && (roundNum - 3) % 6 != 0)
			totalIncome++; //if you win the round and it isn't a PVE round
		
		return totalIncome;
	}
	
	public void addXP(int xpVal) {
		
		if (xpVal == 2) {
			experience += 2;
		} else if (xpVal == 4 && gold > 3) {
			gold -= 4;
			experience += 4;
		} else {
			System.out.println("Not enough gold. Sell champs and try again.");
		}
		
		checkLevel();
		
	}
	
	private void checkLevel() {
		
		if (experience >= expLevels[teamLevel + 1])
			levelUp();		
	}
	
	public void levelUp() {
		
		teamLevel++;
		maxChamps++;		
		//want to include formula for champ probabilities
	}
	
	public String toString() {
		String output = toStringHand() + toStringBench() + toStringField();
		
		return output;
	}
	
	public String toStringHand() {
		String output = "\nCurrent Hand:\n";
		
		for (int i = 0; i < handChamps.length; i++) {
			output = output.concat((i+1) + ": " + handChamps[i].getName() + "\n");
		}		
		
		return output;
	}
	
	public String toStringBench() {
		String output = "\nCurrent Bench:\n";
		
		for (int i = 0; i < deckChamps.length; i++) {
			if (deckChamps[i] != null)
			output = output.concat((i+1) + ": " + deckChamps[i].getName() + "\n");
		}
		
		return output;
	}
	
	public String toStringField() {
		String output = "\nCurrent Field:\n";
		
		for (int i = 0; i < boardChamps.size(); i++) {
			if (boardChamps.get(i) != null && boardChamps.get(i).getName().equals(null) != true) {
				output = output.concat((i+1) + ": " + boardChamps.get(i).getName() + "\n");
			}				
		}	
		
		return output;
	}
	
	public String toStringRecursive(BankChamp root) { //Prints out contents of registry in alphabetical order
		
		if (root.right != null && root.left != null) //If node has two children, follow left, print root, then follow right
			return toStringRecursive(root.left) + root.toStringComplex() + toStringRecursive(root.right);		
		
		if (root.left == null && root.right == null) //If node has no children, return the root data
			return root.toStringComplex();
		
		if (root.right == null) //If node has a left but no right, follow down left
			return  toStringRecursive(root.left) +  root.toStringComplex();		
		
		if (root.left == null) // Same as above, but if left is empty and right has data			
			return root.toStringComplex() + toStringRecursive(root.right);
		
				
		else return root.toStringComplex(); // Base case, just in case.	
	}
	
	private BankChamp checkChampReg(String champName, BankChamp root) {//-----------------------

		champName = nameLength(champName);
		
		
		if (root == null) {
			System.out.println("Error: champ registry not found. Try updating TFT Helper.");
		}
		
		if (champName.substring(0,3).equalsIgnoreCase("mal")) {
			if (champName.length() < 4)
				champName = champName.concat(" ");
			if (root.getName().toLowerCase().contains(champName.substring(0,4).toLowerCase())) { //found champ
				return root; //special case for Malzahar and Malphite
			}
		} else 
		
		if (root.getName().toLowerCase().contains(champName.substring(0,3).toLowerCase())) { //found champ
			return root;
		}
		
		if (champName.compareToIgnoreCase(root.getName()) < 0 && root.left != null) { //alphabetically before
			return(checkChampReg(champName, root.left));
		}
		
		if (champName.compareToIgnoreCase(root.getName()) > 0 && root.right != null) { //alphabetically after
			return(checkChampReg(champName, root.right));
		}
		
		System.out.println("Champion \"" + champName + "\" not found. Please try again.");
		
		
		return null;
	}
	
	public BankItem checkItemReg(String itemName) {//-------------------------------------------
		BankItem root = itemRegistry;
		int regSize = itemNumber;
		itemName = nameLength(itemName);
		
		if (root == null)
			System.out.println("Error: item registry not found. Try updating TFT Helper.");
		
		for (int i = 0; i < regSize + 1; i++) {
			if (root.toString().toLowerCase().contains(itemName.substring(0,3).toLowerCase())) {
				return root;
			}
			root = root.right;
		}
		
		System.out.println(itemName + " not found.\n");
		return null;
	}
	
	public void addGold(int goldAmt) {
		gold += goldAmt;
	}
	
	private void adjustList(ArrayList<GeneralChampion> adjust) {
		
		int size = adjust.size();
		
		for (int i = 0; i < size - 1; i++) {
			if (adjust.get(i) == null) {
				adjust.set(i, adjust.get(i+1));
				adjust.set(i +1, null);
			}
		}		
	}
	
	private void adjustList(GeneralChampion[] adjust) {
		
		for (int i = 0; i < adjust.length - 1; i++) {			
			if (adjust[i] == null) {
				adjust[i] = adjust [i+1];
				adjust[i+1] = null;
			}			
		}		
	}
	
	private String nameLength(String name) { //Adjusts name to be at least 3 chars--------------
		while (name.length() < 3) {
			name = name.concat(" ");
		}
		
		return name;
	}
}
