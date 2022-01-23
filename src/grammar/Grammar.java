package grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class Grammar {
	public String initialRuleIdentifier;
	public HashSet<Element> alphabet;
	public HashSet<Rule> rules;
	public HashMap<Character, Rule> rulesMap;

	public Grammar(String initialRuleIdentifier, HashSet<Element> alphabet, HashSet<Rule> rules) {
		this.initialRuleIdentifier = initialRuleIdentifier;
		this.alphabet = alphabet;
		this.rules = rules;
		this.rulesToHashMap();
	}

	public HashSet<Rule> getRulesWithBigTransitions() {
		HashSet<Rule> rulesWithBigTransitions = new HashSet<Rule>();

		Iterator<Rule> rulesIterator = this.rules.iterator();
		while (rulesIterator.hasNext()) {
			Rule currentRule = rulesIterator.next();
			ArrayList<String> transitions = currentRule.getTransitions();
			for (int i = 0; i < transitions.size(); i++) {
				if (transitions.get(i).length() >= 2) {
					rulesWithBigTransitions.add(currentRule);
					break;
				}
			}
		}

		return rulesWithBigTransitions;
	}

	public void appendRuleToGrammar(Rule rule) {
		rules.add(rule);
		rulesMap.put(rule.getIdentifier(), rule);
	}

	public Character createAndIndexRule(ArrayList<String> transitions) {
		Iterator<Rule> rulesIterator = rules.iterator();
		while(rulesIterator.hasNext()) {
			Rule currentRule = rulesIterator.next();
			if(currentRule.getTransitions().equals(transitions)) {
				return currentRule.getIdentifier();
			}
		}

		Random random = new Random();
		Character randomIdentifier = 'A';
		do {
			randomIdentifier = (char) (random.nextInt('Z' - 'A') + 'A');
		} while (rulesMap.containsKey(randomIdentifier));

		this.appendRuleToGrammar(new Rule(randomIdentifier, transitions));
		return randomIdentifier;
	}

	public void addRule(Rule rule) {
		rules.add(rule);
		rulesMap.put(rule.getIdentifier(), rule);
	}
	
	private void rulesToHashMap() {
		this.rulesMap = new HashMap<Character, Rule>();

		Iterator<Rule> rulesIterator = this.rules.iterator();
		while (rulesIterator.hasNext()) {
			Rule currentRule = rulesIterator.next();
			this.rulesMap.put(currentRule.getIdentifier(), currentRule);
		}
	}
}
