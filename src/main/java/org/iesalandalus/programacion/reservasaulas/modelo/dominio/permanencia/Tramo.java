package org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia;

public enum Tramo {

	MANANA("Mañana"), TARDE("Tarde");

	private String cadenaAMostrar;

	private Tramo(String cadenaAMostrar) {

		this.cadenaAMostrar = cadenaAMostrar;
	}

	public String toString() {

		return this.cadenaAMostrar;

	}
}
