package json_Converter;

import tFTHelper.*;
import java.util.ArrayList;

public class GoldEfficiency extends GeneralChampion {

	//ArrayList<ArrayList<GeneralChampion>> bigBoi = new ArrayList<ArrayList<GeneralChampion>>();
	
	ArrayList<BankChamp> bestChamps = new ArrayList<BankChamp>();
	ArrayList<String> whichStats = new ArrayList<String>();
	
	public double totalEff = 0;
	
	
	public GoldEfficiency() {
		
	}
	
	public void addBest(BankChamp champ, String stat) { //adds best champ for each stat
		if (bestChamps.size() == 0) {
			bestChamps.add(champ);
			whichStats.add(stat);
			return;
		}

		for (int i = 0; i < bestChamps.size(); i++) {
			if (whichStats.get(i).equals(stat)) { //if there already exists an entry for the best stat,
				bestChamps.set(i, champ);		  //replace that stat.
				return;
			}
		}
		
		bestChamps.add(champ);
		whichStats.add(stat);
	}
	
	public BankChamp getChampBest (String statistic) { //returns which champ has highest stat
		
		for (int i = 0; i < whichStats.size(); i++) {
			if (whichStats.get(i).equals(statistic))
				return bestChamps.get(i);
		}
		
		return null;
	}
	
	public double getValueGE(String stat) {
		
		double value = getValueChampion(stat);
		
		if (value != -1) {
			return value;
		}
		
		switch (stat) {
		case "overall":
			return totalEff;
		}
		
		return -1;
		
	}
	
	
	/*public double getSpecificStat(GoldEfficiency reg, String stat) {
		
		for (int i = 0; i < reg.whichStats.size(); i++) {
			if (reg.whichStats.get(i).equals(stat)) {
				return 
			}
				
		}
		
		return 0;
	}*/

	public void calcTotal(GoldEfficiency calcReg) { //calculates overall GE for specific champion
		for (int i = 0; i < calcReg.whichStats.size(); i++) {
			totalEff += getValueChampion(calcReg.whichStats.get(i));
		}
	}
	
	public void setTotalEff(double total) {
		totalEff = total;
	}
	
	public String toString() { //Overall best champions for each stat
		//System.out.println(bestChamps.size() + "_" + whichStats.size());
		
		String output = "";
		GeneralChampion current;
		String currentStat;
		
		for (int i = 0; i < bestChamps.size(); i++) {
			current = bestChamps.get(i);
			currentStat = whichStats.get(i);
			
			output = output.concat(current.getName());
			output = output.concat(", best " + currentStat + " efficiency: " + this.getValueGE(currentStat) + "\n");
		}
		
		return output;
	}
	
	public String thisChampEffToString(GoldEfficiency reg) { //prints out the gold efficiency for a specific champion
		String output = (name + "'s efficiency stats:\n");
		
		for (int i = 0; i < reg.whichStats.size(); i++) {
			output = output.concat(reg.whichStats.get(i) + ": " + this.getValueGE(reg.whichStats.get(i)) + "\n");
		}
		
		output = output.concat("\n");
		
		return output;
	}
	
	public void setMaxMana(double manaSet) {
		totalMana = manaSet;
	}
	
	public void setAbilDmg(double abilDmgSet) {
		abilDmg = abilDmgSet;
	}
	
	public double getAbilDmg() {
		return abilDmg;
	}	
	
}
