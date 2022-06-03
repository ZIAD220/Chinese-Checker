import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Marble marble = (Marble) o;
        return owner == marble.owner && color == marble.color && isValid == marble.isValid;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new Marble(owner, color, isValid);
    }
}
