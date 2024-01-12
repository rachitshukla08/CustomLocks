public class SimpleLock {
    private boolean isLocked;
    private Thread lockedBy;
    private int counter;

    public SimpleLock(){
        this.isLocked = false;
        this.lockedBy = null;
        this.counter = 0;
    }

    public synchronized void lock() throws InterruptedException {
        while(isLocked && !Thread.currentThread().equals(lockedBy)) {
            System.out.println(Thread.currentThread().getId() + " needs to wait.");
            wait();
        }
        isLocked = true;
        counter++;
        lockedBy = Thread.currentThread();
        System.out.println(Thread.currentThread().getId() + " acquired the lock");
    }

    public synchronized void unlock() {
        if(!isLocked)
            throw new RuntimeException("Nothing to unlock");
        if(!lockedBy.equals(Thread.currentThread())) {
            throw new RuntimeException(Thread.currentThread().getId()
                    + " tried to access but locked by " + lockedBy.getId()
            );
        }
        counter--;
        if(counter == 0){
            isLocked = false;
            lockedBy = null;
            notifyAll();
        }

        System.out.println(Thread.currentThread().getId()+" released the lock");
    }

}
