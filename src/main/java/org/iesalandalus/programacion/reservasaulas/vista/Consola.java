package org.iesalandalus.programacion.reservasaulas.vista;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.*;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.Permanencia;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.PermanenciaPorHora;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.PermanenciaPorTramo;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.Tramo;
import org.iesalandalus.programacion.utilidades.Entrada;

public class Consola {

	private static final DateTimeFormatter FORMATO_DIA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private Consola() {
	}

	public static void mostrarMenu() {
		mostrarCabecera("Reservas de aulas");
		for (Opcion opcion : Opcion.values()) {
			System.out.println(opcion);
		}
	}

	public static void mostrarCabecera(String mensaje) {
		System.out.printf("%n%s%n", mensaje);
		String cadena = "%0" + mensaje.length() + "d%n";
		System.out.println(String.format(cadena, 0).replace("0", "-"));
	}

	public static int elegirOpcion() {
		int ordinalOpcion;
		do {
			System.out.print("Elige una opción: ");
			ordinalOpcion = Entrada.entero();
		} while (!Opcion.esOrdinalValido(ordinalOpcion));
		return ordinalOpcion;
	}

	public static Aula leerAula() {

		String nombre = leerNombreAula();
		System.out.print("Introduce el numero de puestos del Aula: ");
		int puestos = Entrada.entero();
		return new Aula(nombre, puestos);
	}

	public static String leerNombreAula() {
		String nombreAula;
		do {
			System.out.print("Introduce el aula: ");
			nombreAula = Entrada.cadena();
		} while (nombreAula.trim().equals(""));
		return nombreAula;
	}

	public static Profesor leerProfesor() {

		String nombre = leerNombreProfesor();
		System.out.print("Introduce el correo del Profesor: ");
		String correo = Entrada.cadena();
		return new Profesor(nombre, correo);
	}

	public static String leerNombreProfesor() {

		System.out.print("Introduce el nombre del Profesor: ");
		String nombreProfesor = Entrada.cadena();
		return nombreProfesor;
	}

	public static Tramo leerTramo() {
		int numero = 0;
		do {
			System.out.print("Introduce tramo horario");
			System.out.print("1.Mañana, 2.Tarde");
			numero = Entrada.entero();
		} while (numero < 1 && numero > 2);
		return Tramo.values()[numero - 1];
	}

	public static LocalDate leerDia() {
		LocalDate fecha = null;
		do {
			System.out.println("Introduzca una fecha en el formato dd/mm/aaaa.");
			try {
				fecha = LocalDate.parse(Entrada.cadena(), FORMATO_DIA);
			} catch (DateTimeParseException e) {
				System.out.println("La fecha introducida no está en el formato correcto o no es válida.");
			}
		} while (fecha == null);
		return fecha;
	}

	public static String leerHora() {
		System.out.print("Introduce la hora: (08:00 - 22:00) ");
		return Entrada.cadena();
	}

	public static Permanencia leerPermanencia() {
		if (elegirPermanencia() == 1) {
			return new PermanenciaPorHora(leerDia(), leerHora());
		} else {
			return new PermanenciaPorTramo(leerDia(), leerTramo());
		}
	}

	private static int elegirPermanencia() {
		int opcion;
		System.out.println("Elija el tipo de Permanencia: ");
		System.out.println(" 1.- Permanencia por Hora. ");
		System.out.println(" 2.- Permanencia por Tramo. ");
		do {
			System.out.println("Elija una opcion: ");
			opcion = Entrada.entero();
		} while (opcion < 1 | opcion > 2);
		return opcion;
	}
}

