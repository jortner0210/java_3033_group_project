package app;

import javafx.event.Event;
import javafx.event.EventType;

public class MyEvent extends Event {

	public static final EventType<MyEvent> SHOW_RECIPE =
			new EventType<>(Event.ANY, "SHOW_RECIPE");
	
	public static final EventType<MyEvent> ADD_RECIPE =
			new EventType<>(Event.ANY, "ADD_RECIPE");
	
	public MyEvent() {
		super(Event.ANY);
	}
	
	
}


