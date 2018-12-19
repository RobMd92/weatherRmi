import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.*;
import java.lang.*;


public class Cloud extends UnicastRemoteObject implements CloudCover {

    /*Initialize with dummy thread as tacky way to avoid
     *nullPointerException before first call to nextFruit
     */
    private Cloudcoverage cloudcoverage = null;


    private Cloud() throws RemoteException {
        startCloud();
    }

    @Override
    public double getCloudCover() throws RemoteException {
        startCloud();
        return cloudcoverage.getCloud();
    }

    @Override
    public boolean doneYet() throws RemoteException {
        startCloud();
        return cloudcoverage.doneYet();
    }

    @Override
    public void reset() throws RemoteException {
        this.cloudcoverage = null;
    }


    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(9090);
            Cloud server = new Cloud();

            Naming.rebind("//localhost:9090/Cloud", server);

        } catch (java.net.MalformedURLException e) {
            System.out.println("Malformed URL for MessageServer name " + e.toString());
        } catch (RemoteException e) {
            System.out.println("Communication error " + e.toString());
        }


    }

    private void startCloud() {
        if (cloudcoverage == null) {
            cloudcoverage = new Cloudcoverage();
            cloudcoverage.start();
        }
    }


}

class Cloudcoverage extends Thread {
    private double cloudCover = 0;
    private boolean done = false;

    public void run() {

        try {
            done = false;
            if (cloudCover > -1) {
                cloudCover = Math.random() * (100 - 1) + 1;
            }


            Thread.sleep(3000);
            done = true;
        } catch (Exception e) {
            System.out.println("Server: Oh no!!!");
        }
    }


    public double getCloud() {
        done = true;
        return cloudCover;
    }

    public boolean doneYet() {
        return done;
    }

    public double reset() {
        done = true;
        return 0;
    }
}

