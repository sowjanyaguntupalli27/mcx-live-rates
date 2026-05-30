package com.vibullion.backend.enums;

import lombok.Getter;

@Getter
public enum Purity {
    PURITY_999(999),
    PURITY_916(916),
    PURITY_875(875),
    PURITY_833(833),
    PURITY_750(750),
    PURITY_667(667),
    PURITY_583(583),
    PURITY_417(417);

    private final int value;

    Purity(int value) {
        this.value = value;
    }

}
