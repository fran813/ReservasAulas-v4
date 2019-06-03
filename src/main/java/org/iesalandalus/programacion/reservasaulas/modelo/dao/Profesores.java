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

import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Profesor;

public class Profesores {

	private static final String NOMBRE_FICHERO_PROFESORES = "ficheros" + File.separator + "profesores.dat";
	
	private List<Profesor> coleccionProfesores;

	public Profesores() {

		coleccionProfesores = new ArrayList<>();

	}

	public Profesores(Profesores profesores) {

		setProfesores(profesores);

	}

	private void setProfesores(Profesores profesores) {

		if (profesores == null) {
			throw new IllegalArgumentException("No se pueden copiar profesores nulos.");
		}
		coleccionProfesores = copiaProfundaProfesores(profesores.coleccionProfesores);
	}

	private List<Profesor> copiaProfundaProfesores(List<Profesor> profesores) {

		List<Profesor> otrosProfesores = new ArrayList<>();
		for (Profesor profesor : profesores) {
			otrosProfesores.add(new Profesor(profesor));
		}

		return otrosProfesores;
	}

	public List<Profesor> getProfesores() {

		return copiaProfundaProfesores(coleccionProfesores);

	}

	public int getNumProfesores() {

		return coleccionProfesores.size();

	}

	public void insertar(Profesor profesor) throws OperationNotSupportedException {

		if (profesor == null) {
			throw new IllegalArgumentException("No se puede insertar un profesor nulo.");
		}
		if (coleccionProfesores.contains(profesor)) {
			throw new OperationNotSupportedException("El profesor ya existe.");
		} else {
			coleccionProfesores.add(new Profesor(profesor));
		}
	}

	public Profesor buscar(Profesor profesor) {

		int indice = coleccionProfesores.indexOf(profesor);
		if (indice != -1) {
			return new Profesor(coleccionProfesores.get(indice));
		} else {
			return null;
		}
	}

	public void borrar(Profesor profesor) throws OperationNotSupportedException {

		if (profesor == null) {
			throw new IllegalArgumentException("No se puede borrar un profesor nulo.");
		}
		if (!coleccionProfesores.remove(profesor)) {
			throw new OperationNotSupportedException("El profesor a borrar no existe.");
		}
	}

	public List<String> representar() {

		List<String> representacion = new ArrayList<>();
		for (Profesor profesor : coleccionProfesores) {
			representacion.add(profesor.toString());
		}
		return representacion;
	}
	
	public void leer() {
		File fichero = new File(NOMBRE_FICHERO_PROFESORES);
		try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(fichero))) {
			Profesor profesor = null;
			do {
				profesor = (Profesor) entrada.readObject();
				insertar(profesor);
			} while (profesor != null);
		} catch (ClassNotFoundException e) {
			System.out.println("No puedo encontrar el profesor que tengo que leer.");
		} catch (FileNotFoundException e) {
			System.out.println("No puedo abrir el fichero de profesores.");
		} catch (EOFException e) {
			System.out.println("Fichero profesores leído satisfactoriamente.");
		} catch (IOException e) {
			System.out.println("Error inesperado de Entrada/Salida.");
		} catch (OperationNotSupportedException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void escribir() {
		File fichero = new File(NOMBRE_FICHERO_PROFESORES);
		try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(fichero))){
			for (Profesor profesor : coleccionProfesores)
				salida.writeObject(profesor);
			System.out.println("Fichero profesores escrito satisfactoriamente.");
		} catch (FileNotFoundException e) {
			System.out.println("No puedo crear el fichero de profesores");
		} catch (IOException e) {
			System.out.println("Error inesperado de Entrada/Salida");
		}
	}
}

