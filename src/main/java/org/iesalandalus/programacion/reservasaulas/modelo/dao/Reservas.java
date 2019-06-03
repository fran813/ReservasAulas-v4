package org.iesalandalus.programacion.reservasaulas.modelo.dao;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.naming.OperationNotSupportedException;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.Permanencia;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.PermanenciaPorHora;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.PermanenciaPorTramo;

public class Reservas {

	private List<Reserva> coleccionReservas;
	private static final float MAX_PUNTOS_PROFESOR_MES = 200f;
	private static final String NOMBRE_FICHERO_RESERVAS = "ficheros" + File.separator + "reservas.dat";

	public Reservas() {

		coleccionReservas = new ArrayList<>();

	}

	public Reservas(Reservas reservas) {

		setReservas(reservas);

	}

	private void setReservas(Reservas reservas) {

		if (reservas == null) {
			throw new IllegalArgumentException("No se pueden copiar reservas nulas.");
		}
		coleccionReservas = copiaProfundaReservas(reservas.coleccionReservas);
	}

	private List<Reserva> copiaProfundaReservas(List<Reserva> reservas) {

		List<Reserva> otrasReservas = new ArrayList<>();
		for (Reserva reserva : reservas) {
			otrasReservas.add(new Reserva(reserva));
		}
		return otrasReservas;
	}

	public List<Reserva> getReservas() {

		return copiaProfundaReservas(coleccionReservas);

	}

	public int getNumReservas() {

		return coleccionReservas.size();

	}

	public void insertar(Reserva reserva) throws OperationNotSupportedException {

		if (reserva == null)
			throw new IllegalArgumentException("No se puede realizar una reserva nula.");
		if (coleccionReservas.contains(reserva))
			throw new OperationNotSupportedException("La reserva ya existe.");
		if (getPuntosGastadosReserva(reserva) > MAX_PUNTOS_PROFESOR_MES)
			throw new OperationNotSupportedException(
					"Esta reserva excede los puntos máximos por mes para dicho profesor.");
		if (!esMesSiguienteOPosterior(reserva))
			throw new OperationNotSupportedException(
					"Sólo se pueden hacer reservas para el mes que viene o posteriores.");

		Reserva reservaActual = getReservaDia(reserva.getPermanencia().getDia());

		if (reservaActual != null) {
			if (reservaActual.getPermanencia() instanceof PermanenciaPorTramo
					&& reserva.getPermanencia() instanceof PermanenciaPorHora)
				throw new OperationNotSupportedException(
						"Ya se ha realizado una reserva por tramo para este día y aula.");
			if (reservaActual.getPermanencia() instanceof PermanenciaPorHora
					&& reserva.getPermanencia() instanceof PermanenciaPorTramo)
				throw new OperationNotSupportedException(
						"Ya se ha realizado una reserva por hora para este día y aula.");
		}
		coleccionReservas.add(new Reserva(reserva));
	}

	private boolean esMesSiguienteOPosterior(Reserva reserva) {

		int mesActual = LocalDate.now().getMonthValue();
		int mesDeReserva = reserva.getPermanencia().getDia().getMonthValue();
		int anoActual = LocalDate.now().getYear();
		int anoDeReserva = reserva.getPermanencia().getDia().getYear();

		return (mesDeReserva > mesActual) || anoDeReserva > anoActual;
	}

	private float getPuntosGastadosReserva(Reserva reserva) {

		float puntosGastados = 0f;
		List<Reserva> lista = getReservasProfesorMes(reserva.getProfesor(), reserva.getPermanencia().getDia());
		for (Reserva reser : lista) {
			puntosGastados += reser.getPuntos();
		}
		return puntosGastados + reserva.getPuntos();

	}

	private List<Reserva> getReservasProfesorMes(Profesor profesor, LocalDate dia) {

		List<Reserva> reservasProfesorMes = new ArrayList<>();
		for (Reserva reserva : coleccionReservas) {
			if (reserva.getProfesor().equals(profesor)
					&& reserva.getPermanencia().getDia().getMonthValue() == dia.getMonthValue()
					&& reserva.getPermanencia().getDia().getYear() == dia.getYear()) {
				reservasProfesorMes.add(new Reserva(reserva));
			}
		}
		return reservasProfesorMes;
	}

