
public class Student extends User {

    public Student(int k, Tutor tutor) {
        super(k, tutor);
    }

    @Override
    public void run() {
        long wait = (long) (Math.random() * MAX_WAIT); // waiting time between two requests
        long occupationTime = (long) (Math.random() * MAX_OCCUPATION); // usage time
        //long wait = 10;
        //long occupationTime = 20;
        int n = 0;
        try {
            while(n < k) {
                if(n > 0) {
                    Thread.sleep(wait);
                }
                int index = tutor.addStudent(this);
                System.out.printf("student %s start (%d)\n", Thread.currentThread().getName(), index);
                Thread.sleep(occupationTime);
                tutor.removeStudent(index);
                System.out.printf("student %s done (%d)\n", Thread.currentThread().getName(), index);
                n++;
            }
        } catch(InterruptedException e) {
            System.out.printf("thread %s interrupted\n", Thread.currentThread().getName());
        }
    }

}
