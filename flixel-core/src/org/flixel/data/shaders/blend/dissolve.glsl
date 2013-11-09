#ifdef GL_ES
	#define LOWP lowp
	precision mediump float;
#else
	#define LOWP
#endif

uniform sampler2D u_texture;
uniform sampler2D u_texture1;
uniform float u_noiseScale;
uniform float u_opacity;

varying vec2 v_texCoord;

void main() 
{
	vec4 base = texture2D(u_texture, v_texCoord);
	vec4 blend = texture2D(u_texture1, v_texCoord);
	float noise = (noise1(vec2(v_texCoord[0] * u_noiseScale)) + 1.0) * 0.5;
	gl_FragColor = (noise < u_opacity) ? blend : base;
}