package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Main extends Application {

    private CommunicationImpl serverCommunicationImpl;
    private ICommunication oponentCommunication;
    private Registry registry;

    // Argumentos
    static String jogadorNome; //= "Fulano";
    static Integer jogadorPort; //= 3000;
    static Integer jogadorNum; //= 1;

    private static void setArgumentos(String nome, int Port, int num){
        jogadorNome = nome; //= "Fulano";
        jogadorPort = Port; //= 3000;
        jogadorNum = num; //= 1;
    }

    // Controller
    Controller cont;
    private Controller setController(Controller controller){
        return this.cont = controller;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        initServerCommunication();
        if (jogadorNum == 2) {
            initClientCommunication();
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Bizingo [" + jogadorNome + " | Jogador: " + jogadorNum + " ]");
        primaryStage.getIcons().add(new Image("file:src/ifce1.png"));
        primaryStage.setScene(new Scene(root, 1008, 602));
        primaryStage.show();

        Controller ctrl = loader.getController();
        setController(ctrl);
        serverCommunicationImpl.setControllerRMI(ctrl);
        ctrl.setNome(jogadorNome);
        ctrl.setNum(jogadorNum);
        ctrl.setPort(jogadorPort);
        ctrl.setConnection(oponentCommunication);
        ctrl.setRegistry(registry);
    }


    private void initClientCommunication() {
        try {
            this.oponentCommunication = (ICommunication) LocateRegistry.getRegistry(jogadorPort).lookup("Communication" + jogadorNum);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    private void initServerCommunication() {
        try {
            this.serverCommunicationImpl = new CommunicationImpl(this.cont,() -> {initClientCommunication();});
            ICommunication stub = (ICommunication) UnicastRemoteObject.exportObject(serverCommunicationImpl, 0);
            if (jogadorNum == 1)
                registry = LocateRegistry.createRegistry(jogadorPort);
            else
                registry = LocateRegistry.getRegistry(jogadorPort);
            registry.rebind("Communication" + jogadorNum, stub);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String Nome = args[0];
        Integer Port = Integer.parseInt(args[1]);
        Integer numero = Integer.parseInt(args[2]);

        setArgumentos(Nome, Port, numero);

        launch(args);
    }
}
