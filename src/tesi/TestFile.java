package tesi;

public class TestFile {

	public static void main(String[] args) {
		
		Engine engine = new ModelEngine();
		
		PetriNet pn = new PetriNet(4,5,2,4,0,1,engine);
		
		String file = "C:\\Users\\Riccardo\\Desktop\\rete.txt";
		
		pn.takeFromFile(file);
		
		pn.viewState();
	}

}
