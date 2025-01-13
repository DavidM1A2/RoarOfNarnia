package com.dslovikosky.narnia.common.utils;

import com.mojang.datafixers.util.Function7;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.codec.StreamCodec;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Function;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LongStreamCodec {
    public static <B, C, T1, T2, T3, T4, T5, T6, T7> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> pCodec1,
            final Function<C, T1> pGetter1,
            final StreamCodec<? super B, T2> pCodec2,
            final Function<C, T2> pGetter2,
            final StreamCodec<? super B, T3> pCodec3,
            final Function<C, T3> pGetter3,
            final StreamCodec<? super B, T4> pCodec4,
            final Function<C, T4> pGetter4,
            final StreamCodec<? super B, T5> pCodec5,
            final Function<C, T5> pGetter5,
            final StreamCodec<? super B, T6> pCodec6,
            final Function<C, T6> pGetter6,
            final StreamCodec<? super B, T7> pCodec7,
            final Function<C, T7> pGetter7,
            final Function7<T1, T2, T3, T4, T5, T6, T7, C> pFactory
    ) {
        return new StreamCodec<>() {
            @Override
            public C decode(B stream) {
                T1 t1 = pCodec1.decode(stream);
                T2 t2 = pCodec2.decode(stream);
                T3 t3 = pCodec3.decode(stream);
                T4 t4 = pCodec4.decode(stream);
                T5 t5 = pCodec5.decode(stream);
                T6 t6 = pCodec6.decode(stream);
                T7 t7 = pCodec7.decode(stream);
                return pFactory.apply(t1, t2, t3, t4, t5, t6, t7);
            }

            @Override
            public void encode(B stream, C input) {
                pCodec1.encode(stream, pGetter1.apply(input));
                pCodec2.encode(stream, pGetter2.apply(input));
                pCodec3.encode(stream, pGetter3.apply(input));
                pCodec4.encode(stream, pGetter4.apply(input));
                pCodec5.encode(stream, pGetter5.apply(input));
                pCodec6.encode(stream, pGetter6.apply(input));
                pCodec7.encode(stream, pGetter7.apply(input));
            }
        };
    }
}
