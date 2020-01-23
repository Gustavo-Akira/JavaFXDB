package gui;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
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
import model.entities.Seller;
import model.services.DepartmentService;
import model.services.SellerService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class SellerController implements Initializable, AListener{
	private SellerService service;
	@FXML
	private TableColumn <Seller, Seller> tableColumnREMOVE;
	@FXML
	private TableColumn <Seller, Seller> tableColumnEDIT;
	@FXML
	private TableView<Seller> lista;
	@FXML
	private TableColumn<Seller,Integer> id;
	@FXML
	private TableColumn<Seller,String> nome;
	@FXML
	private TableColumn<Seller,String> email;
	@FXML
	private TableColumn<Seller,Date> birthdate;
	@FXML
	private TableColumn<Seller,Double> salario;
	@FXML 
	private Button Bnew;
	private ObservableList<Seller> dep;
	@FXML
	public void onClickN(ActionEvent e){
		Stage parent = Utils.current(e);
		Seller obj = new Seller();
		
		createDialogForm(obj,"/gui/Sform.fxml",parent);
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		startSons();
	}
	public void setService(SellerService service) {
			this.service = service;
	}
	private void startSons() {
		id.setCellValueFactory(new PropertyValueFactory<>("id"));
		nome.setCellValueFactory(new PropertyValueFactory<>("name"));
		email.setCellValueFactory(new PropertyValueFactory<>("email"));
		birthdate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		Utils.formatTableColumnDate(birthdate, "dd/MM/yyyy");
		salario.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		Utils.formatTableColumnDouble(salario,2);
		Stage cena =(Stage) Main.getM().getWindow();
		lista.prefHeightProperty().bind(cena.heightProperty());
		
	}
	public void updateTableV() {
		if(service == null) {
			throw new IllegalStateException("Serviço nulo");
		}
		List<Seller> list = service.findAll();
		dep = FXCollections.observableArrayList(list);
		lista.setItems(dep);
		initEditButtons();
		initRemoveButtons();
	}
	private void createDialogForm(Seller o,String name,Stage parent) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(name));
			Pane pane = loader.load();
			SFController controller = loader.getController();
			controller.setDep(o);
			controller.setDPS(new SellerService(), new DepartmentService());
			controller.loadAssociatedObjects();
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
		tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {   
		private final Button button = new Button("edit"); 
	 
	  @Override   protected void updateItem(Seller obj, boolean empty) {    super.updateItem(obj, empty); 
	 
	   if (obj == null) {     
		   setGraphic(null);     
		   return;    
	   } 
	 
	   setGraphic(button);    
	   button.setOnAction(    event -> createDialogForm(     obj, "/gui/Sform.fxml",Utils.current(event)));   
	   }  
		}); 
	} 
private void initRemoveButtons() {  
	tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));  
	tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {         
	private final Button button = new Button("remove"); 

	@Override         
	protected void updateItem(Seller obj, boolean empty) {             super.updateItem(obj, empty); 

   	if (obj == null) {                 
	   setGraphic(null);                 
	 	return;             
   	}

    	setGraphic(button);             
    	button.setOnAction(event -> removeEntity(obj));         
    	}     
	}); 
}
	private void removeEntity(Seller o) {
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