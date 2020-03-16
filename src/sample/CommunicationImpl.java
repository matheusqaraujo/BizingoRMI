package sample;

import java.rmi.RemoteException;
import sample.ICommunication;
import javafx.application.Platform;


public class CommunicationImpl implements ICommunication{


    private Runnable clientInitFunction;

    public Controller cont;

    public void setControllerRMI (Controller controller){
        this.cont = controller;
    }

    public CommunicationImpl(Controller cont, Runnable clientInitFunction) throws RemoteException{
        this.cont = cont;
        this.clientInitFunction = clientInitFunction;

    }


    @Override
    public void ReceberJogada(String acao) throws RemoteException {
        this.cont.ReceberJogada(acao);

    }

    @Override
    public void ReceberMSG(String acao) throws RemoteException {
        this.cont.ReceberMSG(acao);
    }

    @Override
    public void ReiniciarPartida() throws RemoteException {
        this.cont.ReiniciarPartida();
    }

    @Override
    public void DesistirTela(String acao) throws RemoteException {
        this.cont.DesistirTela(acao);
    }

    @Override
    public void TravarTela() throws RemoteException {
        this.cont.TravarTela();
    }

    @Override
    public void NovaTela() throws RemoteException {
        this.cont.NovaTela();
    }

    @Override
    public void ComecarJogo() throws RemoteException {
        this.cont.ComecarJogo();
        clientInitFunction.run();
    }
}