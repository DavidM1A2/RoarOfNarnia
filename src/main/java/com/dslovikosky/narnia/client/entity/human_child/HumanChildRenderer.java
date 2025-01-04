package com.dslovikosky.narnia.client.entity.human_child;

import com.dslovikosky.narnia.common.entity.human_child.HumanChildEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;

public abstract class HumanChildRenderer<T extends HumanChildEntity> extends LivingEntityRenderer<T, HumanChildModel<T>> {
    public HumanChildRenderer(final EntityRendererProvider.Context context) {
        super(context, new HumanChildModel<T>(context.bakeLayer(HumanChildModel.LAYER_LOCATION)), 0.4f);
    }
}
