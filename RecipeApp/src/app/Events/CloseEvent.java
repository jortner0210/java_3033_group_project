package app.Events;

import app.Recipe;
import javafx.event.Event;
import javafx.event.EventType;

public class CloseEvent extends Event {
	public static final EventType<CloseEvent> CLOSE =
			new EventType<>(Event.ANY, "CLOSE");
	
	
	public CloseEvent() {
		super(CLOSE);
	}
}
