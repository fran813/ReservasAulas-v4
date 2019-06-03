package org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia;

import java.io.Serializable;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Permanencia implements Serializable{

	protected LocalDate dia;
	protected static final DateTimeFormatter FORMATO_DIA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	protected Permanencia() {
	}

	protected Permanencia(LocalDate dia) {

		this.setDia(dia);
	}
	
	protected Permanencia(String dia) {
		
		this.setDia(dia);
	}


	public LocalDate getDia() {
		return this.dia;
	}

	protected void setDia(LocalDate dia) {
		
		if(dia == null) {
			throw new IllegalArgumentException("El día de una permanencia no puede ser nulo.");
		}
		this.dia = dia;
	}
	
	protected void setDia(String dia) {
		
		if(dia == null) {
			throw new IllegalArgumentException("El día de una permanencia no puede ser nulo.");
		}
		try {
			this.dia = LocalDate.parse(dia,FORMATO_DIA);
		}catch(DateTimeException e) {
			throw new IllegalArgumentException("El formato del día de la permanencia no es correcto.");
		}
		
	}
	public abstract int getPuntos();
	
	@Override
    public abstract String toString();

	@Override
	public abstract int hashCode();

	@Override
	public abstract boolean equals(Object obj);
	
	
	
	
}
