package Enums;

public enum UnitActions {
    INSERT("INSERT"),
    MODIFY("MODIFY"),
    DELETE("DELETE");

    private final String text;
    UnitActions(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
