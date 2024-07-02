package com.auth.wow.libre.domain.model.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Expansion {
    WOW_CLASSIC(0, "World of Warcraft Clásico", ""),
    THE_BURNING_CRUSADE(1, "The Burning Crusade", ""),
    WRATH_OF_THE_LICH_KING(2, "Wrath of the Lich King", "https://i.ibb.co/z5YV2Sc/d52hwil-197dee2c-7d8a-40c8-a589-8c94dea0a218.png"),
    CATACLYSM(3, "Cataclysm", ""),
    MISTS_OF_PANDARIA(4, "Mists of Pandaria", ""),
    WARLORDS_OF_DRAENOR(5, "Warlords of Draenor", ""),
    LEGION(6, "Legion", ""),
    BATTLE_FOR_AZEROTH(7, "Battle for Azeroth", ""),
    SHADOWLANDS(8, "Shadowlands","" );

    private final int value;
    private final String displayName;
    private final String logo;

    Expansion(int value, String displayName, String logo) {
        this.value = value;
        this.displayName = displayName;
        this.logo = logo;
    }

    public static Expansion getById(int id) {
        return Arrays.stream(values())
                .filter(expansion -> expansion.value == id)
                .findFirst()
                .orElse(null);
    }
}
