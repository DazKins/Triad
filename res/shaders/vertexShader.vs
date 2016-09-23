attribute vec3 position;

uniform int test;

out uint colour;

void main(void)
{
	colour = test;
	gl_Position = vec4(position, 0.0);
}