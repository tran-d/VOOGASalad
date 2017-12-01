package engine;

import player.PlayerManager;

public interface EngineController {
	public void start();
	public void setCurrentWorld(String s);
	public void setPlayerManager(PlayerManager currentPlayerManager);
	public void addWorld(GameWorld w);
	
	public void addBlueprints(GameObjectFactory f);
}
