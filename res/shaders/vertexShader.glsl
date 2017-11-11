#version 330

layout (location = 0) in vec2 position;
layout (location = 1) in vec2 tex;

out vec2 pass_tex;

uniform mat3 mat;

void main()
{
	vec3 r = mat * vec3(position, 1.0);
	
	r = vec3((r.x / 800) - 1.0, (r.y / 450) - 1.0, 1.0);
	
	gl_Position = vec4(r, 1.0);

	pass_tex = tex;
}