package com.mayab.desarrollo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="Usuarios")
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private int id;
	@Column(name = "nombre", nullable = false, length = 100)
	private String nombre;
	@Column(name = "password", nullable = false, length = 100)
	private String password;
	@Column(name = "email", nullable = false, length = 100)
	private String email;
	
	public Usuario(int id, String nombre, String password, String email) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.password = password;
		this.email = email;
	}
	public Usuario() {
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString(){
		return this != null ? String.format("[USUARIO %d nombre: %s email: %s ] ", id,nombre,email) : "No existe el usuario";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {return true;}
		if (obj == null || getClass() != obj.getClass()) {return false;}
		Usuario objeto = (Usuario) obj;
		return this.id == objeto.id && this.nombre.equals(objeto.nombre) && this.email.equals(objeto.email) && this.password.equals(objeto.password);
	}


}
