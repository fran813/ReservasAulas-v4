package org.iesalandalus.programacion.reservasaulas.vista.iugrafica.controladoresvistas;

import java.net.URL;
import java.util.ResourceBundle;

import org.iesalandalus.programacion.reservasaulas.controlador.IControladorReservasAulas;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.vista.iugrafica.utilidades.Dialogos;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControladorInsertarProfesor implements Initializable {

	private static final String ER_OBLIGATORIO = ".+";
	private static final String ER_TELEFONO = "[9][0-9]{8}|[6][0-9]{8}|[7][0-9]{8}";
	private static final String ER_CORREO = "\\w+(?:\\.\\w+)*@\\w+\\.\\w{2,5}";

	private IControladorReservasAulas controladorMVC;
	private ObservableList<Profesor> profesores;

	@FXML private TextField tfCorreo;
	@FXML private TextField tfNombre;
	@FXML private TextField tfTelefono;
	@FXML private Button btInsertar;
	@FXML private Button btCancelar;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tfNombre.textProperty().addListener((ob, ov, nv) -> compruebaCampoTexto(ER_OBLIGATORIO, tfNombre));
		tfTelefono.textProperty().addListener((ob, ov, nv) -> compruebaCampoTexto(ER_TELEFONO, tfTelefono));
		tfCorreo.textProperty().addListener((ob, ov, nv) -> compruebaCampoTexto(ER_CORREO, tfCorreo));
	}

	public void setControladorMVC(IControladorReservasAulas controladorMVC) {
		this.controladorMVC = controladorMVC;
	}

	public void setProfesores(ObservableList<Profesor> profesores) {
		this.profesores = profesores;
	}

	public void inicializa() {
		tfNombre.setText("");
		tfTelefono.setText("");
		tfCorreo.setText("");
	}

	private void compruebaCampoTexto(String er, TextField campoTexto) {
		String texto = campoTexto.getText();
		if (texto.matches(er)) {
			campoTexto.setStyle("-fx-border-color: green");
		} else {
			campoTexto.setStyle("-fx-border-color: red");
		}
	}

	@FXML
	private void cancelar(ActionEvent event) {
		((Stage) btCancelar.getScene().getWindow()).close();
	}

	@FXML
	private void insertarProfesor(ActionEvent event) {
	
		Profesor profesor = null;
		try {
			profesor = getProfesor();
			controladorMVC.insertarProfesor(profesor);
			profesores.add(profesor);
			Stage propietario =((Stage) btInsertar.getScene().getWindow());
			Dialogos.mostrarDialogoInformacion("Añadir profesor", "Profesor añadido satisfactoriamente", propietario);
		} catch (Exception e) {
			Dialogos.mostrarDialogoError("Añadir profesor", e.getMessage());
		}	
	}

	private Profesor getProfesor() {
		if (tfTelefono != null) {
			String nombre = tfNombre.getText();
			String correo = tfCorreo.getText();
			String telefono = tfTelefono.getText();
			return new Profesor(nombre, correo, telefono);
		} else {
			String nombre = tfNombre.getText();
			String correo = tfCorreo.getText();
			return new Profesor(nombre, correo);
		}
	}	
}
