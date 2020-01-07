package tFTHelper;

public class CombinedItem extends ChampItemGeneral{

	String itemName,componentName1,componentName2 = "";
	ChampItemGeneral component1, component2;
	
	public CombinedItem(String itemName) {
		

		
		
	}
	
	public CombinedItem(ChampItemGeneral item1, ChampItemGeneral item2) {
		component1 = item1;
		component2 = item2;
		
		/*hp = component1.hp + component2.hp;
		arm = component1.arm + component2.arm;
		mr = component1.mr + component2.mr;
		as = component1.as + component2.as;
		crtCh = component1.crtCh + component2.crtCh;
		crtDmg = component1.crtDmg + component2.crtDmg;
		dodge = component1.dodge + component2.dodge;
		ap = component1.ap + component2.ap;
		mana = component1.mana + component2.mana;*/
		
		itemName = itemLookup(item1, item2);
		
		
	}
	
	
	private void itemLookup (String itemName) {
		
	}
	
	private String itemLookup (ChampItemGeneral item1, ChampItemGeneral item2) {
		
		
		return"";
	}
	
}
