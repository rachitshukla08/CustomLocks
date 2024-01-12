public class Worker implements Runnable{
    private final SimpleLock lock;

    public Worker(SimpleLock lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            lock.lock();
            lock.lock();
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            lock.unlock();
            lock.unlock();
        }
    }
}
