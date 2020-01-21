package gui;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
public class MainViewController implements Initializable{
	@FXML
	private MenuItem Seller;
	@FXML
	private MenuItem Department;
	@FXML
	private MenuItem About;
	@FXML
	public void onSeller() {
		System.out.println("DEu certo");
	}
	@FXML
	public void onDepartment() {
		System.out.println("Department");
	}
	@FXML
	public void onAbout() {
		loadView("/gui/About.fxml");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
	}
	private void loadView(String name) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(name));
			VBox nbox = loader.load();
			
			Scene m = Main.getM();
			VBox mbox = (VBox)((ScrollPane) m.getRoot()).getContent();
			Node menu = mbox.getChildren().get(0);
			mbox.getChildren().clear();
			mbox.getChildren().add(menu);
			mbox.getChildren().addAll(nbox);
		}catch(IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}
