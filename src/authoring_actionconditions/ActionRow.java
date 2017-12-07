package authoring_actionconditions;

import engine.Actions.ActionFactory;
import engine.operations.OperationFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import tools.DisplayLanguage;

/**
 * Class representing an action row for sprites.
 * 
 * @author DavidTran
 *
 */
public class ActionRow extends ActionConditionRow {

	private static final double TREE_VIEW_WIDTH = 400;
	private static final String INVALID_INPUT_MESSAGE = "InvalidInput";
	private static final String DOUBLE_INPUT_MESSAGE = "EnterDouble";

	private ActionFactory actionFactory;
	private TreeView<HBox> actionTree;

	private TreeItem<HBox> categoryAction = new TreeItem<HBox>();
	private TreeItem<HBox> actionAction = new TreeItem<HBox>();
	private TreeItem<HBox> parameterAction = new TreeItem<HBox>();

	// private TreeItem<HBox> categoryOperation = new TreeItem<HBox>();
	// private TreeItem<HBox> actionOperation = new TreeItem<HBox>();
//	private TreeItem<HBox> parameterOperation = new TreeItem<HBox>();

	private TreeView<HBox> actionTreeView;
	private TreeView<HBox> operationTreeView;
	private OperationFactory operationFactory;

	public ActionRow(int ID, String label, String selectorLabel, boolean isConditionRow,
			ObservableList<Integer> newActionOptions, ActionConditionVBox ACVBox) {

		super(ID, label, selectorLabel, newActionOptions, ACVBox);

		// addBuildActionButton(e -> openBuildWindow());

		actionFactory = new ActionFactory();
		operationFactory = new OperationFactory();

		// this.getItems().add(makeActionChoiceBox("Movement"));
		// actionTree = makeTreeView(makeActionChoiceBox("Movement"));
		// this.getItems().add(actionTree);
		actionTreeView = makeActionTreeView();
		actionTreeView.setPrefWidth(TREE_VIEW_WIDTH);
		// operationTreeView = makeOperationTreeView();

		this.getItems().addAll(actionTreeView);
	}

	/*********************************
	 * ACTIONS
	 *************************************/

	private TreeView<HBox> makeActionTreeView() {
		categoryAction = makeActionCategoryTreeItem();
		TreeView<HBox> tv = new TreeView<HBox>(categoryAction);
		return tv;
	}

	private TreeItem<HBox> makeActionCategoryTreeItem() {
		HBox hb = new HBox();
		hb.getChildren().addAll(new Label("Choose Action Category: "), makeActionCategoryChoiceBox());
		TreeItem<HBox> ti = new TreeItem<HBox>(hb);
		ti.setExpanded(true);
		return ti;
	}

