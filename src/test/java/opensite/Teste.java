package opensite;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Classe de teste que manipula o WebDriver e realiza a conexão com o banco de dados
 * @author Gustavo
 */
public class Teste {
	
	private WebDriver driver;
	private WebElement sendButton, contactBox, messageBox;
	
	private final String SENDBUTTON = "/html/body/mw-app/mw-bootstrap/div/main/mw-main-container/div/mw-main-nav/div/mw-fab-link/a/span[1]/div[2]";
	private final String MESSAGEBOX = "/html/body/mw-app/mw-bootstrap/div/main/mw-main-container/div/mw-conversation-container/div/div[1]/div/mws-message-compose/div/div[2]/div/mws-autosize-textarea/textarea";
	
	/**
	 * Abre o browser
	 */
    public void openBrowser()
    {
    	System.setProperty("webdriver.chrome.driver","chromedriver.exe");
    	driver = new ChromeDriver();
    	driver.get("https://messages.google.com/web/authentication");
    }
    
    /**
     * Fecha o browser
     */
    public void closeBrowser()
    {
    	driver.close();
    	JOptionPane.showMessageDialog(null, "Teste finalizado!");
    }
    
    /**
     * Conecta no banco de dados para puxar a tabela de contatos
     * @return - Lista de contatos
     */
    public List getNumbers()
    {	
    	List<String> numeros = new ArrayList<String>();
    	
    	BD bd = new BD();
		if(bd.getConnection())
		{
			String sql = "SELECT * FROM TB_CONTATO";
			try
			{
				//preparar a instru��o para ser executada
				bd.st = bd.con.prepareStatement(sql);
				bd.rs = bd.st.executeQuery(); //posi��o BOF
				while(bd.rs.next()) //enquanto existir pr�ximo registro
				{
					numeros.add(bd.rs.getString("NUMERO_CONTATO"));
				}
			}
			catch(SQLException erro)
			{
				System.out.println(erro);
			}
			finally
			{
				bd.close();
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Falha na conex�o ao BD!");
		}
		
		return numeros;
    }
    
    /**
     * Envia a mensagem de SMS, ao manipular o browser automaticamente
     * @throws InterruptedException
     */
    public void sendMessage() throws InterruptedException
    {
    	int lz = getNumbers().size();
    	List contatos = getNumbers();
    	
    	for (int i=0;i<lz;i++)
    	{
    		sleeper("xpath", SENDBUTTON);
        	sendButton = driver.findElement(By.xpath(SENDBUTTON));
        	sendButton.click();
        	sleeper("css", "#mat-chip-list-"+i+" > div > input");
        	contactBox = driver.findElement(By.cssSelector("#mat-chip-list-"+i+" > div > input"));
        	contactBox.sendKeys(contatos.get(i).toString());
        	contactBox.sendKeys(Keys.ENTER);
        	sleeper("xpath", MESSAGEBOX);
        	messageBox = driver.findElement(By.xpath(MESSAGEBOX));
        	messageBox.sendKeys("Teste!");
        	messageBox.sendKeys(Keys.ENTER);
        	Thread.sleep(5000);
    	}
    }
    
    /**
     * Classe de apoio, que serve para aguardar elementos da página web carregarem
     * @param tipo - Tipo de elemento
     * @param conteudo - Conteudo elemento
     * @throws InterruptedException
     */
    private void sleeper(String tipo, String conteudo) throws InterruptedException
    {
    	if (tipo == "css")
    	{
    		while (driver.findElements(By.cssSelector(conteudo)).isEmpty())
        	{
        		Thread.sleep(1000);
        	}
    	}
    	else if (tipo == "xpath")
    	{
    		while (driver.findElements(By.xpath(conteudo)).isEmpty())
        	{
        		Thread.sleep(1000);
        	}
    	}
    }
}
