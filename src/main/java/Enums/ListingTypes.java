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
}
