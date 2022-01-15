package grammar;

public class Element implements Identifier {
	private String element;
	
	public Element(String element) throws Exception {
		if (element != null) {
			this.element = element;
		} else {
			throw new Exception ("Sem elementos enviado.");
		}
	} 
	
	@Override
	public String getIdentifier() {
		return element;
	}

}
