#ifdef GL_ES
	#define LOWP lowp
	precision mediump float;
#else
	#define LOWP
#endif

uniform sampler2D u_texture;
uniform sampler2D u_texture1;

varying vec2 v_texCoord;

#define BlendLightenf(base, blend) 		max(blend, base)
#define BlendDarkenf(base, blend) 		min(blend, base)

void main() 
{
	vec4 base = texture2D(u_texture, v_texCoord);
	vec4 blend = texture2D(u_texture1, v_texCoord);
	if(all(lessThan(blend, vec4(0.5))))
		gl_FragColor = BlendDarkenf(base, (2.0 * blend));
	else
		gl_FragColor = BlendLightenf(base, (2.0 *(blend - 0.5)));
}