package grammar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Rule {
	private Character identifier;
	private ArrayList<String> transitions;

	public Rule(Character identifier) {
		this.transitions = new ArrayList<String>();
		this.identifier = identifier;
	}

	public Rule(Character identifier, ArrayList<String> transitions) {
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

	public Character getIdentifier() {
		return this.identifier;
	}

	public void pushTransitions(Collection<String> transitions) {
		Iterator<String> transitionsIterator = transitions.iterator();

		while (transitionsIterator.hasNext()) {
			String currentTransition = transitionsIterator.next();
			if (!this.transitions.contains(currentTransition)) {
				this.transitions.add(currentTransition);
			}
		}
	}

	public void pushTransitions(String transition) {
		if (this.transitions.contains(transition))
			return;
		
		this.transitions.add(transition);
	}

	public void removeTransition(String transition) {
		if (this.transitions.contains(transition))
			this.transitions.remove(transition);
	}
	
	public void removeTransitions(Collection<String> transitions) {
		this.transitions.remove(transitions);
	}

	public ArrayList<String> getTransitions() {
		return this.transitions;
	}

	public void setTransitions(Collection<String> transitions) {
		this.transitions = new ArrayList<String>(transitions);
	}

	public boolean isAnEndElementRule() {
		if (transitions.size() == 1 && transitions.get(0).length() == 1) {
			return Character.isLowerCase(transitions.get(0).charAt(0));
		}
		return false;
	}
}
