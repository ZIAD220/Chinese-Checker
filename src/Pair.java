public class Pair {
    int x;
    int y;
    public Pair(){
        x = y = 0;
    }
    public Pair(int new_x, int new_y){
        x = new_x;
        y = new_y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return x == pair.x && y == pair.y;
    }

    @Override
    protected Object clone() {
        return new Pair(x, y);
    }

    @Override
    public String toString() {
        return "Pair{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
