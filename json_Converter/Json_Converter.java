package json_Converter;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import javax.net.ssl.HttpsURLConnection;

import tFTHelper.*;

public class Json_Converter {	
	
	
	private static final String CONVERT_LOC = "C:\\Users\\thema\\Documents\\TFT Helper\\Output Objects\\TFT Objects.txt";
	private static final String JSON_LOC = "C:\\Users\\thema\\Documents\\TFT Helper\\JSON Downloads\\en_us.json";
	
	private static final String VERS_CHK = "https://raw.communitydragon.org/latest/cdragon/tft/";
	private static final String DL_LOC = "https://raw.communitydragon.org/latest/cdragon/tft/en_us.json";
	
	static Object[] fullRegistry;
	static BankChamp champPoolRoot = null;
	static BankItem itemPool = null;
	static GoldEfficiency effHolder = null;
	static int champPoolSize,itemPoolSize = 0;	

	static String version;
	
	public static void main (String[] args) {
		
		String currentVersionFile = getVersionFile();
		String currentVersionServer = checkVersionServer();
		
		
		if (currentVersionFile.equals(currentVersionServer) != true || currentVersionFile.equals("")) {
		
		downloadUpdate();
		createRegistry();
		saveToFile(currentVersionServer);
		
		System.out.println("Sucessfully updated!\n");
		
		} else {
			System.out.println("TFT Helper up-to-date!\n");
		}
		
		fullRegistry = openFile();
		
		champPoolRoot = (BankChamp) fullRegistry[0];
		itemPool = (BankItem) fullRegistry[1];
		itemPoolSize = (int) fullRegistry[2];
		
		
		//System.out.println(toStringRecursive(champPoolRoot));
		//System.out.println(toStringRecursiveComplex(champPoolRoot));
		//System.out.println(champPoolSize);
		
		/*BankItem current = itemPool;
		
		while (current.right != null) {
			System.out.println(current.toStringComplex());
			System.out.println("--------------------------");
			current = current.right;
		}
		
		System.out.println(current.toStringComplex());
		System.out.println("--------------------------");
		
		System.out.println(itemPoolSize);*/
		
		//System.out.println(effHolder.toString());
		
		
		/*try {
			File delete = new File(CONVERT_LOC); //to automatically delete file, used during development
			delete.delete();
		} catch (IOError e) {
			System.out.println("Error cleaning up file");
		}*/

	}
	
	private static String getVersionFile() {
		
		String versionDate = "";
		String filePath = CONVERT_LOC;
		
		try {			
			FileInputStream fileIn = new FileInputStream(filePath);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);
			versionDate = (String) objectIn.readObject();
			objectIn.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("No existing registry found, updating...");
		} catch (IOException e) {
			System.out.println("Error reading file, updating...");
		} catch(ClassNotFoundException e) {
			System.out.println("Class not found error, updating...");
		} catch (ClassCastException e){
			System.out.println("Class cast exception, updating...");
		}

