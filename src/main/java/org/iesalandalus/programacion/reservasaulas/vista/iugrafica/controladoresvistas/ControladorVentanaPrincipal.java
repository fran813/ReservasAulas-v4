package org.iesalandalus.programacion.reservasaulas.vista.iugrafica.controladoresvistas;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.iesalandalus.programacion.reservasaulas.vista.iugrafica.utilidades.Dialogos;
import org.iesalandalus.programacion.reservasaulas.controlador.IControladorReservasAulas;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.Permanencia;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.PermanenciaPorHora;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.PermanenciaPorTramo;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ControladorVentanaPrincipal {

	//Tabla que lista profesores
	@FXML  private TableView<Profesor> tvProfesores;
    @FXML  private TableColumn<Profesor, String> tcProfesor1;
    @FXML  private TableColumn<Profesor, String> tcCorreo1;
    @FXML  private TableColumn<Profesor, String> tcTelefono1;
    @FXML  private TableColumn<Reserva, String> tcAula1;
    @FXML  private TableColumn<Reserva, String> tcDia1;
    @FXML  private TableColumn<Reserva, String> tcTramoHora1;
    @FXML  private TableColumn<Reserva, String> tcPuntos1;
    @FXML  private ContextMenu cmProfesores;
    @FXML  private MenuItem miAnadirProfesor;
    @FXML  private MenuItem miBorrarProfesor;
    
    //Tabla que lista Aulas
    @FXML  private TableView<Aula> tvAulas;
    @FXML  private TableColumn<Aula, String> tcAula2;
    @FXML  private TableColumn<Aula, String> tcPuestos2;
    @FXML  private TableColumn<Profesor, String> tcProfesor2;
    @FXML  private TableColumn<Reserva, String> tcPuntos2;
    @FXML  private TableColumn<Reserva, String> tcDia2;
    @FXML  private TableColumn<Reserva, String> tcTramoHora2;
    @FXML  private ContextMenu cmAulas;
    @FXML  private MenuItem miAnadirAula;
    @FXML  private MenuItem miBorrarAula;
    
    //Tabla que lista Reservas
    @FXML  private TableView<Reserva> tvReservas;
    @FXML  private TableColumn<Reserva, String> tcProfesor3;
    @FXML  private TableColumn<Reserva, String> tcAula3;
    @FXML  private TableColumn<Reserva, String> tcDia3;
    @FXML  private TableColumn<Reserva, String> tcTramoHora3;
    @FXML  private TableColumn<Reserva, String> tcPuntos3;
    @FXML  private ContextMenu cmReservas;
    @FXML  private MenuItem miAnadirReserva;
    @FXML  private MenuItem miBorrarReserva;
    
 
    @FXML  private TextField tfFiltrarTelefono;
    @FXML  private TextField tfFiltrarCorreo;
    @FXML  private TextField tfFiltrarNombre;
    private IControladorReservasAulas controladorMVC;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private ObservableList<Profesor> profesores = FXCollections.observableArrayList();
    private ObservableList<Aula> aulas = FXCollections.observableArrayList();
    private ObservableList<Reserva> reservas = FXCollections.observableArrayList();
    private FilteredList<Profesor> profesoresFiltrados = new FilteredList<>(profesores, p -> true);
    private FilteredList<Aula> aulasFiltradas = new FilteredList<>(aulas, p -> true);
    private FilteredList<Reserva> reservasFiltradas = new FilteredList<>(reservas, p -> true);
    
    /*private Stage InsertarProfesor;
    private Stage InsertarAula;
    private Stage InsertarReserva;
    */
    
    @FXML
	private void initialize() {
    	
    	//Profesores
		tcProfesor1.setCellValueFactory(profesor -> new SimpleStringProperty(profesor.getValue().getNombre()));
		tcCorreo1.setCellValueFactory(profesor -> new SimpleStringProperty(profesor.getValue().getCorreo()));
		tcTelefono1.setCellValueFactory(profesor -> new SimpleStringProperty(profesor.getValue().getTelefono()));
		tcAula1.setCellValueFactory(reserva -> new SimpleStringProperty(reserva.getValue().getAula().getNombre()));
		tcDia1.setCellValueFactory(reserva -> new SimpleStringProperty(FORMATO_FECHA.format(reserva.getValue().getPermanencia().getDia())));
		tcTramoHora1.setCellValueFactory(reserva -> new SimpleStringProperty(getPermanenciaTramoHora(reserva.getValue())));
		tcPuntos1.setCellValueFactory(reserva -> new SimpleStringProperty(Float.toString(reserva.getValue().getPuntos())));
		
		SortedList<Profesor> profesoresOrdenados = new SortedList<>(profesoresFiltrados);
		profesoresOrdenados.comparatorProperty().bind(tvProfesores.comparatorProperty());
		tvProfesores.setItems(profesoresOrdenados);
		
		//Aulas
		tcAula2.setCellValueFactory(aula -> new SimpleStringProperty(aula.getValue().getNombre()));
		tcPuestos2.setCellValueFactory(aula -> new SimpleStringProperty(Float.toString(aula.getValue().getPuestos())));
		tcProfesor2.setCellValueFactory(profesor -> new SimpleStringProperty(profesor.getValue().getNombre()));
		tcPuntos2.setCellValueFactory(reserva -> new SimpleStringProperty(Float.toString(reserva.getValue().getPuntos())));
		tcDia2.setCellValueFactory(reserva -> new SimpleStringProperty(FORMATO_FECHA.format(reserva.getValue().getPermanencia().getDia())));
		tcTramoHora2.setCellValueFactory(reserva -> new SimpleStringProperty(getPermanenciaTramoHora(reserva.getValue())));
		
		SortedList<Aula> aulasOrdenadas = new SortedList<>(aulasFiltradas);
		aulasOrdenadas.comparatorProperty().bind(tvAulas.comparatorProperty());
		tvAulas.setItems(aulasOrdenadas);
		
		//Reservas
		tcProfesor3.setCellValueFactory(reserva -> new SimpleStringProperty(reserva.getValue().getProfesor().getNombre()));
		tcAula3.setCellValueFactory(reserva -> new SimpleStringProperty(reserva.getValue().getAula().getNombre()));
		tcDia3.setCellValueFactory(reserva -> new SimpleStringProperty(FORMATO_FECHA.format(reserva.getValue().getPermanencia().getDia())));
		tcTramoHora3.setCellValueFactory(reserva -> new SimpleStringProperty(getPermanenciaTramoHora(reserva.getValue())));
		tcPuntos3.setCellValueFactory(reserva -> new SimpleStringProperty(Float.toString(reserva.getValue().getPuntos())));
		
		
		SortedList<Reserva> reservasOrdenadas = new SortedList<>(reservasFiltradas);
		reservasOrdenadas.comparatorProperty().bind(tvReservas.comparatorProperty());
		tvReservas.setItems(reservasOrdenadas);
		
		tfFiltrarNombre.textProperty().addListener((ob, ov, nv) -> 
			profesoresFiltrados.setPredicate(profesor -> {
				if (nv == null || nv.isEmpty()) {
					return true;
				}
				String nombre = profesor.getNombre().toLowerCase();
				return nombre.startsWith(nv.toLowerCase());
			})
		);
		tfFiltrarNombre.focusedProperty().addListener((ob, ov, nv) -> {
			tfFiltrarTelefono.setText("");
			tfFiltrarCorreo.setText("");
		});
		tfFiltrarTelefono.textProperty().addListener((ob, ov, nv) -> 
			profesoresFiltrados.setPredicate(profesor -> {
				if (nv == null || nv.isEmpty()) {
					return true;
				}
				String telefono = profesor.getTelefono().toLowerCase();
				return telefono.startsWith(nv.toLowerCase());
			})
		);
		tfFiltrarTelefono.focusedProperty().addListener((ob, ov, nv) -> {
			tfFiltrarNombre.setText("");
			tfFiltrarCorreo.setText("");
		});
		tfFiltrarCorreo.textProperty().addListener((ob, ov, nv) -> 
			profesoresFiltrados.setPredicate(profesor -> {
				if (nv == null || nv.isEmpty()) {
					return true;
				}
				String correo = profesor.getCorreo().toLowerCase();
				return correo.contains(nv.toLowerCase());
			})
		);
		tfFiltrarCorreo.focusedProperty().addListener((ob, ov, nv) -> {
			tfFiltrarNombre.setText("");
			tfFiltrarTelefono.setText("");
		});
		
	}
    
    private String getPermanenciaTramoHora(Reserva reserva) {
		Permanencia permanencia = reserva.getPermanencia();
		if(permanencia instanceof PermanenciaPorTramo)
			return ((PermanenciaPorTramo) permanencia).getTramo().toString();
		else
			return ((PermanenciaPorHora) permanencia).getHora().toString();
	}
    
    public void setControladorMVC(IControladorReservasAulas controladorMVC) {
		this.controladorMVC = controladorMVC;
	}
    
	
    
    @FXML
	private void salir() {
		if (Dialogos.mostrarDialogoConfirmacion("Salir", "¿Estás seguro de que quieres salir de la aplicación?", null)) {
			controladorMVC.salir();
			System.exit(0);
		}
	}

    @FXML
    private void acercaDe(ActionEvent event) {
    	Alert dialogo = new Alert(AlertType.INFORMATION);
		dialogo.setTitle("Acerca de ...");
		DialogPane panelDialogo = dialogo.getDialogPane();
		panelDialogo.getStylesheets().add(getClass().getResource("../iugtablas.css").toExternalForm());
		panelDialogo.lookupButton(ButtonType.OK).setId("btAceptar");
		VBox contenido = new VBox();
		contenido.setAlignment(Pos.CENTER);
		contenido.setPadding(new Insets(20, 20, 0, 20));
		contenido.setSpacing(20);
		Image logo = new Image(getClass().getResourceAsStream("../../imagenes/logo-ies.png"), 200, 200, true, true);
		Label texto = new Label("Módulo de Programación - JavaFX");
		texto.setStyle("-fx-font: 20 Arial");
		contenido.getChildren().addAll(new ImageView(logo), texto);
		panelDialogo.setHeader(contenido);
		dialogo.showAndWait();
	}

    @FXML
    private void anadirProfesor(ActionEvent event) {
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../vistas/InsertarProfesor.fxml"));
			Parent raiz = loader.load();
			ControladorInsertarProfesor controlador = loader.getController();
			controlador.setControladorMVC(controladorMVC);
			controlador.setProfesores(profesores);
			Scene escena = new Scene(raiz);
			Stage escenario = new Stage();
			escenario.initModality(Modality.APPLICATION_MODAL);
			escenario.setScene(escena);
			escenario.setTitle("Añadir Profesor");
			escenario.showAndWait();
		} catch (IOException e) {
			Logger.getLogger(ControladorVentanaPrincipal.class.getName()).log(Level.SEVERE, null, e);
		}
	}

    @FXML
    private void borrarProfesor(ActionEvent event) {

    }

    @FXML
    private void anadirAula(ActionEvent event) {
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../vistas/InsertarAula.fxml"));
			Parent raiz = loader.load();
			ControladorInsertarAula controlador = loader.getController();
			controlador.setControladorMVC(controladorMVC);
			controlador.setAulas(aulas);
			Scene escena = new Scene(raiz);
			Stage escenario = new Stage();
			escenario.initModality(Modality.APPLICATION_MODAL);
			escenario.setScene(escena);
			escenario.setTitle("Añadir Aula");
			escenario.showAndWait();
		} catch (IOException e) {
			Logger.getLogger(ControladorVentanaPrincipal.class.getName()).log(Level.SEVERE, null, e);
		}
	}

    @FXML
    void borrarAula(ActionEvent event) {

    }

    @FXML
    private void anadirReserva(ActionEvent event) {
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../vistas/InsertarReserva.fxml"));
			Parent raiz = loader.load();
			ControladorInsertarReserva controlador = loader.getController();
			controlador.setControladorMVC(controladorMVC);
			controlador.setReservas(reservas);
			Scene escena = new Scene(raiz);
			Stage escenario = new Stage();
			escenario.initModality(Modality.APPLICATION_MODAL);
			escenario.setScene(escena);
			escenario.setTitle("Añadir reserva");
			escenario.showAndWait();
		} catch (IOException e) {
			Logger.getLogger(ControladorVentanaPrincipal.class.getName()).log(Level.SEVERE, null, e);
		}
	}

    @FXML
    private void borrarReserva(ActionEvent event) {

    }

}
