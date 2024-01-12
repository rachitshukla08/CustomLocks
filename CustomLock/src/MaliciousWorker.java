public class MaliciousWorker implements Runnable {
    private final SimpleLock lock;

    public MaliciousWorker(SimpleLock lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            lock.lock();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            lock.unlock();
        }
    }
}
