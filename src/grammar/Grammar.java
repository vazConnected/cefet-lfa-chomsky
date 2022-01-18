package grammar;

import java.util.HashSet;

public class Grammar {
	public String initialRuleIdentifier;
	public HashSet<Element> alphabet;
	public HashSet<Rule> rules;
	
	public Grammar(String initialRuleIdentifier, HashSet<Element> alphabet, HashSet<Rule> rules) {
		this.initialRuleIdentifier = initialRuleIdentifier;
		this.alphabet = alphabet;
		this.rules = rules;
	}
	
}
