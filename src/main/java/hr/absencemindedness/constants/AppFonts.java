package hr.absencemindedness.constants;

import java.awt.*;

public final class AppFonts {

    private static final String FAMILY = "Segoe UI";

    public static final Font TITLE = new Font(FAMILY, Font.BOLD, 18);
    public static final Font HEADERS = new Font(FAMILY, Font.BOLD, 12);
    public static final Font BODY = new Font(FAMILY, Font.PLAIN, 13);
    public static final Font CELL = new Font(FAMILY, Font.PLAIN, 10);

    private AppFonts(){
        throw new IllegalStateException("Constants class");
    }
}
