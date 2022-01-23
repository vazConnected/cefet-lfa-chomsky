package grammar;

import java.util.ArrayList;

public class Rule implements Identifier {
	private Character identifier;
	private ArrayList< String > transitions;
	
	public Rule(Character identifier){
		this.transitions = new ArrayList< String >();
		this.identifier = identifier;
	} 
	
	public Rule(Character identifier, ArrayList<String> transitions){
		this.transitions = transitions;
		this.identifier = identifier;
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
	
	public void pushTransitions(ArrayList<String> transitions) {
		for (int i = 0; i < transitions.size(); i++) {
			String currentTransition = transitions.get(i);
			
			if(!this.transitions.contains(currentTransition)) {
				this.transitions.add(currentTransition);
			}
		}
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

	public boolean isAnEndElementRule() {
		if(transitions.size() == 1 && transitions.get(0).length() == 1) {
			return Character.isLowerCase(transitions.get(0).charAt(0));
		}
		return false;
	}
}


