package com.lazyledger.commons.enums;

public enum Category {
    FOOD("food"),
    RENT("rent"),
    TRANSPORT("transport"),
    UTILITIES("utilities"),
    ENTERTAINMENT("entertainment"),
    HEALTH("health"),
    EDUCATION("education"),
    OTHER("other");

    private final String value;

    Category(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static Category fromString(String category) {
        for (Category cat : Category.values()) {
            if (cat.value.equalsIgnoreCase(category)) {
                return cat;
            }
        }
        throw new IllegalArgumentException("No enum constant for category: " + category);
    }
}
