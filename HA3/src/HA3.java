
public class HA3 {

    public static void main(String[] args) {
        if(args.length != 3) {
            System.out.println("Usage: n_students n_thesis_students n_professors");
            return;
        }

        int nStudents;
        try {
            nStudents = Integer.parseInt(args[0]);
        } catch(NumberFormatException e) {
            System.err.println("n_students must be an integer");
            throw e;
        }
        int nThesisStudents;
        try {
            nThesisStudents = Integer.parseInt(args[1]);
        } catch(NumberFormatException e) {
            System.err.println("n_thesis_students must be an integer");
            throw e;
        }
        int nProfessors;
        try {
            nProfessors = Integer.parseInt(args[2]);
        } catch(NumberFormatException e) {
            System.err.println("n_professors must be an integer");
            throw e;
        }


    }

}
