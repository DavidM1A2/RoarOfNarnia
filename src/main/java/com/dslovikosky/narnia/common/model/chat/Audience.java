package com.dslovikosky.narnia.common.model.chat;

import java.util.ArrayList;

public class Audience {
    public static final AudienceSelector DEFAULT_NEARBY =
            (npc, level) -> level.getPlayers(p -> p.distanceTo(npc) < 32 && !p.isSpectator());

    public static final AudienceSelector ALL_PLAYERS =
            (npc, level) -> new ArrayList<>(level.getPlayers(p -> !p.isSpectator()));
}