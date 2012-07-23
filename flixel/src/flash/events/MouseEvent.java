package flash.events;

public class MouseEvent extends Event
{
	static public final String MOUSE_UP = "onMouseUp";
	
	public int stageX;
	public int stageY;
	
	public MouseEvent(String type, int stageX, int stageY)
	{
		super(type);
		this.stageX = stageX;
		this.stageY = stageY;
	}	
}