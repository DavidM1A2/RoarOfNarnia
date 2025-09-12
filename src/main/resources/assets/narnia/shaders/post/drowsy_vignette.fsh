#version 150

uniform sampler2D In;

// Std140 uniforms must be 16 byte aligned
layout (std140) uniform TimeUniform {
    // x = vignette strength (0..1), yzw are ignored
    vec4 timeStrengthVec;
};

in vec2 texCoord;
out vec4 fragColor;

void main() {
    // The base color
    vec4 base = texture(In, texCoord);

    // The distance of the coordinate to the center of the screen
    float d = distance(texCoord, vec2(0.5));

    // Clamp the strength from 0 to 1
    float strength = clamp(timeStrengthVec.x, 0.0, 1.0);

    // Radial vignette shape (ease in as strength rises)
    float radius = smoothstep(0.75, 0.0, strength);
    float edge = 0.5;
    float vign = smoothstep(radius, radius + edge, d);

    // Base vignette darkening
    float factor = 1.0 - vign;

    // --- Extra collapse in last 10% ---
    // At strength < 0.9 → collapseFactor = 0
    // At strength = 1.0 → collapseFactor = 1
    float collapseFactor = smoothstep(0.9, 1.0, strength);

    // Mix between vignette-based fade and full black
    factor = mix(factor, 0.0, collapseFactor);

    fragColor = vec4(base.rgb * factor, base.a);
}
