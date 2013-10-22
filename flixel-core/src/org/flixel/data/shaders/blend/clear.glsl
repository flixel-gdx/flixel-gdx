#ifdef GL_ES
	#define LOWP lowp
	precision mediump float;
#else
	#define LOWP
#endif

uniform sampler2D u_texture;

varying vec2 v_texCoord;

void main() 
{
	vec4 blend = texture2D(u_texture, v_texCoord);
	vec4 result;
	result.rgb = blend.rgb;
	result.a = 0.0;
	gl_FragColor = result;
}