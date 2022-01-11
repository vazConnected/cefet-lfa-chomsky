package grammar;

import java.util.ArrayList;

public class Rule implements Identifier{
	private Character identifier;
	private ArrayList< ArrayList<Identifier> > derivation;
	
	public Rule(Character identifier) throws Exception {
		if (identifier != null) {
			this.identifier = identifier;
		} else {
			throw new Exception ("Regra sem identificador!");
		}
	} 
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof Rule) {
			return ((Rule) object).identifier.equals(this.identifier);
		}
		return false;
	}
	
	@Override
	public Character getIdentifier() {
		return this.identifier;
	}

	public void pushDerivation(ArrayList<Identifier> derivation) {
		if (this.derivation.contains(derivation))
			return;
		
		this.derivation.add(derivation);
	}
	
	public void removeDerivation(ArrayList<Identifier> derivation) {
		if (this.derivation.contains(derivation))
			this.derivation.remove(derivation);
	}
	
	public ArrayList< ArrayList<Identifier> > getDerivations(){
		return this.derivation;
	}
}
