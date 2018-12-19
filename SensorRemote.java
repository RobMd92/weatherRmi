import java.rmi.Remote;
import java.rmi.RemoteException;

interface SensorRemote extends Remote {

    boolean humidityDone() throws RemoteException;

    boolean tempDone() throws RemoteException;

    double getTemp() throws RemoteException;

    double getHumidity() throws RemoteException;

    void humReset() throws RemoteException;

    void tempReset() throws RemoteException;


}
