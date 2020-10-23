
public class Student extends User {

    public Student(int k, Tutor tutor) {
        super(k, tutor);
    }

    @Override
    protected void acquire() {
        System.out.printf("student %s request\n", Thread.currentThread().getName());
        index = tutor.addStudent(this);
        System.out.printf("student %s start (%d)\n", Thread.currentThread().getName(), index);
    }

    @Override
    protected void release() {
        System.out.printf("student %s done (%d)\n", Thread.currentThread().getName(), index);
        tutor.removeStudent(index);
    }

}
