package Mauro;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Videojuego {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String titulo;
	private String descripcion;
	private float precio;
	private int stock;
	private boolean disponible;
	private String estaDisponible;
	@OneToMany
	private List<Valoracion> valoraciones= new ArrayList<>();
		
	public Videojuego(String titulo, String descripcion, float precio, int stock) {
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.precio = precio;
		this.stock = stock;
		if(this.stock > 0){
			this.disponible = true;
		}else{
			this.disponible = false;
		}
		this.setEstaDisponible();
	}
	public Videojuego(){
		
	}

	// Getters y Setters
	
	public String getTitulo(){
		return this.titulo;
	}
	
	public void setEstaDisponible() {
		if(this.disponible){
			this.estaDisponible="Disponible para la compra.";
		}else{
			this.estaDisponible="No se encuntra disponible para la venta.";
		}
	}
	public void setTitulo(String titulo){
		this.titulo=titulo;		
	}
	
	public String getDescripcion(){
		return this.descripcion;
	}
	
	public void setDescripcion(String descripcion){
		this.descripcion=descripcion;
	}
	
	public float getPrecio(){
		return this.precio;
	}
	
	public void setPrecio(float precio){
		this.precio=precio; 
	}
	
	public int getStock(){
		return this.stock;
	}
	
	public void setStock(int stock){
		this.stock=stock;
	}
	
	public long getID(){
		return this.id;
	}
	
	public boolean isDisponible() {
		return disponible;
	}
	
	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}
	
	public List<Valoracion> getValoraciones() {
		return valoraciones;
	}
	
	public void setValoraciones(List<Valoracion> valoraciones) {
		this.valoraciones = valoraciones;
	}
	
	@Override
	public String toString() {
		if (this.disponible=true){
			return titulo + " se encuentra disponible para la venta.";
		}else{
			return titulo + " no se encuentra disponible para la venta actualmente.";
		}
	}
					

	
	
}