package flash.events;

public class MouseEvent extends Event
{
	static public final String MOUSE_UP = "onMouseUp";

	static public final String MOUSE_DOWN = "onMouseDown";
	
	public int stageX;
	public int stageY;
	
	public MouseEvent(String type, int stageX, int stageY)
	{
		super(type);
		this.stageX = stageX;
		this.stageY = stageY;
	}	
}