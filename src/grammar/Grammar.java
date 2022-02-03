package grammar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class Grammar {
	public String initialRuleIdentifier;
	public HashSet<Character> alphabet;
	public HashSet<Rule> rules;
	public HashMap<Character, Rule> rulesMap;

	public Grammar(String initialRuleIdentifier, HashSet<Character> alphabet, HashSet<Rule> rules) {
		this.initialRuleIdentifier = initialRuleIdentifier;
		this.alphabet = alphabet;
		this.rules = rules;
		this.rulesToHashMap();
	}

	public HashSet<Rule> getRulesWithTransitionsSizeEqualOrBiggerThan(int value) {
		HashSet<Rule> rulesWithBigTransitions = new HashSet<Rule>();

		Iterator<Rule> rulesIterator = this.rules.iterator();
		while (rulesIterator.hasNext()) {
			Rule currentRule = rulesIterator.next();
			ArrayList<String> transitions = currentRule.getTransitions();
			for (int i = 0; i < transitions.size(); i++) {
				if (transitions.get(i).length() >= value) {
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
	
	public void addRules(Collection<Rule> rules) {
		Iterator<Rule> rulesIterator = rules.iterator();
		while (rulesIterator.hasNext()) {
			Rule currentRule = rulesIterator.next();
			this.rulesMap.put(currentRule.getIdentifier(), currentRule);
		}
		
		this.rules.addAll(rules);
	}
	
	public void removeRule(Rule rule) {
		rules.remove(rule);
		rulesMap.remove(rule.getIdentifier(), rule);
	}
	
	public void removeRules(Collection<Rule> rules) {
		Iterator<Rule> rulesIterator = rules.iterator();
		while (rulesIterator.hasNext()) {
			Rule currentRule = rulesIterator.next();
			this.rulesMap.remove(currentRule.getIdentifier());
		}
		
		this.rules.removeAll(rules);
	}
	
	private HashSet<Rule> getLambdaRulesHS(){
		HashSet<Rule> lambdaRules = new HashSet<Rule>();
		
		Iterator<Rule> rulesIterator = this.rules.iterator();
		while (rulesIterator.hasNext()) {
			Rule currentRule = rulesIterator.next();
			ArrayList<String> transitions = currentRule.getTransitions();
			if (transitions.contains("#"))
				lambdaRules.add(currentRule);
		}
		
		return lambdaRules;
	}
	
	private HashMap<Character, Rule> getLambdaRulesHM(){
		HashMap<Character, Rule> lambdaRules = new HashMap<Character, Rule>();
		
		Iterator<Rule> rulesIterator = this.rules.iterator();
		while (rulesIterator.hasNext()) {
			Rule currentRule = rulesIterator.next();
			
			ArrayList<String> transitions = currentRule.getTransitions();
			
			if (transitions.contains("#"))
				lambdaRules.put(currentRule.getIdentifier(), currentRule);
		}
		
		return lambdaRules;
	}
	
	public HashSet<Rule> getAllLambdaRules(){
		HashSet<Rule> lambdaRules_HS = this.getLambdaRulesHS();
		HashMap<Character, Rule> lambdaRules_HM = this.getLambdaRulesHM();
		
		// Identificar regras que podem ser lambda indiretamente
		int lambdaRulesHS_oldSize = 0;
		do {
			lambdaRulesHS_oldSize = lambdaRules_HS.size();

			HashSet<Rule> possibleLambdaRules = (HashSet<Rule>) this.rules.clone();
			possibleLambdaRules.removeAll(lambdaRules_HS);
			
			Iterator<Rule> possibleLambdaRulesIterator = possibleLambdaRules.iterator();
			while (possibleLambdaRulesIterator.hasNext()) {
				Rule currentRule = possibleLambdaRulesIterator.next();
				ArrayList<String> transitions = currentRule.getTransitions();

				// Se todos os elementos estiverem contidos no lambdaRules, entao a currentRule e uma lambdaRule
				boolean currentRuleIsLambdaRule = true;
				for(int i = 0; i < transitions.size(); i++) {
					currentRuleIsLambdaRule = true;
					String currentTransition = transitions.get(i);
					
					for (int j = 0; j < currentTransition.length(); j++) {
						Character currentElement = currentTransition.charAt(j);
						
						if (!lambdaRules_HM.containsKey(currentElement))
							currentRuleIsLambdaRule = false;
					}
					
					if (currentRuleIsLambdaRule) {
						lambdaRules_HS.add(currentRule);
						lambdaRules_HM.put(currentRule.getIdentifier(), currentRule);
						break;
					}
				}
				
			}
			// Se o tamanho nao mudou, todas as regras lambda foram encontradas
		} while (lambdaRulesHS_oldSize != lambdaRules_HS.size());
		
		return lambdaRules_HS;
	}
	
	public void removeDuplicateTransitions() {
		Iterator<Rule> rulesIterator = rules.iterator();
		while(rulesIterator.hasNext()) {
			Rule currentRule = rulesIterator.next();
			HashSet<String> transitions = new HashSet<String>(currentRule.getTransitions());
			currentRule.setTransitions(transitions);
			rules.add(currentRule);
		}
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
