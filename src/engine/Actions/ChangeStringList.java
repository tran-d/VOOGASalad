package engine.Actions;

import java.util.List;

import engine.Action;
import engine.GameObject;
import engine.World;

public class ChangeStringList implements Action {

	private String varName;
	private List<String> newStringList;
	
	public ChangeStringList(String varName, List<String> newStringList) {
		this.varName = varName;
		this.newStringList = newStringList;
	}
	
	@Override
	public void execute(GameObject asking, World world) {
		asking.setStringListVariable(varName, newStringList);
	}
	
}
