package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.AListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entities.Department;
import model.services.DepartmentService;

public class DFController implements Initializable {
	private Department enti;
	private DepartmentService ser;
	private List<AListener> list = new ArrayList<>();
	@FXML
	private TextField tid;
	@FXML 
	private TextField tname;
	@FXML
	private Button register;
	@FXML
	private Label Errorn;
	@FXML
	private Button cancel;
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
	private Department getFormData() {
		Department o = new Department();
		ValidationException e = new ValidationException("Erro de validação");
		
		o.setId(Utils.transformParsetoInt(tid.getText()));
		if(tname.getText() == null || tname.getText().trim().equals("")) {
			e.addErros("nome", "Campo não pode ser vazio");
		}
		o.setName(tname.getText());
		if(e.getErros().size()>0) {
			throw e;
		}
		return o;
	}
	@FXML
	public void onClickC(ActionEvent e){
		Utils.current(e).close();
	}
	
	public void setDep(Department e) {
		this.enti = e;
	}
	public void setDPS(DepartmentService ser) {
		this.ser = ser;
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
	}
	public void updateFormData() {
		if(enti == null) {
			throw new IllegalStateException("Entidade n existe ou vazia");
		}
		tid.setText(String.valueOf(enti.getId()));
		tname.setText(enti.getName());
		
	}
	private void setEM(Map<String,String> e) {
		Set<String> fields = e.keySet();
		if(fields.contains("name")) {
			Errorn.setText(e.get("name"));
		}
	}
	
}
