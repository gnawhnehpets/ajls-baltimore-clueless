package dictionary.gameEntities;

import java.io.Serializable;

/**
 * status: finished
 * @author mjp
 *
 */
public class WeaponCard extends Card implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5108547803055670391L;
	private Weapon weapon;
	
	public WeaponCard(){super();}
	
	public WeaponCard(Weapon weaponIn) {
		super(weaponIn.getName(), weaponIn.getId());
		weapon = weaponIn;
	}
	public WeaponCard(WeaponCard weaponIn) {
		super(weaponIn.getCardName(), weaponIn.getCardId());
		weapon = weaponIn.getWeapon();
	}
	
	@Override
	public CardType getCardType() {
		return CardType.WEAPON;
	}

	public Weapon getWeapon() {
		return weapon;
	}

}
