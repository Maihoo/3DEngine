package api;
public class Launcher {

	public static void main(String[] args) {
		Game game = new Game("3D Engine (press ESC to move mouse)", 1800, 1000);
		game.start();
	}
}