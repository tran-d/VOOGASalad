package authoring_actionconditions;

import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ActionConditionTab extends Tab {
	
	private static final double SPACING = 10;
	
	private ResourceBundle actionTabResources;
	private ScrollPane actionConditionManager;
	private TopToolBar buttons;
	private ActionConditionVBox actionConditionVBox;
	
	public ActionConditionTab(String title) {
		super(title);
		String resourcePath = "TextResources/" + title.substring(0,title.length() - 1) + "TabResources";
		actionTabResources = ResourceBundle.getBundle(resourcePath); 
		actionConditionManager = new ScrollPane();
		setContent(actionConditionManager);
		setUpActionConditionManager();
		ControllerTopToolBar controllerTopToolBar = new ControllerTopToolBar(buttons,actionConditionVBox);
	}

	private void setUpActionConditionManager() {
		buttons = new TopToolBar(actionTabResources,"AddButtonLabel","Options","SelectorLabel","RemoveButtonLabel");
		actionConditionVBox = new ActionConditionVBox(actionTabResources.getString("SelectorLabel"));
		VBox mainVBox = new VBox(SPACING);
		mainVBox.getChildren().addAll(buttons,actionConditionVBox);
		actionConditionManager.setContent(mainVBox);
	}
}
