package com.dslovikosky.narnia.common.network.packet;

import com.dslovikosky.narnia.common.model.NarniaGlobalData;
import com.dslovikosky.narnia.common.model.chapter.PlayableCharacter;
import com.dslovikosky.narnia.common.model.chapter.Scene;
import com.mojang.logging.LogUtils;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class JoinScenePacketHandler implements IPayloadHandler<JoinScenePacket> {
    private static final Logger LOG = LogUtils.getLogger();

    @Override
    public void handle(final JoinScenePacket payload, final IPayloadContext context) {
        final NarniaGlobalData data = NarniaGlobalData.getInstance(context.player().level());
        final Scene activeScene = data.getActiveScene();

        if (activeScene == null) {
            LOG.warn("No active scene to join for {}.", payload);
            return;
        }

        final PlayableCharacter playableCharacter = activeScene.getChapter().getPlayableActor(payload.characterId());
        if (playableCharacter == null) {
            LOG.warn("No playable-actor to join for {}.", payload);
            return;
        }

        activeScene.getChapter().tryJoin(activeScene, context.player(), playableCharacter);
    }
}
