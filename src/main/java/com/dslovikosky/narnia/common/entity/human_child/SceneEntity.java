package com.dslovikosky.narnia.common.entity.human_child;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

public interface SceneEntity {
    Optional<UUID> getSceneId();

    void setSceneId(@Nonnull UUID sceneId);
}
