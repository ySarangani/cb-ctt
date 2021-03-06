package ch.rethab.cbctt.ea.printer;

import ch.rethab.cbctt.domain.Specification;
import ch.rethab.cbctt.ea.phenotype.CurriculumTimetableWithRooms;
import ch.rethab.cbctt.ea.phenotype.MeetingWithRoom;
import ch.rethab.cbctt.ea.phenotype.TimetableWithRooms;

import java.util.stream.IntStream;

/**
 * Prints a timetable as a table in text format.
 * Each table contains the rooms, courses and teachers
 * of a curriculum.
 *
 * @author Reto Habluetzel, 2015
 */
public class PrettyTextPrinter implements Printer {

    private static final int cellLen = 10;

    private final Specification spec;

    public PrettyTextPrinter(Specification spec) {
        this.spec = spec;
    }

    @Override
    public String print(TimetableWithRooms t) {

        final StringBuilder sb = new StringBuilder();

        t.getCurriculumTimetables().forEach((cid, ctt) -> {
            sb.append("Curriculum: ").append(cid).append("\n");
            sb.append(printCurriculumTimetable(t, ctt));
            sb.append("\n");
        });

        return sb.toString();
    }

    private String printCurriculumTimetable(TimetableWithRooms t, CurriculumTimetableWithRooms ctt) {
        StringBuilder sb = new StringBuilder();

        int lineLen = cellLen * (spec.getNumberOfDaysPerWeek() + 1);

        // headline
        sb.append(padLeft("", cellLen));
        IntStream.range(0, spec.getNumberOfDaysPerWeek()).forEach(day ->
            sb.append(padLeft(String.format("Day %2d", day), cellLen))
        );
        sb.append("\n");

        for (int period = 0; period < spec.getPeriodsPerDay(); period++) {

            sb.append(repeat("-", lineLen)).append("\n");

            // course
            sb.append(padLeft(String.format("Slot %2d | ", period), cellLen));
            for (int day = 0; day < spec.getNumberOfDaysPerWeek(); day++) {
                MeetingWithRoom m = ctt.get(day, period);
                sb.append(padLeft(m != null ? m.getCourse().getId() : "", cellLen));
            }
            sb.append("\n");

            // room
            sb.append(padLeft("| ", cellLen));
            for (int day = 0; day < spec.getNumberOfDaysPerWeek(); day++) {
                MeetingWithRoom m = ctt.get(day, period);
                sb.append(padLeft(m != null ? m.getRoom().getId() : "", cellLen));
            }
            sb.append("\n");

            // teacher
            sb.append(padLeft("| ", cellLen));
            for (int day = 0; day < spec.getNumberOfDaysPerWeek(); day++) {
                MeetingWithRoom m = ctt.get(day, period);
                sb.append(padLeft(m != null ? m.getCourse().getTeacher() : "", cellLen));
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    // source: http://stackoverflow.com/questions/388461/how-can-i-pad-a-string-in-java/391978#391978
    private String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }

    // source: http://stackoverflow.com/a/4903603
    private String repeat(String s, int n) {
        return new String(new char[n]).replace("\0", s);
    }
}
