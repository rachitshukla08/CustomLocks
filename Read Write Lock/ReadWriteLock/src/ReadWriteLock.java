import java.util.HashMap;
import java.util.Map;

public class ReadWriteLock {

    private int writerEntryCount;
//    Preventing writer starvation
    private int writeRequest;

    private Thread writerThread;
    private final Map<Thread, Integer> entryCounts;
    public ReadWriteLock (){
        this.entryCounts = new HashMap<>();
        this.writerEntryCount = 0;
        this.writerThread = null;
        this.writeRequest = 0;
    }

    public synchronized void lockRead() throws InterruptedException {
        while(!allowReadAccess()){
            wait();
        }
        System.out.println(Thread.currentThread().getId() + " acquired readlock");
        int count = entryCounts.getOrDefault(Thread.currentThread(), 0);
        entryCounts.put(Thread.currentThread(), count+1);
    }

    public synchronized void unlockRead(){
        if(!entryCounts.containsKey(Thread.currentThread()))
            throw new RuntimeException("Illegal unlock read");
        int count = entryCounts.getOrDefault(Thread.currentThread(), 0);
        if(count>1)
            entryCounts.put(Thread.currentThread(), count-1);
        else {
            entryCounts.remove(Thread.currentThread());
            notifyAll();
        }
        System.out.println(Thread.currentThread().getId() +" released read lock");
    }

    public synchronized void lockWrite() throws InterruptedException {
        this.writeRequest++;
        while(!allowWriteAccess()){
            wait();
        }
        System.out.println(Thread.currentThread().getId() +" acquired write lock");
        this.writerThread = Thread.currentThread();
        this.writerEntryCount++;
        this.writeRequest--;
    }

    public synchronized void unlockWrite(){
        if(!Thread.currentThread().equals(this.writerThread))
            throw new RuntimeException(Thread.currentThread().getId() +" illegal writer unlock");
        this.writerEntryCount--;
        if(this.writerEntryCount==0) {
            notifyAll();
            this.writerThread = null;
        }
        System.out.println(Thread.currentThread().getId() + " released write lock");

    }

    private boolean allowReadAccess() {
//        Write-Read reentrancy (Same writer can acquire read lock concurrently)
        if(Thread.currentThread().equals(writerThread)) return true;
        if(writerEntryCount>0) return false;
//        Read reentrancy
        if(entryCounts.containsKey(Thread.currentThread())) return true;
        if(writeRequest>0) return false;
        return true;
    }

    public boolean allowWriteAccess() {
//      Read-Write reentrancy
        if(entryCounts.size() == 1 && entryCounts.get(Thread.currentThread())!=null)
            return true;
        if(entryCounts.size()>0) return false;
        if(writerThread == null) return true;
//        Write reentrancy
        return Thread.currentThread().equals(writerThread);
    }
}
