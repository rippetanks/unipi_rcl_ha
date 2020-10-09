import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Computer {

    private User user;
    private boolean occupied;

    public boolean isFree() {
        return user == null && !occupied;
    }

    public void occupy(User user) {
        this.user = user;
        this.occupied = true;
    }

    public void reserve() {
        this.occupied = true;
    }

    public void free() {
        this.user = null;
    }

}
