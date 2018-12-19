
import java.rmi.*;

interface CloudCover extends Remote {
    double getCloudCover() throws RemoteException;

    boolean doneYet() throws RemoteException;

    void reset() throws RemoteException;


}

