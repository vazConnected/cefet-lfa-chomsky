package grammar;

import java.util.HashSet;

public class Grammar {
	private Rule initialRule;
	private HashSet<Element> alphabet;
	private HashSet<Rule> rules;
	
	private Grammar(Rule initialRule, HashSet<Element> alphabet, HashSet<Rule> rules) {
		this.initialRule = initialRule;
		this.alphabet = alphabet;
		this.rules = rules;
	}
	
}
