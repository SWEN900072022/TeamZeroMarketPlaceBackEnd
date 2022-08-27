package Enums;

public enum UserRoles {
    CUSTOMER("customer"),
    SELLER("seller"),
    ADMIN("admin");

    private final String text;
    UserRoles(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
