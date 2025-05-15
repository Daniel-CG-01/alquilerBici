package tema8AlquilerBici;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;


import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.Component;
import javax.swing.table.TableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;

//SINGLETON
class ConnectionSingleton {
	private static Connection con;
	
	public static Connection getConnection() throws SQLException{
		String url="jdbc:mysql://127.0.0.1:3306/alquilerBici";
		String user="root";
		String password="root";
		/*String url="jdbc:mysql://127.0.0.1:3307/alquilerBici";
		String user="alumno";
		String password="alumno";*/
		
		if(con==null||con.isClosed()) {
			con=DriverManager.getConnection(url,user, password);
		}
		return con;
	}
}
public class Tema8AlquilerBici {

	private JFrame frmAlquilerBicis;
	private JTable tableUsuario;
	private JTable tableBici;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Tema8AlquilerBici window = new Tema8AlquilerBici();
					window.frmAlquilerBicis.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Tema8AlquilerBici() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAlquilerBicis = new JFrame();
		frmAlquilerBicis.getContentPane().setBackground(new Color(249, 240, 107));
		frmAlquilerBicis.setTitle("Aplicación de Alquiler");
		frmAlquilerBicis.setBounds(100, 100, 852, 463);
		frmAlquilerBicis.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAlquilerBicis.getContentPane().setLayout(null);
		
		JLabel lblAadirUsuario = new JLabel("Añadir usuario:");
		lblAadirUsuario.setBounds(9, 28, 115, 15);
		frmAlquilerBicis.getContentPane().add(lblAadirUsuario);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(19, 55, 70, 15);
		frmAlquilerBicis.getContentPane().add(lblNombre);
		
		JLabel lblEdad = new JLabel("Edad:");
		lblEdad.setBounds(19, 109, 70, 15);
		frmAlquilerBicis.getContentPane().add(lblEdad);
		
		JLabel lblCuentaBancaria = new JLabel("Cuenta bancaria:");
		lblCuentaBancaria.setBounds(19, 164, 136, 15);
		frmAlquilerBicis.getContentPane().add(lblCuentaBancaria);
		
		JTextArea txtNom = new JTextArea();
		txtNom.setBounds(29, 82, 143, 15);
		frmAlquilerBicis.getContentPane().add(txtNom);
		
		JTextArea txtEdad = new JTextArea();
		txtEdad.setBounds(29, 137, 143, 15);
		frmAlquilerBicis.getContentPane().add(txtEdad);
		
		JTextArea txtCuenta = new JTextArea();
		txtCuenta.setBounds(29, 192, 143, 15);
		frmAlquilerBicis.getContentPane().add(txtCuenta);
		
		JLabel lblAadirBici = new JLabel("Añadir bici:");
		lblAadirBici.setBounds(9, 265, 130, 15);
		frmAlquilerBicis.getContentPane().add(lblAadirBici);
		
		JButton btnAddUsu = new JButton("Añadir");
		
		btnAddUsu.setBounds(38, 228, 117, 25);
		frmAlquilerBicis.getContentPane().add(btnAddUsu);

		JButton btnAddBici = new JButton("Añadir");
		btnAddBici.setBounds(38, 292, 117, 25);
		frmAlquilerBicis.getContentPane().add(btnAddBici);

