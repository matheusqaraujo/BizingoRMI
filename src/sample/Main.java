package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private boolean isServer = true;
    private BizingoSocket connection = isServer ? createServer() : createClient();

    Controller cont;
    private Controller getController(Controller controller){
        return this.cont = controller;
    }

    @Override
    public void init() throws Exception{
        connection.startConnection();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Bizingo");
        primaryStage.setScene(new Scene(root, 1008, 602));
        primaryStage.show();

        Controller ctrl = loader.getController();
        ctrl.getConnection(connection);
        getController(ctrl);
    }

    @Override
    public void stop() throws Exception{
        connection.closeConnection();
    }

    private Server createServer(){
        return new Server(1234, data -> {
            Platform.runLater(() ->{
                cont.Acao(data.toString());
            });
        });
    }

    private Client createClient(){
        return new Client("127.0.0.1",1234, data -> {
            Platform.runLater(() ->{
                cont.Acao(data.toString());
            });
        });
    }

    public static void main(String[] args) { launch(args); }
}
