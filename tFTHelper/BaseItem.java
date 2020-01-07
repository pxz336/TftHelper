package tFTHelper;

public class BaseItem extends ChampItemGeneral {

	private boolean slotTaken,combineable;
	private double mana;
	
	public BaseItem() {
		
		isChampion = false;

		slotTaken = false;	
		combineable = true;
		
	}

	public void addValueItem(String value, double amount) {
		
		value = value.toLowerCase();
		
		if (addValue(value,amount) == true) {
			return;
		}
		
		switch (value) {
			case "critchance":
				crtCh += amount;
				dodge += amount;
				break;
			case "mana":
				mana += amount;
				break;
		}
		
	}	
	
	public String toString() {
		return (name);		
	}
	
	public String toStringComplex() {
		String output = toString() + "\n";
		
		if (hp != 0)
			output = output.concat("Hp = " + hp);
		if (arm != 0)
			output = output.concat("Armor = " + arm);	
		if (mr != 0)
			output = output.concat("Magic Resist = " + mr);	
		if (ad != 0)
			output = output.concat("Attack Damage = " + ad);	
		if (as != 0)
			output = output.concat("Attack Speed = " + as);	
		if (crtCh != 0) {
			output = output.concat("Crit Chance = " + crtCh + "\n");
			output = output.concat("Dodge Chance = " + dodge);
		}
		
		if (crtDmg != 0)
			output = output.concat("Crit Damage = " + crtDmg);	
		if (ap != 0)
			output = output.concat("Ability Power = " + ap);
		if (mana != 0)
			output = output.concat("Mana = " + mana);
		
		output = output.concat("\n");
				
		return output;
	}
}
