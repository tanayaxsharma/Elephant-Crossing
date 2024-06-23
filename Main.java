import javax.swing.*;


class Main extends JFrame  {
    // To eliminate a warning shown in Eclipse, add this constant
    private static final long serialVersionUID = 1L;

    // This is declared volatile so that loops that use
    // it are not optimized strangely. We need to access this
    // variable from different threads, meaning that it can
    // change at any second!! It is volatile.
    // private static volatile boolean done = false;
    // private Game gamePanel = new Game(false);
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    // This is used to establish the animation speed
    public static int delay = 100;

    Main(boolean pause) {
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		add(new Game(pause));
		setVisible(true);
	}

    public static void main(String[] args) throws InterruptedException {
        final boolean pause = false;

        new Main(pause);
    }

}
