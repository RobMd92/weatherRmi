import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Sensor extends UnicastRemoteObject implements SensorRemote {
    private Humidity hum = null;
    private Tempreture temp = null;

    private Sensor() throws RemoteException {

        startTemp();
        startHum();
    }

    @Override
    public boolean humidityDone() throws RemoteException {
        startHum();
        return hum.humDone();
    }

    @Override
    public boolean tempDone() throws RemoteException {
        startTemp();
        return temp.tempDone();
    }

    @Override
    public double getTemp() throws RemoteException {
        startTemp();
        return temp.getTemp();

    }

    @Override
    public double getHumidity() throws RemoteException {
        startHum();
        return hum.getHum();
    }

    @Override
    public void humReset() throws RemoteException {
        this.hum = null;

    }

    @Override
    public void tempReset() throws RemoteException {
        this.temp = null;
    }


    public static void main(String[] args) {

        //connection on port 8080
        try {
            LocateRegistry.createRegistry(8080);
            Sensor server = new Sensor();

            Naming.rebind("//localhost:8080/Sensor", server);


        } catch (java.net.MalformedURLException e) {
            System.out.println("Malformed URL for MessageServer name " + e.toString());
        } catch (RemoteException e) {
            System.out.println("Communication error " + e.toString());
        }

    }
    private void startHum() {
        if (hum == null) {
            hum = new Humidity();
            hum.start();
        }
    }

    private void startTemp() {
        if (temp == null) {
            temp = new Tempreture();
            temp.start();
        }
    }
}

//tempreture thread to get a random number and return it and a boolean when its done
class Tempreture extends Thread {
    private double ge;
    private boolean done = false;

    public void run() {
        try {
            done = false;
            double temp = 1;
            if (temp > -1) {
                ge = Math.random() * (100 - 1) + 1;

            }

            Thread.sleep(1500);

            done = true;


        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public boolean tempDone() {
        return done;
    }

    public double getTemp() {
        done = true;
        return ge;

    }

}
//humidity thread to get a random number and return it and a boolean when its done
class Humidity extends Thread {
    private double ge;
    private boolean done = false;

    public void run() {
        try {
            done = false;
            double hum = 1;
            if (hum > -1) {
                ge = Math.random() * (100 - 1) + 1;
            }
            Thread.sleep(1500);
            done = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public boolean humDone() {
        return done;
    }

    public double getHum() {
        done = true;
        return ge;
    }


}