
class Computer {

    private final int index;

    private User user;
    private int tsCount;

    Computer(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void addTs() {
        tsCount++;
    }

    public int getTsCount() {
        return tsCount;
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
