public class Main {
    public static void main(String[] args) {
        SimpleLock lock = new SimpleLock();
        Thread t1 = new Thread(new Worker(lock));
        Thread t2 = new Thread(new Worker(lock));
        Thread t3 = new Thread(new Worker(lock));
        Thread malicious = new Thread(new MaliciousWorker(lock));
        t1.start();
        t2.start();
        t3.start();
        malicious.start();
    }
}