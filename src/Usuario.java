

public class Usuario {

	private String nombre;
	private String id;
	private int puntuacion;
	private String color;
	
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getPuntuacion() {
		return puntuacion;
	}
	public void setPuntuacion(int puntuacion) {
		this.puntuacion = puntuacion;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color=color;
	}
	public Usuario(String id, String nombre, int puntuacion) {
		this.nombre = nombre;
		this.id = id;
		this.puntuacion = puntuacion;
	}
	
	
}
