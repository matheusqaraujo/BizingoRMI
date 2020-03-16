package sample;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import sample.ICommunication;

public class Controller {

    public GridPane bizingoBoard;
    public TextField bizingoTextField;
    public TextArea bizingoTextArea;
    public Button buttonComecar;

    Integer posicaoAntigaX = 0;
    Integer posicaoAntigaY = 0;
    boolean Flag = false;
    Node origem;

    StringBuilder chat = new StringBuilder();


    // Dados do Jogador
    String nomeJogador;
    Integer numJogador;
    Integer portJogador;

    public ICommunication oponentCommunication;
    public Registry registry;


    public void setNome (String nome){
        this.nomeJogador = nome;
    }
    public void setNum (Integer num){
        this.numJogador = num;
    }
    public void setPort (Integer port){
        this.portJogador = port;
    }
    public void setConnection (ICommunication conec){
        this.oponentCommunication = conec;
    }
    public void setRegistry (Registry reg){
        this.registry = reg;
    }




    @FXML
    public void FazerJogada(Event event){

        Node n = (Node)event.getSource();

        if (Flag == false && n.getTranslateX() == 10.0 && numJogador == 1 && n.getTranslateY() == 5.00){

            origem = n;

            posicaoAntigaX = GridPane.getRowIndex(origem);
            posicaoAntigaY = GridPane.getColumnIndex(origem);

            System.out.println("Peça origem");
            System.out.println("Linha: "+ posicaoAntigaX);
            System.out.println("Coluna: "+ posicaoAntigaY);

            Flag = true;
        }

        else if (Flag == false && n.getTranslateX() == 10.0 && numJogador == 2 && n.getTranslateY() == -5.00){

            origem = n;

            posicaoAntigaX = GridPane.getRowIndex(origem);
            posicaoAntigaY = GridPane.getColumnIndex(origem);

            System.out.println("Peça origem");
            System.out.println("Linha: "+ posicaoAntigaX);
            System.out.println("Coluna: "+ posicaoAntigaY);

            Flag = true;
        }

        else if(Flag) {
            String jogada = posicaoAntigaX.toString() + "-" + posicaoAntigaY.toString() + "-" +  GridPane.getRowIndex(n).toString() + "-" + GridPane.getColumnIndex(n);
            ReceberJogada(jogada);

            try {
                this.oponentCommunication.ReceberJogada(jogada);
                //connec.send(jogada);
            } catch (Exception e) {
                chat.append("Erro ao movimentar peça.\n");
            }

            Flag = false;
        }

        System.out.println("----------------------");
    }

    public void Turno (){
        this.bizingoBoard.setDisable(true);
        this.bizingoBoard.setStyle("-fx-background-color: BLACK");
        this.bizingoBoard.setOpacity(0.7);

        try {
            this.oponentCommunication.TravarTela();
        } catch (Exception e) {
            chat.append("Erro ao trocar turno.\n");
        }

    }

    public void TravarTela(){
        bizingoBoard.setDisable(false);
        bizingoBoard.setStyle("-fx-background-color: #cbecd7");
        bizingoBoard.setOpacity(1);
    }

    @FXML
    public void buttonComecar(){
        bizingoBoard.setDisable(false);
        bizingoBoard.setStyle("-fx-background-color: #cbecd7");
        bizingoBoard.setOpacity(1);

        try {
            this.oponentCommunication.ComecarJogo();
        } catch (Exception e) {
            chat.append("Erro ao trocar turno.\n");
        }

        buttonComecar.setDisable(true);
        buttonComecar.setOpacity(0);
    }

    @FXML
    public void ComecarJogo(){
        buttonComecar.setDisable(true);
        buttonComecar.setOpacity(0);
    }


