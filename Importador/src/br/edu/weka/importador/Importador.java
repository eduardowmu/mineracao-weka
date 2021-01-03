package br.edu.weka.importador;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;

public class Importador 
{	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{	EventQueue.invokeLater(new Runnable() 
		{	public void run() 
			{	try 
				{	Importador window = new Importador();
					window.frame.setVisible(true);
				} catch (Exception e) {System.out.println(e.getMessage());}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Importador() {initialize();}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() 
	{	frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		//botão responsável pela importação dos dados
		//buscando os dados da nossa base de dados.
		JButton btnNewButton = new JButton("Importar");
		btnNewButton.addActionListener(new ActionListener() 
		{	public void actionPerformed(ActionEvent arg0) 
			{	String export = "@relation mercado\n\n";
				try 
				{	Class.forName("com.mysql.cj.jdbc.Driver");
					try 
					{	Connection conn = DriverManager.getConnection(
								"jdbc:mysql://localhost/mercado", "root", "");
					
						//comandos SQL
						Statement statement = conn.createStatement();
						
						String sql = "SELECT idproduto, nome FROM `produtos` WHERE idproduto "
								+ "IN(select idproduto FROM venda_produtos)";
						
						ResultSet rs = statement.executeQuery(sql);
						while(rs.next())
						{	System.out.println(rs.getString("nome"));
							export += "@attribute " + rs.getString("nome") + " {sim}\n";
						}
						//gerando o arquivo arrf
						File file = new File("F:\\Users\\eduardowmu\\Desktop\\meusdoc\\estudo\\"
								+ "FATEC\\7 - OUTROS SEMESTRES\\Udemy\\OUTROS-CURSOS\\"
								+ "MIneração - JAVA\\mercados\\mercado.arrf");
						
						/*Procedimentos do java para gravarmos arquivos em disco*/
						try 
						{	FileOutputStream fos = new FileOutputStream(file);
							try {fos.write(export.getBytes());} 
							catch (IOException e) 
							{System.out.println(e.getMessage());}
							finally 
							{	try {fos.close();} 
								catch (IOException e) 
								{System.out.println(e.getMessage());}
							}
						} 
						catch (FileNotFoundException e) 
						{System.out.println(e.getMessage());}
					} 
					catch (SQLException e) 
					{System.out.println(e.getMessage());}
				} 
				catch (ClassNotFoundException e) 
				{System.out.println(e.getMessage());}
			}
		});
		frame.getContentPane().add(btnNewButton);
	}
}