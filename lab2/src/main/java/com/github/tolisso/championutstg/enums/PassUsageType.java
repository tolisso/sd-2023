package com.github.tolisso.championutstg.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PassUsageType {
    ENTER(true), EXIT(false);

    public final boolean boolValue;
}