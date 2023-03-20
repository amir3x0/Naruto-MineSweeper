package mines;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

//This class extends the Application class and is responsible
//for creating the GUI for the Minesweeper game using JavaFX.
//It uses an FXML file to define the layout of the game and loads it using an FXMLLoader.
public class MinesFX extends Application {

	/*
	 * In the start method, it creates an HBox layout, which will be the root of the
	 * scene. It then creates a new FXMLLoader and sets the location of the FXML
	 * file to load the layout defined in the FXML file and create the corresponding
	 * JavaFX objects. If an IOException is thrown, the stack trace is printed and
	 * the method returns.
	 */
	@Override
	public void start(Stage primaryStage) {

		HBox root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MinesFX.fxml"));
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		/*
		 * An image is created for the background and set as the background of the HBox
		 * layout.
		 */
		Image image = new Image("file:src/mines/background.jpg");
		root.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

		/*
		 * A new Scene object is created with the HBox layout as the root and set as the
		 * scene for the primary stage. The title of the window is set, and the primary
		 * stage is set to a minimum height and width. The primary stage is then shown.
		 */
		Scene s = new Scene(root);
		primaryStage.setTitle("The Amazing Mines Sweeper");
		primaryStage.setScene(s);
		primaryStage.setMinHeight(520);
		primaryStage.setMinWidth(270);
		primaryStage.show();
	}

	/*
	 * The main method is the entry point of the application and calls the launch
	 * method to start the JavaFX application.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
