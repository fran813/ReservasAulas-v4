package org.iesalandalus.programacion.reservasaulas.vista.iugrafica.controladoresvistas;

import org.iesalandalus.programacion.reservasaulas.controlador.IControladorReservasAulas;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.vista.iugtablas.utilidades.Dialogos;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControladorInsertarAula {

	private static final String ER_OBLIGATORIO = ".+";
	private static final String ER_PUESTOS = "\\d{5}";

	private IControladorReservasAulas controladorMVC;
	private ObservableList<Aula> aulas;

	@FXML
	private TextField tfNombre;
	@FXML
	private Button btInsertar;
	@FXML
	private TextField tfPuestos;
	@FXML
	private Button btCancelar;
	
	@FXML
	private void initialize() {
		tfNombre.textProperty().addListener((ob, ov, nv) -> compruebaCampoTexto(ER_OBLIGATORIO, tfNombre));
		tfPuestos.textProperty().addListener((ob, ov, nv) -> compruebaCampoTexto(ER_PUESTOS, tfPuestos));
	}

	public void setControladorMVC(IControladorReservasAulas controladorMVC) {
		this.controladorMVC = controladorMVC;
	}

	public void setAulas(ObservableList<Aula> aulas) {
		this.aulas = aulas;
	}

	public void inicializa() {
		tfNombre.setText("");
		tfPuestos.setText("");
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
	void insertarAula(ActionEvent event) {
		Aula aula = null;
		try {
			aula = new Aula(tfNombre.getText(), Integer.parseInt(tfPuestos.getText()));
			controladorMVC.insertarAula(aula);
			aulas.add(aula);
			Stage propietario = ((Stage) btInsertar.getScene().getWindow());
			Dialogos.mostrarDialogoInformacion("Insertar Aula", "Aula añadida satisfactoriamente", propietario);
		} catch (Exception e) {
			Dialogos.mostrarDialogoError("Insertar Aula", e.getMessage());
		}
	}
}
