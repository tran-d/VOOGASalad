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
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
//	private AuthoringMapEnvironment authMap;

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

	public MapManager(AuthoringEnvironmentManager AEM, Scene currentScene)  {
		setTabTag();
		setManagerName();
		gridIsShowing = new SimpleObjectProperty<Boolean>();
		gridIsShowing.addListener((change, oldValue, newValue)->{
			System.out.println(getClass()+"is updating showing from "+oldValue + "to "+newValue);
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
		if (DGs.size()>0){
			System.out.println("size: number of worlds " + DGs.size());
			System.out.println("AN OLD GRID WAS SAVED AND NOW WILL BE LOADED");
			for (DraggableGrid w: DGs){
				setTab();
				createTab(myTabCount, w);
			}
		} else {
			System.out.println("displaying a new grid");
			setTab();
//			DraggableGrid DG = makeDraggableGrid();
//			createTab(myTabCount, DG);
		}
	}
	
	protected void setManagerName(){
		MANAGERNAME = "MapManager";
	}
	
	protected List<DraggableGrid> getListOfDraggableGrids(){
		return myGDH.loadWorldsFromWorldDirectory();
	}
	
	protected String getManagerName(){
		return MANAGERNAME;
	}
	
	protected SpritePanels makeSpritePanels(SpriteGridHandler mySpriteGridHandler){
		return new SpritePanels(mySpriteGridHandler, myAEM);
	}
	
	protected DraggableGrid makeDraggableGrid(){
		return new DraggableGrid();
	}
	
	public void gridIsShowing(){
		gridIsShowing.set(true);
	}
	
	public void gridIsNotShowing(){
		gridIsShowing.set(false);
	}
	
	public void setGridIsShowing(boolean showing){
		gridIsShowing.set(showing);
	}
	
	public boolean isGridShowing(){
		return gridIsShowing.get();
	}
	
	
	
	
	private void setTab() { //?
		this.setSide(Side.TOP);
		addTab = new Tab();
		addTab.setText(ADD_TAB);
		addTab.setOnSelectionChanged(e -> {
			createTab(myTabCount, makeDraggableGrid());
			mySelectModel.select(currentTab);
		});
		this.getTabs().add(addTab);
	}

	private HBox setupScene(DraggableGrid w) { 
		return setupFEAuthClasses(w);
//		return authMap;
	}
	
	
	private HBox setupFEAuthClasses(DraggableGrid w) { 
		// TODO if it's old project, want all possible worlds, so many worlds!
		allWorlds.add(w);
		mySpriteGridHandler = new SpriteGridHandler(myTabCount, w);
		w.construct(mySpriteGridHandler);
		mySpriteGridHandler.addKeyPress(scene);
		spritePanels = makeSpritePanels(mySpriteGridHandler);
		mySpriteGridHandler.setGridDisplayPanel(spritePanels.getDisplayPanel());
		mySpriteGridHandler.setElementSelectorDisplayPanel(spritePanels.getElementSelectorDisplayPanel());
		AuthoringMapEnvironment authMap = makeAuthoringMapEnvironment(spritePanels, w);
		return authMap;
	}
	
	protected AuthoringMapEnvironment makeAuthoringMapEnvironment(SpritePanels spritePanels, DraggableGrid dg){
		return new AuthoringMapEnvironment(spritePanels, dg);
	}

	private void createTab(int tabCount, DraggableGrid w) { //?
		currentTab = new Tab();
		currentTab.setOnClosed(e -> this.removeWorld(w));
		StringProperty tabMap = new SimpleStringProperty();
		tabMap.bind(Bindings.concat(DisplayLanguage.createStringBinding(TAB_TAG)).concat(" " + Integer.toString(tabCount)));
		currentTab.textProperty().bind(tabMap);
//		currentTab.textProperty().set(TODO: World Name);
		currentTab.setContent(setupScene(w));
		this.getTabs().add(this.getTabs().size() - 1, currentTab);
		myTabCount++;
		System.out.println("tab incremented");	
	}
	
	protected void setTabTag(){
		TAB_TAG = "Map";
	}
	
	private void removeWorld(DraggableGrid w) {
		allWorlds.remove(w);
	}
	 
	private List<AuthoringMapEnvironment> getAllMapEnvironments(){
		List<AuthoringMapEnvironment> allMaps = new ArrayList<AuthoringMapEnvironment>();
		for (Tab t: this.getTabs()) {
			if (!t.getText().equals(ADD_TAB)){
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
}