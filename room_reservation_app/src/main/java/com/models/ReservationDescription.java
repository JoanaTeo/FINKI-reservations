package com.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReservationDescription {
    Час,
    Лабораториски,
    Консултации,
    Испит
}
