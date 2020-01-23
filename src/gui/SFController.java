package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.AListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.scene.control.Alert.AlertType;
import model.entities.Department;
import model.entities.Seller;
import model.services.DepartmentService;
import model.services.SellerService;

public class SFController implements Initializable {
	private Seller enti;
	private SellerService ser;
	private DepartmentService dser;
	private List<AListener> list = new ArrayList<>();
	@FXML
	private ComboBox<Department>comboBox;
	@FXML
	private TextField tid;
	@FXML 
	private TextField tname;
	@FXML 
	private TextField temail;
	@FXML 
	private DatePicker tdate;
	@FXML 
	private TextField tsalary;
	@FXML
	private Button register;
	@FXML
	private Label Errorn;
	@FXML
	private Button cancel;
	private ObservableList<Department> obs;
	@FXML
	public void onClickR(ActionEvent e) {
		if(enti == null) {
			throw new IllegalStateException("Entidade esta vazia");
		}
		if(ser == null) {
			throw new IllegalStateException("Serviço esta vazio");
		}
		try {
			enti =getFormData();
			ser.SorUp(enti);
			notifyChange();
			Utils.current(e).close();
		}catch(ValidationException v){
			setEM(v.getErros()); 
		}
		catch(DbException d) {
			Alerts.showAlert("Erro salvando departamento", null, d.getMessage(), AlertType.ERROR);
		}
	}
	private void notifyChange() {
		// TODO Auto-generated method stub
		for(AListener liste: list) {
			liste.onDataChanged();
		}
	}
	private Seller getFormData() {
		Seller o = new Seller();
		ValidationException e = new ValidationException("Erro de validação");
		
		o.setId(Utils.transformParsetoInt(tid.getText()));
		if(tname.getText() == null || tname.getText().trim().equals("")) {
			e.addErros("nome", "Campo não pode ser vazio");
		}
		o.setName(tname.getText());
		o.setBaseSalary(Double.parseDouble(tsalary.getText()));
		Instant instant = Instant.from(tdate.getValue().atStartOfDay(ZoneId.systemDefault()));
		o.setBirthDate(Date.from(instant));
		o.setEmail(temail.getText());
		o.setDepartment(comboBox.getValue());
		if(e.getErros().size()>0) {
			throw e;
		}
		return o;
	}
	@FXML
	public void onClickC(ActionEvent e){
		Utils.current(e).close();
	}
	
	public void setDep(Seller e) {
		this.enti = e;
	}
	public void setDPS(SellerService ser, DepartmentService dser) {
		this.ser = ser;
		this.dser= dser;
	}
	public void subAListener(AListener a) {
		list.add(a);
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initialzeNodes();
	}
	private void initialzeNodes() {
		Constraints.setTextFieldInteger(tid);
		Constraints.setMax(tname, 30);
		Constraints.setMax(tsalary, 60);
		Utils.formatDatePicker(tdate, "dd/MM/yyyy");
		
		initializeComboBoxDepartment();
	}
	public void updateFormData() {
		if(enti == null) {
			throw new IllegalStateException("Entidade n existe ou vazia");
		}
		tid.setText(String.valueOf(enti.getId()));
		if(enti.getName() != null) {
			tname.setText(enti.getName());
		}
		if(enti.getEmail() != null) {
		temail.setText(String.valueOf(enti.getEmail()));
		}
		Locale.setDefault(Locale.US);
		if(enti.getBaseSalary() != null) {
		tsalary.setText(String.format("%.2f", enti.getBaseSalary()));
		}
		if(enti.getBirthDate() !=null) {
		tdate.setValue(LocalDate.ofInstant(enti.getBirthDate().toInstant(),ZoneId.systemDefault() ));
		}
		if(enti.getDepartment() == null) {
			comboBox.getSelectionModel().selectFirst();
		}else {
			comboBox.setValue(enti.getDepartment());
		}
	}
		
	private void setEM(Map<String,String> e) {
		Set<String> fields = e.keySet();
		if(fields.contains("name")) {
			Errorn.setText(e.get("name"));
		}
	}
	public void loadAssociatedObjects() {
		if(dser == null) {
			throw new IllegalStateException("Seviço do departamento não pode ser nulo");
		}
		List<Department> list = dser.findAll();
		obs = FXCollections.observableArrayList(list);
		comboBox.setItems(obs);
	}
	private void initializeComboBoxDepartment() {
		 Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
		 @Override
		 protected void updateItem(Department item, boolean empty) {
		 super.updateItem(item, empty);
		 setText(empty ? "" : item.getName());
		 }
		 };
		comboBox.setCellFactory(factory);
		comboBox.setButtonCell(factory.call(null));
		}
}
