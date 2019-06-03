package org.iesalandalus.programacion.reservasaulas.vista;

import java.util.List;
import javax.naming.OperationNotSupportedException;
import org.iesalandalus.programacion.reservasaulas.controlador.IControladorReservasAulas;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.*;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.Permanencia;

public class VistaReservasAulas implements IVistaReservasAulas {

	private static final String ERROR = "ERROR: ";
	private static final String NOMBRE_VALIDO = "Nombre";
	private static final String CORREO_VALIDO = "correo@corr.es";
	private IControladorReservasAulas controlador;

	public VistaReservasAulas() {
		Opcion.setVista(this);
	}

	@Override
	public void setControlador(IControladorReservasAulas controlador) {
		this.controlador = controlador;
	}

	@Override
	public void comenzar() {
		int ordinalOpcion;
		do {
			Consola.mostrarMenu();
			ordinalOpcion = Consola.elegirOpcion();
			Opcion opcion = Opcion.getOpcionSegunOrdinal(ordinalOpcion);
			opcion.ejecutar();
		} while (ordinalOpcion != Opcion.SALIR.ordinal());
	}

	@Override
	public void salir() {
		controlador.salir();
	}

	@Override
	public void insertarAula() {
		Consola.mostrarCabecera("Insertar Aula");
		try {
			Aula aula = Consola.leerAula();
			controlador.insertarAula(aula);
			System.out.println("Aula insertada correctamente.");
		} catch (OperationNotSupportedException | IllegalArgumentException e) {
			System.out.println(ERROR + e.getMessage());
		}
	}

	@Override
	public void borrarAula() {
		Consola.mostrarCabecera("Borrar aula");
		try {
			Aula aula = Consola.leerAula();
			controlador.borrarAula(aula);
			System.out.println("Aula borrada correctamente.");
		} catch (OperationNotSupportedException | IllegalArgumentException e) {
			System.out.println(ERROR + e.getMessage());
		}
	}

	@Override
	public void buscarAula() {
		Consola.mostrarCabecera("Buscar Aula");
		try {
			Aula aula = new Aula(Consola.leerNombreAula(), 1);
			aula = controlador.buscarAula(aula);
			if (aula != null) {
				System.out.println("Encontrada el aula" + aula);
			} else {
				System.out.println("No se encontró el aula" + aula);
			}
		} catch (IllegalArgumentException e) {
			System.out.println(ERROR + e.getMessage());
		}
	}

	@Override
	public void listarAulas() {
		Consola.mostrarCabecera("Listar Aulas");
		List<String> Aulas = controlador.representarAulas();
		if (!Aulas.isEmpty()) {
			for (String aula : Aulas) {
				System.out.println(aula);
			}
		} else {
			System.out.println("No hay aulas que listar.");
		}
	}

	@Override
	public void insertarProfesor() {
		Consola.mostrarCabecera("Insertar Profesor");
		try {
			Profesor profesor = Consola.leerProfesor();
			controlador.insertarProfesor(profesor);
			System.out.println("Profesor insertado correctamente.");
		} catch (OperationNotSupportedException | IllegalArgumentException e) {
			System.out.println(ERROR + e.getMessage());
		}
	}

	@Override
	public void borrarProfesor() {
		Consola.mostrarCabecera("Borrar Profesor");
		try {
			Profesor profesor = Consola.leerProfesor();
			controlador.borrarProfesor(profesor);
			System.out.println("Profesor borrado correctamente.");
		} catch (OperationNotSupportedException | IllegalArgumentException e) {
			System.out.println(ERROR + e.getMessage());
		}
	}

	@Override
	public void buscarProfesor() {
		Consola.mostrarCabecera("Buscar profesor");
		Profesor profesor = null;
		try {
			profesor = Consola.leerProfesor();
			profesor = controlador.buscarProfesor(profesor);
			if (profesor != null) {
				System.out.println("El aula buscada es: " + profesor);
			} else {
				System.out.println("No existe ningún profesor con ese nombre.");
			}
		} catch (IllegalArgumentException e) {
			System.out.println(ERROR + e.getMessage());
		}
	}

