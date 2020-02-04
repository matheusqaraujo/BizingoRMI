package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class Controller {
    @FXML
    public GridPane bizingoBoard;
    @FXML
    public void TESTE_ALGUMA_COISA(Event event){

        Node n = (Node)event.getSource();

        String[] data = ((String)n.getUserData()).split("-");
        System.out.println("LINHA: " + data[0]);
        System.out.println("COLUNA: " + data[1]);

    }
}
