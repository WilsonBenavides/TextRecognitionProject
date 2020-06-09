package application;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class ImageRecognitionMain extends Application {

	private static final double WIDTH = 100;
	private static final double HEIGHT = 60;
	private static final double SCALE = 8;

	TextArea txtArea = new TextArea();

	@Override
	public void start(Stage primaryStage) throws Exception {

		BorderPane root = new BorderPane();
		primaryStage.setTitle("Image Recognition Project");
		primaryStage.setScene(createScene(root, WIDTH * SCALE, HEIGHT * SCALE));
		primaryStage.show();

	}

	private Scene createScene(BorderPane root, double cWidth, double cHeight) {

		// File("C:\\Users\\Wilson\\Eclipse-Work\\TextRecognitionProject\\src\\res\\images\\prueba.png");
		Image image = new Image(getClass().getResource("/res/images/prueba.png").toExternalForm());
		ImageView imageView = new ImageView();
		imageView.setImage(image);

		final Rectangle rectangle = new Rectangle(50, 50);
		rectangle.setFill(Color.TRANSPARENT);
		rectangle.setStroke(Color.web("#2c3e50"));
		rectangle.setStrokeWidth(3);
//		rectangle.setFill(Color.BLACK);
		DragResizeMod.makeResizable(rectangle, null);

		Button btn = new Button("click me!");
		btn.setLayoutX(100 * 2);
		btn.setLayoutY(100 * 3);

		btn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent e) {

				int x = (int) rectangle.getLayoutX();
				int y = (int) rectangle.getLayoutY();
				int width = (int) rectangle.getBoundsInLocal().getWidth();
				int height = (int) rectangle.getBoundsInLocal().getHeight();

				try {
					URL imageURL = getClass().getResource("/res/images/prueba.png");
					// File imageFile = new File("src\\res\\images\\prueba.png");
					BufferedImage imageBuff = ImageIO.read(imageURL);

					BufferedImage subImg = imageBuff.getSubimage(x, y, width, height);
					// File outFile = new File("src\\res\\images\\subImageBlackColor.png");
					// ImageIO.write(subImg, "png", outFile);

					ITesseract instance = new Tesseract(); // JNA Interface Mapping
					String tessdata = getClass().getResource("/res/tessdata").toExternalForm().replaceAll("jar:file:/", "");
					// String urlPath = tessdata.substring(1);

					System.out.println(tessdata);
					instance.setDatapath(".");
					instance.setLanguage("spa");
					// ITesseract instance = new Tesseract1(); // JNA Direct Mapping

					String result = instance.doOCR(subImg);
					System.out.println(result);

					txtArea.appendText(result);

				} catch (TesseractException ex) {
					System.err.println(ex.getMessage());
					txtArea.appendText(ex.getMessage());
				} catch (IOException io) {
					System.err.println(io.getMessage());
					txtArea.appendText(io.getMessage());
				}
			}
		});

		Pane content = new Pane();
		content.getChildren().addAll(imageView, rectangle, btn);
		txtArea.setPrefHeight(HEIGHT * 2);

		root.setCenter(content);
		root.setBottom(txtArea);

		Scene scene = new Scene(root, cWidth, cHeight);
		return scene;
	}

	public static void main(String[] args) {
		launch(args);
	}
}