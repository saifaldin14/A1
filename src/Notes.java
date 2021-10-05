import java.io.*;
import java.util.*;

public class Notes {
    public int width;
    private int height;
    ArrayList<String> colors = new ArrayList<String>();
    HashMap<Integer, Note> notes = new HashMap<Integer, Note>();

    public Notes() {
        this.width = 0;
        this.height = 0;
    }

    public Notes(int w, int h, ArrayList<String> clrs) {
        this.width = w;
        this.height = h;
        this.colors = clrs;
    }

    public Note getNote(int id) {
        return notes.get(id);
    }

    public HashMap<Integer, Note> getRegular (String color, ArrayList<Integer> coordinates, String refersTo) {
        HashMap<Integer, Note> returnNotes = new HashMap<Integer, Note>();
        boolean isColor = !color.isEmpty(); // Will be true is color is not empty and false if it is empty
        boolean isCoord = true ? coordinates.size() > 0 : false; // Will be true is there are coordinates and false if there aren;t
        boolean isRefer = !refersTo.isEmpty(); // Will be true if there is a string and false if there isn't

        for (Note note : notes.values()) {
            //Check if color exists and make sure it's a valid color and get notes with that color
            boolean shouldColor = true ? !isColor : false;
            boolean shouldCoord = true ? !isCoord : false;
            boolean shouldRefer = true ? !isRefer : false;

            if (isColor && color.equals(note.getColor()))
                shouldColor = true;

            if (isCoord && coordinates.equals(note.getCoordinates()))
                shouldCoord = true;

            if (isRefer && note.getMessage().contains(refersTo))
                shouldRefer = true;

            if (shouldColor && shouldCoord && shouldRefer)
                returnNotes.put(note.getId(), note);
        }

        return returnNotes;
    }

    public HashMap<Integer, Note> getPin() {
        HashMap<Integer, Note> returnNotes = new HashMap<Integer, Note>();

        for (Note note : notes.values()) {
            if (note.getPinStatus().equals("Pinned"))
                returnNotes.put(note.getId(), note);
        }
        return returnNotes;
    }

    public synchronized void post(int x, int y, int width, int height, String color, String msg) {
        int id = notes.size() + 1;
        if (checkColors(color) && checkCoordinates(x, y) && checkDimensions(width, height)) {
            Note note = new Note(id, x, y, width, height, color, msg);
            notes.put(id, note);
        }
    }

    public synchronized void clear() {
        notes.clear();
    }

    public synchronized void shake() {
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getPinStatus().equals("Unpinned"))
                notes.remove(i);
        }
    }

    private boolean checkColors(String color) {
        return colors.contains(color);
    }

    private boolean checkCoordinates (int x, int y) {
        if (x <= width && y <= height && x >= 0 && y >= 0)
            return true;
        return false;
    }

    private boolean checkDimensions (int pWidth, int pHeight) {
        if (pWidth > width || pWidth < 0)
            return false;
        if (pHeight > height || pHeight < 0)
            return false;
        return true;
    }
}
