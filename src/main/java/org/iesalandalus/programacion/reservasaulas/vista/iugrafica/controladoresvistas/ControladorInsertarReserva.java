package org.iesalandalus.programacion.reservasaulas.vista.iugrafica.controladoresvistas;


import org.iesalandalus.programacion.reservasaulas.vista.iugrafica.utilidades.Dialogos;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.iesalandalus.programacion.reservasaulas.controlador.IControladorReservasAulas;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.Permanencia;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.PermanenciaPorHora;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControladorInsertarReserva implements Initializable{

	private static final String ER_OBLIGATORIO = ".+";
	private static final String ER_TELEFONO = "950[0-9]{6}|[679][0-9] {8}";
	private static final String ER_CORREO = "\\w+(?:\\.\\w+)*@\\w+\\.\\w{2,5}";
	
	private IControladorReservasAulas controladorMVC;
	private ObservableList<Reserva> reservas;
	private ObservableList<Profesor> profesores;
	private ObservableList<Aula> aulas;
	private ObservableList<String> horas = FXCollections.observableArrayList("8:00","9:00","10:00","11:00",
			"12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00","22:00");
	private ObservableList<String> tramo = FXCollections.observableArrayList("Mañana","Tarde");
	
	@FXML private RadioButton rbTramo;
    @FXML private TextField tfAula;
    @FXML private DatePicker dpDia;
    @FXML private RadioButton rbHora;
    @FXML private TextField tfHora;
    @FXML private Button btInsertar;
    @FXML private TextField tfProfesor;
    @FXML private ChoiceBox<String> cbTramo;
    @FXML private Button btCancelar;
    
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		tfAula.textProperty().addListener((ob, ov, nv) -> compruebaCampoTexto(ER_OBLIGATORIO, tfAula));
		tfHora.textProperty().addListener((ob, ov, nv) -> compruebaCampoTexto(ER_OBLIGATORIO, tfHora));
		tfProfesor.textProperty().addListener((ob, ov, nv) -> compruebaCampoTexto(ER_OBLIGATORIO, tfProfesor));
		rbTramo.setVisible(false);
		rbHora.setVisible(false);
		dpDia.setValue(null);
		cbTramo.setValue(null);
	}
    
    public void setControladorMVC(IControladorReservasAulas controladorMVC) {
		this.controladorMVC = controladorMVC;
	}
	
	public void setReservas(ObservableList<Reserva> reservas) {
		this.reservas = reservas;
	}
	
	public void setProfesor(ObservableList<Profesor> profesores) {
		this.profesores = profesores;
	}
	
	public void setAulas(ObservableList<Aula> aulas) {
		this.aulas = aulas;
	}
	
	public void inicializa() {
		tfAula.setText("");
		tfProfesor.setText("");
		tfHora.setText("");
		dpDia.setValue(null);
		cbTramo.setValue(null);
		rbTramo.setVisible(false);
		rbHora.setVisible(false);
	}
    
	@FXML
    private void insertarReserva(ActionEvent event) {
		Reserva reserva = null;
		try {
			reserva = getReserva();
			controladorMVC.realizarReserva(reserva);
			reservas.add(reserva);
			Stage propietario =((Stage) btInsertar.getScene().getWindow());
			Dialogos.mostrarDialogoInformacion("Reserva Añadida", "Reserva añadida satisfactoriamente", propietario);
		} catch (Exception e) {
			Dialogos.mostrarDialogoError("Añadir reserva", e.getMessage());
		}	
	}
	
    @FXML
	private void cancelar() {
		((Stage) btCancelar.getScene().getWindow()).close();
	}

    
	private void compruebaCampoTexto(String er, TextField campoTexto) {	
		String texto = campoTexto.getText();
		if (texto.matches(er)) {
			campoTexto.setStyle("-fx-border-color: green");
		}
		else {
			campoTexto.setStyle("-fx-border-color: red");
		}
	}
	

	private Reserva getReserva() {
		Profesor profesor = new Profesor(tfProfesor.getText(), ER_OBLIGATORIO);
		Aula aula = new Aula(tfAula.getText(), 100);
		Permanencia permanencia = getPermanenciaHoraTramo(dpDia.getValue());
		return new Reserva(profesor,aula,permanencia);
	}
	
	private Permanencia getPermanenciaHoraTramo(LocalDate value) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
