package SortingTable;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Scores implements Serializable {
    private String name;
    private LocalDateTime begin;
    private LocalDateTime end;
    private int steps;
    private boolean done;
    private String dText;

    public Scores() {
    }

    public Scores(String name, LocalDateTime begin, LocalDateTime end, int steps, boolean done) {
        this.name = name;
        this.begin = begin;
        this.end = end;
        this.steps = steps;
        this.done = done;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getBegin() {
        return begin;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public int getSteps() {
        return steps;
    }

    public boolean isDone() {
        return done;
    }

    public String getdText() {
        return dText;
    }

    public void setdText(String dText) {
        this.dText = dText;
    }

    @Override
    public String toString() {
        return "Scores{" +
                "name='" + name + '\'' +
                ", begin=" + begin +
                ", end=" + end +
                ", steps=" + steps +
                ", done=" + done +
                '}';
    }
}