	private ChoiceBox<String> makeActionCategoryChoiceBox() {
		ObservableList<String> categories = FXCollections.observableList(actionFactory.getCategories());
		ChoiceBox<String> cb = new ChoiceBox<>(categories);
		System.out.println("cats: " + categories);

		cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

				// System.out.println(actions.get(newValue.intValue()));
				// getItems().add(makeParameterChoiceBox(actions.get(newValue.intValue())));
				categoryAction.getChildren().clear();
				categoryAction.getChildren()
						.add(makeActionTreeItem(categories.get(cb.getSelectionModel().getSelectedIndex())));
			}
		});
		return cb;
	}

	private TreeItem<HBox> makeActionTreeItem(String category) {
		HBox hb = new HBox();
		hb.getChildren().addAll(new Label("Choose Action: "), makeActionChoiceBox(category));
		actionAction = new TreeItem<HBox>(hb);
		actionAction.setExpanded(true);
		return actionAction;
	}

	private ChoiceBox<String> makeActionChoiceBox(String category) {
		ObservableList<String> actions = FXCollections.observableList(actionFactory.getActions(category));
		ChoiceBox<String> cb = new ChoiceBox<>(actions);
		System.out.println("Acts: " + actions);

		cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

				// System.out.println(actions.get(newValue.intValue()));
				// getItems().add(makeParameterChoiceBox(actions.get(newValue.intValue())));
				actionAction.getChildren().clear();
				actionAction.getChildren()
						.add(makeActionParameterTreeItem(actions.get(cb.getSelectionModel().getSelectedIndex())));
			}
		});
		return cb;
	}

	private TreeItem<HBox> makeActionParameterTreeItem(String action) {
		HBox hb = new HBox();
		hb.getChildren().add(new Label("Choose Action Parameter(s): "));

		TreeItem<HBox> parameterAction = new TreeItem<HBox>(hb);
		makeActionParameterChildren(action, parameterAction, hb);
		parameterAction.setExpanded(true);
		return parameterAction;
	}

	private void makeActionParameterChildren(String action, TreeItem<HBox> parameterAction, HBox hb) {
		ObservableList<String> parameters = FXCollections.observableList(actionFactory.getParameters(action));
		System.out.println("Params: " + parameters);

		hb.getChildren().add(new Label("[ "));

		for (String param : parameters) {
			hb.getChildren().add(new Label(param + " "));

			TreeItem<HBox> paramTV = makeOperationNameTreeItem(param);

			if (param.equals("Double")) {
				TextField tf = new TextField();
				TreeItem<HBox> tfTreeItem = new TreeItem<HBox>(new HBox(new Label("Insert Double: "), tf));
				parameterAction.getChildren().add(tfTreeItem);
				tf.setOnKeyReleased(e -> {
					checkDoubleInput(tf);
					checkEmptyInput(tf, parameterAction, paramTV, parameterAction.getChildren().indexOf(tfTreeItem));
				});
			} else if (param.equals("String")) {
				TextField tf = new TextField();
				TreeItem<HBox> tfTreeItem = new TreeItem<HBox>(new HBox(new Label("Insert String: "), tf));
				parameterAction.getChildren().add(tfTreeItem);
				tf.setOnKeyReleased(e -> {
					checkEmptyInput(tf, parameterAction, paramTV, parameterAction.getChildren().indexOf(tfTreeItem));
				});
			}

			parameterAction.getChildren().add(paramTV);

		}

		hb.getChildren().add(new Label("]"));

		// ChoiceBox<String> cb = new ChoiceBox<>(parameters);

		// cb.getSelectionModel().selectedIndexProperty().addListener(new
		// ChangeListener<Number>() {
		//
		// @Override
		// public void changed(ObservableValue<? extends Number> observable, Number
		// oldValue, Number newValue) {
		//
		// // System.out.println(actions.get(newValue.intValue()));
		// // getItems().add(makeParameterChoiceBox(actions.get(newValue.intValue())));
		// parameterAction.getChildren().clear();
		// parameterAction.getChildren()
		// .add(makeOperationCategoryTreeItem(parameters.get(cb.getSelectionModel().getSelectedIndex())));
		// }
		// });
		// return cb;
	}

	/****************************** OPERATIONS ******************************/

	// private TreeView<HBox> makeOperationTreeView() {
	// categoryOperation = makeOperationCategoryTreeItem();
	// TreeView<HBox> tv = new TreeView<HBox>(categoryOperation);
	// return tv;
	// }

	private TreeItem<HBox> makeOperationNameTreeItem(String actionParameter) {
		HBox hb = new HBox();
		hb.getChildren().addAll(new Label("Choose Operation: "));
		TreeItem<HBox> categoryOperation = new TreeItem<HBox>(hb);

		hb.getChildren().add(makeOperationCategoryChoiceBox(actionParameter, categoryOperation));

		categoryOperation.setExpanded(true);
		return categoryOperation;
	}

	private ChoiceBox<String> makeOperationCategoryChoiceBox(String actionParameter, TreeItem<HBox> categoryOperation) {
		// TODO change "Boolean" to actionParameter
		ObservableList<String> operations = FXCollections.observableList(operationFactory.getOperations("Boolean"));
		ChoiceBox<String> cb = new ChoiceBox<>(operations);
		System.out.println("ops: " + operations);

		cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

				// System.out.println(actions.get(newValue.intValue()));
				// getItems().add(makeParameterChoiceBox(actions.get(newValue.intValue())));
				System.out.println("Selected: " + operations.get(cb.getSelectionModel().getSelectedIndex()));
				categoryOperation.getChildren().clear();
				categoryOperation.getChildren()
						.add(makeParameterOperationTreeItem(operations.get(cb.getSelectionModel().getSelectedIndex())));
			}
		});

		return cb;
	}

	private TreeItem<HBox> makeParameterOperationTreeItem(String operation) {

		HBox hb = new HBox();
		hb.getChildren().add(new Label("Choose Operation Parameter(s): "));

		TreeItem<HBox> parameterOperation = new TreeItem<HBox>(hb);
		makeOperationParameterChildren(operation, parameterOperation, hb);
		parameterOperation.setExpanded(true);
		return parameterOperation;
	}

	private void makeOperationParameterChildren(String operation, TreeItem<HBox> parameterOperation, HBox hb) {
		ObservableList<String> parameters = FXCollections.observableList(operationFactory.getParameters(operation));
		System.out.println("Op Params: " + parameters);

//		hb.getChildren().add(new Label("[ "));
//
//		for (String param : parameters) {
//			hb.getChildren().add(new Label(param + " "));
//			parameterOperation.getChildren().add(makeOperationCategoryTreeItem(param));
//		}
//	
//		hb.getChildren().add(new Label("]"));
		
		hb.getChildren().add(new Label("[ "));

		for (String param : parameters) {
			hb.getChildren().add(new Label(param + " "));

			TreeItem<HBox> paramTV = makeOperationNameTreeItem(param);

			if (param.equals("Double")) {
				TextField tf = new TextField();
				TreeItem<HBox> tfTreeItem = new TreeItem<HBox>(new HBox(new Label("Insert Double: "), tf));
				parameterOperation.getChildren().add(tfTreeItem);
				tf.setOnKeyReleased(e -> {
					checkDoubleInput(tf);
					checkEmptyInput(tf, parameterOperation, paramTV, parameterOperation.getChildren().indexOf(tfTreeItem));
				});
			} else if (param.equals("String")) {
				TextField tf = new TextField();
				TreeItem<HBox> tfTreeItem = new TreeItem<HBox>(new HBox(new Label("Insert String: "), tf));
				parameterOperation.getChildren().add(tfTreeItem);
				tf.setOnKeyReleased(e -> {
					checkEmptyInput(tf, parameterOperation, paramTV, parameterOperation.getChildren().indexOf(tfTreeItem));
				});
			}

			parameterOperation.getChildren().add(paramTV);

		}

		hb.getChildren().add(new Label("]"));

	}

	private ChoiceBox<String> makeParameterOperationChoiceBox(String operation, TreeItem<HBox> parameterOperation) {
		ObservableList<String> operations = FXCollections.observableList(operationFactory.getParameters(operation));
		ChoiceBox<String> cb = new ChoiceBox<>(operations);
		System.out.println(operations);

		cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

				// System.out.println(actions.get(newValue.intValue()));
				// getItems().add(makeParameterChoiceBox(actions.get(newValue.intValue())));
				parameterOperation.getChildren().clear();
				// parameterOperation.getChildren()
				// .add(makeActionOperationTreeItem(operations.get(cb.getSelectionModel().getSelectedIndex())));
			}
		});
		return cb;
	}

	private void checkDoubleInput(TextField tf) {
		try {
			if (!tf.getText().equals(""))
				Double.parseDouble(tf.getText());
		} catch (NumberFormatException e) {
			showError(INVALID_INPUT_MESSAGE, DOUBLE_INPUT_MESSAGE);
//			tf.clear();
		}

	}

	private void checkEmptyInput(TextField tf, TreeItem<HBox> parameterAction, TreeItem<HBox> paramTV,
			int tfTreeViewIndex) {
		try {
			if (!tf.getText().equals(""))
				parameterAction.getChildren().remove(paramTV);
			else {
				if (!parameterAction.getChildren().contains(paramTV))
					System.out.println(tfTreeViewIndex);
				parameterAction.getChildren().add(tfTreeViewIndex + 1, paramTV);
			}
		} catch (Exception e) {

		}
	}

	private void showError(String header, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.contentTextProperty().bind(DisplayLanguage.createStringBinding(header));
		alert.headerTextProperty().bind(DisplayLanguage.createStringBinding(content));
		alert.show();
	}

	private void addBuildActionButton(EventHandler<ActionEvent> handler) {
		Button buildActionButton = new Button(actionConditionVBoxResources.getString("BuildActionButton"));
		buildActionButton.setOnAction(handler);
		getItems().add(buildActionButton);
	}

	private void openBuildWindow() {
		// if (view == null && actionOptions.getSelected() != null)
		// view = new BuildActionView(ACVBox, (ActionConditionRow)
		// ACVBox.getChildren().get(labelInt - 1));
	}

}
