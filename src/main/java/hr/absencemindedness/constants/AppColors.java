package hr.absencemindedness.constants;

import java.awt.*;

public final class AppColors {

    public static final Color HEADER_BG = new Color(50,40,80);
    public static final Color HEADER_FG = new Color(200,210,230);
    public static final Color GRID_BG = new Color(245,245,248);
    public static final Color GRID_FG = new Color(50,60,80);
    public static final Color FOOTER_BG = new Color(240,240,244);
    public static final Color FOOTER_BRD = new Color(210,210,218);
    public static final Color WHITE = new Color(255,255,255);
    public static final Color ICON_HVR = new Color(220,225,235);
    public static final Color ICON_BRD = new Color(0,0,0,40);

    public static final Color STATUS_USED = new Color(200,50,50);
    public static final Color STATUS_APPROVED = new Color(80,160,80);
    public static final Color STATUS_PLANED = new Color(100,160,220);
    public static final Color STATUS_NON_WORKING = new Color(200,200,200);
    public static final Color STATUS_PATERNITY = new Color(255, 0,255);
    public static final Color STATUS_FREE_DAY_BLOOD = new Color(255,255,0);
    public static final Color STATUS_HOME_OFFICE = new Color(255,175,175);

    private AppColors(){
        throw new IllegalStateException("Constants class");
    }
}
