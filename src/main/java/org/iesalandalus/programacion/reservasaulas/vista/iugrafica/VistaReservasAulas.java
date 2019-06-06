package org.iesalandalus.programacion.reservasaulas.vista.iugrafica;

import org.iesalandalus.programacion.reservasaulas.controlador.IControladorReservasAulas;
import org.iesalandalus.programacion.reservasaulas.vista.IVistaReservasAulas;
import org.iesalandalus.programacion.reservasaulas.vista.iugrafica.controladoresvistas.ControladorVentanaPrincipal;
import org.iesalandalus.programacion.reservasaulas.vista.iugrafica.utilidades.Dialogos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class VistaReservasAulas extends Application implements IVistaReservasAulas {

	private IControladorReservasAulas controladorMVC = null;
	private static VistaReservasAulas instancia = null;
	
	public VistaReservasAulas() {
		if (instancia != null) {
			controladorMVC = instancia.controladorMVC;
		} else {
			instancia = this;
		}
	}

	@Override
	public void start(Stage escenarioPrincipal) {
		try {
			FXMLLoader cargadorVentanaPrincipal = new FXMLLoader(getClass().getResource("vistas/VentanaPrincipal.fxml"));
			VBox raiz = cargadorVentanaPrincipal.load();	
			ControladorVentanaPrincipal cVentanaPrincipal = cargadorVentanaPrincipal.getController();
			cVentanaPrincipal.setControladorMVC(controladorMVC);
			Scene escena = new Scene(raiz);
			escenarioPrincipal.setOnCloseRequest(e -> confirmarSalida(escenarioPrincipal, e));
			escenarioPrincipal.setTitle("Reserva de Aulas");
			escenarioPrincipal.setScene(escena);
			escenarioPrincipal.setResizable(false);
			escenarioPrincipal.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private void confirmarSalida(Stage escenarioPrincipal, WindowEvent e) {
		if (Dialogos.mostrarDialogoConfirmacion("Salir", "¿Estás seguro de que quieres salir de la aplicación?", escenarioPrincipal)) {
			controladorMVC.salir();
			escenarioPrincipal.close();
		}
		else
			e.consume();	
	}

	@Override
	public void setControlador(IControladorReservasAulas controlador) {
		this.controladorMVC = controlador;
	}

	@Override
	public void comenzar() {
		launch(this.getClass());
	}

	@Override
	public void salir() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertarAula() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void borrarAula() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buscarAula() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void listarAulas() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertarProfesor() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void borrarProfesor() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buscarProfesor() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void listarProfesores() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void realizarReserva() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void anularReserva() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void listarReservas() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void listarReservasAula() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void listarReservasProfesor() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void listarReservasPermanencia() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void consultarDisponibilidad() {
		// TODO Auto-generated method stub
		
	}

}

	