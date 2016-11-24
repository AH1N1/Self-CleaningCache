;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RENT on 2016-11-22.
 */
public class SelfCleaningCache<T> {
    //inner generic class that stores given data and the time it was last red
    private class Pair<T> {
        private Long milis;
        private T data;

        public Pair(T data) {
            this.data = data;
            milis = System.currentTimeMillis();
        }

        public Pair(T data, Long milis) {
            this.milis = milis;
            this.data = data;
        }

        public void setMilis(Long milis) {
            this.milis = milis;
        }

        public void setData(T data) {
            this.data = data;
        }

        public Long getMilis() {

            return milis;
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
        MyList.get(index).setMilis(System.currentTimeMillis());
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
                                if (MyList.size() > 0 && System.currentTimeMillis() - MyList.get(i).getMilis() > millis)
                                    MyList.remove(i);
                            }


                            Thread.sleep(3000);
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

    public static void main(String[] args) {
        System.out.println("a");
    }
}
