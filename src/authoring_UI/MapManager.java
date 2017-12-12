package authoring_UI;

import java.util.ArrayList;
import java.util.List;

import authoring.AuthoringEnvironmentManager;
import authoring.SpritePanels.GameElementSelector;
import authoring.SpritePanels.SpritePanels;
import engine.utilities.data.GameDataHandler;
import gui.welcomescreen.WelcomeScreen;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import tools.DisplayLanguage;

public class MapManager extends TabPane {
	public static final int VIEW_WIDTH = MainAuthoringGUI.AUTHORING_WIDTH - ViewSideBar.VIEW_MENU_HIDDEN_WIDTH;
	public static final int VIEW_HEIGHT = WelcomeScreen.HEIGHT - 35;
	protected String TAB_TAG;
	protected static final String ADD_TAB = "+";
	protected String MANAGERNAME;

	protected Stage stage;
	protected Scene scene;
	protected SingleSelectionModel<Tab> mySelectModel;
	protected Tab addTab;
	protected ObjectProperty<Boolean> gridIsShowing;
	// private AuthoringMapEnvironment authMap;

	private ViewSideBar sideBar;
	private GameElementSelector mySprites;
	protected AuthoringEnvironmentManager myAEM;
	private int myTabCount = 1;
	private Tab currentTab;
	private boolean oldProject;
	private String projectName = "TestProject";
	private GameDataHandler myGDH;
	private List<DraggableGrid> allWorlds = new ArrayList<DraggableGrid>();
	private Pane mapEditor = new Pane();
	private SpritePanels spritePanels;
	private SpriteGridHandler mySpriteGridHandler;

	public MapManager(AuthoringEnvironmentManager AEM, Scene currentScene) {
		setTabTag();
		setManagerName();
		gridIsShowing = new SimpleObjectProperty<Boolean>();
		gridIsShowing.addListener((change, oldValue, newValue) -> {
			System.out.println(getClass() + "is updating showing from " + oldValue + "to " + newValue);
			this.mySpriteGridHandler.setGridIsShown(newValue);
		});
		myAEM = AEM;
		myGDH = myAEM.getGameDataHandler();
		scene = currentScene;
		mapEditor.getChildren().add(this);
		mySelectModel = this.getSelectionModel();
		this.setPrefWidth(VIEW_WIDTH);
		this.setPrefHeight(VIEW_HEIGHT);
		this.setLayoutX(ViewSideBar.VIEW_MENU_HIDDEN_WIDTH);

		List<DraggableGrid> DGs = getListOfDraggableGrids();
		if (DGs.size() > 0) {
			oldProject = true;
			System.out.println("size: number of worlds " + DGs.size());
			System.out.println("AN OLD GRID WAS SAVED AND NOW WILL BE LOADED");
			for (DraggableGrid w : DGs) {
				setTab(w);
			}
		} else {
			System.out.println("displaying a new grid");
			setTab();
		}
	}

	protected void setManagerName() {
		MANAGERNAME = "MapManager";
	}

	protected List<DraggableGrid> getListOfDraggableGrids() {
		return myGDH.loadWorldsFromWorldDirectory();
	}

	protected String getManagerName() {
		return MANAGERNAME;
	}

	protected SpritePanels makeSpritePanels(SpriteGridHandler mySpriteGridHandler) {
		return new SpritePanels(mySpriteGridHandler, myAEM);
	}

	protected DraggableGrid makeDraggableGrid() {
		return new DraggableGrid();
	}

	private void setTab(DraggableGrid w) { // ?
		this.setSide(Side.TOP);
		addTab = new Tab();
		addTab.setText(ADD_TAB);
		addTab.setOnSelectionChanged(e -> {
			createTab(w);
			mySelectModel.select(currentTab);
		});
		this.getTabs().add(addTab);
	}

	public void gridIsShowing() {
		gridIsShowing.set(true);
	}

