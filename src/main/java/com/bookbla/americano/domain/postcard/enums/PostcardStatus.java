package com.bookbla.americano.domain.postcard.enums;

public enum PostcardStatus {

    PENDING, ACCEPT, REFUSED, ALL_WRONG;

    public boolean isRefused() {
        return this == REFUSED;
    }
}