    public void ReceberJogada(String jogada){

        String[] data = (jogada).split("-");

        int antigoX = Integer.parseInt(data[0]);
        int antigoY = Integer.parseInt(data[1]);
        int novoX = Integer.parseInt(data[2]);
        int novoY = Integer.parseInt(data[3]);

        Node original = null;

        ObservableList<Node> childrens = bizingoBoard.getChildren();
        for (Node node : childrens) {
            if(bizingoBoard.getRowIndex(node) == antigoX && bizingoBoard.getColumnIndex(node) == antigoY && node.getTranslateX() == 10.0 ){
                original = node;
                break;
            }
        }

        System.out.println("!!!!!!!");
        GridPane.setRowIndex(original, novoX);
        GridPane.setColumnIndex(original, novoY);
        System.out.println("Peça movimentada");
        System.out.println("Linha: "+ posicaoAntigaX + " > " + novoX);
        System.out.println("Coluna: "+ posicaoAntigaY + " > " + novoY);

        CondicaoVitoria(novoX, novoY, original.getTranslateY());
    }

    public void CondicaoVitoria(int x, int y, double peca){

        if (x == 0 && y >= 8 && y <= 12 && peca == -5.00){
            String vencedor = "O jogador 2 ganhou!";

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MENSAGEM");
            alert.setHeaderText(null);
            alert.setContentText(vencedor);
            alert.show();
        }
        else if (x == 10 && y >= 1 && y <= 19 && peca == 5.00){
            String vencedor = "O jogador 1 ganhou!";

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MENSAGEM");
            alert.setHeaderText(null);
            alert.setContentText(vencedor);
            alert.show();
        }


    }

    @FXML
    public void DigitarMSG(){
        String mensagem = "M" + "-" + nomeJogador + ": ";
        mensagem += String.valueOf(bizingoTextField.getText());

        String[] msg = (mensagem).split("-");

        System.out.println(mensagem);
        bizingoTextField.setText("");

        chat.append(msg[1]);
        chat.append("\n");
        bizingoTextArea.setText(chat.toString());
        bizingoTextArea.setScrollTop(Double.MAX_VALUE);

        try {
            this.oponentCommunication.ReceberMSG(mensagem);
            //connec.send(mensagem);
        } catch (Exception e) {
            chat.append("Erro ao enviar mensagem.\n");
        }
    }

    public void ReceberMSG(String mensagem){
        String[] msg = (mensagem).split("-");

        chat.append(msg[1]);
        chat.append("\n");
        bizingoTextArea.setText(chat.toString());
        bizingoTextArea.setScrollTop(Double.MAX_VALUE);
        System.out.println(chat);
    }

    public void ReiniciarPartida(){
        String mensagem = "N";

        /*
        try {
            connec.send(mensagem);
        } catch (Exception e) {
            chat.append("Erro ao enviar mensagem.\n");
        }

         */

        NovaTela();
    }

    public void NovaTela(){
        //tabuleiroJogador.hide();
        //tabuleiroJogador.setTitle("Bizingo [" + nomeJogador + " | Jogador: " + numJogador + " ]");
        //tabuleiroJogador.getIcons().add(new Image("file:src/ifce1.png"));
        //tabuleiroJogador.setScene(new Scene(rootJogador, 1008, 602));
        //tabuleiroJogador.show();
        //Node x = bizingoBoard.getChildren();
        //bizingoBoard.add();

        /*
        ObservableList<Node> childrens = bizingoBoard.getChildren();
        for (Node node : childrens) {
            if(bizingoBoard.getRowIndex(node) == antigoX && bizingoBoard.getColumnIndex(node) == antigoY){
                bizingoBoard.add(node);
            }

        }

         */
    }

    public void Desistir(){
        String msg = "D" + "-" + nomeJogador;


        try {
            this.oponentCommunication.DesistirTela(msg);
            //connec.send(msg);
        } catch (Exception e) {
            chat.append("Erro ao enviar mensagem.\n");
        }

        DesistirTela(msg);

        try {
            registry.unbind("Communication" + numJogador);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }

    public void DesistirTela(String nome){
        String[] data = (nome).split("-");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("MENSAGEM");
        alert.setHeaderText(null);
        alert.setContentText("O jogador " + data[1] + " desistiu!");
        alert.showAndWait();
    }

}
