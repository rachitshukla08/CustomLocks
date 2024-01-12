public class Store {

//    As name is changed outside any synchronized block so to take care of memory vis issues
    private volatile String name;
    private final ReadWriteLock lock;

    public Store(ReadWriteLock lock){
        this.lock = lock;
        this.name = "bla";
    }

    public String read() throws InterruptedException {
        lock.lockRead();
        try {
            String val = name;
            Thread.sleep(1000);
            log(val);
            return val;
        } catch (Exception e){
            throw new RuntimeException(e);
        } finally {
            lock.unlockRead();
        }
    }

    public void write() throws InterruptedException {
        lock.lockWrite();
        try {
            name += "bla";
            foo();
        } catch (Exception e){
            throw new RuntimeException(e);
        } finally {
            lock.unlockWrite();
        }
    }

    public void log(String val) throws InterruptedException {
        lock.lockWrite();
        try {
            System.out.println("Name: "+val);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlockWrite();
        }
    }

    public void foo() throws InterruptedException {
        lock.lockRead();
        lock.unlockRead();
    }
}
