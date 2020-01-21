package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;


public class Main extends Application {
	private static Scene mainScene;
	@Override
	public void start(Stage stage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			ScrollPane parent = loader.load();
			parent.setFitToHeight(true);
			parent.setFitToWidth(true);
			mainScene = new Scene(parent);
			stage.setScene(mainScene);
			stage.setTitle("Principal");
			stage.show();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	public static Scene getM() {
		return mainScene;
	}
	public static void main(String[] args) {
		launch(args);
	}
}
