package engine.operations.doubleops;

import engine.GameObject;
import engine.Layer;
import engine.operations.vectorops.VectorOperation;

/**
 * @author Ian Eldridge-Allegra
 *
 */
public class Magnitude implements DoubleOperation {

	private VectorOperation vector;

	public Magnitude(VectorOperation vector) {
		this.vector = vector;
	}
	
	@Override
	public Double evaluate(GameObject asking, Layer world) {
		return vector.evaluate(asking, world).magnitude();
	}

}