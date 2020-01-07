package tFTHelper;

import java.io.*;
import java.lang.Cloneable;
import json_Converter.GoldEfficiency;

public class GeneralChampion extends ChampItemGeneral {

	private int goldCost,champLvl;	

	protected double hpGain,adGain,goldEff,abilDmg = 0; //for leveling up	
	protected double manaGain,startMana,totalMana;	
	private double atkRng,atkDps,abilDps,totalDps;	
	private boolean abilAOE;
	private String abilName = "";
	
	//String attrib1,attrib2,attrib3;  Will take a while to get to class attributes and synergies
	
	ChampItemGeneral[] items = new ChampItemGeneral[3];
	private GoldEfficiency thisChampionGE;
	
	public GeneralChampion() { //-------------------------------------------------------
		
		isChampion = true;
		
		goldCost = 0;
		champLvl = 1;
		
		abilAOE = false;
		atkRng = 1.90; 					//melee range
		name = new String("");
		startMana = 0;
		manaGain = 8; 					//Goes up by 2 per star level
		
		
		hpGain = 1.8;		//
		adGain = 1.25;		//Values shared across all champs
		ap = 1.0;			//
		
		items[0] = new ChampItemGeneral();
		items[1] = new ChampItemGeneral();
		items[2] = new ChampItemGeneral();
		

		dpsCalcs();
				
	}
	
	public boolean AddItem(String itemName) {  //notYetImplemented----------------------
		
		
		
		return false;
		
	}
	
	
	public void levelUp() {
		hp = (int)(hp*hpGain);
		ad = (int)(ad*adGain);		
		manaGain += 2;
		goldCost = goldCost * 3;
		champLvl++;
		
	}
	
	public void dpsCalcs() {
		//Need to look further into DPS calcs
		
		atkDps = ad * as * (crtCh + 1) * crtDmg;
		abilDps = manaGain * as * abilDmg * ap;
		totalDps = atkDps + abilDps;
	}
	
//=Getters, Setters, Etc.===============================================================	
	
	public void addValueChampion(String value, double amount) {
		
		value = value.toLowerCase();
		
		if (addValue(value,amount) == true)
			return;
		
		switch (value) {
			case "cost":
				goldCost = (int) amount;
				break;
			case "critchance":
				crtCh += amount;
				break;
			case "initialmana":
				startMana = amount;
				break;
			case "mana":
				totalMana = amount;
				break;
			case "range":
				atkRng = amount;
				break;
				
		}
		
	}
	
	public double getValueChampion(String valueGet) {
		
		valueGet = valueGet.toLowerCase();
		
		double value = getValue(valueGet);
		
		if (value != -1) 
			return value;
		
		
		switch (valueGet) {
			case "cost":
				return (double) goldCost;
			case "initialmana":
				return startMana;
			case "mana":
				return totalMana;
			case "range":
				return atkRng;
				
			default:
				//System.out.println(valueGet + " not found in GeneralChampion class");
				return value;
		}		
		
	}
	
	public String getName() {
		return super.toString();
	}
	
	public GoldEfficiency getChampGE() {
		return thisChampionGE;
	}
	
	public void initializeEffHolder() {
		if (thisChampionGE == null) {
			thisChampionGE = new GoldEfficiency();
			thisChampionGE.setValue("name", name);
		}
	}
	
	public void setGE(GoldEfficiency geToSet) {
		thisChampionGE = geToSet;
	}
	
	/*public void setValueEfficHolder(String stat, double amount) {
		thisChampionGE.addValueChampion(stat, amount);
	}
	
	public double getValueEfficHolder(String stat) {
		return (thisChampionGE.getValueChampion(stat));
	}
	
	public String getGEStats(GoldEfficiency registry) {
		return thisChampionGE.thisChampEffToString(registry);
	}
	
	public double getGEStats(GoldEfficiency registry, String stat) {
		return thisChampionGE.getSpecificStat(registry,stat);
	}*/
	
	
	public void addAp(double apAdd) {
		ap += apAdd;
	}
	
	public void setRange(double rangeSet) {
		atkRng = rangeSet;
	}
	
	public void addRange(double rangeAdd) { //Percent-based range increase
		atkRng = (int)(atkRng * rangeAdd);
	}


	public void setAoe(boolean aoeStatus) {
		abilAOE = aoeStatus;
	}
		
	
	public void setAbilName(String abilNameSet) {
		abilName = abilNameSet;
	}
	
	/*public void setChampAttrib(String at1, String at2, String at3) { Need to implement classes at some point
		attrib1 = at1;
		attrib2 = at2;
		attrib3 = at3;				
	}*/
	
	
	public String toString() {
		String output = name;		
		output = output.concat(", level " + champLvl);
		output = output.concat("\nItems:\n1: " + items[0].toString() + "\n2: " + items[1].toString() + "\n3: " + items[2].toString() + "\n");		
		
		return(output);		
	}
	
	public String toStringComplex() {
		String outputComplex = toString();
		outputComplex = outputComplex.concat("\nCost: " + goldCost);
		outputComplex = outputComplex.concat("\nArmor: " + arm);
		outputComplex = outputComplex.concat("\nMr: " + mr);
		outputComplex = outputComplex.concat("\nHp: " + hp);
		outputComplex = outputComplex.concat("\nHp Gain: " + hpGain);
		outputComplex = outputComplex.concat("\nDodge Chance: " + dodge + "\n");
		
		outputComplex = outputComplex.concat("\nAd: " + ad);
		outputComplex = outputComplex.concat("\nAd Gain: " + adGain);
		outputComplex = outputComplex.concat("\nAttack Speed: " + as);
		outputComplex = outputComplex.concat("\nCrit Chance: " + crtCh);
		outputComplex = outputComplex.concat("\nCrit Damage: " + crtDmg);
		outputComplex = outputComplex.concat("\nAttack Range: " + atkRng);
		outputComplex = outputComplex.concat("\nBasic Attack DPS: " + atkDps + "\n");
		

		outputComplex = outputComplex.concat("\nAp: " + ap);
		outputComplex = outputComplex.concat("\nAbility Name: " + abilName);
		outputComplex = outputComplex.concat("\nAbility Damage: " + abilDmg);
		outputComplex = outputComplex = outputComplex.concat("\nMax Mana: " + totalMana);
		outputComplex = outputComplex.concat("\nStarting Mana: " + startMana);
		outputComplex = outputComplex.concat("\nMana Gain: " + manaGain);
		outputComplex = outputComplex.concat("\nAbility DPS: " + abilDps + "\n");
		

		outputComplex = outputComplex = outputComplex.concat("\nTotal DPS: " + totalDps + "\n-------------------------------\n");

		
		return (outputComplex);
		
	}
	
	protected Object clone () throws CloneNotSupportedException {
		return super.clone();
	}
}
