package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    // Argumentos
    static boolean jogadorTipo; //= false;
    static String jogadorNome; //= "Sicrano";
    static String jogadorIP; //= "127.0.0.1";
    static Integer jogadorPort; //= 1234;
    static Integer jogadorNum; //= 2;

    private static void setArgumentos(boolean tipo, String nome, String IP, int Port, int num){
        jogadorTipo = tipo; //= false;
        jogadorNome = nome; //= "Sicrano";
        jogadorIP = IP; //= "127.0.0.1";
        jogadorPort = Port; //= 1234;
        jogadorNum = num; //= 2;
    }

    // Conexão
    private BizingoSocket connection;

    // Controller
    Controller cont;
    private Controller setController(Controller controller){
        return this.cont = controller;
    }

    @Override
    public void init() throws Exception{
        connection = jogadorTipo ? createServer() : createClient();
        connection.startConnection();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Bizingo [" + jogadorNome + " | Jogador: " + jogadorNum + " ]");
        primaryStage.getIcons().add(new Image("file:src/ifce1.png"));
        primaryStage.setScene(new Scene(root, 1008, 602));
        primaryStage.show();

        Controller ctrl = loader.getController();
        ctrl.setConnection(connection);
        setController(ctrl);

        ctrl.setNome(jogadorNome);
        ctrl.setNum(jogadorNum);
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

    public static void main(String[] args) {

        Boolean Tipo = Boolean.parseBoolean(args[0]);
        String Nome = args[1];
        String IP = args[2];
        Integer Port = Integer.parseInt(args[3]);
        Integer numero = Integer.parseInt(args[4]);

        setArgumentos(Tipo, Nome, IP, Port, numero);

        launch(args);
    }
}
