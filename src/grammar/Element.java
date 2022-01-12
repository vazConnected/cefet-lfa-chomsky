package grammar;

public class Element implements Identifier {
	private Character element;
	
	public Element(Character element) throws Exception {
		if (element != null) {
			this.element = element;
		} else {
			throw new Exception ("Sem elementos enviado.");
		}
	} 
	
	@Override
	public Character getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

}
