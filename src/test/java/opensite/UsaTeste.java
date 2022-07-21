package opensite;

public class UsaTeste {

	public static void main(String[] args) throws InterruptedException {
		
		Teste t1 = new Teste();
		t1.openBrowser();
		t1.sendMessage();
		t1.closeBrowser();
	}
}
