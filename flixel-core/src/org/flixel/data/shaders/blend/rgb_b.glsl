#ifdef GL_ES
	#define LOWP lowp
	precision mediump float;
#else
	#define LOWP
#endif

uniform sampler2D u_texture;
uniform sampler2D u_texture1;

varying vec2 v_texCoord;

const float num1 = 1.0;

void main() 
{	
	vec4 base = texture2D(u_texture, v_texCoord);
	vec4 blend = texture2D(u_texture1, v_texCoord);
	vec4 result = vec4(1.0);
	result.r = base.r;
	result.g = base.g;
	result.b = blend.b;
	gl_FragColor = result;
}