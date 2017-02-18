//Event class defines how events are treated on the calendar. Most fields are inputted by user
public class Event {
	private String eventName; //stores the name of the event
	private String eventLocation; //stores the location of the event
	private String eventDescription; //stores description of the event
	private int eventType; //stores event type; int value is mapped onto strings in display
	private int eventYear; //year of event
	private int eventMonth; //month of event
	private int eventDate; //day of event
	private int eventStart; //time event starts
	private int eventEnd; //time event ends
	public int eventId; //unique ID for event, generated automatically internally
	
   //Constructor for event
	public Event(String title, int year, int month, int date, int start, int end, int type, String location, String description,int id) {
		eventName = title;
		eventYear = year;
		eventId = id;
		if(month < 1 || month > 12) throw new IllegalArgumentException();
		eventMonth = month;
		if(date < 1) throw new IllegalArgumentException();
		if(eventMonth != 2 && eventMonth < 8) {
			if(eventMonth % 2 == 1 && date > 31) throw new IllegalArgumentException();
			else if( date > 30) throw new IllegalArgumentException();
		}
		else if(eventMonth == 2) {
			if(eventYear % 4 == 0 && date > 29) throw new IllegalArgumentException();
			else if(date > 28) throw new IllegalArgumentException();
		}
		else {
			if(eventMonth % 2 == 0 && date > 31) throw new IllegalArgumentException();
			else if(date > 30) throw new IllegalArgumentException();
		}
		eventDate = date;
		if(start / 100 < 0 || start / 100 > 23 || start % 100 < 0 || start % 100 > 59) throw new IllegalArgumentException();
		eventStart = start;
		if(end / 100 < 0 || end / 100 > 23 || end % 100 < 0 || end % 100 > 59) throw new IllegalArgumentException();
		eventEnd = end;
		if(type < 0 || type > 5) throw new IllegalArgumentException();
		eventType = type;
		eventLocation = location;
		eventDescription = description;
	}
	
   //sets event name
	public void setName(String title) {
		eventName = title;
	}
	
   //sets event locatin
	public void setLocation(String location) {
		eventLocation = location;
	}
	
   //sets event description
	public void setDescription(String description) {
		eventDescription = description;
	}
	
   //sets event type
	public void setType(int type) {
		if(type < 0 || type > 5) throw new IllegalArgumentException();
		eventType = type;
	}
	
   //sets event year
	public void setYear(int year) {
		eventYear = year;
	}
	
   //sets event month
	public void setMonth(int month) {
		if(month < 1 || month > 12) throw new IllegalArgumentException();
		eventMonth = month;
	}
	
   //sets event date, checks for invalid dates depending on months and years
	public void setDate(int date) {
		if(date < 1) throw new IllegalArgumentException();
		if(eventMonth != 2 && eventMonth < 8) {
			if(eventMonth % 2 == 1 && date > 31) throw new IllegalArgumentException();
			else if( date > 30) throw new IllegalArgumentException();
		}
		else if(eventMonth == 2) {
			if(eventYear % 4 == 0 && date > 29) throw new IllegalArgumentException();
			else if(date > 28) throw new IllegalArgumentException();
		}
		else {
			if(eventMonth % 2 == 0 && date > 31) throw new IllegalArgumentException();
			else if(date > 30) throw new IllegalArgumentException();
		}
		eventDate = date;
	}
	
   //sets start and end time of event
	public void setTime(int start, int end) {
		if(start / 100 < 0 || start / 100 > 23 || start % 100 < 0 || start % 100 > 59) throw new IllegalArgumentException();
		eventStart = start;
		if(end / 100 < 0 || end / 100 > 23 || end % 100 < 0 || end % 100 > 59) throw new IllegalArgumentException();
		eventEnd = end;
	}
	
   //returns event name as string
	public String getName() {return eventName;}
	
   //returns event location as string
	public String getLocation() {return eventLocation;}
	
   //returns event description as string
	public String getDescription() {return eventDescription;}
	
   //returns event type as corresponding interger value
   public int getType() {return eventType;}
	
   //returns year event takes place
	public int getYear() {return eventYear;}
	
   //returns month event takes place
	public int getMonth() {return eventMonth;}
	
   //returns day event takes place
	public int getDate() {return eventDate;}
	
   //returns start time of event
	public int getStart() {return eventStart;}
	
   //returns end time of event
	public int getEnd() {return eventEnd;}
}
