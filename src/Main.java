import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import grammar.*;

public class Main {
	public static void main(String args[]) throws Exception {
		if (args.length != 1) {
			System.err.println("Arquivo não detectado. Favor inserir com a gramática [GLC]");
			return;
		}

		FileReader fileReader = null;
		try {
			fileReader = new FileReader(args[0]);
		} catch (Exception exception) {
			exception.printStackTrace();
			return;
		}

		// Setup JSON
		JSONTokener tokener = new JSONTokener(fileReader);
		JSONObject object = new JSONObject(tokener);
		JSONArray glc = object.getJSONArray("glc");

		// Cadastrar elementos
		HashSet<Element> elements = new HashSet<Element>();
		JSONArray symbols = glc.getJSONArray(1);
		for (int i = 0; i < symbols.length(); i++) {
			elements.add(new Element(symbols.get(i).toString().charAt(0)));
		}

		// Cadastrar regras
		HashMap<Character, Rule> rules = new HashMap<Character, Rule>();
		JSONArray states = glc.getJSONArray(0);
		for (int i = 0; i < states.length(); i++) {
			Element identifier = new Element(states.getString(i).toString().charAt(0));
			Rule currentRule = new Rule(identifier.getIdentifier());
			rules.put(identifier.getIdentifier(), currentRule);
		}

		// Inserir transicoes nas regras
		JSONArray transitions = glc.getJSONArray(2);
		for(int transitionsCounter = 0; transitionsCounter < transitions.length(); transitionsCounter++) {
			JSONArray currentTransitionArray = transitions.getJSONArray(transitionsCounter);
			
			Character currentRuleIdentification = currentTransitionArray.get(0).toString().charAt(0);
			String currentTransitionString = currentTransitionArray.get(1).toString();

			if (rules.containsKey(currentRuleIdentification)) {
				Rule currentRule = rules.get(currentRuleIdentification);
				currentRule.pushTransitions(currentTransitionString);
				rules.put(currentRuleIdentification, currentRule);
			}
		}

		// Criar gramática e definir regra inicial
		String start = glc.getString(3);
		HashSet<Rule> rulesInHashSet = new HashSet<Rule>(rules.values());
		Grammar grammar = new Grammar(start, elements, rulesInHashSet);
		
		// Traduzir G para FNC
		grammar = Chomsky.applyChomsky(grammar);

		Main.printFormatedGrammar(grammar);
		
		/*
		 * Testes de leitura do JSON System.out.println(states.toString());
		 * System.out.println(symbols.toString());
		 * System.out.println(transitions.toString());
		 * System.out.println(start.toString());
		 */
	}

	private static void printFormatedGrammar(Grammar grammar) {
		Iterator <Rule> rulesIterator = grammar.rules.iterator();
		while(rulesIterator.hasNext()) {
			Rule currentRule = rulesIterator.next();
			ArrayList< String > currentRuleTransitions = currentRule.getTransitions();
			String transitionsString = "";
			
			for(int i = 0; i < currentRuleTransitions.size(); i++) {
				transitionsString += currentRuleTransitions.get(i) + " | ";
			}

			if(transitionsString.length() > 2)
				transitionsString = transitionsString.substring(0, transitionsString.length() -2);
			
			System.out.println(currentRule.getIdentifier().toString() + " -> " + transitionsString);
		}
	}
	
	private static void printGrammarInJSON() {}
}