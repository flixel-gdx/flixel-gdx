#ifdef GL_ES
	#define LOWP lowp
	precision mediump float;
#else
	#define LOWP
#endif

uniform sampler2D u_texture;
uniform sampler2D u_texture1;

varying vec2 v_texCoord;


void main() 
{	
	vec4 base = texture2D(u_texture, v_texCoord);
	vec4 blend = texture2D(u_texture1, v_texCoord);
	if(lessThan(blend, vec4(0.5)) == true)
	{	 
		gl_FragColor = (2.0 * base * blend + base * base * (1.0 - 2.0 * blend));
	}
	else
	{
		gl_FragColor = (sqrt(base) * (2.0 * blend - 1.0) + 2.0 * base * (1.0 - blend));
	}
}