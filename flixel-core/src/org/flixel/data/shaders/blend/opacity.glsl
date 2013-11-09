#ifdef GL_ES
	#define LOWP lowp
	precision mediump float;
#else
	#define LOWP
#endif

uniform sampler2D u_texture;
uniform sampler2D u_texture1;
uniform float u_opacity;

varying vec2 v_texCoord;

void main() 
{
	vec4 base = texture2D(u_texture, v_texCoord);	
	vec4 blend = texture2D(u_texture1, v_texCoord);		
	
	vec4 result = blend + base;
    result = clamp(result, 0.0, 1.0);
         	
	gl_FragColor = mix(base, result, u_opacity);
}