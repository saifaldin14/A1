import java.io.*;
import java.util.*;

public class Note {
    private int x;
    private int y;
    private int noteWidth;
    private int noteHeight;
    private String message;
    private String color;
    private int pinNumber;
    private boolean pinStatus;

    public Note(int pX, int pY, int width, int height, String clr, String msg) {
        x = pX;
        y = pY;
        noteWidth = width;
        noteHeight = height;
        color = clr;
        message = msg;
        pinNumber = 0;
        pinStatus = false;
    }

    public synchronized void pinValue() {
        pinNumber++;
        setPinStatus();
    }

    public synchronized void unpinValue() {
        if (pinNumber > 0) {
            pinNumber--;
        } else {
            pinNumber = 0;
        }
        setPinStatus();
    }

    public boolean getPinStatus() {
        return (pinStatus);
    }

    public String getMessage() {
        return message;
    }

    public String getColor() {
        return color;
    }

    public int[] getNoteDimensions() {
        int[] dimensions = new int[]{noteWidth, noteHeight};
        return dimensions;
    }

    public int[] getCoordinates() {
        int[] coordinates = new int[]{x, y};
        return coordinates;
    }

    private synchronized void setPinStatus() {
        if (pinNumber > 0) {
            pinStatus = true;
        } else {
            pinStatus = false;
        }
    }
}