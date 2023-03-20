package mines;

import javafx.collections.ObservableList;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;

/*This class is the controller for the GUI of the Minesweeper game.
 *  It implements the Initializable interface, 
 *  which is used to initialize the controller with data from the FXML file.
 *  */
public class MinesFXController implements Initializable {
	// several private fields and FXML fields
	private MediaPlayer mediaPlayer, soundPlayer;
	private Media music;

	@FXML
	private HBox root;

	@FXML
	private VBox menu;

	@FXML
	private GridPane boardarea;

	@FXML
	private Mines game;

	@FXML
	private TextArea Twidth, Theight, Tmines;

	@FXML
	private Text narorator;

	@FXML
	private Button reset, play, stop, guidemark;

	@FXML
	private ImageView swithcher;

	@FXML
	private Boolean finished;

	/*
	 * The xybutton class is an inner class that extends the Button class and adds x
	 * and y properties to represent the coordinates of the button on the game
	 * board.
	 */
	private class xybutton extends Button {
		private int x, y;

		public xybutton(int i, int j) {
			this.x = i;
			this.y = j;
			this.setPrefSize(40, 40);
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

	}

	/*
	 * The initialize method is called when the controller is created and is used to
	 * set up the background music by creating a new MediaPlayer object and setting
	 * its cycle count to INDEFINITE so it loops indefinitely. The play method is
	 * then called to start playing the background music.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		music = new Media(getClass().getResource("Pelly - Naruto.mp3").toExternalForm());
		mediaPlayer = new MediaPlayer(music);
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		audioplay();
	}

	// The audioplay and audiostop methods are used to start and stop the background
	// music. and guideButtonClicked used to show symbols guide
	public void audioplay() {
		mediaPlayer.play();
	}

	public void audiostop() {
		mediaPlayer.stop();
	}

	public void guideButtonClicked(ActionEvent guidemark) {
		swithcher.setImage(new Image(getClass().getResourceAsStream("guide.jpg"), 200, 150, false, false));
	}

	/*
	 * The newgame method is called when the play button is clicked and is used to
	 * create a new game with the specified number of rows, columns, and mines. It
	 * also sets up the game board by creating a new GridPane and adding buttons to
	 * represent each location on the board.
	 */
	public void newgame(ActionEvent event) {
		// sets finished to false
		finished = false;
		// gets the number of rows from the text field
		int rows = Integer.valueOf(Twidth.getText());
		// gets the number of columns from the text field
		int cols = Integer.valueOf(Theight.getText());
		// gets the number of mines from the text field
		int numofmines = Integer.valueOf(Tmines.getText());
		// creates a new Mines object with the specified number of rows, columns, and
		// mines
		game = new Mines(rows, cols, numofmines);
		// creates a new GridPane object
		GridPane gridofmines = new GridPane();
		// sets the text of the narorator text field
		narorator.setText("Let's fight");
		// sets the image of the swithcher ImageView
		swithcher.setImage(new Image(getClass().getResourceAsStream("starting.jpg"), 200, 150, false, false));
		// nested loop to add buttons to the gridpane representing the locations on the
		// board
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				// creates a new xybutton object with the current row and column
				xybutton innerbutton = new xybutton(i, j);
				GridPane.setHgrow(innerbutton, Priority.ALWAYS);
				GridPane.setVgrow(innerbutton, Priority.ALWAYS);
				innerbutton.setMaxWidth(Double.MAX_VALUE);
				innerbutton.setMaxHeight(Double.MAX_VALUE);
				gridofmines.add(innerbutton, i, j);
				ObservableList<Node> childrens = gridofmines.getChildren();
				repaint(childrens);

				// The onMouseClicked method is called when a button on the game board is
				// clicked and is used to open or flag the corresponding location on the
				innerbutton.setOnMouseClicked(buttonEvent -> {
					// Check if the game has not already been finished
					if (!finished) {
						// If the click event is a left mouse click
						if (buttonEvent.getButton() == MouseButton.PRIMARY) {
							// Check if the button has not been flagged
							if (!game.get(innerbutton.getX(), innerbutton.getY()).equals("F")) {
								// Open the button on the game board
								boolean clicked = game.open(innerbutton.getX(), innerbutton.getY());
								repaint(childrens);
								// If the click resulted in a loss, call the lost function
								if (!clicked) {
									lost(childrens);
								} else {
									// If the game is done and the player has won
									if (game.isDone()) {
										finished = true;
										game.setShowAll(true);
										narorator.setText("thanks for the help");
										swithcher.setImage(new Image(getClass().getResourceAsStream("after fight.jpg"),
												200, 150, false, false));
										Alert alert = new Alert(Alert.AlertType.INFORMATION);
										ImageView imagewin = new ImageView(new Image(
												getClass().getResourceAsStream("victory.jpg"), 200, 150, false, false));
										imagewin.setFitHeight(200);
										imagewin.setFitWidth(300);
										alert.setGraphic(imagewin);
										alert.setContentText("YOU WON");
										repaint(childrens);
										alert.show();
									} else {
										// Randomly set the text and image for the narrator and switcher
										Random rd = new Random();
										switch (rd.nextInt(3)) {
										case 0:
											narorator.setText("good job");
											swithcher.setImage(new Image(getClass().getResourceAsStream("goodjob1.jpg"),
													200, 150, false, false));
											break;
										case 1:
											narorator.setText("keep it up");
											swithcher.setImage(new Image(getClass().getResourceAsStream("goodjob2.jpg"),
													200, 150, false, false));
											break;
										default:
											narorator.setText("a few more");
											swithcher.setImage(new Image(getClass().getResourceAsStream("goodjob3.jpg"),
													200, 150, false, false));
										}
									}

								}

							}
							// The setOnMouseClicked method is called when a button on the game board is
							// clicked and is used to open or flag the corresponding location on the board
						} else if (buttonEvent.getButton() == MouseButton.SECONDARY
								&& (game.get(innerbutton.getX(), innerbutton.getY()).equals("F")
										|| game.get(innerbutton.getX(), innerbutton.getY()).equals("."))) {
							// if the location is not already flagged
							if (!game.get(innerbutton.getX(), innerbutton.getY()).equals("F")) {
								// toggle the flag on the location in the game
								game.toggleFlag(innerbutton.getX(), innerbutton.getY());
								// set the text of the narrator
								narorator.setText("LOOK OUT!!");
								// play the sound
								soundPlayer = new MediaPlayer(
										new Media(getClass().getResource("Sharingan.mp3").toExternalForm()));
								soundPlayer.play();
								// set the image of the swithcher
								swithcher.setImage(new Image(getClass().getResourceAsStream("sasuke_helps.jpg")));
								// set the background of the button to the flag image
								innerbutton.setBackground(new Background(new BackgroundImage(
										new Image(getClass().getResourceAsStream("flag.jpg")),
										BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
										BackgroundPosition.CENTER, new BackgroundSize(innerbutton.getPrefWidth(),
												innerbutton.getPrefHeight(), false, false, false, false))));
							} else {
								// untoggle the flag on the game board
								game.toggleFlag(innerbutton.getX(), innerbutton.getY());
								// change the text on the narrator
								narorator.setText("I got it sasuke");
								// change the image on the switcher
								swithcher.setImage(new Image(getClass().getResourceAsStream("nohelp.jpg")));
								// change the background image of the button to notrevealed.jpg
								innerbutton.setBackground(new Background(new BackgroundImage(
										new Image(getClass().getResourceAsStream("notrevealed.jpg")),
										BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
										BackgroundPosition.CENTER, new BackgroundSize(innerbutton.getPrefWidth(),
												innerbutton.getPrefHeight(), false, false, false, false))));

							}
						}
					}
				});
			}
		}
		boardarea.getChildren().clear();
		boardarea.getChildren().add(gridofmines);
		// getting the window stage and resizing the window
		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.setWidth(rows * 50 + 270);
		if (cols > 10) {
			stage.setHeight(520 + (cols - 10) * 50);
		} else {
			stage.setHeight(520);
		}
	}

	/*
	 * The lost method is called when the player loses the game and is used to
	 * display a message and image to the player. It also sets the finished variable
	 * to true, shows all the mines on the board, and plays a sound effect. The
	 * method takes in an ObservableList of Nodes as a parameter, which is used to
	 * repaint the game board.
	 */
	public void lost(ObservableList<Node> childrens) {
		finished = true;
		game.setShowAll(true); // shows all the mines on the board
		narorator.setText("bakka,you lost!!!");
		swithcher.setImage(new Image(getClass().getResourceAsStream("youlost.jpg")));
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		ImageView imagelost = new ImageView(new Image(getClass().getResourceAsStream("loser.jpg")));
		imagelost.setFitHeight(200); // setting the height of the image
		imagelost.setFitWidth(300); // setting the width of the image
		alert.setGraphic(imagelost);
		alert.setContentText("Gather your chakra and try again");
		repaint(childrens);
		soundPlayer = new MediaPlayer(new Media(getClass().getResource("naruto saying baka.mp3").toExternalForm()));
		soundPlayer.play(); // playing the sound effect
		alert.show(); // showing the alert message
	}

	// The repaint method is called to update the game board with the current state
	// of the game
	public void repaint(ObservableList<Node> childrens) {
		for (Node childs : childrens) {
			xybutton child = (xybutton) childs;
			// if the button's state is " " (blank) in the game object, set the button's
			// background to the image of a blank
			if (game.get(child.getX(), child.getY()).equals(" ")) {
				child.setBackground(new Background(new BackgroundImage(
						new Image(getClass().getResourceAsStream("blank.jpg")), BackgroundRepeat.NO_REPEAT,
						BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
						new BackgroundSize(child.getPrefWidth(), child.getPrefHeight(), false, false, false, false))));
			} // if the button's state is "1" in the game object, set the button's
				// background to the image of a one
			if (game.get(child.getX(), child.getY()).equals("1")) {
				child.setBackground(new Background(new BackgroundImage(
						new Image(getClass().getResourceAsStream("one.jpg")), BackgroundRepeat.NO_REPEAT,
						BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
						new BackgroundSize(child.getPrefWidth(), child.getPrefHeight(), false, false, false, false))));
			} // if the button's state is "2" in the game object, set the button's
				// background to the image of a two
			if (game.get(child.getX(), child.getY()).equals("2")) {
				child.setBackground(new Background(new BackgroundImage(
						new Image(getClass().getResourceAsStream("two.jpg")), BackgroundRepeat.NO_REPEAT,
						BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
						new BackgroundSize(child.getPrefWidth(), child.getPrefHeight(), false, false, false, false))));
			} // if the button's state is "3" in the game object, set the button's
				// background to the image of a three
			if (game.get(child.getX(), child.getY()).equals("3")) {
				child.setBackground(new Background(new BackgroundImage(
						new Image(getClass().getResourceAsStream("three.jpg")), BackgroundRepeat.NO_REPEAT,
						BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
						new BackgroundSize(child.getPrefWidth(), child.getPrefHeight(), false, false, false, false))));
			} // if the button's state is "4" in the game object, set the button's
				// background to the image of a four
			if (game.get(child.getX(), child.getY()).equals("4")) {
				child.setBackground(new Background(new BackgroundImage(
						new Image(getClass().getResourceAsStream("four.jpg")), BackgroundRepeat.NO_REPEAT,
						BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
						new BackgroundSize(child.getPrefWidth(), child.getPrefHeight(), false, false, false, false))));
			} // if the button's state is "5" in the game object, set the button's
				// background to the image of a five
			if (game.get(child.getX(), child.getY()).equals("5")) {
				child.setBackground(new Background(new BackgroundImage(
						new Image(getClass().getResourceAsStream("five.jpg")), BackgroundRepeat.NO_REPEAT,
						BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
						new BackgroundSize(child.getPrefWidth(), child.getPrefHeight(), false, false, false, false))));
			} // if the button's state is "6" in the game object, set the button's
				// background to the image of a six
			if (game.get(child.getX(), child.getY()).equals("6")) {
				child.setBackground(new Background(new BackgroundImage(
						new Image(getClass().getResourceAsStream("six.jpg")), BackgroundRepeat.NO_REPEAT,
						BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
						new BackgroundSize(child.getPrefWidth(), child.getPrefHeight(), false, false, false, false))));
			} // if the button's state is "7" in the game object, set the button's
				// background to the image of a seven
			if (game.get(child.getX(), child.getY()).equals("7")) {
				child.setBackground(new Background(new BackgroundImage(
						new Image(getClass().getResourceAsStream("seven.jpg")), BackgroundRepeat.NO_REPEAT,
						BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
						new BackgroundSize(child.getPrefWidth(), child.getPrefHeight(), false, false, false, false))));
			} // if the button's state is "8" in the game object, set the button's
				// background to the image of a eight
			if (game.get(child.getX(), child.getY()).equals("8")) {
				child.setBackground(new Background(new BackgroundImage(
						new Image(getClass().getResourceAsStream("eight.jpg")), BackgroundRepeat.NO_REPEAT,
						BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
						new BackgroundSize(child.getPrefWidth(), child.getPrefHeight(), false, false, false, false))));
			} // if the button's state is "X" (mine) in the game object, set the button's
				// background to the image of a bomb
			if (game.get(child.getX(), child.getY()).equals("X")) {
				child.setBackground(new Background(new BackgroundImage(
						new Image(getClass().getResourceAsStream("bomb.jpg")), BackgroundRepeat.NO_REPEAT,
						BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
						new BackgroundSize(child.getPrefWidth(), child.getPrefHeight(), false, false, false, false))));
			} // if the button's state is "." (not revealed yet) in the game object, set the
				// button's
				// background to the image of a notrevealed
			if (game.get(child.getX(), child.getY()).equals(".")) {
				child.setBackground(new Background(new BackgroundImage(
						new Image(getClass().getResourceAsStream("notrevealed.jpg")), BackgroundRepeat.NO_REPEAT,
						BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
						new BackgroundSize(child.getPrefWidth(), child.getPrefHeight(), false, false, false, false))));
			}
		}
	}
}
