import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A Tutor manages access to the computers.
 */
public class Tutor {

    public static final int N_COMPUTER = 20;

    private final List<Computer> computers = new ArrayList<>(N_COMPUTER);

    private final Lock lock = new ReentrantLock();

    // condition variable for professors
    private final Condition profCond = lock.newCondition();
    // condition variable for students
    private final Condition studentCond = lock.newCondition();

    // number of professors in queue
    private int profCount;

    public Tutor() {
        for(int i = 0; i < N_COMPUTER; i++) {
            computers.add(new Computer(i, lock.newCondition()));
        }
    }

    public int addStudent(Student student) throws InterruptedException {
        lock.lock();
        try {
            while(profCount > 0) {
                System.out.println("[TUTOR] student waits to give priority to professors");
                studentCond.await();
            }
            Computer computer;
            while((computer = findComputerForStudent()) == null) {
                System.out.println("[TUTOR] student waits because there are no free computer");
                studentCond.await();
            }
            computer.setUser(student);
            return computer.getIndex();
        } finally {
            lock.unlock();
        }
    }

    public void removeStudent(int index) {
        lock.lock();
        Computer computer = computers.get(index);
        computer.free();
        wakeRightUser(computer);
        lock.unlock();
    }

    public void addProfessor(Professor professor) throws InterruptedException {
        lock.lock();
        try {
            profCount++;
            while (!isAllComputerFree()) {
                System.out.println("[TUTOR] professor waits for the whole laboratory to be free");
                profCond.await();
            }
            for (Computer c : computers) {
                c.setUser(professor);
            }
            profCount--;
        } finally {
            lock.unlock();
        }
    }

    public void removeProfessor() {
        lock.lock();
        try {
            for (Computer c : computers) {
                c.free();
                wakeRightUser(c);
            }
        } finally {
            lock.unlock();
        }
    }

    public void addThesisStudent(ThesisStudent thesisStudent) throws InterruptedException {
        lock.lock();
        try {
            Computer computer = computers.get(thesisStudent.getComputerIndex());
            computer.addTs();
            while(profCount > 0) {
                System.out.println("[TUTOR] thesis student waits to give priority to professors");
                computer.getTsCond().await();
            }
            while(!computer.isFree()) {
                System.out.println("[TUTOR] thesis student waits for a student or professor to finish");
                computer.getTsCond().await();
            }
            computer.setUser(thesisStudent);
            computer.removeTs();
        } finally {
            lock.unlock();
        }
    }

    public void removeThesisStudent(ThesisStudent thesisStudent) {
        lock.lock();
        try {
            Computer computer = computers.get(thesisStudent.getComputerIndex());
            computer.free();
            wakeRightUser(computer);
        } finally {
            lock.unlock();
        }
    }

    private boolean isAllComputerFree() {
        for(Computer c : computers) {
            if(!c.isFree()) {
                return false;
            }
        }
        return true;
    }

    private Computer findComputerForStudent() {
        for(Computer c : computers) {
            if(c.isFree() && c.getTsCount() == 0) {
                return c;
            }
        }
        return null;
    }

    private void wakeRightUser(Computer computer) {
        if(profCount > 0) {
            profCond.signal();
        } else if(computer.getTsCount() > 0) {
            computer.getTsCond().signal();
        } else {
            studentCond.signal();
        }
    }

}