		// Crear modelo de tabla usuario
		DefaultTableModel modeloUsuario = new DefaultTableModel();
		modeloUsuario.addColumn("ID");
		modeloUsuario.addColumn("Nombre");
		modeloUsuario.addColumn("Edad");
		modeloUsuario.addColumn("Cuenta Bancaria");
		// Añadir valores al iniciar el programa al modelo
		try {
			Connection con = ConnectionSingleton.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM usuario");
			Object[] row = new Object[4];
			while (rs.next()) {
				row[0] = rs.getString("idusuario");
				row[1] = rs.getString("nombre");
				row[2] = rs.getInt("edad");
				row[3] = rs.getString("cuentaBancaria");
				modeloUsuario.addRow(row);
			}
			rs.close();
			stmt.close();
			con.close();

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frmAlquilerBicis, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		//Funcionamiento botón añadir para usuario
		btnAddUsu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nom=txtNom.getText();
				int edad=Integer.parseInt(txtEdad.getText());
				String cuenta=txtCuenta.getText();
				//Crear nuevo usuario
				try {
					Connection con=ConnectionSingleton.getConnection();
					PreparedStatement insPstmt=con.prepareStatement("INSERT INTO usuario VALUES (null, ?, ?, ?)");
					insPstmt.setString(1, nom);
					insPstmt.setInt(2, edad);
					insPstmt.setString(3, cuenta);
					insPstmt.executeUpdate();
					insPstmt.close();

					//Refrescar tabla usuario
					modeloUsuario.setRowCount(0);
					
					Statement stmt=con.createStatement();
					ResultSet rs=stmt.executeQuery("SELECT * FROM usuario");
					Object[] row=new Object[4];
					while(rs.next()) {
						row[0] = rs.getString("idusuario");
						row[1] = rs.getString("nombre");
						row[2] = rs.getInt("edad");
						row[3] = rs.getString("cuentaBancaria");
						modeloUsuario.addRow(row);
					}
					rs.close();
					stmt.close();
					con.close();
					JOptionPane.showMessageDialog(frmAlquilerBicis, "Usuario creado.");
				}catch(SQLException eAddUser) {
					JOptionPane.showMessageDialog(frmAlquilerBicis, eAddUser.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		tableUsuario = new JTable(modeloUsuario);
		tableUsuario.setBounds(286, 55, 268, 149);
		
		JScrollPane scrollPaneU = new JScrollPane(tableUsuario);
		scrollPaneU.setBounds(252, 54, 268, 150);
		frmAlquilerBicis.getContentPane().add(scrollPaneU);
		
		JLabel lblUsuarios = new JLabel("Usuarios:");
		lblUsuarios.setBounds(252, 28, 70, 15);
		frmAlquilerBicis.getContentPane().add(lblUsuarios);
		
		JLabel lblBicis = new JLabel("Bicis:");
		lblBicis.setBounds(553, 28, 70, 15);
		frmAlquilerBicis.getContentPane().add(lblBicis);
		
		//Crear modelo de la tabla bici
		
		DefaultTableModel modeloBici = new DefaultTableModel();
		modeloBici.addColumn("ID");
		modeloBici.addColumn("Usuario");
		
		// Añadir valores al iniciar el programa al modelo
		try {
			Connection con = ConnectionSingleton.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM bici");
			Object[] row = new Object[2];
			while (rs.next()) {
				row[0] = rs.getString("idbici");
				row[1] = rs.getString("usuario");
				modeloBici.addRow(row);
			}
			rs.close();
			stmt.close();
			con.close();

		} catch (SQLException e4) {
			JOptionPane.showMessageDialog(frmAlquilerBicis, e4.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		
		//Funcionamiento botón añadir para bici
		btnAddBici.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Connection con=ConnectionSingleton.getConnection();
					PreparedStatement insPstmt=con.prepareStatement("INSERT INTO bici VALUES (null, 0)");
					insPstmt.executeUpdate();
					insPstmt.close();
					
					//Refrescar tabla bici
					modeloBici.setRowCount(0);
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT * FROM bici");
					Object[] row = new Object[2];
					while (rs.next()) {
						row[0] = rs.getString("idbici");
						row[1] = rs.getString("usuario");
						modeloBici.addRow(row);
					}
					rs.close();
					stmt.close();
					con.close();
					JOptionPane.showMessageDialog(frmAlquilerBicis, "Bici añadida.");
				} catch (SQLException eAddBici) {
					JOptionPane.showMessageDialog(frmAlquilerBicis, eAddBici.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		
		tableBici = new JTable(modeloBici);
		tableBici.setBounds(553, 55, 265, 152);
		
		JScrollPane scrollPaneB = new JScrollPane(tableBici);
		scrollPaneB.setBounds(553, 55, 261, 150);
		frmAlquilerBicis.getContentPane().add(scrollPaneB);
		
		JLabel lblAlquilar = new JLabel("Alquilar bici:");
		lblAlquilar.setBounds(238, 233, 108, 14);
		frmAlquilerBicis.getContentPane().add(lblAlquilar);
		
		JLabel lblU = new JLabel("Usuario:");
		lblU.setBounds(238, 265, 62, 14);
		frmAlquilerBicis.getContentPane().add(lblU);
		
		JComboBox comboBoxUsuario = new JComboBox();
		comboBoxUsuario.setBounds(294, 261, 46, 22);
		//Añadir usuarios al comboBox
		try {
			Connection con=ConnectionSingleton.getConnection();
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery("SELECT idusuario FROM usuario WHERE idusuario !=0");
			while(rs.next()) {
				comboBoxUsuario.addItem(rs.getString("idusuario"));
			}
			rs.close();
			stmt.close();
			con.close();
			
		}catch(SQLException e) {
			JOptionPane.showMessageDialog(frmAlquilerBicis, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		frmAlquilerBicis.getContentPane().add(comboBoxUsuario);
		
		JLabel lblB = new JLabel("Bici:");
		lblB.setBounds(354, 265, 46, 14);
		frmAlquilerBicis.getContentPane().add(lblB);
		
		JComboBox comboBoxBici = new JComboBox();
		//Añadir bicis al comboBox
		try {
			Connection con=ConnectionSingleton.getConnection();
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery("SELECT idbici FROM bici");
			while(rs.next()) {
				comboBoxBici.addItem(rs.getString("idbici"));
			}
			rs.close();
			stmt.close();
			con.close();
		}catch(SQLException e) {
			JOptionPane.showMessageDialog(frmAlquilerBicis, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		comboBoxBici.setBounds(389, 261, 46, 22);
		frmAlquilerBicis.getContentPane().add(comboBoxBici);
		
		JButton btnAlquilar = new JButton("Alquilar");
		btnAlquilar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int idu=(Integer) comboBoxUsuario.getSelectedItem();
				int idb=(Integer) comboBoxBici.getSelectedItem();
				Connection con;
				try {
					con = ConnectionSingleton.getConnection();
					PreparedStatement pstmt=con.prepareStatement("SELECT COUNT(*), idbici AS cantidadBicis FROM bici WHERE usuario=?");
					pstmt.setInt(1, idu);
					ResultSet rs=pstmt.executeQuery();
					int cantidadBici=0;//la cantidad de bicis que está en uso por el usuario
					int idbConsulta=0;//id de la bici para luego comprobar que no esté alquilada
					while(rs.next()) {
						cantidadBici=rs.getInt("cantidadBicis");
						idbConsulta=rs.getInt("idbici");
					}
					rs.close();
					pstmt.close();
					//Realizar el alquilir sólo si el usuario no tiene ninguna bici alquilada
					if(cantidadBici>0) {
						JOptionPane.showMessageDialog(frmAlquilerBicis, "El usuario "+idu+" ya tiene una bici alquilada", "ERROR", JOptionPane.ERROR_MESSAGE);
					}else if(idbConsulta!=idb){
						JOptionPane.showMessageDialog(frmAlquilerBicis, "La bici "+idb+" ya está alquilada", "ERROR", JOptionPane.ERROR_MESSAGE);

					}else {
						PreparedStatement updPstmt=con.prepareStatement("UPDATE bici SET usuario=? WHERE idbici=?");
						updPstmt.setInt(1, idu);
						updPstmt.setInt(2, idb);
						updPstmt.executeUpdate();
						updPstmt.close();
						con.close();
						JOptionPane.showMessageDialog(frmAlquilerBicis, "Bici "+idb+" alquilada.");
					}
					
				} catch (SQLException eupd) {
					JOptionPane.showMessageDialog(frmAlquilerBicis, eupd.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnAlquilar.setBounds(308, 293, 89, 23);
		frmAlquilerBicis.getContentPane().add(btnAlquilar);

	}
}