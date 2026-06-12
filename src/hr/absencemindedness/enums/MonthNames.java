package hr.absencemindedness.enums;

public enum MonthNames {

    JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER;

    public String getLabel() {
        return switch (this) {
            case JANUARY -> "siječanj";
            case FEBRUARY -> "veljača";
            case MARCH -> "ožujak";
            case APRIL -> "travanj";
            case MAY -> "svibanj";
            case JUNE -> "lipanj";
            case JULY -> "srpanj";
            case AUGUST -> "kolovoz";
            case SEPTEMBER -> "rujan";
            case OCTOBER -> "listopad";
            case NOVEMBER -> "studeni";
            case DECEMBER -> "prosinac";
        };
    }
}