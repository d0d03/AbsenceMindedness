package hr.absencemindedness.enums;

import java.awt.*;

public enum DayStatus {
    NONE, USED, APPROVED, PLANED, NON_WORKING, PATERNITY, FREE_DAY_BLOOD, HOME_OFFICE;

    public Color getColor(){
        return switch (this){
            case USED -> new Color(200,50,50);
            case APPROVED -> new Color(80,160,80);
            case PLANED -> new Color(100,160,220);
            case NON_WORKING -> new Color(200,200,200);
            case PATERNITY -> new Color(255, 0,255);
            case FREE_DAY_BLOOD -> new Color(255,255,0);
            case HOME_OFFICE -> new Color(255,175,175);
            case NONE -> Color.WHITE;
        };
    }

    public String getLabel(){
        return switch (this){
            case USED -> "Iskorišteni dani";
            case APPROVED -> "Odobreni dani";
            case PLANED -> "Planirani dani";
            case NON_WORKING -> "Neradni dani";
            case PATERNITY -> "Očinski dopust";
            case FREE_DAY_BLOOD -> "Slobodan dan - krv";
            case HOME_OFFICE -> "Rad od kuće";
            case NONE -> "Nema";
        };
    }

}
