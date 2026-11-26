
package ifrs.ibiruba.agendamentoservico;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

//Classe principal da aplicação.

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("telaPrincipal"), 1100, 720);

        stage.setTitle("Agendamento Serviço");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                App.class.getResource(fxml + ".fxml")
        );

        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
