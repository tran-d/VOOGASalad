package authoring_UI.cutscene;

import java.util.ArrayList;
import java.util.List;

import authoring_UI.MapManager;
import authoring_UI.displayable.DisplayableManager;
import engine.utilities.data.GameDataHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tools.DisplayLanguage;

/**
 * Class that represents the pane containing all dialogue authoring components
 * 
 * @author DavidTran
 *
 */
public class CutsceneManager extends DisplayableManager {

	private static final double NODE_SPACING = 20;
	private static final double BUTTON_WIDTH = 300;
	private static final double BUTTON_HEIGHT = 75;
	private static final String ADD_BUTTON_PROMPT = "New";
	private static final String SAVE_BUTTON_PROMPT = "Save";
	private static final String DELETE_BUTTON_PROMPT = "Delete";

	private HBox hb;
	private CutsceneEditor currentEditor;
	private CutsceneTabPane dView;
	private List<CutsceneEditor> editorList;
	private int currentEditorIndex = 0;
	private CutsceneExtractor dExtractor;
	private CutsceneListView listView;

	private Tab mapCutscenesTab;
	private GameDataHandler GDH;

	public CutsceneManager(GameDataHandler GDH) {
		this.GDH = GDH;
		dView = new CutsceneTabPane();
		editorList = new ArrayList<>();
		dExtractor = new CutsceneExtractor();
		hb = new HBox(NODE_SPACING);
		hb.setLayoutX(10);
		hb.getChildren().addAll(dView, createSeparator(), createButtonPanel());

		// test
		// addDefaultDialogueButton();
		// addUserDialogueButton("blah", -1);

	}

	@Override
	protected Separator createSeparator() {
		return super.createSeparator();
	}

	@Override
	protected Separator createShortSeparator(int height) {
		return super.createShortSeparator(height);
	}

	/*************************** PUBLIC METHODS **********************************/

	public void addCutsceneListener(Tab cutsceneTab) {
		mapCutscenesTab = cutsceneTab;
		updateListView();
	}

	public HBox getPane() {
		return hb;
	}

	@Override
	protected void updateListView() {
		dExtractor.extract(editorList);
		listView = new CutsceneListView(dExtractor.getDialogueList());
		System.out.println(listView);

		mapCutscenesTab.setContent(listView);
	}

	@Override
	protected void save() {
		if (currentEditor != null && !currentEditor.getName().trim().equals("")) {
			
			if (editorList.contains(currentEditor)) {
				editorList.remove(currentEditor);
			}
			editorList.add(currentEditorIndex, currentEditor);

			// editorList.add(currentEditor);
			addUserDialogueButton(currentEditor.getName());
			// currentEditor = null;
		}
		System.out.println("# editors: " + editorList.size());

		updateListView();
	}

	protected void newEditor() {
		currentEditor = new CutsceneEditor(e -> save(), GDH);
		currentEditorIndex = editorList.size();

		loadEditor(currentEditorIndex);
	}

	protected void loadEditor(int index) {

		System.out.println("Load index: " + index);
		
		if (hb.getChildren().size() >= 4) {
			hb.getChildren().remove(5 - 1);
			hb.getChildren().remove(4 - 1);

		}

		if (editorList.size() <= index) {
			hb.getChildren().addAll(createShortSeparator(660), currentEditor.getParent());
		} else {
			hb.getChildren().addAll(createShortSeparator(660), editorList.get(index).getParent());
			currentEditor = editorList.get(index);

		}

		currentEditorIndex = index;
	}

	protected VBox createButtonPanel() {
		VBox vb = new VBox(NODE_SPACING);
		vb.getChildren().addAll(createButton(ADD_BUTTON_PROMPT, e -> newEditor()),
				createButton(SAVE_BUTTON_PROMPT, e -> save()), createButton(DELETE_BUTTON_PROMPT, e -> delete()));
		return vb;
	}

	protected void prev() {
		if (currentEditorIndex > 0) {
			currentEditorIndex -= 1;
			hb.getChildren().remove(4);
			hb.getChildren().add(editorList.get(currentEditorIndex).getParent());
		}
	}

	protected void next() {
		if (currentEditorIndex < editorList.size() - 1) {
			System.out.println("Editor List Size Called in Next: " + editorList.size());
			System.out.println("Current Editor Index Called in Next: " + currentEditorIndex);
			currentEditorIndex += 1;
			hb.getChildren().remove(4);
			hb.getChildren().add(editorList.get(currentEditorIndex).getParent());
		}
	}

	private void addDefaultDialogueButton() {
		Button btn = new Button("Default Dialogue #1");
		btn.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		// btn.setOnAction(e -> loadEditor(currentEditor));
		// change number
		dView.addDefaultCutsceneButton(0, btn);

	}

	private void addUserDialogueButton(String name) {
		System.out.println("Click Editor Index: " + currentEditorIndex);
		Button btn = new Button(name);
		btn.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		dView.addUserCutsceneButton(currentEditorIndex, btn);
		btn.setOnAction(e -> loadEditor(dView.getButtonIndex(btn)));
	}

	protected void delete() {

		for(int i = 0; i < editorList.size(); i++) {
			System.out.println("Element of Editor List Before Delete: " + editorList.get(i));
		}


		if (!editorList.isEmpty()) {
			
			removeUserDialogueButton();
			
			if (editorList.size() > 1) {
				System.out.println("Editor List Before Delete: " + editorList.size());
				System.out.println("Editor Index Before Delete: " + currentEditorIndex);

				if (currentEditorIndex == editorList.size() - 1) {
					prev();
					editorList.remove(currentEditorIndex + 1);

				} else {
					next();
					editorList.remove(currentEditorIndex - 1);
					currentEditorIndex -= 1;
				}
				
				currentEditor = editorList.get(currentEditorIndex);
			}

			else {
				hb.getChildren().remove(4);
				hb.getChildren().remove(3);
				editorList.remove(currentEditorIndex);
				currentEditorIndex -= 1;
			}
		}

		System.out.println("Editor List Size After Delete: " + editorList.size());
		System.out.println("Editor Index After Delete: " + currentEditorIndex);

		for(int i = 0; i < editorList.size(); i++) {
			System.out.println("Element of Editor List After Delete: " + editorList.get(i));
		}

	}

	private void removeUserDialogueButton() {
		int id = currentEditorIndex;
		dView.removeUserCutsceneButton(id);
	}

	
	
}
