package grammar;

import java.util.ArrayList;
import java.util.Iterator;

public class Chomsky {
	private static Grammar removeLambdaRules(Grammar grammar) {
		Iterator<Rule> rulesIterator = grammar.rules.iterator();
		
		while (rulesIterator.hasNext()) {
			Rule currentRule = rulesIterator.next();	
			ArrayList<String> transitions = currentRule.getTransitions();
			transitions.remove("#");
		}
		
		return grammar;
	};
	
	private static Grammar removeUnitaryRules(Grammar grammar) {
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
