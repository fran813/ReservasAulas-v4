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
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.PermanenciaPorTramo;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.Tramo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class ControladorInsertarReserva implements Initializable {
	
    @FXML private TextField tfAula;
    @FXML private TextField tfProfesor;
    @FXML private TextField tfCorreo;
    @FXML private ComboBox<String> cbHora;
    @FXML private RadioButton rbTramo;
    @FXML private DatePicker dpDia;
    @FXML private RadioButton rbHora;
    @FXML private Button btInsertar;
    @FXML private ComboBox<String> cbTramo;
    @FXML private Button btCancelar;
    @FXML private ToggleGroup tgReserva;

    
    private static final String ER_OBLIGATORIO = ".+";
    private static final String ER_CORREO = "\\w+(?:\\.\\w+)*@\\w+\\.\\w{2,5}";
    
    private IControladorReservasAulas controladorMVC;
	private ObservableList<Reserva> reservas;
	private ObservableList<String> horas = FXCollections.observableArrayList("08:00","09:00","10:00","11:00",
			"12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00");
	private ObservableList<String> tramo = FXCollections.observableArrayList("Mañana","Tarde");


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		rbHora.setToggleGroup(tgReserva);
		rbTramo.setToggleGroup(tgReserva);
		tfProfesor.textProperty().addListener((ob, ov, nv) -> compruebaCampoTexto(ER_OBLIGATORIO, tfProfesor));
		tfCorreo.textProperty().addListener((ob, ov, nv) -> compruebaCampoTexto(ER_CORREO, tfCorreo));
	}
	
	public void setControladorMVC(IControladorReservasAulas controladorMVC) {
		this.controladorMVC = controladorMVC;
	}
	
	public void setReservas(ObservableList<Reserva> reservas) {
		this.reservas = reservas;
	}
	
	public void inicializa() {
		tfProfesor.setText("");
		tfAula.setText("");
		tfCorreo.setText("");
		dpDia.setValue(null);
		cbHora.setValue(null);
		cbTramo.setValue(null);
		rbHora.setVisible(true);
		rbTramo.setVisible(true);
	}
	
    @FXML
    private void elegirPermanencia(ActionEvent event) {
    if (rbHora.isSelected()) {
    	cbHora.setDisable(false);
    	cbTramo.setDisable(true);
    	cbHora.setItems(horas);
    	}else {
    		cbHora.setDisable(true);
    		cbTramo.setDisable(false);
    		cbTramo.setItems(tramo);		
    	}
    }

    @FXML
	private void insertarReserva() {
		Reserva reserva = null;
		try {
			reserva = getReserva();
			controladorMVC.realizarReserva(reserva);
			reservas.add(reserva);
			Stage propietario =((Stage) btInsertar.getScene().getWindow());
			Dialogos.mostrarDialogoInformacion("Añadir reserva", "Reserva añadida satisfactoriamente", propietario);
		} catch (Exception e) {
			Dialogos.mostrarDialogoError("Añadir reserva", e.getMessage());
		}	
	}
    private Reserva getReserva() {
    	Profesor profesor = new Profesor(tfProfesor.getText(), tfCorreo.getText());
		Aula aula = new Aula(tfAula.getText(), 10);
		//No se como hacerlo con los constructores que tenemos creados de antes para que coja los puestos de un
		//aula creada ya que para realizar esto no es necesario tener el aula creada, se que esta mal ya que de esta manera estoy diciendo que todas las aulas tienen 10 puestos
		Permanencia permanencia = getPermanenciaHoraTramo(dpDia.getValue());
		return new Reserva(profesor,aula,permanencia);
	}

	private Permanencia getPermanenciaHoraTramo(LocalDate dia) {
		//Para una Hora
		if (rbHora.isSelected()) {
			return new PermanenciaPorHora(dia, cbHora.getValue());
		}
		//Para un Tramo
		if (cbTramo.getValue().equals("Mañana")) {
			return new PermanenciaPorTramo(dia, Tramo.MANANA);
		} else {
			return new PermanenciaPorTramo(dia, Tramo.TARDE);
		}
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
}