	public void gridIsNotShowing() {
		gridIsShowing.set(false);
	}

	public void setGridIsShowing(boolean showing) {
		gridIsShowing.set(showing);
	}

	public boolean isGridShowing() {
		return gridIsShowing.get();
	}

	private void setTab() { // ?
		this.setSide(Side.TOP);
		addTab = new Tab(ADD_TAB);
		addTab.setOnSelectionChanged(e -> {
			createTab(makeDraggableGrid());
			mySelectModel.select(currentTab);
		});
		this.getTabs().add(addTab);
	}

	private HBox setupScene(DraggableGrid w) {
		return setupFEAuthClasses(w);
	}

	private HBox setupFEAuthClasses(DraggableGrid w) {
		allWorlds.add(w);
		if (oldProject) {
			mySpriteGridHandler = w.getSGH();
		}
		else mySpriteGridHandler = new SpriteGridHandler(myTabCount, w);
		if (mySpriteGridHandler == null) System.out.println("SGH IS NULL IN MAPMAN");
		w.construct(mySpriteGridHandler);
		mySpriteGridHandler = w.getSGH();
		mySpriteGridHandler.addKeyPress(scene);
		spritePanels = makeSpritePanels(mySpriteGridHandler);
		mySpriteGridHandler.setGridDisplayPanel(spritePanels.getDisplayPanel());
		mySpriteGridHandler.setElementSelectorDisplayPanel(spritePanels.getElementSelectorDisplayPanel());
		AuthoringMapEnvironment authMap = makeAuthoringMapEnvironment(spritePanels, w);
		return authMap;
	}

	protected AuthoringMapEnvironment makeAuthoringMapEnvironment(SpritePanels spritePanels, DraggableGrid dg) {
		return new AuthoringMapEnvironment(spritePanels, dg);
	}

	private void createTab(DraggableGrid w) { // ?
		currentTab = createEditableTab();
		currentTab.setOnClosed(e -> this.removeWorld(w));
		currentTab.setContent(setupScene(w));
		this.getTabs().add(this.getTabs().size() - 1, currentTab);
		myTabCount++;
		System.out.println("tab incremented");
	}

	protected void setTabTag() {
		TAB_TAG = "Map";
	}

	private void removeWorld(DraggableGrid w) {
		allWorlds.remove(w);
		myTabCount--;
	}

	private List<AuthoringMapEnvironment> getAllMapEnvironments() {
		List<AuthoringMapEnvironment> allMaps = new ArrayList<AuthoringMapEnvironment>();
		for (Tab t : this.getTabs()) {
			if (!t.getText().equals(ADD_TAB)) {
				AuthoringMapEnvironment AME = (AuthoringMapEnvironment) t.getContent();
				allMaps.add(AME);
			}
		}
		return allMaps;
	}

	public Pane getPane() {
		return mapEditor;
	}

	public Tab getDialoguesTab() {
		return spritePanels.getDialoguesTab();
	}

	public List<DraggableGrid> getAllWorlds() {
		return allWorlds;
	}

	private Tab createEditableTab() {
		StringProperty tabMap = new SimpleStringProperty();
		tabMap.bind(Bindings.concat(DisplayLanguage.createStringBinding(TAB_TAG))
				.concat(" " + Integer.toString(myTabCount)));
		//
		final Label label = new Label(tabMap.get());
		// cannot bind editable tab label!!

		final Tab tab = new Tab();
		tab.setGraphic(label);
		final TextField textField = new TextField();
		label.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				if (((MouseEvent) event).getClickCount() == 2) {
					textField.setText(label.getText());
					tab.setGraphic(textField);
					textField.selectAll();
					textField.requestFocus();
				}
			}
		});

		textField.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				label.setText(textField.getText());
				tab.setGraphic(label);
			}
		});

		textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					label.setText(textField.getText());
					tab.setGraphic(label);
				}
			}
		});

		return tab;
	}

}