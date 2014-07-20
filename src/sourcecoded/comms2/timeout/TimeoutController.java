package sourcecoded.comms2.timeout;

public class TimeoutController {

    public static void execute(Thread thread, long timeout) throws TimeoutException {
        thread.start();
        try {
            thread.join(timeout);
        } catch (InterruptedException e) {}

        if (thread.isAlive()) {
            thread.interrupt();
            throw new TimeoutException();
        }

    }

}
