package project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class Main extends Application
{
    public static Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        AnchorPane root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("project.fxml")));
        mainStage = primaryStage;

        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(Objects.requireNonNull(getClass().getResource("stylesheet.css")).toExternalForm());
        primaryStage.setTitle("Desk Management and Booking System");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();

        primaryStage.getIcons().add(new Image("/project/images/launcher_icon.png"));
    }


    public static void main(String[] args)
    {
        Application.launch(args);
    }
}
