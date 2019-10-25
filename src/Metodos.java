
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Metodos {
	
	private String turno = "blancas";
	
	private ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
	
	private int puntos1 = 0;
	
	private int puntos2 = 0;
	
	public void setTurno(String turno) {
		this.turno = turno;
	}
	
	public String getTurno() {
		return turno;
	}
	
	public void setPuntos1(int puntos) {
		this.puntos1 = puntos;
	}
	
	public int getPuntos1() {
		return puntos1;
	}
	
	public void setPuntos2(int puntos) {
		this.puntos2 = puntos;
	}
	
	public int getPuntos2() {
		return puntos2;
	}

	public ArrayList<Usuario> getUsuarios(){
		return usuarios;
	}

	public void cargar(ArrayList<Usuario> usuarios) { // ESTE METODO VA A LEER LA BASE DE DATOS Y CARGAR LOS USUARIOS EN UN ARRAYLIST
		try {
			Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/damas?user=root&password=1234");
			Statement st = (Statement) con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM usuarios ORDER BY Puntuacion DESC;");
			while(rs.next()) {
				String id = rs.getString("idUsuarios");
				String nombre = rs.getString("Nombre");
				String puntos = rs.getString("Puntuacion");
				int num = Integer.parseInt(puntos);
				Usuario u = new Usuario(id, nombre, num);
				usuarios.add(u);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		this.usuarios = usuarios;
	}
	
	public void paLaTabla(JTable table) {  // ESTE METODO VA A INSERTAR LOS USUARIOS EN UNA TABLA PARA PODER VER LA INFORMACIÓN DE ESTOS
		Object[][] datos = new Object[usuarios.size()][3];
		int contador = 0;
		for(int i=0; i<usuarios.size(); i++){
			String[] este = {usuarios.get(i).getId(), usuarios.get(i).getNombre(), ""+usuarios.get(i).getPuntuacion()};
			datos[contador] = este;
			contador++;
		}
		table.setModel(new DefaultTableModel(
				datos,
				new String[] {
					"ID", "Nombre", "Puntuaci\u00F3n"
				}
			) {
				boolean[] columnEditables = new boolean[] {
					false, false, false
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			});
	}
	
	public void paLaLista(JList list) {	// ESTE METODO VA A INSERTAR LOS NOMBRES DE LOS USUARIOS EN UNA LISTA PARA PODER ELEGIRLOS A LA HORA DE JUGAR LA PARTIDA
		String[] nombres = new String[usuarios.size()];
		int contador = 0;
		for(int i=0; i<usuarios.size(); i++) {
			nombres[contador] = usuarios.get(i).getNombre();
			contador++;
		}
		list.setModel(new AbstractListModel() {
			String[] values = nombres;
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
	}
	
	public void elegir(JButton b, JButton[][] c, Metodos m, Usuario u1, Usuario u2) { // ESTE METODO ES EL QUE SE EJECUTA CADA VEZ QUE SE PULSA UN BOTÓN DEL TABLERO
		boolean partida = true;
		if(!m.fichasNegras(c)) {  // COMPRUEBA SI NO QUEDAN FICHAS NEGRAS, DE SER ASÍ LA PARTIDA ACABARÍA Y GANARÍAN LAS BLANCAS
			JOptionPane.showMessageDialog(null, "¡Ganan las blancas! ¡Felicidades!");
			partida = false;
		}
		if(!m.fichasBlancas(c)) { // COMPRUEBA SI NO QUEDAN FICHAS BLANCAS, DE SER ASÍ LA PARTIDA ACABARÍA Y GANARÍAN LAS NEGRAS
			JOptionPane.showMessageDialog(null, "¡Ganan las negras! ¡Felicidades!");
			partida = false;
		}
		if(partida == true) {
			boolean encontrado = false;
			if(b.getName().equals("blanca") || b.getName().equals("negra")) {
				for(int i=0; i<4; i++) {
					for(int e=0; e<8; e++) {
						if(c[e][i].getName().equals("seleccionadab") || c[e][i].getName().equals("seleccionadan")) {
							encontrado = true;
						}
					}
				}
				if(encontrado == false) {
					if(b.getName().equals("blanca") & m.getTurno().equals("blancas")) {
						b.setName("seleccionadab");
						b.setIcon(new ImageIcon(Main.class.getResource("/iconos/seleccionada.png")));
					}
					if(b.getName().equals("negra") & m.getTurno().equals("negras")) {
						b.setName("seleccionadan");
						b.setIcon(new ImageIcon(Main.class.getResource("/iconos/seleccionada.png")));
					}
				}
			}
			else {
				if(b.getName().equals("vacia")) {
					for(int i=0; i<4; i++) {
						for(int e=0; e<8; e++) {
							if(c[e][i].getName().equals("seleccionadan")) {
								if(!m.comer(c[e][i], c)) {
									if(b.getX()==c[e][i].getX()+40 & b.getY() == c[e][i].getY()+40 || b.getX()==c[e][i].getX()-40 & b.getY() == c[e][i].getY()+40 ) {
										b.setIcon(new ImageIcon(Main.class.getResource("/iconos/negra.png")));
										b.setName("negra");
										m.setTurno("blancas");
										c[e][i].setIcon(null);
										c[e][i].setName("vacia");
									}
								}else {
									if(b.getX()==c[e][i].getX()+80 & b.getY() == c[e][i].getY()+80) {
										for(int j=0; j<4; j++) {
											for(int s=0; s<8; s++) {
												if(c[s][j].getName().equals("blanca") & c[s][j].getX() == c[e][i].getX()+40 & c[s][j].getY() == c[e][i].getY()+40) {
													c[s][j].setName("vacia");
													c[s][j].setIcon(null);
													b.setIcon(new ImageIcon(Main.class.getResource("/iconos/negra.png")));
													b.setName("negra");
													if(!m.comer(b, c)) {
														m.setTurno("blancas");
													}
													c[e][i].setIcon(null);
													c[e][i].setName("vacia");
													m.setPuntos2(m.getPuntos2()+2);
												}
											}
										}
									}
									if(b.getX()==c[e][i].getX()-80 & b.getY() == c[e][i].getY()+80) {
										for(int j=0; j<4; j++) {
											for(int s=0; s<8; s++) {
												if(c[s][j].getName().equals("blanca") & c[s][j].getX() == c[e][i].getX()-40 & c[s][j].getY() == c[e][i].getY()+40) {
													c[s][j].setName("vacia");
													c[s][j].setIcon(null);
													b.setIcon(new ImageIcon(Main.class.getResource("/iconos/negra.png")));
													b.setName("negra");
													if(!m.comer(b, c)) {
														m.setTurno("blancas");
													}
													c[e][i].setIcon(null);
													c[e][i].setName("vacia");
													m.setPuntos2(m.getPuntos2()+2);
												}
											}
										}
									}
								}
							}
							if(c[e][i].getName().equals("seleccionadab")) {
								if(!m.comer(c[e][i], c)) {
									if(b.getX()==c[e][i].getX()+40 & b.getY() == c[e][i].getY()-40 || b.getX()==c[e][i].getX()-40 & b.getY() == c[e][i].getY()-40) {
										b.setIcon(new ImageIcon(Main.class.getResource("/iconos/blanca.png")));
										b.setName("blanca");
										m.setTurno("negras");
										c[e][i].setIcon(null);
										c[e][i].setName("vacia");
									}
								}else {
									if(b.getX()==c[e][i].getX()+80 & b.getY() == c[e][i].getY()-80) {
										for(int j=0; j<4; j++) {
											for(int s=0; s<8; s++) {
												if(c[s][j].getName().equals("negra") & c[s][j].getX() == c[e][i].getX()+40 & c[s][j].getY() == c[e][i].getY()-40) {
													c[s][j].setName("vacia");
													c[s][j].setIcon(null);
													b.setIcon(new ImageIcon(Main.class.getResource("/iconos/blanca.png")));
													b.setName("blanca");
													if(!m.comer(b, c)) {
														m.setTurno("negras");
													}
													c[e][i].setIcon(null);
													c[e][i].setName("vacia");
													m.setPuntos1(m.getPuntos1()+2);
												}
											}
										}
									}
									if(b.getX()==c[e][i].getX()-80 & b.getY() == c[e][i].getY()-80) {
										for(int j=0; j<4; j++) {
											for(int s=0; s<8; s++) {
												if(c[s][j].getName().equals("negra") & c[s][j].getX() == c[e][i].getX()-40 & c[s][j].getY() == c[e][i].getY()-40) {
													c[s][j].setName("vacia");
													c[s][j].setIcon(null);
													b.setIcon(new ImageIcon(Main.class.getResource("/iconos/blanca.png")));
													b.setName("blanca");
													if(!m.comer(b, c)) {
														m.setTurno("negras");
													}
													c[e][i].setIcon(null);
													c[e][i].setName("vacia");
													m.setPuntos1(m.getPuntos1()+2);
												}
											}
										}
									}
								}
							}
						}
					}
				}
				if(b.getName().equals("seleccionadab")) {
					b.setName("blanca");
					b.setIcon(new ImageIcon(Main.class.getResource("/iconos/blanca.png")));
				}
				if(b.getName().equals("seleccionadan")) {
					b.setName("negra");
					b.setIcon(new ImageIcon(Main.class.getResource("/iconos/negra.png")));
				}
			}
			if(!m.fichasNegras(c) || !m.fichasBlancas(c)) {
				if(!m.fichasNegras(c)) {
					JOptionPane.showMessageDialog(null, "¡Ganan las blancas! ¡Felicidades!");
					u1.setPuntuacion(u1.getPuntuacion()+5+m.getPuntos1());
					u2.setPuntuacion(u2.getPuntuacion()+m.getPuntos2());
					partida = false;
				}else {
					JOptionPane.showMessageDialog(null, "¡Ganan las negras! ¡Felicidades!");
					u1.setPuntuacion(u1.getPuntuacion()+m.getPuntos1());
					u2.setPuntuacion(u2.getPuntuacion()+5+m.getPuntos2());
					partida = false;
				}
				try {
					Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/damas?user=root&password=1234");
					Statement st = (Statement) con.createStatement();
					st.execute("UPDATE `damas`.`usuarios` SET `Puntuacion` = '"+u1.getPuntuacion()+"' WHERE (`idUsuarios` = '"+u1.getId()+"')");
					st.execute("UPDATE `damas`.`usuarios` SET `Puntuacion` = '"+u2.getPuntuacion()+"' WHERE (`idUsuarios` = '"+u2.getId()+"')");
					st.close();
					con.close();
					
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public void insertar(JTextField t) { // ESTE METODO VA A CREAR UN NUEVO USUARIO Y A INSERTARLO EN LA BASE DE DATOS
		try {
			Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/damas?user=root&password=1234");
			Statement st = (Statement) con.createStatement();
			st.execute("INSERT INTO usuarios (Nombre) VALUES ('"+ t.getText() +"') ");
			st.close();
			con.close();
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		int numero = usuarios.size()+1;
		Usuario u = new Usuario (""+numero, t.getText(), 0);
		this.usuarios.add(u);
	}
	
	public void crearTablero(JPanel p, JButton[][] botones, Metodos m, Usuario u1, Usuario u2, JLabel  l) { // ESTE METODO VA A CREAR EL TABLERO Y A INSERTAR LOS BOTONES EN UNA MATRIZ 
		for(int i=0; i<4; i++) {
			int x = 247;
			int y = 148;
			y = y+(i*80);
			for(int e=0; e<4; e++) {
				JPanel rojo = new JPanel();
				rojo.setBackground(Color.RED);
				rojo.setBounds(x, y, 40, 40);
				p.add(rojo);
				x = x+80;
			}
			x = 207;
			y = 188;
			y = y+(i*80);
			for(int j=0; j<4; j++) {
				JPanel rojo = new JPanel();
				rojo.setBackground(Color.RED);
				rojo.setBounds(x, y, 40, 40);
				p.add(rojo);
				x = x+80;
			}
		}
		int contador= 0;
		for(int i=0; i<3; i++) {
			int x = 207;
			int y = 148;
			y = y+(contador*40);
			x = x+(contador%2*40);
			for(int e=0; e<4; e++) {
				JButton negra = new JButton("");
				negra.setBounds(x, y, 40, 40);
				negra.setBackground(Color.BLACK);
				negra.setIcon(new ImageIcon(Main.class.getResource("/iconos/negra.png")));
				negra.setName("negra");
				p.add(negra);
				botones[contador][e] = negra;
				negra.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						m.elegir(negra, botones, m, u1, u2);
						l.setText("Le toca a las "+m.getTurno());
					}
				});
				x = x+80;
			}
			contador++;
		}
		for(int i=0; i<2; i++) {
			int x = 207;
			int y = 148;
			y = y+(contador*40);
			x = x+(contador%2*40);
			for(int e=0; e<4; e++) {
				JButton vacia = new JButton("");
				vacia.setBounds(x, y, 40, 40);
				vacia.setBackground(Color.BLACK);
				vacia.setName("vacia");
				p.add(vacia);
				botones[contador][e] = vacia;
				vacia.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						m.elegir(vacia, botones, m, u1, u2);
						l.setText("Le toca a las "+m.getTurno());
					}
				});
				x = x+80;
			}
			contador++;
		}
		for(int i=0; i<3; i++) {
			int x = 207;
			int y = 148;
			y = y+(contador*40);
			x = x+(contador%2*40);
			for(int e=0; e<4; e++) {
				JButton negra = new JButton("");
				negra.setBounds(x, y, 40, 40);
				negra.setBackground(Color.BLACK);
				negra.setIcon(new ImageIcon(Main.class.getResource("/iconos/blanca.png")));
				negra.setName("blanca");
				p.add(negra);
				botones[contador][e] = negra;
				negra.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						m.elegir(negra, botones, m, u1, u2);
						l.setText("Le toca a las "+m.getTurno());
					}
				});
				x = x+80;
			}
			contador++;
		}
	}

	public boolean comer(JButton b, JButton[][] c) { // ESTE METODO COMPRUEBA SI EL BOTÓN (FICHA) QUE LE PASAS POR PARAMETRO PUEDE COMER EN ESE MOMENTO Y DEVUELVE UN BOOLEANO
		boolean comer = false;
		if(b.getName().equals("negra") || b.getName().equals("seleccionadan")) {
			for(int i=0; i<4; i++) {
				for(int e=0; e<8; e++) {
					if(c[e][i].getX()==b.getX()+40 & c[e][i].getY()==b.getY()+40) {
						if(c[e][i].getName().equals("blanca")) {
							for(int a=0; a<4; a++) {
								for(int o=0; o<8; o++) {
									if(c[o][a].getX()==b.getX()+80 & c[o][a].getY()==b.getY()+80) {
										if(c[o][a].getName().equals("vacia"))
											comer = true;
									}
								}
							}
						}
					}
					if(c[e][i].getX()==b.getX()-40 & c[e][i].getY()==b.getY()+40) {
						if(c[e][i].getName().equals("blanca")) {
							for(int a=0; a<4; a++) {
								for(int o=0; o<8; o++) {
									if(c[o][a].getX()==b.getX()-80 & c[o][a].getY()==b.getY()+80) {
										if(c[o][a].getName().equals("vacia"))
											comer = true;
									}
								}
							}
						}
					}
				}
			}
		}
		if(b.getName().equals("blanca") || b.getName().equals("seleccionadab")) {
			for(int i=0; i<4; i++) {
				for(int e=0; e<8; e++) {
					if(c[e][i].getX()==b.getX()+40 & c[e][i].getY()==b.getY()-40) {
						if(c[e][i].getName().equals("negra")) {
							for(int a=0; a<4; a++) {
								for(int o=0; o<8; o++) {
									if(c[o][a].getX()==b.getX()+80 & c[o][a].getY()==b.getY()-80) {
										if(c[o][a].getName().equals("vacia"))
											comer = true;
									}
								}
							}
						}
					}
					if(c[e][i].getX()==b.getX()-40 & c[e][i].getY()==b.getY()-40) {
						if(c[e][i].getName().equals("negra")){
							for(int a=0; a<4; a++) {
								for(int o=0; o<8; o++) {
									if(c[o][a].getX()==b.getX()-80 & c[o][a].getY()==b.getY()-80) {
										if(c[o][a].getName().equals("vacia"))
											comer = true;
									}
								}
							}
						}
					}
				}
			}
		}
		return comer;
	}

	public boolean fichasBlancas(JButton[][] c) { // ESTE METODO COMPRUEBA SI QUEDAN FICHAS BLANCAS Y DEVUELVE UN BOOLEANO
		boolean partida = false;
		for(int i=0; i<4; i++) {
			for(int e=0; e<8; e++) {
				if(c[e][i].getName().equals("blanca") || c[e][i].getName().equals("seleccionadab"))
					partida = true;
			}
		}
		return partida;
	}
	
	public boolean fichasNegras(JButton[][] c) { // ESTE METODO COMPRUEBA SI QUEDAN FICHAS NEGRAS Y DEVUELVE UN BOOLEANO
		boolean partida = false;
		for(int i=0; i<4; i++) {
			for(int e=0; e<8; e++) {
				if(c[e][i].getName().equals("negra") || c[e][i].getName().equals("seleccionadan"))
					partida = true;
			}
		}
		return partida;
	}
}
