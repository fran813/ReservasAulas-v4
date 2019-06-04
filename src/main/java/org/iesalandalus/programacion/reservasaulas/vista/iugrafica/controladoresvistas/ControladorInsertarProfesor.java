package org.iesalandalus.programacion.reservasaulas.vista.iugrafica.controladoresvistas;

import org.iesalandalus.programacion.reservasaulas.controlador.IControladorReservasAulas;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.vista.iugrafica.utilidades.Dialogos;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControladorInsertarProfesor {

	private static final String ER_OBLIGATORIO = ".+";
	private static final String ER_TELEFONO = "950[0-9]{6}|[679][0-9] {8}";
	private static final String ER_CORREO = "\\w+(?:\\.\\w+)*@\\w+\\.\\w{2,5}";

	private IControladorReservasAulas controladorMVC;
	private ObservableList<Profesor> profesores;

	@FXML
	private TextField tfCorreo;
	@FXML
	private TextField tfNombre;
	@FXML
	private TextField tfTelefono;
	@FXML
	private Button btInsertar;
	@FXML
	private Button btCancelar;

	@FXML
	private void initialize() {
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
	void cancelar(ActionEvent event) {
		((Stage) btCancelar.getScene().getWindow()).close();
	}

	@FXML
	void insertarProfesor(ActionEvent event) {
		Profesor profesor = null;
		try {
			if (tfTelefono.getText().equals(""))
				profesor = new Profesor(tfNombre.getText(), tfCorreo.getText());
			else
				profesor = new Profesor(tfNombre.getText(), tfCorreo.getText(), tfTelefono.getText());
			controladorMVC.insertarProfesor(profesor);
			profesores.add(profesor);
			Stage propietario = ((Stage) btInsertar.getScene().getWindow());
			Dialogos.mostrarDialogoInformacion("Insertar Profesor", "Profesor añadido satisfactoriamente", propietario);
		} catch (Exception e) {
			Dialogos.mostrarDialogoError("Insertar Profesor", e.getMessage());
		}
	}
}
