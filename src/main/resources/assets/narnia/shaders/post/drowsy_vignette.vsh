#version 150

// From BLIT_SCREEN
in vec3 Position;

out vec2 texCoord;

void main() {
    gl_Position = vec4(Position, 1.0);

    // Convert from clip space (-1..1) to UV (0..1)
    texCoord = (gl_Position.xy * 0.5) + 0.5;
}
