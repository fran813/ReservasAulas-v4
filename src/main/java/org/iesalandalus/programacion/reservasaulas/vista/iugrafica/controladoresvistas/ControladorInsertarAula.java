package org.iesalandalus.programacion.reservasaulas.vista.iugrafica.controladoresvistas;

import java.net.URL;
import java.util.ResourceBundle;

import org.iesalandalus.programacion.reservasaulas.controlador.IControladorReservasAulas;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.vista.iugrafica.utilidades.Dialogos;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControladorInsertarAula implements Initializable{

	private static final String ER_OBLIGATORIO = ".+";

	private IControladorReservasAulas controladorMVC;
	private ObservableList<Aula> aulas;

	@FXML private TextField tfNombre;
	@FXML private TextField tfPuestos;
	@FXML private Button btInsertar;
	@FXML private Button btCancelar;
	

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
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tfNombre.textProperty().addListener((ob, ov, nv) -> compruebaCampoTexto(ER_OBLIGATORIO, tfNombre));
		tfPuestos.textProperty().addListener((ob, ov, nv) -> compruebaCampoTexto(ER_OBLIGATORIO, tfPuestos));
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
	private void insertarAula(ActionEvent event) {
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
