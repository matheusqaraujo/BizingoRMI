package sample;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class Controller {

    public GridPane bizingoBoard;
    public TextField bizingoTextField;
    public TextArea bizingoTextArea;

    Integer posicaoAntigaX = 0;
    Integer posicaoAntigaY = 0;
    boolean Flag = false;
    Node origem;

    StringBuilder chat = new StringBuilder();

    //Conexao

    BizingoSocket connec;

    public void getConnection(BizingoSocket connection){
        this.connec = connection;
    }

    String nomeJogador;

    public void getNome (String nome){
        this.nomeJogador = nome;
    }


    @FXML
    public void FazerJogada(Event event){

        Node n = (Node)event.getSource();

        if (Flag == false && n.getTranslateX() == 10.0){

            origem = n;

            posicaoAntigaX = GridPane.getRowIndex(origem);
            posicaoAntigaY = GridPane.getColumnIndex(origem);

            System.out.println("Peça origem");
            System.out.println("Linha: "+ posicaoAntigaX);
            System.out.println("Coluna: "+ posicaoAntigaY);

            Flag = true;
        }

        else if(Flag) {
            String jogada = "J" + "-" + posicaoAntigaX.toString() + "-" + posicaoAntigaY.toString() + "-" +  GridPane.getRowIndex(n).toString() + "-" + GridPane.getColumnIndex(n);
            ReceberJogada(jogada);


            try {
                connec.send(jogada);
            } catch (Exception e) {
                chat.append("Erro ao movimentar peça.\n");
            }

            Flag = false;
        }

        System.out.println("----------------------");
    }

    //boolean meuTurno = true;

    public void Turno (boolean meuTurno){
        if (meuTurno){
            this.bizingoBoard.setDisable(false);
            //meuTurno = false;
        }

        else{
            this.bizingoBoard.setDisable(true);
            //meuTurno = true;
        }
    }


    public void ReceberJogada(String jogada){

        String[] data = (jogada).split("-");

        int antigoX = Integer.parseInt(data[1]);
        int antigoY = Integer.parseInt(data[2]);
        int novoX = Integer.parseInt(data[3]);
        int novoY = Integer.parseInt(data[4]);

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
        //origem.setTranslateY(origem.getTranslateY() * -1);
        System.out.println("Peça movimentada");
        System.out.println("Linha: "+ posicaoAntigaX + " > " + novoX);
        System.out.println("Coluna: "+ posicaoAntigaY + " > " + novoY);

        CondicaoVitoria(novoX, novoY);
    }

    public void CondicaoVitoria(int x, int y){

        if (x == 0 && y >= 8 && y <= 12){
            String vencedor = "O jogador 2 ganhou!";

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MENSAGEM");
            alert.setHeaderText(null);
            alert.setContentText(vencedor);
            alert.show();
        }
        else if (x == 10 && y >= 1 && y <= 19){
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
        String mensagem = "M" + "-";
        mensagem += String.valueOf(bizingoTextField.getText());

        String[] msg = (mensagem).split("-");

        System.out.println(mensagem);
        bizingoTextField.setText("");

        chat.append(msg[1]);
        chat.append("\n");
        bizingoTextArea.setText(chat.toString());
        //String chat = String.valueOf(bizingoTextArea.getText());
        //System.out.println(chat);

        try {
            connec.send(mensagem);
        } catch (Exception e) {
            chat.append("Erro ao enviar mensagem.\n");
        }
    }

    public void ReceberMSG(String mensagem){
        String[] msg = (mensagem).split("-");

        chat.append(msg[1]);
        chat.append("\n");
        bizingoTextArea.setText(chat.toString());
        System.out.println(chat);
    }

    public void ReiniciarPartida(){
        //primaryStage.hide();
        //start(new Stage());
    }

    public void Desistir(){
        String msg = "D";

        try {
            connec.send(msg);
        } catch (Exception e) {
            chat.append("Erro ao enviar mensagem.\n");
        }

        DesistirTela();

        System.exit(0);
    }

    public void DesistirTela(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("MENSAGEM");
        alert.setHeaderText(null);
        alert.setContentText("O jogador desistiu!");
        alert.showAndWait();
    }

    public void Acao(String acao){

        String[] data = (acao).split("-");

        String x = data[0];

        switch (x){
            case "J":
                ReceberJogada(acao);
                break;
            case "M":
                ReceberMSG(acao);
                break;
            case "R":
                ReiniciarPartida();
                break;
            case "D":
                DesistirTela();
                break;
        }
    }

}
