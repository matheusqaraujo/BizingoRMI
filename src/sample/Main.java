package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    // Argumentos
    boolean jogadorTipo = true;
    String jogadorNome = "Fulano";
    String jogadorIP = "127.0.0.1";
    Integer jogadorPort = 1234;


    //private boolean isServer = true;
    private BizingoSocket connection = jogadorTipo ? createServer() : createClient();
    //private BizingoSocket connection = isServer ? createServer() : createClient();
    //private String nomeJogador = isServer ? "Server" : "Client";

    Controller cont;
    private Controller setController(Controller controller){
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
        ctrl.setConnection(connection);
        setController(ctrl);

        ctrl.setNome(jogadorNome);
    }

    @Override
    public void stop() throws Exception{
        connection.closeConnection();
    }

    private Server createServer(){
        return new Server(jogadorPort, data -> {
            Platform.runLater(() ->{
                cont.Acao(data.toString());
            });
        });
    }

    private Client createClient(){
        return new Client(jogadorIP,jogadorPort, data -> {
            Platform.runLater(() ->{
                cont.Acao(data.toString());
            });
        });
    }

    public static void main(String[] args) { launch(args); }
}
