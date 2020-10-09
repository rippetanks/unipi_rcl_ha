import java.util.ArrayList;
import java.util.List;

public class Tutor {

    private final List<Computer> computers = new ArrayList<>();

    public Tutor() {
        for(int i = 0; i < 20; i++) {
            computers.add(new Computer());
        }
    }

    public void addStudent(Student student) {

    }

    public void addProfessor(Professor professor) {

    }

    public void addThesisStudent(ThesisStudent thesisStudent) {

    }

}
