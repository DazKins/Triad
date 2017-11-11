#version 330

in vec2 pass_tex;

uniform sampler2D texture_sampler;

out vec4 color;

void main()
{
//	gl_FragColor = vec4(texture2D(texture_sampler, pass_tex).rgb, 1.0);
	color = texture2D(texture_sampler, pass_tex).rgba;
//	gl_FragColor = vec4(pass_tex.xy, 1.0, 1.0);
//	gl_FragColor = texture2D(texture_sampler, pass_tex).rgba;
//	gl_FragColor = vec4(1.0, 0.0, 1.0, 1.0);
}