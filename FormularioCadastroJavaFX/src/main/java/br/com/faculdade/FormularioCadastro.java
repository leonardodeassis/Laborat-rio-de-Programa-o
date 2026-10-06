package br.com.faculdade;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FormularioCadastro extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                FormularioCadastro.class.getResource("FormularioCadastro.fxml")
        );

        Parent root = loader.load();

        Scene scene = new Scene(root, 430, 330);
        scene.getStylesheets().add(
                FormularioCadastro.class.getResource("estilo.css").toExternalForm()
        );

        stage.setTitle("Formulário para cadastro de Pessoa");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
