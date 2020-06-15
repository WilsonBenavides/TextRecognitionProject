package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
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
		primaryStage.setMaximized(true);
		primaryStage.show();

	}

	private Scene createScene(BorderPane root, double cWidth, double cHeight) {

		// File("C:\\Users\\Wilson\\Eclipse-Work\\TextRecognitionProject\\src\\res\\images\\prueba.png");
		Image image = new Image(getClass().getResource("/res/images/prueba2.jpg").toExternalForm());
		ImageView imageView = new ImageView();
		imageView.setImage(image);
		ScrollPane sp = new ScrollPane();
		sp.setContent(imageView);
		sp.setHbarPolicy(ScrollBarPolicy.ALWAYS);
		sp.setVbarPolicy(ScrollBarPolicy.ALWAYS);

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
					System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
					// String imgLocation =
					// getClass().getResource("/res/images/prueba2.jpg").toExternalForm().replaceAll("file:/",
					// "");
					Mat img = new Mat();
					// img =
					// Imgcodecs.imread("C:\\Users\\Wilson\\Eclipse-Work\\TextRecognitionProject\\src\\res\\images\\prueba2.jpg");
					img = Imgcodecs.imread("src\\res\\images\\prueba2.jpg");
					// Imgcodecs.imwrite("C:\\Users\\Wilson\\Eclipse-Work\\TextRecognitionProject\\src\\res\\images\\01_opencv.jpg",
					// img);
					Imgcodecs.imwrite("src\\res\\images\\01_opencv.jpg", img);
					System.out.println("save 01_opencv.jpg");

					Mat imgGray = new Mat();
					Imgproc.cvtColor(img, imgGray, Imgproc.COLOR_BGR2GRAY);
					Imgcodecs.imwrite("src\\res\\images\\02_opencv_gray.jpg", imgGray);
					System.out.println("save 02_opencv_gray.jpg");

					Mat imgGrayIn = new Mat();
					Core.bitwise_not(imgGray, imgGrayIn);
					Imgcodecs.imwrite("src\\res\\images\\03_opencv_gray_invert.jpg", imgGrayIn);
					System.out.println("save 03_opencv_gray_in.jpg");

					Mat imgGaussianBlur = new Mat();
					Imgproc.GaussianBlur(imgGray, imgGaussianBlur, new Size(3, 3), 0);
					Imgcodecs.imwrite("src\\res\\images\\04_opencv_blur.jpg", imgGaussianBlur);
					System.out.println("save 04_opencv_blur.jpg");

					Mat imgGaussianBlurIn = new Mat();
					Imgproc.GaussianBlur(imgGrayIn, imgGaussianBlurIn, new Size(3, 3), 0);
					Imgcodecs.imwrite("src\\res\\images\\05_opencv_blur_in.jpg", imgGaussianBlurIn);
					System.out.println("save 05_opencv_blur_in.jpg");

					Mat imgAdaptiveThreshold = new Mat();
					Imgproc.adaptiveThreshold(imgGaussianBlur, imgAdaptiveThreshold, 255,
							Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 99, 4);
					Imgcodecs.imwrite("src\\res\\images\\06_opencv_adaptative.jpg", imgAdaptiveThreshold);
					System.out.println("save 06_opencv_adaptative.jpg");

					Mat imgAdaptiveThresholdIn = new Mat();
					Imgproc.adaptiveThreshold(imgGaussianBlur, imgAdaptiveThresholdIn, 255,
							Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 7, 1);
					Imgcodecs.imwrite("src\\res\\images\\07_opencv_adaptative_in.jpg", imgAdaptiveThresholdIn);
					System.out.println("save 07_opencv_adaptative_in.jpg");
					
					Mat imgAdaptiveThresholdNew = new Mat();
					Imgproc.adaptiveThreshold(imgAdaptiveThresholdIn, imgAdaptiveThresholdNew, 255,
							Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 99, 4);
					Imgcodecs.imwrite("src\\res\\images\\08_opencv_adaptative_new.jpg", imgAdaptiveThresholdNew);
					System.out.println("save 08_opencv_adaptative_new.jpg");

					//URL imageURL = getClass().getResource("/res/images/06_opencv_adaptative.jpg");
					URL imageURL = getClass().getResource("/res/images/07_opencv_adaptative_in.jpg");
					System.out.println("resource image: " + imageURL.getPath());
					// File imageFile = new File("src\\res\\images\\prueba.png");
					BufferedImage imageBuff = ImageIO.read(imageURL);

					BufferedImage subImg = imageBuff.getSubimage(x, y, width, height);
					File outFile = new File("src\\res\\images\\subImageBlackColor.png");
					ImageIO.write(subImg, "png", outFile);
					System.out.println("save Subimage..");

					ITesseract instance = new Tesseract(); // JNA Interface Mapping
					String tessdata = getClass().getResource("/res/tessdata").toExternalForm().replaceAll("file:/", "");
					// String urlPath = tessdata.substring(1);

					System.out.println(tessdata);

					// instance.setDatapath("."); //production only
					instance.setDatapath(tessdata);
					instance.setLanguage("spa");
					instance.setTessVariable("tessedit_char_whitelist",
							"abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPRSTUVWXYZ0123456789.,");
					// ITesseract instance = new Tesseract1(); // JNA Direct Mapping

					String result = instance.doOCR(subImg).trim();
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
		content.getChildren().addAll(rectangle, btn);

		txtArea.setPrefHeight(HEIGHT * 2);

		root.setCenter(sp);
		root.getChildren().add(content);
		root.setBottom(txtArea);

		Scene scene = new Scene(root, cWidth, cHeight);

		return scene;
	}

	public static void main(String[] args) {
		launch(args);
	}
}