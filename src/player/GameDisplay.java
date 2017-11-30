package player;

import java.net.URISyntaxException;
import java.util.List;

import default_pkg.SceneController;
import engine.GameMaster;
import engine.sprite.DisplayableImage;
import engine.utilities.data.GameDataHandler;
import gui.welcomescreen.WelcomeScreen;
import javafx.scene.ParallelCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class GameDisplay {
	
	private Stage stage;
	private Scene scene;
	private BorderPane rootPane;
	private Pane gamePane;
	private SceneController sceneController;
	private PlayerManager playerManager;
	private GameDataHandler gameDataHandler;
	private ParallelCamera camera;
	
	public GameDisplay(Stage currentStage, SceneController currentSceneController) {
		stage = currentStage;
		rootPane = new BorderPane();
		gamePane = new Pane();
		rootPane.setCenter(gamePane);
		sceneController = currentSceneController;
		scene = new Scene(rootPane, WelcomeScreen.WIDTH, WelcomeScreen.HEIGHT);
		camera = new ParallelCamera();
	}
	
	public void createGameDisplay() {
		scene.setOnKeyPressed(e -> playerManager.setKeyPressed(e.getCode()));
		scene.setOnKeyReleased(e -> playerManager.setKeyReleased(e.getCode()));
		scene.setOnMousePressed(e -> playerManager.setPrimaryButtonDown(e.getX(), e.getY()));
		scene.setOnMouseReleased(e -> playerManager.setPrimaryButtonUp(e.getX(), e.getY()));
		
		scene.setCamera(camera);
		
		createBack();
	}
	
	private void createBack() {
		Button back = new Button("Back");
		back.setOnMouseClicked(e -> leaveGame());
		rootPane.setTop(back);
	}
	
	private void leaveGame() {
		sceneController.switchScene(SceneController.WELCOME_SCREEN_KEY);
		playerManager.stop();
	}
	
	public void setPlayerManager(PlayerManager currentPlayerManager) {
		playerManager = currentPlayerManager;
		
	}
	
	public void setDataHandler(GameDataHandler currentGameDataHandler) {
		gameDataHandler = currentGameDataHandler;
		
	}
	
	public void setUpdatedImages (List<DisplayableImage> images) {
		//TODO; takes in new image file name, location, and size for all objects
		gamePane.getChildren().clear();
		ImageView gameImage = null;
		for (DisplayableImage image : images) {
			try {
				gameImage = new ImageView(gameDataHandler.getImage(image.getFileName()));
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			gameImage.setFitWidth(image.getWidth());
			gameImage.setFitHeight(image.getHeight());
			gameImage.setRotate(image.getHeading());
			gameImage.setX(image.getX()-image.getWidth()/2);
			gameImage.setY(image.getY()-image.getHeight()/2);
			gamePane.getChildren().add(gameImage);
		}
	}
	
	public Scene getScene() {
		return scene;
	}
	
	public Pane getPane() {
		return gamePane;
	}
}