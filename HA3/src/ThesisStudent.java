
public class ThesisStudent extends Student {

    // index of workstation
    // the value is between 0 and 19 inclusive
    private final int index = (int) (Math.random() * 20);

    public ThesisStudent(int k, Tutor tutor) {
        super(k, tutor);
    }

    public int getComputerIndex() {
        return index;
    }

    @Override
    public void run() {
        long wait = (long) (Math.random() * MAX_WAIT); // waiting time between two requests
        long occupationTime = (long) (Math.random() * MAX_OCCUPATION); // usage time
        int n = 0;
        try {
            while(n < k) {
                Thread.sleep(wait);
                tutor.addThesisStudent(this);
                System.out.printf("thesis student %s start (%d)\n", Thread.currentThread().getName(), index);
                Thread.sleep(occupationTime);
                tutor.removeThesisStudent(this);
                System.out.printf("thesis student %s done (%d)\n", Thread.currentThread().getName(), index);
                n++;
            }
        } catch(InterruptedException e) {
            System.out.printf("thread %s interrupted\n", Thread.currentThread().getName());
        }
    }

}
