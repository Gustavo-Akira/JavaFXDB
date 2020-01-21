package gui.util;
import javafx.scene.control.TextField;
public class Constraints {
	public static void setTextFieldInteger(TextField t) {
		t.textProperty().addListener((obs,o,n)->{
			if(n != null && !n.matches("\\d*")) {
				t.setText(o);
			}
		});
	}
	public static void setMax(TextField t, int m) {
		t.textProperty().addListener((obs,o,n)->{
			if(n != null && n.length() > m) {
				t.setText(o);
			}
		});
	}
}
