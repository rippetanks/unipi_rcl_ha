
public class Professor extends User {

    public Professor(int k, Tutor tutor) {
        super(k, tutor);
    }

    @Override
    protected void acquire() {
        System.out.printf("professor %s request\n", Thread.currentThread().getName());
        tutor.addProfessor(this);
        System.out.printf("professor %s start\n", Thread.currentThread().getName());
    }

    @Override
    protected void release() {
        System.out.printf("professor %s done\n", Thread.currentThread().getName());
        tutor.removeProfessor();
    }

}
