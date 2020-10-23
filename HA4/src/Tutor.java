import java.util.ArrayList;
import java.util.List;

public class Tutor {

    public static final int N_COMPUTER = 20;

    private final List<Computer> computers = new ArrayList<>(N_COMPUTER);

    private int profCount;

    public Tutor() {
        for(int i = 0; i < N_COMPUTER; i++) {
            computers.add(new Computer(i));
        }
    }

    public synchronized int addStudent(Student student) {
        Computer computer;
        while(profCount > 0 || (computer = findComputerForStudent()) == null) {
            try {
                System.out.println("[STUDENT] wait");
                wait();
            } catch(InterruptedException e) { }
        }
        computer.setUser(student);
        return computer.getIndex();
    }

    public void removeStudent(int index) {
        Computer computer = computers.get(index);
        synchronized (this) {
            computer.free();
            notifyAll();
        }
    }

    public synchronized void addThesisStudent(ThesisStudent thesisStudent) {
        Computer computer = computers.get(thesisStudent.getComputerIndex());
        computer.addTs();
        while(profCount > 0 || !computer.isFree()) {
            try {
                System.out.println("[THESIS STUDENT] wait");
                wait();
            } catch(InterruptedException e) { }
        }
        computer.setUser(thesisStudent);
        computer.removeTs();
    }

    public void removeThesisStudent(ThesisStudent thesisStudent) {
        Computer computer = computers.get(thesisStudent.getComputerIndex());
        synchronized (this) {
            computer.free();
            notifyAll();
        }
    }

    public synchronized void addProfessor(Professor professor) {
        profCount++;
        while(!isAllComputerFree()) {
            try {
                System.out.println("[PROFESSOR] wait");
                wait();
            } catch(InterruptedException e) { }
        }
        for(Computer c : computers) {
            c.setUser(professor);
        }
        profCount--;
    }

    public synchronized void removeProfessor() {
        for(Computer c : computers) {
            c.free();
        }
        notifyAll();
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

}
