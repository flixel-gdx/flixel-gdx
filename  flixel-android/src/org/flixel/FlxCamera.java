package org.flixel;

public class FlxCamera
{
	private final int NOTHING = 0;
	private final int SIMPLE = 1;
	private final int TRAILSPRITE = 2;
	private final int TRAILPOINT = 3;

	public int mode;

	public FlxSprite cameraTarget = null;

	// Default stuff
	private float _defaultOffsetX;
	private float _defaultOffsetY;

	// General camera stuff
	public float cameraOffsetX;
	public float cameraOffsetY;

	// Camera boundaries
	public float cameraMinX;
	public float cameraMinY;
	public float cameraMaxX;
	public float cameraMaxY;

	// Trailing stuff
	public float trailX;
	public float trailY;
	public float trailSpeedX;
	public float trailSpeedY;
	public FlxTrailMatchListener trailMatchCallback;

	public float trailTargetPointX;
	public float trailTargetPointY;

	public FlxCamera()
	{
		_defaultOffsetX = FlxG.width / 2;
		_defaultOffsetY = FlxG.height / 2;

		mode = NOTHING;
	}

	public void adjustBounds(float MinX, float MinY, float MaxX, float MaxY)
	{
		cameraMinX = MinX;
		cameraMinY = MinY;
		cameraMaxX = MaxX;
		cameraMaxY = MaxY;
	}

	public void simpleFollowSprite(FlxSprite Target, float OffsetX, float OffsetY)
	{
		mode = SIMPLE;

		cameraTarget = Target;
		if(OffsetX == -1)
			cameraOffsetX = _defaultOffsetX;
		else
			cameraOffsetX = OffsetX;
		if(OffsetY == -1)
			cameraOffsetY = _defaultOffsetY;
		else
			cameraOffsetY = OffsetY;
	}

	public void simpleFollowSprite(FlxSprite Target, float OffsetX)
	{
		simpleFollowSprite(Target, OffsetX, -1);
	}

	public void simpleFollowSprite(FlxSprite Target)
	{
		simpleFollowSprite(Target, -1, -1);
	}

	public void trailFollowSprite(FlxSprite Target, float TrailSpeedX, float TrailSpeedY, float OffsetX, float OffsetY,	FlxTrailMatchListener MatchCallback)
	{
		mode = TRAILSPRITE;

		cameraTarget = Target;
		if(OffsetX == -1)
			cameraOffsetX = _defaultOffsetX;
		else
			cameraOffsetX = OffsetX;
		if(OffsetY == -1)
			cameraOffsetY = _defaultOffsetY;
		else
			cameraOffsetY = OffsetY;

		trailX = getX();
		trailY = getY();
		trailSpeedX = TrailSpeedX;
		trailSpeedY = TrailSpeedY;

		trailMatchCallback = MatchCallback;
	}

	public void trailFollowSprite(FlxSprite Target, float TrailSpeedX, float TrailSpeedY, float OffsetX, float OffsetY)
	{
		trailFollowSprite(Target, TrailSpeedX, TrailSpeedY, OffsetX, OffsetY, null);
	}

	public void trailFollowSprite(FlxSprite Target, float TrailSpeedX, float TrailSpeedY, float OffsetX)
	{
		trailFollowSprite(Target, TrailSpeedX, TrailSpeedY, OffsetX, -1, null);
	}

	public void trailFollowSprite(FlxSprite Target, float TrailSpeedX, float TrailSpeedY)
	{
		trailFollowSprite(Target, TrailSpeedX, TrailSpeedY, -1, -1, null);
	}

	public void update()
	{
		float tx = 0;
		float ty = 0;
		if(cameraTarget != null)
		{
			tx = cameraTarget.x - cameraTarget.offset.x + cameraTarget.frameWidth / 2 - cameraOffsetX;
			ty = cameraTarget.y - cameraTarget.offset.y + cameraTarget.frameHeight / 2 - cameraOffsetY;
		}

		switch(mode)
		{
			case SIMPLE:
				if(cameraTarget != null)
					lookAt(tx, ty);
				break;
			case TRAILSPRITE:
				if(cameraTarget != null)
					trailUpdate(tx, ty);
				break;
			case TRAILPOINT:
				trailUpdate(trailTargetPointX, trailTargetPointY);
				break;
		}
	}

	public void lookAt(float X, float Y)
	{
		FlxG.scroll.x = -X;
		FlxG.scroll.y = -Y;
		if(-FlxG.scroll.x < cameraMinX)
			FlxG.scroll.x = -cameraMinX;
		if(-FlxG.scroll.x + FlxG.width > cameraMaxX)
			FlxG.scroll.x = -(cameraMaxX - FlxG.width);
		if(-FlxG.scroll.y < cameraMinY)
			FlxG.scroll.y = -cameraMinY;
		if(-FlxG.scroll.y + FlxG.height > cameraMaxY)
			FlxG.scroll.y = -(cameraMaxY - FlxG.height);
	}

	private void trailUpdate(float TargetX, float TargetY)
	{
		if(trailX < TargetX)
		{
			trailX += FlxG.elapsed * trailSpeedX;
			if(trailX > TargetX)
				trailX = TargetX;
		}
		if(trailX > TargetX)
		{
			trailX -= FlxG.elapsed * trailSpeedX;
			if(trailX < TargetX)
				trailX = TargetX;
		}
		if(trailY < TargetY)
		{
			trailY += FlxG.elapsed * trailSpeedY;
			if(trailY > TargetY)
				trailY = TargetY;
		}
		if(trailY > TargetY)
		{
			trailY -= FlxG.elapsed * trailSpeedY;
			if(trailY < TargetY)
				trailY = TargetY;
		}

		if(trailX == TargetX && trailY == TargetY && trailMatchCallback != null)
		{
			trailMatchCallback.trailMatch();
		}

		lookAt(trailX, trailY);
	}

	public void stop()
	{
		mode = NOTHING;
	}

	public float getX()
	{
		return -FlxG.scroll.x;
	}

	public float getY()
	{
		return -FlxG.scroll.y;
	}
}
