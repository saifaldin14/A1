import java.io.*;
import java.util.*;

public class Note {
    private int id;
    private int x;
    private int y;
    private int noteWidth;
    private int noteHeight;
    private String message;
    private String color;
    private int pinNumber;
    private String pinStatus;

    public Note(int id, int pX, int pY, int width, int height, String clr, String msg) {
        this.id = id;
        this.x = pX;
        this.y = pY;
        this.noteWidth = width;
        this.noteHeight = height;
        this.color = clr;
        this.message = msg;
        this.pinNumber = 0;
        this.pinStatus = "Unpinned";
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

    public int getId() {
        return id;
    }

    public String getPinStatus() {
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
            pinStatus = "Pinned";
        } else {
            pinStatus = "Unpinned";
        }
    }
}
