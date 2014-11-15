#ifdef GL_ES
	#define LOWP lowp
	precision mediump float;
#else
	#define LOWP
#endif

uniform sampler2D u_texture;
uniform sampler2D u_texture1;

varying vec2 v_texCoord;

const vec4 white = vec4(1.0);

void main() 
{	
	vec4 base = texture2D(u_texture, v_texCoord);
	vec4 blend = texture2D(u_texture1, v_texCoord);	
	
	if(all(equal(blend, white)))
		gl_FragColor = blend;
	else
		gl_FragColor = min(white, (base * base / (white - blend)));	
}