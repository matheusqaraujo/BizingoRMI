package sample;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ICommunication extends Remote{

    void ReceberJogada(String acao) throws RemoteException;
    void ReceberMSG(String acao) throws RemoteException;
    void ReiniciarPartida() throws RemoteException;
    void DesistirTela(String acao) throws RemoteException;
    void TravarTela() throws RemoteException;
    void NovaTela() throws RemoteException;
    void ComecarJogo() throws RemoteException;

}