import java.util.Date;

public class Movement {

    private Date date;
    private Reason reason;

    protected Movement() {

    }

    public Movement(Date date, Reason reason) {
        this.date = date;
        this.reason = reason;
    }

    public Date getDate() {
        return date;
    }

    public Reason getReason() {
        return reason;
    }

}
