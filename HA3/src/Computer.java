import java.util.concurrent.locks.Condition;

/**
 * Representation of a workstation.
 */
class Computer {

    // computer index in the laboratory
    private final int index;
    // condition variable for thesis students
    private final Condition tsCond;

    // current user
    private User user;
    // number of thesis students in queue
    private int tsCount;

    Computer(int index, Condition tsCond) {
        this.index = index;
        this.tsCond = tsCond;
    }

    public int getIndex() {
        return index;
    }

    public Condition getTsCond() {
        return tsCond;
    }

    public int getTsCount() {
        return tsCount;
    }

    public void addTs() {
        tsCount++;
    }

    public void removeTs() {
        tsCount--;
    }

    public boolean isFree() {
        return user == null;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void free() {
        this.user = null;
    }

}
