package tFTHelper;

import java.io.*;

public class ChampItemGeneral implements Serializable, Cloneable {

	protected double hp,arm,mr,ad,as,crtCh,crtDmg,dodge,ap= 0;
	protected String name;
	boolean isChampion;
	
	
	public ChampItemGeneral() {		
		name = "";		
	}
	
	public boolean addValue(String value, double amount) {
				
		switch (value) {
			case "health":
			case "hp":
				hp += amount;
				break;
			case "armor":
				arm += amount;
				break;
			case "magicresist":
				mr += amount;
				break;
			case "ad":
			case "damage":
				ad += amount;
				break;
			case "as":
			case "attackspeed":
				as += amount;
				break;
			case "critchance":
				crtCh += amount;
				dodge += amount;
				break;
			case "crtdmg":
			case "critmultiplier":
				crtDmg += amount;
				break;
			case "ap":
				ap += amount;
				break;

			default:
				return false;
				
		}
		
		return true;
		
	}
	
	public double getValue (String valueGet) {
		
		switch (valueGet) {
		case "health":
		case "hp":
			return hp;			
		case "armor":
			return arm;			
		case "magicresist":
			return mr;			
		case "ad":
		case "damage":
			return ad;			
		case "as":
		case "attackspeed":
			return as;			
		case "critchance":
			return crtCh;
		case "dodge":
			return dodge;		
		case "crtdmg":
		case "critmultiplier":
			return crtDmg;			
		case "ap":
			return ap;
			
		default:
			//System.out.println(valueGet + " not found in superclass");
			return -1;			
		}		
	}
	
	public void setValue(String value, String inputName) {
		name = inputName;
	}
	
	public String toString() {
		return name;
	}
	

}
