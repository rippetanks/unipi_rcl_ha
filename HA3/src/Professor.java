
public class Professor extends User {

    public Professor(int k, Tutor tutor) {
        super(k, tutor);
    }

    @Override
    public void run() {
        long wait = (long) (Math.random() * MAX_WAIT); // waiting time between two requests
        long occupationTime = (long) (Math.random() * MAX_OCCUPATION); // usage time
        int n = 0;
        try {
            while(n < k) {
                Thread.sleep(wait);
                tutor.addProfessor(this);
                System.out.printf("professor %s start \n", Thread.currentThread().getName());
                Thread.sleep(occupationTime);
                tutor.removeProfessor();
                System.out.printf("professor %s done \n", Thread.currentThread().getName());
                n++;
            }
        } catch(InterruptedException e) {
            System.out.printf("thread %s interrupted\n", Thread.currentThread().getName());
        }
    }

}
