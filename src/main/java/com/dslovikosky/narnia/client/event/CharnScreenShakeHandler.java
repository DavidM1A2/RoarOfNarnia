package com.dslovikosky.narnia.client.event;

import net.minecraft.util.Mth;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;

import java.util.Random;

public class CharnScreenShakeHandler {
    private static final Random RAND = new Random();
    private double seedX;
    private double seedY;
    private double seedZ;
    private double seedFov;

    private double startTimeSec = 0.0;
    private double durationSec = 0.0;
    private float intensityStart = 0f;
    private float intensityEnd = 0f;

    @SubscribeEvent
    public void onComputeFov(final ViewportEvent.ComputeFov event) {
        if (!isActive()) {
            return;
        }

        final double elapsedSeconds = elapsedSeconds();
        final float fade = envelope(elapsedSeconds);

        final float intensity = currentIntensity();
        final double fovNoise = smoothNoise(elapsedSeconds * 3.5, seedFov);
        final double fovMultiplier = 1.0 + (fovNoise * (intensity * 0.01) * fade * 2.0);

        // clamp to avoid extreme FOV
        double newFov = event.getFOV() * fovMultiplier;
        newFov = Math.max(40.0, Math.min(140.0, newFov));
        event.setFOV((float) newFov);
    }

    @SubscribeEvent
    public void onComputeCameraAnglesEvent(final ViewportEvent.ComputeCameraAngles event) {
        if (!isActive()) {
            return;
        }

        final double elapsedSeconds = elapsedSeconds();
        final float fade = envelope(elapsedSeconds);

        final double yawNoise = smoothNoise(elapsedSeconds * 1.2, seedX);
        final double pitchNoise = smoothNoise(elapsedSeconds * 1.35, seedY);
        final double rollNoise = smoothNoise(elapsedSeconds * 0.9, seedZ);

        final float intensity = currentIntensity();
        final float yawOffsetDeg = (float) (yawNoise * intensity * 3.5f * fade);
        final float pitchOffsetDeg = (float) (pitchNoise * intensity * 2.2f * fade);
        final float rollOffsetDeg = (float) (rollNoise * intensity * 2.8f * fade);

        // apply: the event exposes get/set pitch/yaw/roll (angles in degrees)
        event.setYaw(event.getYaw() + yawOffsetDeg);
        event.setPitch(event.getPitch() + pitchOffsetDeg);
        event.setRoll(event.getRoll() + rollOffsetDeg);
    }

    public void start(final float intensityStart, final float intensityEnd, final float seconds) {
        this.seedX = RAND.nextDouble() * 1000.0;
        this.seedY = RAND.nextDouble() * 1000.0;
        this.seedZ = RAND.nextDouble() * 1000.0;
        this.seedFov = RAND.nextDouble() * 1000.0;
        this.intensityStart = intensityStart;
        this.intensityEnd = intensityEnd;
        this.durationSec = Math.max(0.001, seconds);
        this.startTimeSec = System.nanoTime() / 1e9;
    }

    private float currentIntensity() {
        final double elapsedSeconds = elapsedSeconds();
        if (elapsedSeconds >= durationSec) {
            return 0f;
        }

        final float progress = (float) (elapsedSeconds / durationSec);
        return Mth.lerp(progress, intensityStart, intensityEnd);
    }

    private double elapsedSeconds() {
        if (startTimeSec == 0.0) {
            return Double.POSITIVE_INFINITY;
        }
        return (System.nanoTime() / 1e9) - startTimeSec;
    }

    private boolean isActive() {
        final double elapsedSeconds = elapsedSeconds();
        return elapsedSeconds < durationSec;
    }

    // a smooth pseudo-noise using a few sine octaves (cheap, no external lib)
    private double smoothNoise(double t, double seed) {
        double v = 0.0;
        v += 1.0 * Math.sin((t * 2.0) + seed);
        v += 0.5 * Math.sin((t * 6.0) + seed * 1.3);
        v += 0.25 * Math.sin((t * 18.0) + seed * 2.1);
        v += 0.125 * Math.sin((t * 40.0) + seed * 3.3);
        // normalize to roughly [-1,1]
        return v * (1.0 / (1.0 + 0.5 + 0.25 + 0.125));
    }

    private float envelope(double t) {
        if (t >= durationSec) {
            return 0f;
        }
        final double norm = Math.max(0.0, 1.0 - (t / durationSec)); // 1 -> 0
        // ease-out
        final double eased = 1 - Math.pow(1 - norm, 6);
        return (float) eased;
    }
}
