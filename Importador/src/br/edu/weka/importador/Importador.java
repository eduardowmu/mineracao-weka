package br.edu.weka.importador;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class Importador {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Importador window = new Importador();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Importador() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		//botão responsável pela importação dos dados
		//buscando os dados da nossa base de dados.
		JButton btnNewButton = new JButton("Importar");
		btnNewButton.addActionListener(new ActionListener() 
		{	public void actionPerformed(ActionEvent arg0) 
			{	try 
				{	Class.forName("com.mysql.cj.jdbc.Driver");
					try 
					{	Connection conn = DriverManager.getConnection(
								"jdbc:mysql://localhost/mercado", "root", "");
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
