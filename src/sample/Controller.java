package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class Controller {
    @FXML
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


        if (Flag == false){
            origem = n;

            posicaoAntigaX = GridPane.getRowIndex(n);
            posicaoAntigaY = GridPane.getColumnIndex(n);

            System.out.println("Peça origem");
            System.out.println("Linha: "+ GridPane.getRowIndex(n));
            System.out.println("Coluna: "+ GridPane.getColumnIndex(n));

            Flag = true;
        }

        else {
            GridPane.setRowIndex(origem, GridPane.getRowIndex(n));
            GridPane.setColumnIndex(origem, GridPane.getColumnIndex(n));
            //origem.setTranslateY(origem.getTranslateY() * -1);
            System.out.println("Peça movimentada");
            System.out.println("Linha: "+ posicaoAntigaX + " > " + GridPane.getRowIndex(n));
            System.out.println("Coluna: "+ posicaoAntigaY + " > " + GridPane.getColumnIndex(n));

            Flag = false;
        }

        System.out.println("----------------------");

    }
    @FXML
    public void DigitarMSG(){
        //Node msg = (Node)event.getSource();
        String mensagem = String.valueOf(bizingoTextField.getText());
        System.out.println(mensagem);
        bizingoTextField.setText("");


        //chat.append(mensagem);
        //chat.append("\n");
        //bizingoTextArea.setText(chat.toString());
        //String chat = String.valueOf(bizingoTextArea.getText());
        //System.out.println(chat);

        try {
            connec.send(mensagem);
        } catch (Exception e) {
            //messages.appendText("Failed to send \n");
        }
        ReceberMSG(mensagem);
    }

    public void ReceberMSG(String mensagem){
        chat.append(mensagem);
        chat.append("\n");
        bizingoTextArea.setText(chat.toString());
        //bizingoTextArea.getScrollTop();
        System.out.println(chat);
    }

}
