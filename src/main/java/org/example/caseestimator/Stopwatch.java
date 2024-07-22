package org.example.caseestimator;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Stopwatch {
    private Instant start;
    private Instant end;


    public void startTimer() {
        start = Instant.now();
    }

    public void stopTimer() {
        end = Instant.now();
    }

    public String getTimeElapsed() {
        if (start == null) {
            return "00:00:00";
        }
        if (end == null) {
            long timeElapsed = Instant.now().toEpochMilli() - start.toEpochMilli();
            end = Instant.ofEpochMilli(timeElapsed);
        }
        long seconds = ChronoUnit.SECONDS.between(Instant.ofEpochMilli(start.toEpochMilli()), Instant.ofEpochMilli(end.toEpochMilli()));
        long minutes = seconds / 60;
        long hours = minutes / 60;
        return String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60);
    }
}
