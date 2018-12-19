import java.rmi.Naming;

class Client {

    public Client() {

    }

    public static void main(String[] args) {
        RiskChecker testFruit;
        CloudCover cloudCover = null;
        SensorRemote sensorRemote = null;
        try {
            cloudCover = (CloudCover) Naming.lookup("rmi://localhost:9090/Cloud");
            sensorRemote = (SensorRemote) Naming.lookup("rmi://localhost:8080/Sensor");

        } catch (Exception e) {
            System.err.println(e);
            System.out.println("Oh no1!!!");
        }

        testFruit = new RiskChecker(cloudCover, sensorRemote);
        testFruit.start();


    }
}


class RiskChecker extends Thread {

    private final SensorRemote sensorRemote;

    private final CloudCover cloudCover;

    private double temp;
    private double hum;
    private double cloud;
    private boolean tempReady = false;
    private boolean humReady = false;
    private boolean cloudReady = false;

    public RiskChecker(CloudCover f, SensorRemote s) {
        cloudCover = f;

        sensorRemote = s;
    }


    public void run() {
        double risk;
        System.out.println("~~~~Risk Checker Started~~~~");
        while (true) {
            try {
                Thread.sleep(10000);

                if (cloudCover.doneYet()) {
                    cloud = cloudCover.getCloudCover();
                    cloudReady = true;
                }


                if (sensorRemote.tempDone()) {
                    temp = sensorRemote.getTemp();
                    tempReady = true;
                }


                if (sensorRemote.humidityDone()) {
                    hum = sensorRemote.getHumidity();
                    humReady = true;
                }

                if (humReady && tempReady && cloudReady) {
                    risk = temp * (1 - (hum / 100)) * (1 - (cloud / 100));
                    sensorRemote.humReset();
                    sensorRemote.tempReset();
                    cloudCover.reset();
                    humReady = false;
                    tempReady = false;
                    cloudReady = false;

                    if (risk > 20) {
                        System.out.println("Checking...Risk Detected (" + risk + ")!!!");
                    } else {
                        System.out.println("Checking...No Risk (" + risk + ")!!!");
                    }
                }


            } catch (Exception e) {
                System.out.println("Checker: no risk!!!");
                e.printStackTrace();
            }
        }
    }
}