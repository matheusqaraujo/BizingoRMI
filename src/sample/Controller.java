package sample;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
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

    public BizingoSocket getConnection(BizingoSocket connection){
        return this.connec = connection;
    }


    @FXML
    public void TESTE_ALGUMA_COISA(Event event){

        Node n = (Node)event.getSource();


        //String[] data = ((String)n.getUserData()).split("-");
        //System.out.println("LINHA: " + data[0]);
        //System.out.println("COLUNA: " + data[1]);

        if (n.getTranslateX() == 10.0){
            if (Flag == false){
                origem = n;

                posicaoAntigaX = GridPane.getRowIndex(origem);
                posicaoAntigaY = GridPane.getColumnIndex(origem);

                System.out.println("Peça origem");
                System.out.println("Linha: "+ GridPane.getRowIndex(n));
                System.out.println("Coluna: "+ GridPane.getColumnIndex(n));

                Flag = true;
            }

            else {
                String jogada = "J" + "-" + posicaoAntigaX.toString() + "-" + posicaoAntigaY.toString() + "-" +  GridPane.getRowIndex(n).toString() + "-" + GridPane.getColumnIndex(n);
                ReceberJogada(jogada);

                try {
                    connec.send(jogada);
                } catch (Exception e) {
                    chat.append("Failed to send \n");
                }

            /*
            GridPane.setRowIndex(origem, GridPane.getRowIndex(n));
            GridPane.setColumnIndex(origem, GridPane.getColumnIndexn));
            //origem.setTranslateY(origem.getTranslateY() * -1);
            System.out.println("Peça movimentada");
            System.out.println("Linha: "+ posicaoAntigaX + " > " + GridPane.getRowIndex(n));
            System.out.println("Coluna: "+ posicaoAntigaY + " > " + GridPane.getColumnIndex(n));
            */
                Flag = false;
            }

            System.out.println("----------------------");
        }


    }


    public void ReceberJogada(String jogada){

        String[] data = (jogada).split("-");

        int antigoX = Integer.parseInt(data[1]);
        int antigoY = Integer.parseInt(data[2]);
        int novoX = Integer.parseInt(data[3]);
        int novoY = Integer.parseInt(data[4]);

        Node original = null;

        bizingoBoard.getChildren();
        ObservableList<Node> childrens = bizingoBoard.getChildren();
        for (Node node : childrens) {
            if(bizingoBoard.getRowIndex(node) == antigoX && bizingoBoard.getColumnIndex(node) == antigoY && node.getTranslateX() == 10.0 )  {
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

    }

    @FXML
    public void DigitarMSG(){
        //Node msg = (Node)event.getSource();
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
            chat.append("Failed to send \n");
        }
        //ReceberMSG(mensagem);
    }

    public void ReceberMSG(String mensagem){
        String[] msg = (mensagem).split("-");

        chat.append(msg[1]);
        chat.append("\n");
        bizingoTextArea.setText(chat.toString());
        //bizingoTextArea.getScrollTop();
        System.out.println(chat);
    }

    public void ReiniciarPartida(){}

    public void Desistir(){}

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
                Desistir();
                break;
        }
    }

}
