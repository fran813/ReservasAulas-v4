package org.iesalandalus.programacion.reservasaulas.vista.iugrafica.controladoresvistas;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

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
    @FXML  private ContextMenu cmProfesores;
    @FXML  private MenuItem miAnadirProfesor;
    @FXML  private MenuItem miBorrarProfesor;
    
    //Tabla que lista Aulas
    @FXML  private TableView<Aula> tvAulas;
    @FXML  private TableColumn<Aula, String> tcAula2;
    @FXML  private TableColumn<Aula, String> tcPuestos2;
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
    @FXML  private TableColumn<Reserva, String> tcCorreo3;
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
    
    private ControladorInsertarReserva cInsertarReserva;
    private ControladorInsertarAula cInsertarAula;
    private ControladorInsertarProfesor cInsertarProfesor;
    private Stage insertarProfesor;
    private Stage insertarAula;
    private Stage insertarReserva;
    
    
    @FXML
	private void initialize() {
    	
    	//Profesores
		tcProfesor1.setCellValueFactory(profesor -> new SimpleStringProperty(profesor.getValue().getNombre()));
		tcCorreo1.setCellValueFactory(profesor -> new SimpleStringProperty(profesor.getValue().getCorreo()));
		tcTelefono1.setCellValueFactory(profesor -> new SimpleStringProperty(profesor.getValue().getTelefono()));

		SortedList<Profesor> profesoresOrdenados = new SortedList<>(profesoresFiltrados);
		profesoresOrdenados.comparatorProperty().bind(tvProfesores.comparatorProperty());
		tvProfesores.setItems(profesoresOrdenados);
		
		//Aulas
		tcAula2.setCellValueFactory(aula -> new SimpleStringProperty(aula.getValue().getNombre()));
		tcPuestos2.setCellValueFactory(aula -> new SimpleStringProperty(Float.toString(aula.getValue().getPuestos())));

		SortedList<Aula> aulasOrdenadas = new SortedList<>(aulasFiltradas);
		aulasOrdenadas.comparatorProperty().bind(tvAulas.comparatorProperty());
		tvAulas.setItems(aulasOrdenadas);
	
		//Reservas
		tcProfesor3.setCellValueFactory(reserva -> new SimpleStringProperty(reserva.getValue().getProfesor().getNombre()));
		tcAula3.setCellValueFactory(reserva -> new SimpleStringProperty(reserva.getValue().getAula().getNombre()));
		tcDia3.setCellValueFactory(reserva -> new SimpleStringProperty(FORMATO_FECHA.format(reserva.getValue().getPermanencia().getDia())));
		tcTramoHora3.setCellValueFactory(reserva -> new SimpleStringProperty(getPermanenciaTramoHora(reserva.getValue())));
		tcPuntos3.setCellValueFactory(reserva -> new SimpleStringProperty(Float.toString(reserva.getValue().getPuntos())));
		tcCorreo3.setCellValueFactory(reserva -> new SimpleStringProperty(reserva.getValue().getProfesor().getCorreo()));
		
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
    private void anadirProfesor(ActionEvent event) throws IOException {
    	crearAnadirProfesor();
		insertarProfesor.showAndWait();
	}

    private void crearAnadirProfesor() throws IOException {
    	if (insertarProfesor == null) {
			insertarProfesor = new Stage();
			FXMLLoader cargadorInsertarProfesor = new FXMLLoader(
						getClass().getResource("../vistas/InsertarProfesor.fxml"));
			VBox raizInsertarProfesor = cargadorInsertarProfesor.load();
			cInsertarProfesor = cargadorInsertarProfesor.getController();
			cInsertarProfesor.setControladorMVC(controladorMVC);
			cInsertarProfesor.setProfesores(profesores);
			Scene escenaInsertarProfesor = new Scene(raizInsertarProfesor);
			insertarProfesor.setTitle("Insertar Profesor");
			insertarProfesor.initModality(Modality.APPLICATION_MODAL); 
			insertarProfesor.setScene(escenaInsertarProfesor);
		} else {
			cInsertarProfesor.inicializa();
		}
	}

	@FXML
    private void borrarProfesor(ActionEvent event) {
		Profesor profesor = null;
		try {
			profesor = tvProfesores.getSelectionModel().getSelectedItem();
			if (profesor != null && Dialogos.mostrarDialogoConfirmacion("Borrar", "¿Estás seguro de que quieres borrar el profesor?", null)) {
				controladorMVC.borrarProfesor(profesor);
				profesores.remove(profesor);
				Dialogos.mostrarDialogoInformacion("Borrar profesor", "Profesor borrado satisfactoriamente");
			}
		} catch (Exception e) {
			Dialogos.mostrarDialogoError("Borrar profesor", e.getMessage());
		}
	}

    @FXML
    private void anadirAula(ActionEvent event) throws IOException {
    	crearAnadirAula();
		insertarAula.showAndWait();
	}

    private void crearAnadirAula() throws IOException {
    	if (insertarAula == null) {
    		insertarAula = new Stage();
			FXMLLoader cargadorInsertarAula = new FXMLLoader(
						getClass().getResource("../vistas/InsertarAula.fxml"));
			VBox raizInsertarAula = cargadorInsertarAula.load();
			cInsertarAula = cargadorInsertarAula.getController();
			cInsertarAula.setControladorMVC(controladorMVC);
			cInsertarAula.setAulas(aulas);
			Scene escenaInsertarAula = new Scene(raizInsertarAula);
			insertarAula.setTitle("Insertar Aula");
			insertarAula.initModality(Modality.APPLICATION_MODAL); 
			insertarAula.setScene(escenaInsertarAula);
		} else {
			cInsertarAula.inicializa();
		}
	}

	@FXML
    private void borrarAula(ActionEvent event) {
		Aula aula = null;
		try {
			aula = tvAulas.getSelectionModel().getSelectedItem();
			if (aula != null && Dialogos.mostrarDialogoConfirmacion("Borrar", "¿Estás seguro de que quieres borrar el aula?", null)) {
				controladorMVC.borrarAula(aula);
				aulas.remove(aula);
				Dialogos.mostrarDialogoInformacion("Borrar aula", "Aula borrada satisfactoriamente");
			}
		} catch (Exception e) {
			Dialogos.mostrarDialogoError("Borrar aula", e.getMessage());
		}
	}

    @FXML
    private void anadirReserva(ActionEvent event) throws IOException {
    	crearAnadirReserva();
		insertarReserva.showAndWait();
	}

    private void crearAnadirReserva() throws IOException {
    	if (insertarReserva == null) {
    		insertarReserva = new Stage();
			FXMLLoader cargadorInsertarReserva = new FXMLLoader(
						getClass().getResource("../vistas/InsertarReserva.fxml"));
			VBox raizInsertarReserva = cargadorInsertarReserva.load();
			cInsertarReserva = cargadorInsertarReserva.getController();
			cInsertarReserva.setControladorMVC(controladorMVC);
			cInsertarReserva.setReservas(reservas);
			Scene escenaInsertarReserva = new Scene(raizInsertarReserva);
			insertarReserva.setTitle("Insertar Reserva");
			insertarReserva.initModality(Modality.APPLICATION_MODAL); 
			insertarReserva.setScene(escenaInsertarReserva);
		} else {
			cInsertarReserva.inicializa();
		}
	}

	@FXML
    private void borrarReserva(ActionEvent event) {
    	Reserva reserva = null;
		try {
			reserva = tvReservas.getSelectionModel().getSelectedItem();
			if (reserva != null && Dialogos.mostrarDialogoConfirmacion("Borrar", "¿Estás seguro de que quieres borrar la reserva?", null)) {
				controladorMVC.anularReserva(reserva);
				reservas.remove(reserva);
				Dialogos.mostrarDialogoInformacion("Borrar reserva", "Reserva borrada satisfactoriamente");
			}
		} catch (Exception e) {
			Dialogos.mostrarDialogoError("Borrar reserva", e.getMessage());
		}
    }
}
