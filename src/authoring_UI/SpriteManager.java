package authoring_UI;

import java.util.ArrayList;

import authoring.AuthoringEnvironmentManager;
import authoring.SpriteObject;
import authoring.SpriteObjectGridManagerI;
import authoring.SpriteObjectI;
import authoring.SpriteParameterFactory;
import authoring.SpriteParameterI;
import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class SpriteManager extends TabPane {
	private DraggableGrid myGrid;
	private VBox mySprites;
	private AuthoringEnvironmentManager myAEM;
	private SpriteObjectGridManagerI mySOGM;
	private SpriteParameterFactory mySPF;
	private ArrayList<SpriteObject> mySpriteObjs = new ArrayList<SpriteObject>();
	
	protected SpriteManager(DraggableGrid grid, AuthoringEnvironmentManager AEM, SpriteObjectGridManagerI SOGM) {
		mySPF = new SpriteParameterFactory();
	    myAEM = AEM;
	    mySOGM = SOGM;
		myGrid = grid;
		mySprites = new VBox();
		getParams();
		createSprites();
        	createSpriteTabs();
        	this.setPrefWidth(110);

	}
	
	private void createSprites() {
		SpriteObject s1 = mySpriteObjs.get(0);
		SpriteObject s2 = mySpriteObjs.get(1);
		SpriteObject s3 = mySpriteObjs.get(2);
		SpriteObject s4 = mySpriteObjs.get(3);
		myAEM.addDefaultSprite(s1);
		myAEM.addDefaultSprite(s2);
		myAEM.addDefaultSprite(s3);
		myAEM.addDefaultSprite(s4);

		mySprites.getChildren().addAll(s1, s2, s3, s4);
		myGrid.addDragObject(s1);
		myGrid.addDragObject(s2);
		myGrid.addDragObject(s3);
		myGrid.addDragObject(s4);

//		createImageStack("tree.png");
//		createImageStack("brick.png");
//		createImageStack("water.png");
//		createImageStack("pikachu.png");	    
	}
	
	public void getParams() {
		ArrayList<String> urls = new ArrayList<String>();
		urls.add("tree.png");
		urls.add("brick.png");
		urls.add("pikachu.png");
		urls.add("water.png");
		
		double i = 10.0;
		ArrayList<String> s = new ArrayList<String>();
		s.add("hello");
		s.add("world");
		s.add("bye");
		for (int h = 0; h < 4; h++) {
			SpriteObject SO = new SpriteObject(urls.get(h));
			ArrayList<SpriteParameterI> myParams = new ArrayList<SpriteParameterI>();
			myParams.add(mySPF.makeParameter("canFight", true));
			myParams.add(mySPF.makeParameter("health", i));
			myParams.add(mySPF.makeParameter("name", s.get(0)));
//			for (SpriteParameterI SP : myParams) {
//				System.out.println(SP.getName());
//				System.out.println(SP.getClass());
//			}
			for (SpriteParameterI SP: myParams){
				SO.addParameter(SP);
			}
//			mySObjects.add(SO);
			Integer [] loc1 = new Integer[]{h,h+1};
			Integer [] loc2 = new Integer[]{h+1,h+2};
			Integer [] loc3 = new Integer[]{h+2,h+3};
			ArrayList<Integer[]> locs = new ArrayList<Integer[]>();
			locs.add(loc1);
			locs.add(loc2);
			locs.add(loc3);
//			mySOGM.populateCell(SO, locs);
//			i*=2;
			mySpriteObjs.add(SO);
		}
	}
	
	private void createImageStack(String imageName) {
		StackPane imageStack = new StackPane();
		for (int k = 0; k < 10; k ++) {
			ImageView image = new ImageView(new Image(imageName));
			image.setFitWidth(45);
			image.setFitHeight(45);
			imageStack.getChildren().add(image);
			//myGrid.addDragObject(image);
		}
		//mySprites.getChildren().add(imageStack);
	}
	
    private void createSpriteTabs() {
		Tab defaultSpriteTab = new Tab();
		defaultSpriteTab.setText("Default Sprites");
		defaultSpriteTab.setContent(mySprites);
		defaultSpriteTab.setClosable(false);
		
		Tab mySpriteTab = new Tab();
		mySpriteTab.setText("User Sprites");
		mySpriteTab.setClosable(false);
     
		this.getTabs().addAll(defaultSpriteTab, mySpriteTab);
		this.setSide(Side.RIGHT);
    }

}