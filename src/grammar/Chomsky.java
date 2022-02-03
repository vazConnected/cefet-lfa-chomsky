package grammar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Chomsky {
	private static boolean unitaryRuleExists(Grammar grammar) {
		@SuppressWarnings("unchecked")
		HashSet<Rule> rules = (HashSet<Rule>) grammar.rules.clone();
		Iterator<Rule> rulesIterator = rules.iterator();
		
		while (rulesIterator.hasNext()) {
			ArrayList<String> transitions = rulesIterator.next().getTransitions();
			
			for(int i = 0; i < transitions.size(); i++) {
				String currentTransition = transitions.get(i);
				
				if (currentTransition.length() == 1 ) {
					Character identifier = currentTransition.charAt(0);
					if (Character.isUpperCase(identifier))
						return true;
				}	
			}
		}
		
		return false;
	}
	
	private static Grammar updateAlphabet(Grammar grammar) {
		HashSet<Character> alphabet = new HashSet<Character>();
		
		Iterator<Rule> ruleIterator = grammar.rules.iterator();
		while (ruleIterator.hasNext()) {
			Rule rule = ruleIterator.next();
			
			ArrayList<String> transitions = rule.getTransitions();
			Iterator<String> transitionIterator = transitions.iterator();
			while ( transitionIterator.hasNext() ) {
				String currentTransition = transitionIterator.next();
				
				for (int i = 0; i < currentTransition.length(); i++) {
					char currentElement = currentTransition.charAt(i);
					
					if ( !Character.isAlphabetic(currentElement) || Character.isLowerCase(currentElement) ) {
						alphabet.add(currentElement);
					}
				}
			}
			
		}
		
		grammar.alphabet = alphabet;
		return grammar;
	}
	
	private static HashSet<Rule> getRuleInstances(Rule rule, Grammar grammar) {
		@SuppressWarnings("unchecked")
		HashSet<Rule> allRules = (HashSet<Rule>) grammar.rules.clone();
		
		HashSet<Rule> ruleInstances = new HashSet<Rule>();
		String ruleIdentifier = rule.getIdentifier().toString();
		
		Iterator<Rule> rulesIterator = allRules.iterator();
		while (rulesIterator.hasNext()) {
			Rule currentRule = rulesIterator.next();
			ArrayList<String> transitions = currentRule.getTransitions();
			
			for (int i = 0; i < transitions.size(); i++) {
				String currentTransition = transitions.get(i);
				
				if (currentTransition.contains(ruleIdentifier))
					ruleInstances.add(currentRule);
			}
		}
		
		return ruleInstances;
	}
	
	private static HashSet<String> getNewCombinations(HashSet<Rule> lambdaRules, String transition){
		HashSet<String> newCombinations = new HashSet<String>();
		newCombinations.add(transition);
		
		Iterator<Rule> lambdaRulesIterator = lambdaRules.iterator();
		while (lambdaRulesIterator.hasNext()) {
			Rule currentLambdaRule = lambdaRulesIterator.next();
			String currentLambdaRuleIdentifier = currentLambdaRule.getIdentifier().toString();
			
			if (transition.contains(currentLambdaRuleIdentifier)) {
				String transition_copy = transition;
				transition_copy = transition_copy.replaceFirst(currentLambdaRuleIdentifier, "");
				if ( !transition_copy.isEmpty())
					newCombinations.addAll( Chomsky.getNewCombinations(lambdaRules, transition_copy) );	
			}
		}
		
		return newCombinations;
	}
	
	private static Grammar removeLambdaRules(Grammar grammar) {
		HashSet<Rule> lambdaRuleInstances = new HashSet<Rule>();
		HashSet<Rule> lambdaRules = grammar.getAllLambdaRules(); // Diretas e Indiretas
				
		// Obter regras em que transicoes para regras lambda aparecem
		Iterator<Rule> lambdaRulesIterator = lambdaRules.iterator();
		while (lambdaRulesIterator.hasNext()) {
			Rule currentLambdaRule = lambdaRulesIterator.next();
			
			lambdaRuleInstances.addAll(Chomsky.getRuleInstances(currentLambdaRule, grammar));
		}
		
		// Obter novas combinacoes de transicoes
		HashSet<Rule> rulesToOverride = new HashSet<Rule>();
		Iterator<Rule> lambdaRuleInstancesIterator = lambdaRuleInstances.iterator();
		while (lambdaRuleInstancesIterator.hasNext()) {
			Rule currentLambdaRuleInstance = lambdaRuleInstancesIterator.next();
			HashSet<String> newCombinations = new HashSet<String>();
			
			ArrayList<String> transitions = currentLambdaRuleInstance.getTransitions();
			for (int i = 0; i < transitions.size(); i++) {
				newCombinations.addAll( Chomsky.getNewCombinations(lambdaRules, transitions.get(i)) );
			}
			
			currentLambdaRuleInstance.pushTransitions(newCombinations);
			rulesToOverride.add(currentLambdaRuleInstance);
		}
		grammar.addRules(rulesToOverride);
		
		// Excluir transicoes lambda
		lambdaRulesIterator = lambdaRules.iterator();
		while (lambdaRulesIterator.hasNext()) {
			Rule currentRule = lambdaRulesIterator.next();
			ArrayList<String> transitions = currentRule.getTransitions();
			transitions.remove("#");
		}
		grammar.addRules(lambdaRules);

		return grammar;
	}

	private static Grammar removeUnitaryRules(Grammar grammar) {
		@SuppressWarnings("unchecked")
		HashSet<Rule> allRules = (HashSet<Rule>) grammar.rules.clone();

		Iterator<Rule> rulesIterator = allRules.iterator();		
		while (rulesIterator.hasNext()) {
			Rule currentRule = rulesIterator.next();
			ArrayList<String> currentTransitionArray = currentRule.getTransitions();
			
			for (int i = 0; i < currentTransitionArray.size(); i++) {
				String currentTransition = currentTransitionArray.get(i);

				boolean unitaryTransition = currentTransition.length() == 1;
				boolean transitionToRule = Character.isUpperCase(currentTransition.charAt(0));
				
				// Transicao unitaria identificada
				if (unitaryTransition && transitionToRule) {
					Rule ruleToGetTransitions = grammar.rulesMap.get(currentTransition.charAt(0));
					
					currentRule.pushTransitions(ruleToGetTransitions.getTransitions());
					currentRule.removeTransition(currentTransition);
				}
			}
		}

		return grammar;
	}

	private static Grammar removeUnusedRules(Grammar grammar) {
		Iterator<Rule> rulesIterator = grammar.rules.iterator();
		ArrayList<Rule> rulesToBeRemoved = new ArrayList<Rule>();
		
		while (rulesIterator.hasNext()) {
			Rule currentRule = rulesIterator.next();
			
			if (currentRule.getTransitions().size() == 0)
				rulesToBeRemoved.add(currentRule);
		}
		
		
		if(rulesToBeRemoved.size() > 0) {
			HashSet<Rule> newSetOfRules = new HashSet<Rule>();
			
			for(int i = 0; i < rulesToBeRemoved.size(); i++) {
				String identifier = rulesToBeRemoved.get(i).getIdentifier().toString();
				grammar.removeRule(rulesToBeRemoved.get(i));

				// Remove ocorrencias da regra
				Iterator<Rule> _rulesIterator = grammar.rules.iterator();
				while (_rulesIterator.hasNext()) {
					Rule _currentRule = _rulesIterator.next();
					ArrayList<String> transitions = _currentRule.getTransitions();
					
					for(int j = 0; j < transitions.size(); j++) {
						String currentTransition = transitions.get(j).replaceAll(identifier, "");;
						transitions.set(j, currentTransition);
					}
					
					while (transitions.remove("")); // Deleta transicoes em branco
					
					_currentRule.setTransitions(transitions);
					newSetOfRules.add(_currentRule);
					
				}
				
				grammar.rules = newSetOfRules;
			}
		}
		
		grammar.removeDuplicateTransitions();
		return grammar;
	}

	private static Grammar reorganizeRulesToDerivationMaxSizeTwo(Grammar grammar) {
		HashSet<Substitution> substitutions = new HashSet<Substitution>(); // <old, new>
		HashSet<Rule> rulesToBeConsidered = grammar.getRulesWithTransitionsSizeEqualOrBiggerThan(2);
		
		Iterator<Rule> rulesIterator = rulesToBeConsidered.iterator();
		while (rulesIterator.hasNext()) {
			Rule currentRule = rulesIterator.next();
			
			ArrayList<String> transitions = currentRule.getTransitions();
			for(int i = 0; i < transitions.size(); i++) {
				String currentTransition = transitions.get(i);
				
				if (currentTransition.length() >= 2) {
					for (int j = 0; j < currentTransition.length(); j++) {
						Character oldElement = currentTransition.charAt(j);
						
						if ( Character.isLowerCase(oldElement) || !Character.isAlphabetic(oldElement)) {
							ArrayList<String> transition = new ArrayList<String>();
							transition.add( oldElement.toString() );
							
							String newRuleIdentifier = grammar.createAndIndexRule( transition ).toString();
							
							Substitution substitution = new Substitution(oldElement.toString(), newRuleIdentifier);
							substitutions.add(substitution);
						}
					}
				}
			}
		}
		
		ArrayList<Substitution> charactersToBeSubstituted = new ArrayList<Substitution>(substitutions);
		
		rulesIterator = rulesToBeConsidered.iterator();
		while (rulesIterator.hasNext()) {
			Rule currentRule = rulesIterator.next();
			
			ArrayList<String> transitions = currentRule.getTransitions();
			for (int i = 0; i < transitions.size(); i++) {
				String currentTransition = transitions.get(i);
				
				for (int j = 0; j < charactersToBeSubstituted.size(); j++) {
					String oldElement = charactersToBeSubstituted.get(j)._old;
					String newElement = charactersToBeSubstituted.get(j)._new;
					
					if (currentTransition.contains(oldElement) && currentTransition.length() > 1)
						currentTransition = currentTransition.replace(oldElement, newElement);
				}
				transitions.set(i, currentTransition);
			}
			currentRule.setTransitions(transitions);
			grammar.addRule(currentRule);
		}
		
		return grammar;
	}

	private static Grammar substituteDerivationsWithSizeBiggerThanThree(Grammar grammar) {
		HashSet<Substitution> substitutions = new HashSet<Substitution>(); // <old, new>
		HashSet<Rule> rulesToBeConsidered = grammar.getRulesWithTransitionsSizeEqualOrBiggerThan(3);
		
		Iterator<Rule> rulesIterator = rulesToBeConsidered.iterator();
		while(rulesIterator.hasNext()) {
			Rule currentRule = rulesIterator.next();
			ArrayList<String> transitions = currentRule.getTransitions();
			
			for(int i = 0; i < transitions.size(); i++) {
				String currentTransition = transitions.get(i);
				int transitionSize = currentTransition.length();
				
				if (transitionSize >= 3) {
					String newTransition = currentTransition.subSequence(transitionSize - 2, transitionSize).toString();
					ArrayList<String> newTransitionsAL = new ArrayList<String>();
					newTransitionsAL.add(newTransition);
					
					String newRuleIdentifier = grammar.createAndIndexRule(newTransitionsAL).toString();
					
					Substitution substitution = new Substitution(newTransition, newRuleIdentifier);
					substitutions.add(substitution);
				}
			}
		}
		
		ArrayList<Substitution> sequencesToBeSubstituted = new ArrayList<Substitution>(substitutions);
		
		rulesIterator = rulesToBeConsidered.iterator();
		while (rulesIterator.hasNext()) {
			Rule currentRule = rulesIterator.next();
			
			ArrayList<String> transitions = currentRule.getTransitions();
			for (int i = 0; i < transitions.size(); i++) {
				String currentTransition = transitions.get(i);
				
				for (int j = 0; j < sequencesToBeSubstituted.size(); j++) {
					String oldElement = sequencesToBeSubstituted.get(j)._old;
					String newElement = sequencesToBeSubstituted.get(j)._new;
					
					if (currentTransition.contains(oldElement))
						currentTransition = currentTransition.replace(oldElement, newElement);
				}
				transitions.set(i, currentTransition);
			}
			currentRule.setTransitions(transitions);
			grammar.addRule(currentRule);
		}
		
		grammar = Chomsky.updateAlphabet(grammar);
		return grammar;
	}
	
	public static Grammar applyChomsky(Grammar grammar) {
		grammar = removeLambdaRules(grammar);
		
		do {
			grammar = removeUnitaryRules(grammar);
			grammar = removeUnusedRules(grammar);	
		}while (unitaryRuleExists(grammar));

		grammar = reorganizeRulesToDerivationMaxSizeTwo(grammar);
		grammar = substituteDerivationsWithSizeBiggerThanThree(grammar);
		return grammar;
	}
}

class Substitution{
	public String _old;
	public String _new;
	
	public Substitution (String _old, String _new) {
		this._new = _new;
		this._old = _old;
	}
	
	public boolean equals(Object o) {
		if (o instanceof Substitution) {
			Substitution s = (Substitution) o;
			if (s._old.equals(this._old)) {
				return true;
			}
		}
		return false;
	}
}
