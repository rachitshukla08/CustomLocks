public class Main {
    public static void main(String[] args) {
        ReadWriteLock readWriteLock = new ReadWriteLock();
        Store store = new Store(readWriteLock);
        Thread r1 = new Thread(new Reader(store));
        Thread r2 = new Thread(new Reader(store));
        Thread w1 = new Thread(new Writer(store));
        Thread w2 = new Thread(new Writer(store));
        r1.start();
        r2.start();
        w1.start();
        w2.start();
    }
}