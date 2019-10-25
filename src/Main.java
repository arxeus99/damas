import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import javax.swing.JSeparator;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import java.awt.Label;
import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;

public class Main {

	private JFrame frame;
	private JTextField textField;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
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
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Metodos m = new Metodos();
		m.cargar(m.getUsuarios());
		Usuario[] jugadores = new Usuario[2];
		JButton[][] botones = new JButton[8][4];
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.white);
		frame.setBounds(20, 50, 709, 639);
		frame.getContentPane().setLayout(null);
		
		JPanel elegir = new JPanel();			//
		elegir.setBackground(Color.PINK);		//
		elegir.setBounds(0, 0, 691, 592);		//
		elegir.setVisible(false);				// EL PANEL DONDE SE ELIGEN LOS USUARIOS QUE VAN A JUGAR
		elegir.setLayout(null);					//
		frame.getContentPane().add(elegir);		//
		
		JPanel partida = new JPanel();			//
		partida.setBounds(0, 0, 691, 592);		//
		frame.getContentPane().add(partida);	//
		partida.setBackground(Color.PINK);		// EL PANEL DEL TABLERO DONDE SE JUEGA LA PARTIDA
		partida.setVisible(false);				//
		partida.setLayout(null);				//
		
		JPanel menu = new JPanel();				//
		menu.setBounds(0, 0, 691, 592);			//
		frame.getContentPane().add(menu);		//
		menu.setBackground(Color.PINK);			// EL PANEL DEL MENÚ QUE ES EL PRIMERO QUE SE VE AL INICIAR
		menu.setVisible(true);					//
		menu.setLayout(null);					//
		
		JPanel usuarios = new JPanel();			//
		usuarios.setBounds(0, 0, 691, 592);		//
		usuarios.setBackground(Color.PINK);		//
		usuarios.setVisible(false);				// EL PANEL DONDE SE PUEDEN VER LOS USUARIOS
		usuarios.setLayout(null);				//
		frame.getContentPane().add(usuarios);	//
		
		JPanel crear = new JPanel();			//
		crear.setBounds(0, 0, 691, 592);		//
		crear.setBackground(Color.PINK);		//
		crear.setVisible(false);				// EL PANEL DONDE SE PUEDEN CREAR NUEVOS USUARIOS
		crear.setLayout(null);					//
		frame.getContentPane().add(crear);		//
		
		JLabel lblElijeLosUsuariosque = new JLabel("Elige los usuarios que van a jugar");
		lblElijeLosUsuariosque.setBounds(130, 84, 296, 16);
		elegir.add(lblElijeLosUsuariosque);
		
		JButton btnNewButton = new JButton("Jugar");
		JList list = new JList();
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				menu.setVisible(false);
				elegir.setVisible(true);
				
				m.paLaLista(list);
				list.setBounds(130, 139, 404, 321);
				elegir.add(list);
			}
		});
		btnNewButton.setBounds(47, 184, 161, 61);
		menu.add(btnNewButton);
		
		JButton btnConfirmar = new JButton("Confirmar");
		btnConfirmar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(list.getSelectedIndices().length == 2) {
					elegir.setVisible(false);
					partida.setVisible(true);
					int[] elegidos = list.getSelectedIndices();
					int contador = 0;
					for (int x=0; x<2; x++) {
						for(int i=0; i<m.getUsuarios().size(); i++) {
							if(i == elegidos[x]) {
								jugadores[contador] = m.getUsuarios().get(i);
								contador++;
							}
						}
					}
					
					JLabel lblNewLabel = new JLabel(jugadores[0].getNombre());
					jugadores[0].setColor("blancas");
					lblNewLabel.setBounds(79, 496, 56, 16);
					partida.add(lblNewLabel);
					
					JLabel label = new JLabel(jugadores[1].getNombre());
					jugadores[1].setColor("negras");
					label.setBounds(553, 83, 56, 16);
					partida.add(label);
					
					JLabel lblNewLabel_2 = new JLabel("Le toca a las "+m.getTurno());
					lblNewLabel_2.setBounds(67, 249, 161, 30);
					partida.add(lblNewLabel_2);
					m.setTurno("blancas");
					m.crearTablero(partida, botones, m, jugadores[0], jugadores[1], lblNewLabel_2);
					JButton btnVolverAlMen = new JButton("Volver al men\u00FA ");
					btnVolverAlMen.setBounds(12, 13, 200, 25);
					btnVolverAlMen.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent arg0) {
							partida.removeAll();
							partida.setVisible(false);
							menu.setVisible(true);
							m.setTurno("blancas");
							m.setPuntos1(0);
							m.setPuntos2(0);
						}
					});
					partida.add(btnVolverAlMen);
					m.setPuntos1(0);
					m.setPuntos2(0);
				}else {
					JOptionPane.showMessageDialog(null, "Debe seleccionar exactamente dos jugadores");
				}
			}
		});
		btnConfirmar.setBounds(470, 503, 97, 25);
		elegir.add(btnConfirmar);
		
		JButton btnVolverAlMen_1 = new JButton("Volver al men\u00FA");
		btnVolverAlMen_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				elegir.setVisible(false);
				menu.setVisible(true);
			}
		});
		btnVolverAlMen_1.setBounds(12, 13, 142, 25);
		elegir.add(btnVolverAlMen_1);
		
		
		JButton VolverAlMen = new JButton("Volver al men\u00FA ");
		VolverAlMen.setBounds(12, 13, 200, 25);
		VolverAlMen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				usuarios.setVisible(false);
				menu.setVisible(true);
			}
		});
		usuarios.add(VolverAlMen);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(81, 61, 572, 468);
		usuarios.add(scrollPane);
		
		table = new JTable();
		JButton btnNewButton_1 = new JButton("Mostrar Usuarios");
		btnNewButton_1.setBounds(47, 252, 161, 41);
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				menu.setVisible(false);
				usuarios.setVisible(true);
				scrollPane.setViewportView(table);
				m.paLaTabla(table);
				table.getColumnModel().getColumn(0).setResizable(true);
				table.getColumnModel().getColumn(0).setPreferredWidth(92);
				table.getColumnModel().getColumn(1).setResizable(true);
				table.getColumnModel().getColumn(1).setPreferredWidth(113);
				table.getColumnModel().getColumn(2).setResizable(true);
				table.getColumnModel().getColumn(2).setPreferredWidth(207);
			}
		});
		menu.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Crear Nuevo Usuario");
		btnNewButton_2.setBounds(47, 300, 161, 41);
		btnNewButton_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				menu.setVisible(false);
				crear.setVisible(true);
			}
		});
		menu.add(btnNewButton_2);
		
		JLabel lblNewLabel_1 = new JLabel("New label");
		lblNewLabel_1.setIcon(new ImageIcon(Main.class.getResource("/iconos/damas.jpg")));
		lblNewLabel_1.setBounds(0, 0, 691, 592);
		menu.add(lblNewLabel_1);
		
		JButton Volver = new JButton("Volver al men\u00FA ");
		Volver.setBounds(12, 13, 200, 25);
		Volver.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				crear.setVisible(false);
				menu.setVisible(true);
			}
		});
		crear.add(Volver);
		
		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setBounds(176, 106, 56, 16);
		crear.add(lblNombre);
		
		textField = new JTextField();
		textField.setBounds(256, 104, 116, 22);
		crear.add(textField);
		textField.setColumns(10);
		
		JButton btnCrear = new JButton("Crear");
		btnCrear.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				m.insertar(textField);
				textField.setText("");
				JOptionPane.showMessageDialog(null, "Usuario creado con exito");
			}
		});
		btnCrear.setBounds(176, 415, 97, 25);
		crear.add(btnCrear);
		
	}
}
