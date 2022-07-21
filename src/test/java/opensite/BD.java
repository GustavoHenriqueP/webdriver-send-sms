package opensite;

import java.sql.*;

public class BD {
	
	// realiza a conexão ao BD usando o Driver
	public Connection con = null;
	
	// realiza a execução de instruções SQL
	public PreparedStatement st = null;
	
	// manipula uma tabela em memória
	public ResultSet rs = null;
	
	private final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private final String DATABASENAME = "DBOPENSITE";
	private final String URL = "jdbc:sqlserver://localhost:1433;databasename="+DATABASENAME;
	private final String LOGIN = "sa";
	private final String PASSWORD = "123456";
	
	/**
	 * Realiza a conexção ao banco de dados
	 * @return - true em caso de sucesso ou false caso contr�rio
	 */
	public boolean getConnection()
	{
		try
		{
			Class.forName(DRIVER); //carregando o DRIVER
			con = DriverManager.getConnection(URL,LOGIN,PASSWORD);
			System.out.println("Conectado com sucesso!");
			return true;
		}
		catch(ClassNotFoundException erro)
		{
			System.out.println("Driver n�o encontrado!");
			return false;
		}
		catch(SQLException erro)
		{
			System.out.println("Falha na conex�o " + erro);
			return false;
		}
	}
	
	/**
	 * Encerra a conexão com o banco de dados, fechando todos os objetos utilizados
	 */
	public void close()
	{
		try
		{
			if(rs!=null) rs.close();
			if(st!=null) st.close();
			if(con!=null)
				{
					con.close();
					System.out.println("Desconectou..");
				}
		}
		catch(SQLException erro) {}
	}
}
