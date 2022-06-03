import java.util.ArrayList;

public class Utils {
    public static ArrayList<Pair> deepCopyPairs(ArrayList<Pair> pairs) {
        ArrayList<Pair> result = new ArrayList<>();
        for (Pair pair: pairs) {
            result.add((Pair)pair.clone());
        }
        return result;
    }

    public static double getEucDistance(int xs, int ys, int xd, int yd) {
        return Math.sqrt(Math.pow(xd - xs, 2) + Math.pow(yd-ys, 2));
    }
}
