package hr.absencemindedness.models;

import java.time.LocalDate;

public record Holiday(LocalDate date, String localName, String name) {
}
