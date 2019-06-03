package org.iesalandalus.programacion.reservasaulas.modelo.dao;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.naming.OperationNotSupportedException;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Aula;


public class Aulas {
	
	private static final String NOMBRE_FICHERO_AULAS = "ficheros" + File.separator + "aulas.dat";
	private List<Aula>coleccionAulas;
	
	public Aulas() {

		coleccionAulas = new ArrayList<>();

	}
	
	public Aulas(Aulas aulas) {
		
		setAulas(aulas);
		
	}
	
	private void setAulas(Aulas aulas) {
	
		if (aulas == null) {
			throw new IllegalArgumentException("No se pueden copiar aulas nulas.");
		}
			coleccionAulas = copiaProfundaAulas(aulas.coleccionAulas);
	}

	private List<Aula> copiaProfundaAulas(List<Aula> aulas) {
		
		   List<Aula> otrasAulas = new ArrayList<>();
			for (Aula aula : aulas) {
				otrasAulas.add(new Aula(aula));
			}
		        
		        return otrasAulas;
	}

	public List<Aula> getAulas() {
		
		return copiaProfundaAulas(coleccionAulas) ;
		
	}
	public int getNumAulas() {
		
		return coleccionAulas.size();
		
	}
	
	public void insertar(Aula aula) throws OperationNotSupportedException{
		
		if (aula ==null) {
			throw new IllegalArgumentException("No se puede insertar un aula nula.");
		}
		if (coleccionAulas.contains(aula)) {
			throw new OperationNotSupportedException("El aula ya existe.");
		} else {
			coleccionAulas.add(new Aula(aula));
		}
	}
		
	
	
	public Aula buscar(Aula aula) {
		
		int indice = coleccionAulas.indexOf(aula);
		if (indice != -1) {
			return new Aula(coleccionAulas.get(indice));
		}else {
			return null;
		}
	}
	
	public void borrar (Aula aula) throws OperationNotSupportedException{
		
		if (aula == null) {
			throw new IllegalArgumentException("No se puede borrar un aula nula.");
		}
		if (!coleccionAulas.remove(aula)) {
			throw new OperationNotSupportedException("El aula a borrar no existe.");
		}
	}
			
	public List<String> representar() {
		
		List<String> representacion = new ArrayList<>();
        for (Aula aula : coleccionAulas) {
                representacion.add(aula.toString());
        }
        return representacion;
	}

	public void leer() {
		File fichero = new File(NOMBRE_FICHERO_AULAS);
		try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(fichero))) {
			Aula aula = null;
			do {
				aula = (Aula) entrada.readObject();
				insertar(aula);
			} while (aula != null);
		} catch (ClassNotFoundException e) {
			System.out.println("No puedo encontrar el aula que tengo que leer.");
		} catch (FileNotFoundException e) {
			System.out.println("No puedo abrir el fichero de aulas.");
		} catch (EOFException e) {
			System.out.println("Fichero aulas leído satisfactoriamente.");
		} catch (IOException e) {
			System.out.println("Error inesperado de Entrada/Salida.");
		} catch (OperationNotSupportedException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void escribir() {
		File fichero = new File(NOMBRE_FICHERO_AULAS);
		try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(fichero))){
			for (Aula aula : coleccionAulas)
				salida.writeObject(aula);
			System.out.println("Fichero aulas escrito satisfactoriamente.");
		} catch (FileNotFoundException e) {
			System.out.println("No puedo crear el fichero de aulas");
		} catch (IOException e) {
			System.out.println("Error inesperado de Entrada/Salida");
		}
	}
}

