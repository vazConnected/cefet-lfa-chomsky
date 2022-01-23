package grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Chomsky {
	private static Grammar removeLambdaRules(Grammar grammar) {
		Iterator<Rule> rulesIterator = grammar.rules.iterator();

		while (rulesIterator.hasNext()) {
			Rule currentRule = rulesIterator.next();
			ArrayList<String> transitions = currentRule.getTransitions();
			while (transitions.contains("#")) {
				transitions.remove("#");
			}
		}

		return grammar;
	}

	private static Grammar removeUnitaryRules(Grammar grammar) {
		Iterator<Rule> rulesIterator = grammar.rules.iterator();

		while (rulesIterator.hasNext()) {
			Rule currentRule = rulesIterator.next();
			ArrayList<String> currentTransitionArray = currentRule.getTransitions();
			
			for (int i = 0; i < currentTransitionArray.size(); i++) {
				String currentTransition = currentTransitionArray.get(i);
				
				boolean unitaryTransition = currentTransition.length() == 1;
				boolean transitionToRule = Character.isUpperCase(currentTransition.charAt(0));
				
				// Transicao unitaria identificada
				if (unitaryTransition && transitionToRule) {
					Iterator<Rule> _rulesIterator = grammar.rules.iterator();
					while (_rulesIterator.hasNext()) {
						Rule rule = _rulesIterator.next();
						
						if ( rule.getIdentifier().equals(currentTransition.charAt(0)) ) {
							ArrayList<String> newTransitionsArray = currentRule.getTransitions();
							newTransitionsArray.remove(currentTransition);
							currentRule.setTransitions(newTransitionsArray);
							
							currentRule.pushTransitions(rule.getTransitions()); // Copia todas as transicoes para a regra atual
							break;
						}
					}
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
				grammar.rules.remove(rulesToBeRemoved.get(i));

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

						if ( Character.isLowerCase(oldElement) ) {
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
		
		
		return grammar;
	}

	public static Grammar applyChomsky(Grammar grammar) {
		grammar = removeLambdaRules(grammar);
		grammar = removeUnitaryRules(grammar);
		grammar = removeUnusedRules(grammar);
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
