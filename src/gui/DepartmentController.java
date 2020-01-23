package gui;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.AListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class DepartmentController implements Initializable, AListener{
	private DepartmentService service;
	@FXML
	private TableColumn <Department, Department> tableColumnREMOVE;
	@FXML
	private TableColumn <Department, Department> tableColumnEDIT;
	@FXML
	private TableView<Department> lista;
	@FXML
	private TableColumn<Department,Integer> id;
	@FXML
	private TableColumn<Department,String> nome;
	@FXML 
	private Button Bnew;
	private ObservableList<Department> dep;
	@FXML
	public void onClickN(ActionEvent e){
		Stage parent = Utils.current(e);
		Department obj = new Department();
		
		createDialogForm(obj,"/gui/Dform.fxml",parent);
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		startSons();
	}
	public void setService(DepartmentService service) {
			this.service = service;
	}
	private void startSons() {
		id.setCellValueFactory(new PropertyValueFactory<>("id"));
		nome.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		Stage cena =(Stage) Main.getM().getWindow();
		lista.prefHeightProperty().bind(cena.heightProperty());
		
	}
	public void updateTableV() {
		if(service == null) {
			throw new IllegalStateException("Serviço nulo");
		}
		List<Department> list = service.findAll();
		dep = FXCollections.observableArrayList(list);
		lista.setItems(dep);
		initEditButtons();
		initRemoveButtons();
	}
	private void createDialogForm(Department o,String name,Stage parent) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(name));
			Pane pane = loader.load();
			DFController controller = loader.getController();
			controller.setDep(o);
			controller.setDPS(new DepartmentService());
			controller.subAListener(this);
			controller.updateFormData();
			Stage d = new Stage();
			d.setTitle("Coloque a informação do departamento");
			d.setScene(new Scene(pane));
			d.setResizable(false);
			d.initOwner(parent);
			d.initModality(Modality.WINDOW_MODAL);
			d.showAndWait();
		}catch(IOException e) {
			Alerts.showAlert("IO Exception", "Error loading a view", e.getMessage(), AlertType.ERROR);
		}
	}
	@Override
	public void onDataChanged() {
		updateTableV();
	}
	private void initEditButtons() {  
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));  
		tableColumnEDIT.setCellFactory(param -> new TableCell<Department, Department>() {   
		private final Button button = new Button("edit"); 
	 
	  @Override   protected void updateItem(Department obj, boolean empty) {    super.updateItem(obj, empty); 
	 
	   if (obj == null) {     
		   setGraphic(null);     
		   return;    
	   } 
	 
	   setGraphic(button);    
	   button.setOnAction(    event -> createDialogForm(     obj, "/gui/Dform.fxml",Utils.current(event)));   
	   }  
		}); 
	} 
private void initRemoveButtons() {  
	tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));  
	tableColumnREMOVE.setCellFactory(param -> new TableCell<Department, Department>() {         
	private final Button button = new Button("remove"); 

	@Override         
	protected void updateItem(Department obj, boolean empty) {             super.updateItem(obj, empty); 

   	if (obj == null) {                 
	   setGraphic(null);                 
	 	return;             
   	}

    	setGraphic(button);             
    	button.setOnAction(event -> removeEntity(obj));         
    	}     
	}); 
}
	private void removeEntity(Department o) {
		Optional<ButtonType> result =Alerts.showConfirmation("Confirmation","Tem certeza que você quer deletar ?");
		if(result.get() == ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("Servicço esta vazio");
			}
			try {
			service.remove(o);
			updateTableV();
			}catch(DbIntegrityException e) {
				Alerts.showAlert("Erro removendo objeto", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}