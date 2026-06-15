package hr.absencemindedness.enums;

import hr.absencemindedness.constants.AppColors;

import java.awt.*;

public enum DayStatus {
    NONE(AppColors.WHITE),
    USED(AppColors.STATUS_USED),
    APPROVED(AppColors.STATUS_APPROVED),
    PLANED(AppColors.STATUS_PLANED),
    NON_WORKING(AppColors.STATUS_NON_WORKING),
    PATERNITY(AppColors.STATUS_PATERNITY),
    FREE_DAY_BLOOD(AppColors.STATUS_FREE_DAY_BLOOD),
    HOME_OFFICE(AppColors.STATUS_HOME_OFFICE);

    private final Color color;

    DayStatus(Color color){
        this.color = color;
    }

    public Color getColor(){
       return color;
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
