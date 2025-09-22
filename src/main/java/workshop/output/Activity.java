package workshop.output;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record Activity(String activity, DayOfWeek day, String location, LocalTime time) { }
