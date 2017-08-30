package thirdrdhand.smacktutorial.activities.transactions;

/**
 * Created by pacit on 2017/08/30.
 */

public class Transaction {
    protected String id;
    protected String title;
    protected String type;
    protected String statatus;
    protected String transTo;
    protected String transFrom;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatatus() {
        return statatus;
    }

    public void setStatatus(String statatus) {
        this.statatus = statatus;
    }

    public String getTransTo() {
        return transTo;
    }

    public void setTransTo(String transTo) {
        this.transTo = transTo;
    }

    public String getTransFrom() {
        return transFrom;
    }

    public void setTransFrom(String transFrom) {
        this.transFrom = transFrom;
    }
}
