package com.dblqt.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Move {
    private final Train tain;
    private final Path destination;
    private final int startTime;


}
