#version 330
#extension GL_ARB_separate_shader_objects : require

// From the POSITION vertex binding the pipeline declares
layout(location = 0) in vec3 Position;
layout(location = 0) out vec2 texCoord;

void main() {
    gl_Position = vec4(Position, 1.0);

    // Convert from clip space (-1..1) to UV (0..1)
    texCoord = (gl_Position.xy * 0.5) + 0.5;
}
