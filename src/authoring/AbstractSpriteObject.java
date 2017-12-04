package authoring;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class AbstractSpriteObject extends ImageView {

	protected HashMap<String, ArrayList<SpriteParameterI>> categoryMap = new HashMap<String, ArrayList<SpriteParameterI>>();
	protected HashMap<String, ArrayList<SpriteParameterI>> possibleCategoryMap = new HashMap<String, ArrayList<SpriteParameterI>>();;
	protected ArrayList<AbstractSpriteObject> myInventory;
	
	// protected ImageView myImageView;
	protected String myImageURL;
	protected Integer[] myPositionOnGrid;
	protected String myName;
	protected double myNumCellsWidth;
	protected double myNumCellsHeight;
	protected String myUniqueID;
	protected String mySavePath;

	public AbstractSpriteObject() {
		super();
		setUniqueID();
	}

	public AbstractSpriteObject(boolean isRecreation) {
		if (isRecreation) {
			// Nothing
		} else {
			setUniqueID();
		}
	}

	public AbstractSpriteObject(String fileURL) {
		this();
		setupImageURLAndView(fileURL);
		System.out.println(fileURL);
		myName = fileURL.split("\\.")[0];
		// myName = fileURL.split(".")[0];
	}

	AbstractSpriteObject(HashMap<String, ArrayList<SpriteParameterI>> inCategoryMap) {
		this();
		categoryMap = new HashMap<String, ArrayList<SpriteParameterI>>(inCategoryMap);
	}

	AbstractSpriteObject(HashMap<String, ArrayList<SpriteParameterI>> inCategoryMap, String fileURL) {
		this();
		categoryMap = new HashMap<String, ArrayList<SpriteParameterI>>(inCategoryMap);
		setupImageURLAndView(fileURL);

	}

	protected void setUniqueID() {
		if (myUniqueID == null) {
			myUniqueID = SpriteIDGenerator.getInstance().getUniqueID();
		}
	}

	public void setUniqueID(String ID) {
		if (myUniqueID==null){
			myUniqueID = ID;
		}
	}

	public String getUniqueID() {
		return myUniqueID;
	}
	
	public ArrayList<AbstractSpriteObject> getInventory(){
		if (myInventory==null){
			myInventory = new ArrayList<AbstractSpriteObject>();
		}
		return myInventory;
	}
	
	public void setInventory(Collection<AbstractSpriteObject> inventoryList){
		if (myInventory==null){
			myInventory = new ArrayList<AbstractSpriteObject>();
		}
		myInventory.clear();
		myInventory.addAll(inventoryList);
	}
	
	public void addToInventory(AbstractSpriteObject ASO){
		myInventory.add(ASO);
	}
	
	public boolean removeFromInventory(AbstractSpriteObject ASO){
		if (getInventory().contains(ASO)){
			getInventory().remove(ASO);
			return true;
		}
		return false;
	}
	
	public void clearInventory(){
		myInventory.clear();
	}

	protected void setupImageURLAndView(String fileURL) {
		myImageURL = fileURL;
		this.setImage(new Image(myImageURL));
		this.setFitWidth(45);
		this.setFitHeight(45);
	}

	public double getNumCellsWidth() {
		return myNumCellsWidth;
	}

	public void setNumCellsWidth(double in) {
		myNumCellsWidth = in;
	}

	public double getNumCellsHeight() {
		return myNumCellsHeight;
	}

	public void setNumCellsHeight(double in) {
		myNumCellsHeight = in;
	}

	protected double[] getCenterCoordinates() {
		return new double[] { getXCenterCoordinate(), getYCenterCoordinate() };
	}

	public double getYCenterCoordinate() {
		double height = getNumCellsHeight();
		double ypos = getRowOnGrid();
		double centery = ypos + height / 2;
		return centery;
	}

	public double getXCenterCoordinate() {
		double width = getNumCellsWidth();
		double xpos = getColumnOnGrid();
		double centerx = xpos + width / 2;
		return centerx;
	}

	
	public ImageView getImageView() {
		// if (this.getImage() == null){
		// setupImageURLAndView(getImageURL());
		// }
		return this;
	}

	
	public Integer[] getPositionOnGrid() {
		return myPositionOnGrid;
	}

	public int getRowOnGrid() {
		return getPositionOnGrid()[0];
	}

	public int getColumnOnGrid() {
		return getPositionOnGrid()[1];
	}


	public void setPositionOnGrid(Integer[] pos) {
		myPositionOnGrid = pos;
	}

	
	public void setImageURL(String fileLocation) {
		setupImageURLAndView(fileLocation);
	}

	public String getImageURL() {
		// System.out.println("myImageURL: "+myImageURL);
		// File f = new File("brick.png");
		// System.out.println("File:\n" + f.toURI().toString());
		// Image image = new Image(f.toURI().toString());
		// Image image = null;
		// try {
		// image = new Image(myImageURL);
		// Alert al = new Alert(AlertType.INFORMATION);
		// al.setGraphic(new ImageView(image));
		// al.showAndWait();
		// } catch (Exception e) {
		// //
		// }
		// System.out.println("getImage().file: "+this.getImage().get);
		return myImageURL;
	}

	
	public void setName(String name) {
		myName = name;
	}

	
	public HashMap<String, ArrayList<SpriteParameterI>> getParameters() {
		return categoryMap;
	}

	
	public void addParameter(SpriteParameterI SP) {
		addParameter("General", SP);

	}

	public boolean addCategory(String category) {
		if (!categoryMap.containsKey(category)) {
			categoryMap.put(category, new ArrayList<SpriteParameterI>());
			System.out.println("Catgeory added: categoryMap is " + categoryMap);
			return true;
		}
		return false;
	}

	public void addParameter(String category, SpriteParameterI SP) {
		addCategory(category);
		ArrayList<SpriteParameterI> val = categoryMap.get(category);
		val.add(SP);
		categoryMap.put(category, val);
	}

	public void addPossibleParameter(String category, SpriteParameterI SP) {
		if (!possibleCategoryMap.containsKey(category)) {
			possibleCategoryMap.put(category, new ArrayList<SpriteParameterI>());
		}

		ArrayList<SpriteParameterI> val = possibleCategoryMap.get(category);
		val.add(SP);
		possibleCategoryMap.put(category, val);
	}

	public int acceptPossibleParameters() {
		int ret = 0;
		for (Entry<String, ArrayList<SpriteParameterI>> keyVal : possibleCategoryMap.entrySet()) {
			String key = keyVal.getKey();
			
			ArrayList<SpriteParameterI> val = keyVal.getValue();
			System.out.println("Key: "+ key + ", Val: " + val);
			for (SpriteParameterI item : val) {
				boolean added = addCategory(key);
				if (added) {
					ret += 1;
				}
				addParameter(key, item);
			}
		}
		return ret;
	}

	public void clearPossibleParameters() {
		this.possibleCategoryMap.clear();
	}

	
	public void applyParameterUpdate(HashMap<String, ArrayList<SpriteParameterI>> newParams) {
		replaceCategoryMap(newParams);
	}

	public void setParameterMap(HashMap<String, ArrayList<SpriteParameterI>> newParams) {
		replaceCategoryMap(newParams);
	}

	protected void replaceCategoryMap(HashMap<String, ArrayList<SpriteParameterI>> newParams) {
		categoryMap = new HashMap<String, ArrayList<SpriteParameterI>>(newParams);
	}

	
	public boolean isSame(AbstractSpriteObject other) {
		if (!(other instanceof AbstractSpriteObject)) {
			return false;
		}
		AbstractSpriteObject otherSO = (AbstractSpriteObject) other;
		System.out.println("Using custom equals method for Sprite Object");
		HashMap<String, ArrayList<SpriteParameterI>> otherMap = otherSO.getParameters();
		HashMap<String, ArrayList<SpriteParameterI>> thisMap = this.getParameters();
		for (String category : otherMap.keySet()) {
			if (!thisMap.keySet().contains(category)) {
				return false;
			}
			ArrayList<SpriteParameterI> otherParamList = otherMap.get(category);
			ArrayList<SpriteParameterI> thisParamList = new ArrayList<SpriteParameterI>(thisMap.get(category));
			if (otherParamList.size() != thisParamList.size()) {
				return false;
			}
			Iterator<SpriteParameterI> otherIt = otherParamList.iterator();
			while (otherIt.hasNext()) {
				SpriteParameterI otherSPI = otherIt.next();
				Iterator<SpriteParameterI> thisIt = thisParamList.iterator();
				while (thisIt.hasNext()) {
					SpriteParameterI thisSPI = thisIt.next();
					if (thisSPI.isSame(otherSPI)) {
						thisIt.remove();
						break;
					}
				}
			}
			if (thisParamList.size() > 0) {
				return false;
			}
		}
		return true;
	}
	
	public abstract AbstractSpriteObject newCopy();

	protected ArrayList<SpriteParameterI> getSpriteParametersMatching(String type) {
		ArrayList<SpriteParameterI> ret = new ArrayList<SpriteParameterI>();
		Class desiredClass;
		switch (type) {
		case "Boolean":
			desiredClass = BooleanSpriteParameter.class;
			break;
		case "Double":
			desiredClass = DoubleSpriteParameter.class;
			break;
		case "String":
			desiredClass = StringSpriteParameter.class;
			break;
		default:
			desiredClass = SpriteParameter.class;
			break;
		}

		for (SpriteParameterI SPI : getAllParameters()) {
			if (SPI.getClass().equals(desiredClass)) {
				ret.add(SPI);
			}
		}
		return ret;
	}

	public ArrayList<String> getParameterNamesMatching(String type) {
		ArrayList<SpriteParameterI> concreteParameters = getSpriteParametersMatching(type);
		ArrayList<String> ret = new ArrayList<String>();
		concreteParameters.forEach((item) -> {
			ret.add(item.getName());
		});
		return ret;
	}

	protected ArrayList<SpriteParameterI> getAllParameters() {
		ArrayList<SpriteParameterI> ret = new ArrayList<SpriteParameterI>();
		for (ArrayList<SpriteParameterI> SPI_LIST : getParameters().values()) {
			for (SpriteParameterI SPI : SPI_LIST) {
				ret.add(SPI);
			}
		}
		return ret;
	}

	
	public String getName() {
		return myName;
	}

	
	public void updateCategoryName(String prev, String next) {

		if (getParameters().containsKey(prev)) {
			if (!next.equals(prev)) {
				getParameters().put(next, getParameters().remove(prev));
			}
		} else {
			this.addCategory(next);
		}
		System.out.println("updatecategories: " + this.categoryMap);

	}

	// protected HashMap<String, ArrayList<SpriteParameterI>> categoryMap = new
	// HashMap<String, ArrayList<SpriteParameterI>>();
	// protected HashMap<String, ArrayList<SpriteParameterI>> possibleCategoryMap
	// = new HashMap<String, ArrayList<SpriteParameterI>>();;
	// // protected ImageView myImageView;
	// protected String myImageURL;
	// protected Integer[] myPositionOnGrid;
	// protected String myName;
	// protected double myNumCellsWidth;
	// protected double myNumCellsHeight;
	// protected String myUniqueID;
	// public Field[] getInfoToSerialize(){
	// Field[] f = this.getClass().getDeclaredFields();
	// for (int i=0;i<f.length;i++){
	// System.out.println(f.g)
	// System.out.println(f[i]);
	// }
	// System.out.println();
	// return f;
	// }
	
	public String getSavePath(){
		return mySavePath;
	}
	
	public void setSavePath(String path){
		mySavePath = path;
	}

}