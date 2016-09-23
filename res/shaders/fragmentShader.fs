in uint colour;

out vec4 out_Color;

void main(void)
{
	out_Color = vec4(colour / 255, 0.0, 1.0, 1.0);
}