import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Scanner;

public class VideoTest extends JFrame implements KeyListener {
    private static final int VIDEO_WIDTH = 640;
    private static final int VIDEO_HEIGHT = 360;
    private static final String VIDEO_BACKGROUND = "-fx-background-color: black";

    private final MediaPlayer PLAYER;
    private Boolean paused;

    /**
     * Start the video test
     */
    public VideoTest() {
        // Getting video URL
        Scanner console = new Scanner(System.in);
        System.out.println("Enter your media file path (from content root):");
        String url = console.nextLine(); // Example: src/TwistersTrailer.mp4

        File mediaFile = new File(url); // Create a new file object
        while (!mediaFile.exists()) { // If file doesn't exist
            // Prompt user to re-enter a path
            System.out.println("Your file doesn't exist? Try again: ");
            url = console.nextLine();
            mediaFile = new File(url); // Create new file object using new path
        }

        System.out.println("Closing scanner...");
        console.close();

        // Creating media player
        System.out.println("Creating media player...");
        PLAYER = new MediaPlayer(
                new Media(mediaFile.toURI().toString())
        );
        paused = false;

        // Gui setup
        System.out.println("Setting up GUI...");
        setName("Main Frame");
        setBounds(0, 0, VIDEO_WIDTH, VIDEO_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setFocusable(true);
        setAlwaysOnTop(true);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(34, 40, 76));

        JPanel panel = new JPanel();
        panel.setName("Panel");
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.BLACK);
        panel.setFocusable(false);

        JLabel label = new JLabel("Video test for group project:");
        label.setName("Label");
        label.setForeground(Color.WHITE);
        label.setBackground(new Color(0, 0, 0, 0)); // Invisible
        label.setFocusable(false);

        panel.add(label, BorderLayout.NORTH);
        getContentPane().add(panel, BorderLayout.CENTER);

        // Loading video
        System.out.println("Loading video...");
        panel.add(createVideoPanel(panel.getSize(), PLAYER), BorderLayout.CENTER);

        // Adding key listener
        addKeyListener(this);

        // Loading GUI
        System.out.println("Loading frame...");
        setVisible(true);

        // Focusing GUI
        System.out.println("Focusing frame...");
        requestFocusInWindow();

        // Playing video
        System.out.println("Playing video...");
        PLAYER.play();
    }

    /**
     * Creates a new JFXPanel set-up with a video player
     * @param size The size of the panel
     * @param player The media player of the media
     * @return The set-up JFXPanel
     */
    public JFXPanel createVideoPanel(Dimension size, MediaPlayer player) {
        // Create a new JFXPanel
        JFXPanel panel = new JFXPanel();
        panel.setName("Video Panel");
        panel.setBackground(new Color(0, 0, 0));
        panel.setFocusable(false);

        // Create a media viewer (basically allows the user to view the data being outputted by MediaPlayer)
        MediaView viewer = new MediaView(player);
        viewer.setFocusTraversable(false);

        // Center video (centering the viewer)
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        viewer.setX((screen.getWidth() - size.width) / 2);
        viewer.setY((screen.getHeight() - size.height) / 2);

        // Resize video (resizing the viewer)
        DoubleProperty width = viewer.fitWidthProperty();
        DoubleProperty height = viewer.fitHeightProperty();
        width.bind(Bindings.selectDouble(viewer.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(viewer.sceneProperty(), "height"));
        viewer.setPreserveRatio(true); // Keep the aspect ratio

        // Create a StackPane layout (will stack its components back-to-front)
        StackPane layout = new StackPane();
        layout.setStyle(VIDEO_BACKGROUND); // Setting a background for space that isn't taken up by the video (it's a little weird, it takes in a String, not a Color obj)
        layout.setFocusTraversable(false);

        // Creating a new scene (type of container, like the ContentPane of a JFrame)
        Scene scene = new Scene(layout);

        // Setup
        layout.getChildren().add(viewer);

        // Apply the scene to the JFXPanel
        panel.setScene(scene);

        return panel; // Return JXFPanel for usage
    }

    public static void main(String[] args) {
        new JFXPanel(); // Work around "Toolkit not initialized"
        new VideoTest(); // Start the video test
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource().equals(this)) { // If event came from the frame
            if (e.getKeyCode() == KeyEvent.VK_SPACE) { // If user pressed space bar
                if (paused) { // If video is already paused
                    System.out.println("Play");
                    PLAYER.play(); // Play the video
                    paused = false;
                } else {
                    System.out.println("Pause");
                    PLAYER.pause(); // Pause the video
                    paused = true;
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}