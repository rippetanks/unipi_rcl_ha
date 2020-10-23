import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HA4 {

    public static final int MAX_K = 5;

    public static void main(String[] args) {
        if(args.length != 3) {
            System.out.println("Usage: n_students n_thesis_students n_professors");
            return;
        }

        int nStudents;
        try {
            nStudents = Integer.parseInt(args[0]);
            System.out.println("n° students -> " + nStudents);
        } catch(NumberFormatException e) {
            System.err.println("n_students must be an integer");
            throw e;
        }
        int nThesisStudents;
        try {
            nThesisStudents = Integer.parseInt(args[1]);
            System.out.println("n° thesis students -> " + nThesisStudents);
        } catch(NumberFormatException e) {
            System.err.println("n_thesis_students must be an integer");
            throw e;
        }
        int nProfessors;
        try {
            nProfessors = Integer.parseInt(args[2]);
            System.out.println("n° professors -> " + nProfessors);
        } catch(NumberFormatException e) {
            System.err.println("n_professors must be an integer");
            throw e;
        }

        int k = new Random().nextInt(MAX_K) + 1;
        //int k = 1;
        System.out.println("K is " + k);

        Tutor tutor = new Tutor();

        List<Thread> threads = new ArrayList<>();
        long start = System.currentTimeMillis();

        for(int i = 0; i < nStudents; i++) {
            Thread t = new Thread(new Student(k, tutor));
            t.start();
            threads.add(t);
        }
        for(int i = 0; i < nThesisStudents; i++) {
            Thread t = new Thread(new ThesisStudent(k, tutor));
            t.start();
            threads.add(t);
        }
        for(int i = 0; i < nProfessors; i++) {
            Thread t = new Thread(new Professor(k, tutor));
            t.start();
            threads.add(t);
        }

        for(Thread t : threads) {
            try {
                t.join();
            } catch(InterruptedException e) { }
        }
        long end = System.currentTimeMillis();
        System.out.printf("\n\nELAPSED %d ms\n\n", (end - start));
    }

}
