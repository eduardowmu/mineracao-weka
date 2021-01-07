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
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import weka.associations.Apriori;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import javax.swing.JTextField;

public class Importador 
{	private JFrame frame;
	private JTextArea textArea;
	private JTextField textField;
	public static void main(String[] args) 
	{	EventQueue.invokeLater(new Runnable() 
		{	public void run() 
			{	try 
				{	Importador window = new Importador();
					window.frame.setVisible(true);
				} 
				catch (Exception e) 
				{System.out.println(e.getMessage());}
			}
		});
	}

	public Importador() {initialize();}

	private void initialize() 
	{	frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//botão responsável pela importação dos dados
		//buscando os dados da nossa base de dados.
		JButton btnNewButton = new JButton("Importar");
		btnNewButton.addActionListener(new ActionListener() 
		{	public void actionPerformed(ActionEvent arg0) 
			{	String export = "@relation mercado\n\n";
				List colunasNome = new ArrayList();
				List colunasId = new ArrayList();
				
				try 
				{	Class.forName("com.mysql.cj.jdbc.Driver");
					try 
					{	Connection conn = DriverManager.getConnection(
								"jdbc:mysql://localhost/mercado", "root", "");
					
						//comandos SQL
						Statement statement = conn.createStatement();
						
						String sql = "SELECT idproduto, nome "
								+ "FROM `produtos` WHERE idproduto "
								+ "IN(select idproduto "
								+ "FROM venda_produtos)";
						
						ResultSet rs = statement.executeQuery(sql);
						while(rs.next())
						{	System.out.println(rs.getString("nome"));
							colunasNome.add(rs.getString("nome"));
							colunasId.add(rs.getInt("idProduto"));
							export += "@attribute " + rs.getString("nome") + 
									" {sim}\n";
						}
						
						export += "\n@data\n";
						
						Statement vendas = conn.createStatement();
						Statement vendaProdutos = conn.createStatement();
						ResultSet rsVendas = vendas.executeQuery("SELECT * "
								+ "FROM vendas");
						
						while(rsVendas.next())
						{	System.out.println(rsVendas.getDate("data_venda"));
							//pegando posição por posição o valor da coluna.
							//Se o ResultSet estiver vazio, quer dizer que o
							//produto não existe, se tiver algum valor nele
							//quer dizer que foi retornado id correspondente
							//então o produto esta contido na venda.
							for(int i = 0; i < colunasId.size(); i++)
							{	ResultSet rsVendaProdutos = 
									vendaProdutos.executeQuery(
									"SELECT idvenda FROM venda_produtos "
										+ "WHERE idproduto = " 
										+ colunasId.get(i) 
										+ " AND idvenda = " 
										+ rsVendas.getInt("idvenda"));
								//se existir produto
								if(rsVendaProdutos.next())
								{export += "sim,";}
								
								else {export += "?,";}
							}
							export = export.substring(0, export.length() - 1);
							export += "\n";
						}
						
						export = export.replace('é', 'e').replace('ã', 'a');
						
						//gerando o arquivo arrf
						File file = new File("F:\\Users\\eduardowmu\\"
								+ "Desktop\\meusdoc\\estudo\\"
								+ "FATEC\\7 - OUTROS SEMESTRES\\"
								+ "Udemy\\OUTROS-CURSOS\\"
								+ "MIneração - JAVA\\mercados\\"
								+ "mercado.arrf");
						
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
		frame.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("130px:grow"),
				ColumnSpec.decode("75px"),
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("93px"),},
			new RowSpec[] {
				FormSpecs.LINE_GAP_ROWSPEC,
				RowSpec.decode("23px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		textField = new JTextField();
		frame.getContentPane().add(textField, "1, 2, fill, default");
		textField.setColumns(10);
		frame.getContentPane().add(btnNewButton, "2, 2, left, top");
		
		JButton btnNewButton_1 = new JButton("Gerar regras");
		btnNewButton_1.addActionListener(new ActionListener() 
		{	public void actionPerformed(ActionEvent arg0) 
			{	try 
				{	DataSource ds = new DataSource(
						"F:\\Users\\eduardowmu\\"
						+ "Desktop\\meusdoc\\estudo\\"
						+ "FATEC\\7 - OUTROS SEMESTRES\\"
						+ "Udemy\\OUTROS-CURSOS\\"
						+ "MIneração - JAVA\\mercados\\"
						+ "mercado.arrf");
					/*essa variável irá armazenar todos os registros
					 *da base. Irá capturar todos os dados capturados
					 *do objeto "ds"*/
					Instances ins = ds.getDataSet();
					System.out.println(ins.toString());
					
					/*objeto para geração das regras*/
					Apriori ap = new Apriori();
					ap.setNumRules(Integer.parseInt(textField.getText()));
					ap.buildAssociations(ins);
					textArea.setText(ap.toString());
				} 
				catch (Exception e) 
				{System.out.println(e.getMessage());}
			}
		});
		frame.getContentPane().add(btnNewButton_1, 
				"4, 2, left, top");
		
		textArea = new JTextArea();
		frame.getContentPane().add(textArea, 
				"1, 6, 4, 1, fill, fill");
	}
}