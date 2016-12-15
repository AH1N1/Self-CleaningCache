

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RENT on 2016-11-22.
 */
public class SelfCleaningCache<T> {
    //inner generic class that stores given data and the time it was last red
    private class Pair<T> {
        private Long millis;
        private T data;

        public Pair(T data) {
            this.data = data;
            millis = System.currentTimeMillis();
        }

        public Pair(T data, Long millis) {
            this.millis = millis;
            this.data = data;
        }

        public void setMillis(Long millis) {
            this.millis = millis;
        }

        public void setData(T data) {
            this.data = data;
        }

        public Long getMillis() {

            return millis;
        }

        public T getData() {
            return data;
        }
    }

    //arraylist that stores all generic data
    private List<Pair> MyList = new ArrayList<Pair>();
    private long millis;

    // creates cache that will clear all data unread longer then time in millis given as parameter
    public SelfCleaningCache(long millis) {

        this.millis = millis;
        start();
    }

    //adds new Pair class to MyList.
    public void add(T data) {
        MyList.add(new Pair(data, System.currentTimeMillis()));

    }

    // returns selected data stored in MyList and sets its millis to current time. Data is selected by its index;
    public T get(int index) {
        T tmp = (T) MyList.get(index).getData();
        MyList.get(index).setMillis(System.currentTimeMillis());
        return tmp;
    }

    // creates and starts new sychronized daemon thread that monitors  all data stored in MyList and removes Pairs which data is unread longer than time in millis given in constructor
    public synchronized void start() {

        Thread cleaner = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        if (MyList.size() != 0) {
                            for (int i = 0; i < MyList.size(); i++) {
                                if (MyList.size() > 0 && System.currentTimeMillis() - MyList.get(i).getMillis() > millis)
                                    MyList.remove(i);
                            }
                            Thread.sleep(millis);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        cleaner.setDaemon(true);
        cleaner.start();

    }

    public int size() {
        return MyList.size();
    }

    public static void main(String[] args) throws InterruptedException {
        SelfCleaningCache<String> selfCleaningCache = new SelfCleaningCache<String>(2000);
        selfCleaningCache.add("someString");
        System.out.println("created new cache with 'millis' set to 2000");
        System.out.println("cache storages "+ selfCleaningCache.size()+" object");
        System.out.println("main thread going to sleep for 1 sec");
        Thread.sleep(1000);
        System.out.println("main thread woke up");
        System.out.println("after 1 sec cache storages "+ selfCleaningCache.size()+ " object");
        System.out.println("main thread going to sleep for 3 sec");
        Thread.sleep(3000);
        System.out.println("main thread woke up");
        System.out.println("after 3 sec cache storages "+ selfCleaningCache.size()+ " objects");
    }
}
