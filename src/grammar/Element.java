package grammar;

public class Element implements Identifier {
	private Character element;

	public Element(Character element) {
		this.element = element;
	}

	@Override
	public Character getIdentifier() {
		return element;
	}

}
