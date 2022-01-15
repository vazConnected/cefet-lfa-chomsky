import java.io.FileReader;
import java.util.HashSet;
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
			elements.add(new Element(symbols.get(i).toString()));
		}

		// Cadastrar regras
		HashSet<Rule> rules = new HashSet<Rule>();
		JSONArray states = glc.getJSONArray(0);
		for (int i = 0; i < states.length(); i++) {
			Element identifier = new Element(states.getString(i).toString());
			rules.add(new Rule(identifier.getIdentifier()));
		}

		// Inserir transicoes nas regras
		JSONArray transitions = glc.getJSONArray(2);
		for (int i = 0; i < transitions.length(); i++) {
			JSONArray currentTransition = (JSONArray) transitions.get(i);
			// Teste
			System.out
					.println(currentTransition.get(0).toString().length() + " - " + currentTransition.get(0).toString()
							+ " - " + rules.contains(new Rule(currentTransition.get(0).toString())));

		}

		// Criar gramática e definir regra inicial
		String start = glc.getString(3);

		// Grammar grammar;

		/*
		 * Testes de leitura do JSON System.out.println(states.toString());
		 * System.out.println(symbols.toString());
		 * System.out.println(transitions.toString());
		 * System.out.println(start.toString());
		 */
	}
}