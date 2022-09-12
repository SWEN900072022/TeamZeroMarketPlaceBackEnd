package Enums;

public enum ListingTypes {
    FIXED_PRICE("FIXED_PRICE"),
    AUCTION("AUCTION");

    private final String text;
    ListingTypes(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }

    public static ListingTypes fromString(String text) {
        for (ListingTypes lt : ListingTypes.values()) {
            if (lt.text.equals(text)) {
                return lt;
            }
        }
        return null; // Raise an exception at some point later
    }
}
