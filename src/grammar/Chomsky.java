package grammar;

public class Chomsky {
	private static Grammar removeLambdaRules(Grammar grammar) {};
	
	private static Grammar removeUnitaryRules(Grammar grammar) {};
	
	private static Grammar removeUnusedRules(Grammar grammar) {};
	
	private static Grammar reorganizeRulesToDerivationMaxSizeTwo(Grammar grammar) {};
	
	private static Grammar substituteDerivationsWithSizeBiggerThanThree(Grammar grammar) {};
	
	public static Grammar applyChomsky(Grammar grammar) {
		grammar = removeLambdaRules(grammar);
		grammar = removeUnitaryRules(grammar);
		grammar = removeUnusedRules(grammar);
		grammar = reorganizeRulesToDerivationMaxSizeTwo(grammar);
		return substituteDerivationsWithSizeBiggerThanThree(grammar); 
	}
}
