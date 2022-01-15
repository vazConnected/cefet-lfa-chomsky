package grammar;

import java.util.ArrayList;

public class Rule implements Identifier {
	private String identifier;
	private ArrayList< ArrayList<Identifier> > transitions;
	
	public Rule(String identifier) throws Exception {
		if (identifier != null) {
			this.identifier = identifier;
		} else {
			throw new Exception ("Regra sem identificador!");
		}
	} 
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof Rule) {
			Rule rule = (Rule) object;
			return rule.identifier.equals(this.identifier);
		}
		return false;
	}
	
	@Override
	public String getIdentifier() {
		return this.identifier;
	}

	public void pushDerivation(ArrayList<Identifier> derivation) {
		if (this.transitions.contains(derivation))
			return;
		
		this.transitions.add(derivation);
	}
	
	public void removeDerivation(ArrayList<Identifier> derivation) {
		if (this.transitions.contains(derivation))
			this.transitions.remove(derivation);
	}
	
	public ArrayList< ArrayList<Identifier> > getDerivations(){
		return this.transitions;
	}
}
