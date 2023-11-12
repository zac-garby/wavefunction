package uk.co.zacgarby.wfc;

public class Coord {
    public int x, y;
    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coord translateIndex(int i, int w, int h) {
        switch (i) {
            case 0:
                return new Coord(x, (y + 1 + h) % h);
            case 1:
                return new Coord((x + 1 + w) % w, y);
            case 2:
                return new Coord(x, (y - 1 + h) % h);
            case 3:
                return new Coord((x - 1 + w) % w, y);
            default:
                throw new RuntimeException("invalid translation index");
        }
    }

    @Override
    public String toString() {
        return "<" + x + ", " + y + ">";
    }
}
