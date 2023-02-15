package com.example.barberbooking.utils;

public class BarberCalendarHour {
    private String startHour;
    private String endHour;
    private boolean isAvailable;


    public BarberCalendarHour(String startHour, String endHour, boolean isAvailable) {
        this.startHour = startHour;
        this.endHour = endHour;
        this.isAvailable = isAvailable;
    }

    public BarberCalendarHour() {}

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    public String getEndHour() {
        return endHour;
    }

    public String getStartHour() {
        return startHour;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