	@Override
	public void listarProfesores() {
		Consola.mostrarCabecera("Listar Profesor");
		List<String> Profesores = controlador.representarProfesores();
		if (!Profesores.isEmpty()) {
			for (String profesor : Profesores) {
				System.out.println(profesor);
			}
		} else {
			System.out.println("No hay profesor que listar.");
		}
	}

	@Override
	public void realizarReserva() {
		Consola.mostrarCabecera("Realizar Reserva");
		try {
			Reserva reserva = leerReserva(Consola.leerProfesor());
			controlador.realizarReserva(reserva);
			System.out.println("Has realizado correctamente la reserva.");
		} catch (OperationNotSupportedException | IllegalArgumentException e) {
			System.out.println(ERROR + e.getMessage());
		}
	}

	private Reserva leerReserva(Profesor profesor) {
		Permanencia permanencia = Consola.leerPermanencia();
		Aula aula = controlador.buscarAula(new Aula(Consola.leerNombreAula(), 69));
		return new Reserva(profesor, aula, permanencia);
	}

	@Override
	public void anularReserva() {
		Consola.mostrarCabecera("Borrar reserva");
		try {
			Profesor profesor = new Profesor(NOMBRE_VALIDO, CORREO_VALIDO);
			Reserva reserva = leerReserva(profesor);
			controlador.anularReserva(reserva);
			System.out.println("Reserva borrada correctamente.");
		} catch (OperationNotSupportedException | IllegalArgumentException e) {
			System.out.println(ERROR + e.getMessage());
		}
	}

	@Override
	public void listarReservas() {
		Consola.mostrarCabecera("Listar reservas");
		List<String> reservas = controlador.representarReservas();
		if (!reservas.isEmpty()) {
			for (String reserva : reservas) {
				System.out.println(reserva);
			}
		} else {
			System.out.println("No hay reservas que listar.");
		}
	}

	@Override
	public void listarReservasAula() {
		Consola.mostrarCabecera("Listar reservas aula");
		Aula aula = Consola.leerAula();
		List<Reserva> reservasAula = controlador.getReservasAula(aula);
		if (!reservasAula.isEmpty()) {
			for (Reserva reserva : reservasAula) {
				System.out.println(reserva);
			}
		} else {
			System.out.println("No hay reservas que listar.");
		}
	}

	@Override
	public void listarReservasProfesor() {
		Consola.mostrarCabecera("Listar reservas profesor");
		Profesor profesor = Consola.leerProfesor();
		List<Reserva> reservasProfesor = controlador.getReservasProfesor(profesor);
		if (!reservasProfesor.isEmpty()) {
			for (Reserva reserva : reservasProfesor) {
				System.out.println(reserva);
			}
		} else {
			System.out.println("No hay reservas que listar.");
		}
	}

	@Override
	public void listarReservasPermanencia() {
		Consola.mostrarCabecera("Listar reservas profesor");
		Permanencia permanencia = Consola.leerPermanencia();
		List<Reserva> reservasPermanencia = controlador.getReservasPermanencia(permanencia);
		if (!reservasPermanencia.isEmpty()) {
			for (Reserva reserva : reservasPermanencia) {
				System.out.println(reserva);
			}
		} else {
			System.out.println("No hay reservas que listar.");
		}
	}

	@Override
	public void consultarDisponibilidad() {
		Consola.mostrarCabecera("Consultar disponibilidad");
		Aula aula = Consola.leerAula();
		Permanencia permanencia = Consola.leerPermanencia();
		boolean disponible = controlador.consultarDisponibilidad(aula, permanencia);
		if (disponible == true) {
			System.out.println("Aula disponible");
		} else {
			System.out.println("Aula no disponible");
		}

	}

}
