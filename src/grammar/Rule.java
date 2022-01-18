package grammar;

import java.util.ArrayList;

public class Rule implements Identifier {
	private Character identifier;
	private ArrayList< String > transitions;
	
	public Rule(Character identifier) throws Exception {
		this.transitions = new ArrayList< String >();
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
	public Character getIdentifier() {
		return this.identifier;
	}

	public void pushTransitions(String transition) {
		if (this.transitions.contains(transition))
			return;
		
		this.transitions.add(transition);
	}
	
	public void removeTransitions(String transition) {
		if (this.transitions.contains(transition))
			this.transitions.remove(transition);
	}
	
	public ArrayList< String > getTransitions(){
		return this.transitions;
	}
	
	public void setTransitions(ArrayList< String > transitions) {
		this.transitions = transitions;
	}
}
