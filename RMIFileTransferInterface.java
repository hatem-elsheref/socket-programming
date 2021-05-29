package com.project.rmi;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIFileTransferInterface extends Remote {
    public byte[] downloadFromServer(String filename) throws RemoteException, NotBoundException;
    public String filename() throws RemoteException;
    public byte[] accept(String filename) throws RemoteException;
    public void refuse() throws RemoteException;
}
