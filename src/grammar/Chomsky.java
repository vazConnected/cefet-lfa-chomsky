package grammar;

import java.util.ArrayList;
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
	};

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
	};

	private static Grammar removeUnusedRules(Grammar grammar) {
		return grammar;
	};

	private static Grammar reorganizeRulesToDerivationMaxSizeTwo(Grammar grammar) {
		return grammar;
	};

	private static Grammar substituteDerivationsWithSizeBiggerThanThree(Grammar grammar) {
		return grammar;
	};

	public static Grammar applyChomsky(Grammar grammar) {
		grammar = removeLambdaRules(grammar);
		grammar = removeUnitaryRules(grammar);
		grammar = removeUnusedRules(grammar);
		grammar = reorganizeRulesToDerivationMaxSizeTwo(grammar);
		return substituteDerivationsWithSizeBiggerThanThree(grammar);
	}
}
