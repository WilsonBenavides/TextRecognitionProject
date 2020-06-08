package application;

import java.io.File;
import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class Main extends Application {
	
	private static final double WIDTH = 100;
	private static final double HEIGHT = 60;
	private static final double SCALE = 8;

	@Override
	public void start(Stage primaryStage) throws Exception {

		Pane root = new Pane();
		primaryStage.setScene(createScene(root, WIDTH * SCALE, HEIGHT * SCALE));
		primaryStage.show();

	}
	
	private Scene createScene(Pane root, double cWidth, double cHeight) throws FileNotFoundException {
		
		//File imageFile = new File("C:\\Users\\Wilson\\Eclipse-Work\\TextRecognitionProject\\src\\res\\images\\prueba.png");
		File imageFile = new File("src\\res\\images\\prueba.png");
		Image image = new Image(getClass().getResource("/res/images/prueba.png").toExternalForm());
		ImageView imageView = new ImageView();
		imageView.setImage(image);
		
		ITesseract instance = new Tesseract();  // JNA Interface Mapping
        instance.setDatapath("src\\res\\tessdata");
		//instance.setDatapath("/src/res/tessdata");
        instance.setLanguage("spa");
        // ITesseract instance = new Tesseract1(); // JNA Direct Mapping

        try {
            String result = instance.doOCR(imageFile);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
		
		
		root.getChildren().add(imageView);
		Scene scene = new Scene(root, cWidth, cHeight);
		return scene;
	}

	public static void main(String[] args) {
		launch(args);
	}
}