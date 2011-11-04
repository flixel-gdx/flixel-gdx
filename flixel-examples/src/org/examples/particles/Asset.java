package org.examples.particles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Asset
{
	static public TextureRegion particle1;
	static public TextureRegion particle2;
	
	static public void create()
	{
		Texture ParticleAtlas = new Texture(Gdx.files.internal("examples/particles/particles.png"));
		particle1 = new TextureRegion(ParticleAtlas, 0, 0, 1, 1);
		particle2 = new TextureRegion(ParticleAtlas, 0, 0, 2, 2);
	}
}
