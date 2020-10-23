
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
    protected void acquire() {
        System.out.printf("thesis student %s request\n", Thread.currentThread().getName());
        tutor.addThesisStudent(this);
        System.out.printf("thesis student %s start (%d)\n", Thread.currentThread().getName(), index);
    }

    @Override
    protected void release() {
        System.out.printf("thesis student %s done (%d)\n", Thread.currentThread().getName(), index);
        tutor.removeThesisStudent(this);
    }

}
