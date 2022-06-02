public class Marble {
    int owner; // 0->No owner, 1->Player 1, 2->Player 2.
    int color;
    boolean isValid;
    public Marble()
    {
        owner = 0;
        color = 0;
        isValid = false;
    }
    public Marble(int own, int clr, boolean ok)
    {
        owner = own;
        color = clr;
        isValid = ok;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
