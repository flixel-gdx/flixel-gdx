#ifdef GL_ES
	#define LOWP lowp
	precision mediump float;
#else
	#define LOWP
#endif

uniform sampler2D u_texture;
uniform sampler2D u_texture1;

varying vec2 v_texCoord;

const vec4 white = vec4(1.0, 1.0, 1.0, 1.0);
const vec4 lumCoeff = vec4(0.2125, 0.7154, 0.0721, 1.0);

void main() 
{	
	vec4 base = texture2D(u_texture, v_texCoord);
	vec4 blend = texture2D(u_texture1, v_texCoord);
	
	float luminance = dot(base, lumCoeff);
	if (luminance < 0.45)
	    gl_FragColor = 2.0 * blend * base;
	else if (luminance > 0.55)
	    gl_FragColor = white - 2.0 * (white - blend) * (white - base);
	else
	{
	    vec4 result1 = 2.0 * blend * base;
	    vec4 result2 = white - 2.0 * (white - blend) * (white - base);
	    gl_FragColor = mix(result1, result2, (luminance - 0.45) * 10.0);
	}
}