	private Reserva getReservaDia(LocalDate dia) {

		Reserva reservaHecha = null;
		for (Reserva reserva : coleccionReservas) {
			if (reserva.getPermanencia().getDia().equals(dia)) {
				reservaHecha = new Reserva(reserva);
			}
		}
		return reservaHecha;

	}

	public Reserva buscar(Reserva reserva) {

		int indice = coleccionReservas.indexOf(reserva);
		if (indice != -1) {
			return new Reserva(coleccionReservas.get(indice));
		} else {
			return null;
		}
	}

	public void borrar(Reserva reserva) throws OperationNotSupportedException {

		if (reserva == null) {
			throw new IllegalArgumentException("No se puede anular una reserva nula.");
		}
		if (coleccionReservas.contains(reserva)) {
			coleccionReservas.remove(reserva);
		} else {
			throw new OperationNotSupportedException("La reserva a anular no existe.");
		}
	}

	public List<String> representar() {

		List<String> representacion = new ArrayList<>();
		for (Reserva reserva : coleccionReservas) {
			representacion.add(reserva.toString());
		}
		return representacion;
	}

	public List<Reserva> getReservasProfesor(Profesor profesor) {

		List<Reserva> reservasProfesor = new ArrayList<>();
		for (Reserva reserva : coleccionReservas) {
			if (reserva.getProfesor().equals(profesor)) {
				reservasProfesor.add(new Reserva(reserva));
			}
		}
		return reservasProfesor;
	}

	public List<Reserva> getReservasAula(Aula aula) {

		List<Reserva> reservasAula = new ArrayList<>();
		for (Reserva reserva : coleccionReservas) {
			if (reserva.getAula().equals(aula)) {
				reservasAula.add(new Reserva(reserva));
			}
		}
		return reservasAula;
	}

	public List<Reserva> getReservasPermanencia(Permanencia permanencia) {

		List<Reserva> reservasPermanencia = new ArrayList<>();
		for (Reserva reserva : coleccionReservas) {
			if (reserva.getPermanencia().equals(permanencia)) {
				reservasPermanencia.add(new Reserva(reserva));
			}
		}
		return reservasPermanencia;
	}

	public boolean consultarDisponibilidad(Aula aula, Permanencia permanencia) {

		if (aula == null) {
			throw new IllegalArgumentException("No se puede consultar la disponibilidad de un aula nula.");
		}
		if (permanencia == null) {
			throw new IllegalArgumentException("No se puede consultar la disponibilidad de una permanencia nula.");
		}

		for (Reserva reserva : coleccionReservas) {
			if (reserva.getAula().equals(aula) && reserva.getPermanencia().getDia().equals(permanencia.getDia())) {
				return false;
			}
		}
		return true;
	}
	
	public void leer() {
		File fichero = new File(NOMBRE_FICHERO_RESERVAS);
		try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(fichero))) {
			Reserva reserva = null;
			do {
				reserva = (Reserva) entrada.readObject();
				insertar(reserva);
			} while (reserva != null);
		} catch (ClassNotFoundException e) {
			System.out.println("No puedo encontrar la reserva que tengo que leer.");
		} catch (FileNotFoundException e) {
			System.out.println("No puedo abrir el fichero de reservas.");
		} catch (EOFException e) {
			System.out.println("Fichero reservas leído satisfactoriamente.");
		} catch (IOException e) {
			System.out.println("Error inesperado de Entrada/Salida.");
		} catch (OperationNotSupportedException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void escribir() {
		File fichero = new File(NOMBRE_FICHERO_RESERVAS);
		try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(fichero))){
			for (Reserva reserva : coleccionReservas)
				salida.writeObject(reserva);
			System.out.println("Fichero reservas escrito satisfactoriamente.");
		} catch (FileNotFoundException e) {
			System.out.println("No puedo crear el fichero de reservas");
		} catch (IOException e) {
			System.out.println("Error inesperado de Entrada/Salida");
		}
	}

}