		return versionDate;
	}
	
	private static String checkVersionServer() {
		
		String websiteURL = VERS_CHK;
		String currentLine;
		String fileName = "en_us.json";

		
		try {
			
			URL updateLoc = new URL(websiteURL);
			HttpsURLConnection connect = (HttpsURLConnection) updateLoc.openConnection();
			connect.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0");
			InputStream in = connect.getInputStream();
			InputStreamReader reader = new InputStreamReader(in);
			BufferedReader bufIn = new BufferedReader(reader);
						
			do {
				currentLine = bufIn.readLine();
				
				if (currentLine.contains(fileName)) {					
						return currentLine.substring(87,104);									
				}
				
			} while (currentLine != null);  

		} catch (MalformedURLException e) {
			System.out.println("Error connecting to update server (checking version).");
			return "";
		} catch (IOException e) {
			System.out.println("IO Error.");
		}
		
		return "";		
	}

	private static void downloadUpdate() {
				
		String websiteText = "";
		String currentLine;
		
		try {			
			URL updateLoc = new URL(DL_LOC);
			HttpsURLConnection connect = (HttpsURLConnection) updateLoc.openConnection();
			connect.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0");
			InputStream in = connect.getInputStream();
			InputStreamReader reader = new InputStreamReader(in);
			BufferedReader bufIn = new BufferedReader(reader);
						
			do {
				currentLine = bufIn.readLine();
			    websiteText = websiteText.concat(currentLine + "\n");			    			    
				} while (currentLine != null);  
			
			FileOutputStream fileOut = new FileOutputStream(JSON_LOC);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			
			objectOut.writeObject(websiteText);
			
			objectOut.close();

			} catch (MalformedURLException e) {
					System.out.println("Error connecting to update server (downloading update).");
			} catch (IOException e) {
					System.out.println("IO Error, please try again.");
		}		
	}
	
	private static void createRegistry (){
		
		try {
		BufferedReader input = new BufferedReader(new FileReader(JSON_LOC));		
		lineReader(input);
		//System.out.println("\nArraylist Created.");		
		input.close();		
		}
		catch (IOException e){
			System.out.println("Error opening file");
			return;
		}
		
		champEffCalc(champPoolRoot,effHolder);
		
	}
	
	private static String toStringRecursive(BankChamp root) { //==Prints out contents of registry in alphabetical order==
		
		if (root.right != null && root.left != null) //If node has two children, follow left, print root, then follow right
			return toStringRecursive(root.left) + root.toString() + toStringRecursive(root.right);		
		
		if (root.left == null && root.right == null) //If node has no children, return the root data
			return root.toString();
		
		if (root.right == null) //If node has a left but no right, follow down left
			return  toStringRecursive(root.left) +  root.toString();		
		
		if (root.left == null) // Same as above, but if left is empty and right has data			
			return root.toString() + toStringRecursive(root.right);
		
				
		else return root.toString(); // Base case, just in case.	
	}
	
	private static String toStringRecursiveComplex(BankChamp root) { //==Prints out contents of registry in alphabetical order==
		
		if (root.right != null && root.left != null) //If node has two children, follow left, print root, then follow right
			return toStringRecursiveComplex(root.left) + root.toStringComplex() + toStringRecursiveComplex(root.right);		
		
		if (root.left == null && root.right == null) //If node has no children, return the root data
			return root.toStringComplex();
		
		if (root.right == null) //If node has a left but no right, follow down left
			return  toStringRecursiveComplex(root.left) +  root.toStringComplex();		
		
		if (root.left == null) // Same as above, but if left is empty and right has data			
			return root.toStringComplex() + toStringRecursiveComplex(root.right);
		
				
		else return root.toStringComplex(); // Base case, just in case.	
	}
	
	private static void saveToFile(String versionDate) {//------------------------------------------------------------------------------
		
		String filePath = CONVERT_LOC;
		File outputFile = new File(filePath);
		outputFile.delete();
		
		try {
		FileOutputStream fileOut = new FileOutputStream(filePath);
		ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
		
		objectOut.writeObject(versionDate);
		
		objectOut.writeObject(champPoolRoot);
		System.out.println("Champions succesfully saved.");
		
		objectOut.writeObject(itemPool);		
		System.out.println("Items succesfully saved.");
		
		objectOut.writeObject(itemPoolSize);
		
		objectOut.close();
		
		}
		catch (IOException e) {
			System.out.println("Error saving to file");
		}
		
	}
	
	public static Object[] openFile() {
		
		Object[] output= new Object[3];
		
		String filePath = CONVERT_LOC;
		//BankChamp bankChamps = null;
		
		try {		
		FileInputStream fileIn = new FileInputStream(filePath);
		ObjectInputStream objectIn = new ObjectInputStream(fileIn);
		
		objectIn.readObject();
		output[0] = (BankChamp) objectIn.readObject();
		output[1] =  (BankItem) objectIn.readObject();
		output[2] =  (Integer) objectIn.readObject();
		//System.out.println("Champs succesfully loaded.\n");		
		objectIn.close();
		}
		catch (IOException e) {
			System.out.println("Error reading file");
		}
		catch(ClassNotFoundException e) {
			System.out.println("Error casting file type (Champions)");
		}	
		
		return output;		
	}
	
	/*public static BankItem openItemFile() {
		
		String filePath = CONVERT_LOC;
		BankItem bankItems = null;
		
		try {		
		FileInputStream fileIn = new FileInputStream(filePath);
		ObjectInputStream objectIn = new ObjectInputStream(fileIn);
		
		//champPoolRoot = (ArrayList<GeneralChampion>) objectIn.readObject();
		objectIn.readObject(); //gets rid of version date
		objectIn.readObject(); //gets rid of champs before items

		bankItems = (BankItem) objectIn.readObject();		
		//System.out.println("Items succesfully loaded.\n");
		objectIn.close();	
		
		}
		catch (IOException e) {
			System.out.println("Error reading file");
		}
		catch(ClassNotFoundException e) {
			System.out.println("Error casting file type (Items)");
		}
		
		return bankItems;		
	}*/
	
	private static void lineReader(BufferedReader jsonFile) {
		
		try {
			jsonFile = itemRegistry(jsonFile);
			jsonFile = champRegistry(jsonFile);
			//jsonFile = TraitsRegistry(jsonFile);
			
		} catch (IOException e) {
			System.out.println("Error reading file");
		}
		
		return;
	}
	
	private static BufferedReader champRegistry(BufferedReader fileToRead) throws IOException {
		
		String currentLine = "";
		BankChamp champToRegister;
		effHolder = new GoldEfficiency();
		
		int index1,index2,index3;
		String champValue;
		double statAmount;
		
		while (currentLine.equals("        \"2\": {") != true) {
			currentLine = fileToRead.readLine();
		}	
		
		do {
			currentLine = fileToRead.readLine();				
			if (currentLine.contains("                    \"cost\":")) { //Indicates a champion to add
				champToRegister = new BankChamp();					
				while(currentLine.equals("                    \"traits\": [") != true) {
					try {						
						index1 = charFinder('"',1,currentLine) + 1;
						index2 = charFinder('"',2,currentLine);
						index3 = charFinder(':',1,currentLine) + 2;
						
						if (currentLine.contains("name")) {
							champToRegister.setValue("name", currentLine.substring(index3 + 1, currentLine.length() - 2));
						} else {
							champValue = currentLine.substring(index1,index2);
							statAmount = Double.parseDouble(currentLine.substring(index3,currentLine.length() - 1));
							champToRegister.addValueChampion(champValue,statAmount);
							
							makeGoldEff(champToRegister,champValue,statAmount,effHolder);
						}							
					} catch (NumberFormatException e) {							
					} catch (StringIndexOutOfBoundsException e) {							
					} finally {
					currentLine = fileToRead.readLine();
					}
				}					
				champToRegister.dpsCalcs();
				champPoolRoot = addToRegistry(champToRegister,champPoolRoot);
				champPoolSize++;				
				}		
			} while (currentLine.equals("            \"name\": \"War of the Elements\",") != true);		
		return fileToRead;
	}
	
	
	private static void makeGoldEff (BankChamp champ, String stat, double amount, GoldEfficiency goldEff) {
		int goldCost = (int) champ.getValueChampion("cost");
		double valueToAdd;
		double statEff,currentMost;
		
		if (goldCost == 0 || stat.equals("cost"))
			return;
		
		currentMost = goldEff.getValueChampion(stat);
		
		if(stat.equals("mana")) {
			if (amount == 0) {
				return;
			} else {				
				statEff = goldCost/amount; //want lowest mana and highest goldcost

			}
		} else {		
		statEff = amount/goldCost; //otherwise want highest stats for lowest cost		
		}
		
		if (statEff > currentMost) {
			if (stat.equals("mana")) {
				goldEff.setMaxMana(statEff);
			} else {
			valueToAdd = statEff - currentMost;
			goldEff.addValueChampion(stat, valueToAdd);			
			}
			
			goldEff.addBest(champ, stat);
		}		
				
	}
	
	private static BankChamp addToRegistry (BankChamp add, BankChamp root) { // Adds champions to binary tree in alphabetical order
		
		if (root == null) { //Adds to root
			root = add;			

		} else if (add.getName().compareToIgnoreCase(root.getName()) < 0) { //adds to left
			if (root.left == null) {
				root.left = add;
				//System.out.println("Added to left");
				return root;
				
			} else { //goes down left
				addToRegistry (add, root.left);
			}			
			
		} else if (add.getName().compareToIgnoreCase(root.getName()) > 0) { //adds to right
			if (root.right == null) {
				root.right = add;
				return root;
				
			}  else { //goes down right
				addToRegistry (add, root.right);
			}			
		}
		
		return root;		
	}
	
	private static BufferedReader itemRegistry(BufferedReader fileToRead) throws IOException {
		
		String currentLine = "";
		String value = "";
		double amount = 0;
		
		BankItem itemToRegister;
	
			do {
				currentLine = fileToRead.readLine();
				
				if (currentLine.contains("\"desc\": \"+@") || currentLine.contains("\"desc\": \" +@") ){ //Indicates a base item to add
					value = currentLine.substring(
							(charFinder('@',1,currentLine) + 1),charFinder('@',2,currentLine));
					fileToRead.readLine();
					currentLine = fileToRead.readLine();
					amount = Double.parseDouble(currentLine.substring
							((charFinder(':',1,currentLine) + 1),(currentLine.length() - 1)));
					
					//System.out.println(value);
					//System.out.println(amount);
					
					itemToRegister = new BankItem();					
					itemToRegister.addValueItem(value, amount);
					
					
					while (currentLine.contains("            \"name\": \"") != true) {
						currentLine = fileToRead.readLine();
					}
					
					itemToRegister.setValue("name", currentLine.substring(
							charFinder('"',3,currentLine) + 1, currentLine.length() - 1 ));					
					itemAdder(itemToRegister);
					itemPoolSize++;
					
				}
		
			} while (currentLine.equals("    \"sets\": {") != true);
		
		
		return fileToRead;
		
	}
	
	private static void itemAdder(BankItem itemToAdd) {
		BankItem current = itemPool;
		BankItem temp;
		boolean added = false;
		
		//Checks if item pool empty
		if (itemPoolSize == 0) {
			itemPool = itemToAdd;
			return;				
		}
		
		if (itemToAdd.toString().compareToIgnoreCase(current.toString()) < 0 &&
				itemToAdd.toString().equals(current.toString()) != true) { //checks if alphabetically first
			itemPool.left = itemToAdd;
			itemToAdd.right = itemPool;
			itemPool = itemToAdd;			
			return;
		}		
		
		while (added == false) {
			if(itemToAdd.toString().equals(current.toString())) {//if same name...		
				replaceItem(current, itemToAdd);
				return;

			} else if (current.right == null) { //if no more entries exist...
				current.right = itemToAdd; //add to end
				added = true;
				
			} else if (itemToAdd.toString().compareToIgnoreCase(current.right.toString()) < 0) { //if adding comes before next...
				temp = current.right;	   //
				current.right = itemToAdd; //
				itemToAdd.left = current;  //insert
				itemToAdd.right = temp;	   //
				added = true;			   //
				
			} else { //move on to next in list...
				temp = null;
				current = current.right;
			}
		}		
	}
	
	private static void replaceItem(BankItem current, BankItem itemAdd) {
		
		if (current.left == null) { //if first in sequence
			itemAdd.right = current.right;
			current.right.left = itemAdd;
			itemPool = itemAdd;
		}
		
		if (current.left != null) {
			itemAdd.left = current.left;
			current.left.right = itemAdd;
		}		
												//If last instance is desired, un-comment
		if (current.right != null){
			itemAdd.right = current.right;
			current.right.left = itemAdd;
		}
		
		return;
		
	}
	
	private static void	champEffCalc(BankChamp root, GoldEfficiency holder) {
		//Done after champ reg is made and most efficient values for stats found		
		
		String stat;
		String oe = "overall";
		double effAmount,thisAmount,calcEff;
		
		root.initializeEffHolder();
		GoldEfficiency currentChampGE = root.getChampGE(); //Gets current champ's Gold Eff
		
		for (int i = 0; i < holder.whichStats.size(); i++) { //Goes through current best stats list,
			stat = holder.whichStats.get(i);				 //& compares current champ stats.
			effAmount = holder.getValueChampion(stat); //Max
			thisAmount = root.getValueChampion(stat);  //This champ's value
			
			if (stat.equals("mana")) { // Want less mana
				if(thisAmount != 0) {
					thisAmount = root.getValueChampion("cost") / thisAmount;
				}				
				thisAmount = thisAmount * -1;
			}
						
			calcEff = thisAmount/effAmount;	
			//System.out.println(root.getName() + " " + stat + " " + calcEff);
			currentChampGE.addValueChampion(stat,calcEff); //Puts value in current champ's gold efficiency holder
		}		
		
		currentChampGE.calcTotal(holder);	 //Sums up total gold value of current champ's stats.	
		BankChamp bestOverall = holder.getChampBest(oe);	//Gets current best overall stat champion in registry
		
		if(bestOverall == null) {	
			holder.addBest(root, oe); //If there is none, adds current champ to it.
			holder.setTotalEff(currentChampGE.getValueGE(oe));
		} else {
			GoldEfficiency bestOverallGE = bestOverall.getChampGE(); 
			if (currentChampGE.getValueGE(oe) > bestOverallGE.getValueGE(oe)) {
				holder.addBest(root, oe);
				holder.setTotalEff(currentChampGE.getValueGE(oe));
				//System.out.println(root.getName() + " new best overall----------------------------------------------------------");
			}
		}
		
		//System.out.println(currentChampGE.thisChampEffToString(holder));
		
		if (root.right != null) //Goes through rest of registry and repeats
			champEffCalc(root.right,holder);
		if (root.left != null)
			champEffCalc(root.left,holder);
		
		return;		
	}
	
	private static int charFinder(char target, int goalNumber, String lineRead) throws StringIndexOutOfBoundsException { //finds the Nth incidence of a character from a line
		
		int charCount = 0;
		int index = 0;
		char currChar;
		
		while (charCount < goalNumber) {
			currChar = lineRead.charAt(index);
			
			if (currChar == target) {
				charCount++;
				if (charCount == goalNumber)
					return index;
			}			
			
			index++;
		}
		
		return index;
	}
	
}
