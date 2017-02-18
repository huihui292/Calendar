//import declerations
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

//App is responsible for the program itself. Implements functionality of all other classes into a UI
public class App {
   //object declerations
	private JFrame frame;
	private JPanel panel;
	JTabbedPane tabbedPane;
	JLabel lbldetailsName;
	JLabel detailsLocation;
	JLabel detailsType;
	JLabel detailsEnd;
	JLabel detailsStart;
	JLabel detailsDate;
	JLabel evID;
	JEditorPane detailsDescription;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField searchLocation;
	private ArrayList<Event> currentlyShown = new ArrayList<Event>();
	private int distanceFromTop = 10;
	private int height = 25;
	/**
	 * App constructor
	 */
	public App() {
		initialaize();
		frame.setVisible(true);
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialaize() {
		//setup Jframe in which application will be displayed
		frame = new JFrame();
		 frame.addWindowListener(new WindowListener() {
			@Override
			public void windowActivated(WindowEvent e) {}
			@Override
			public void windowClosed(WindowEvent e) {}
			@Override
			public void windowClosing(WindowEvent e) {}
			@Override
			public void windowDeactivated(WindowEvent e) {
				frame.setState(JFrame.ICONIFIED);
			}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowOpened(WindowEvent e) {}
		 });
		frame.getContentPane().setBackground(SystemColor.window);
		ArrayList<Image> icons = new ArrayList<Image>();
      //FOR ICON DISPLAY
		icons.add(new ImageIcon(App.class.getResource("/ic_event_black_24dp_1x.png")).getImage());
		icons.add(new ImageIcon(App.class.getResource("/ic_event_white_24dp_2x.png")).getImage());
		frame.setIconImages((java.util.List<? extends Image>) icons);
		frame.setResizable(false);//prevent resize because layout is non scalable
		frame.setBounds(100, 100, 873, 615);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		//use a consistent and familiar UI style and not the java default
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UIManager.put("TabbedPane.focus", new Color(0, 0, 0, 0));//get rid of dotted line on selected tab

		//Where the events are going to be displayed after the user has searched
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 435, 564);
		frame.getContentPane().add(scrollPane);
		
		panel = new JPanel();
		panel.setBackground(new Color(240, 255, 240));
		scrollPane.setViewportView(panel);
		panel.setLayout(null);
		
		//Add the tabs to differentiate the different functions of the application
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(null);
		tabbedPane.setBounds(455, 11, 402, 564);
		frame.getContentPane().add(tabbedPane);
			//Add a JPanel to the first tab that will contain the UI to add events
		JPanel Add = new JPanel();
		Add.setBackground(new Color(255, 255, 204));
		tabbedPane.addTab("<html><h4 style='margin-right:5px;margin-left:5px;margin-top:1px;margin-bottom:0px;'>Add</h4></html>",Add);
		Add.setLayout(null);
			//Event Name Label
			JLabel lblEventName = new JLabel("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Event Name</h4></html>");
			lblEventName.setBounds(10, 11, 377, 21);
			Add.add(lblEventName);
			//Field to get input for event Name
			JTextField eventName = new JTextField();
			eventName.setBounds(10, 32, 377, 21);
			Add.add(eventName);
			eventName.setColumns(10);
			//Figure out current year to set a reasonable upper and lower limit for the year selection
			Date date = new Date();
			Calendar cal = Calendar.getInstance();
		    cal.setTime(date);
		    int currentYear = cal.get(Calendar.YEAR);
			//Month label
			JLabel lblmonth = new JLabel("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Month</h4></html>");
			lblmonth.setBounds(10, 64, 53, 21);
			Add.add(lblmonth);
			//Make spinner from array of Months
			String months[] = {"January","February","March","April","May","June","July","August","September","October","November","December"};
			SpinnerModel month = new SpinnerListModel(months);
			JSpinner Months = new JSpinner(month);
			Months.setBounds(10, 82, 84, 21);
			Add.add(Months);
			//Day label
			JLabel dayLbl = new JLabel("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Day</h4></html>");
			dayLbl.setBounds(105, 64, 53, 21);
			Add.add(dayLbl);
			//Day selector uses listeners to set upper limit based on year and month
			SpinnerNumberModel days = new SpinnerNumberModel(1, 1, 31, 1);
			JSpinner Day = new JSpinner(days);
			Day.setBounds(105, 82, 84, 21);
			Add.add(Day);
		    //Year label
			JLabel lblyear = new JLabel("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Year</h4></html>");
			lblyear.setBounds(199, 64, 53, 21);
			Add.add(lblyear);
		    //Add year selector
		    SpinnerModel yearModel = new SpinnerNumberModel(currentYear, currentYear-20, currentYear+20, 1);//range 20 years before and after current year
			JSpinner Year = new JSpinner(yearModel);
			Year.setEditor(new JSpinner.NumberEditor(Year,"#"));
			Year.setBounds(199, 82, 84, 21);
			Add.add(Year);
			//Start Time Label
			JLabel lblstartTime = new JLabel("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px;text-decoration:underline;'>Start Time</h4></html>");
			lblstartTime.setBounds(10, 114, 84, 21);
			Add.add(lblstartTime);
			//Start Hour Label
			JLabel lblStartHour = new JLabel("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Hour</h4></html>");
			lblStartHour.setBounds(10, 131, 53, 21);
			Add.add(lblStartHour);
			//Start Hour selector
			SpinnerModel startHour = new SpinnerNumberModel(0, 0, 23, 1);
			JSpinner starthours = new JSpinner(startHour);
			starthours.setBounds(10, 149, 53, 21);
			Add.add(starthours);
			//Start Minute label
			JLabel lblStartMinute = new JLabel("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Minutes</h4></html>");
			lblStartMinute.setBounds(73, 131, 66, 21);
			Add.add(lblStartMinute);
			//Start Minute selector
			SpinnerModel startMinute = new SpinnerNumberModel(0, 0, 59, 1);
			JSpinner startMinutes = new JSpinner(startMinute);
			startMinutes.setBounds(73, 149, 53, 21);
			Add.add(startMinutes);
			//End time label
			JLabel lblendTime = new JLabel("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px;text-decoration:underline;'>End Time</h4></html>");
			lblendTime.setBounds(167, 114, 84, 21);
			Add.add(lblendTime);
			//End hour label
			JLabel lblEndHour = new JLabel("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Hour</h4></html>");
			lblEndHour.setBounds(167, 131, 53, 21);
			Add.add(lblEndHour);
			//End hour selector
			SpinnerModel endHour = new SpinnerNumberModel(0, 0, 23, 1);
			JSpinner endHours = new JSpinner(endHour);
			endHours.setBounds(167, 149, 53, 21);
			Add.add(endHours);
			//End minutes label
			JLabel lblEndMinutes = new JLabel("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Minutes</h4></html>");
			lblEndMinutes.setBounds(230, 131, 66, 21);
			Add.add(lblEndMinutes);
			//End minute selector
			SpinnerModel endMinute = new SpinnerNumberModel(0, 0, 59, 1);
			JSpinner endMinutes = new JSpinner(endMinute);
			endMinutes.setBounds(230, 149, 53, 21);
			Add.add(endMinutes);
			//Event type label
			JLabel lblType = new JLabel("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Type</h4></html>");
			lblType.setBounds(10, 177, 53, 21);
			Add.add(lblType);
			//Event type selector
			String eventTypes[] = {"Holiday", "Concert", "Meetings/Appointment", "Birthday", "Exam", "Other"};
			SpinnerModel types = new SpinnerListModel(eventTypes);
			JSpinner type = new JSpinner(types);
			type.setBounds(10, 199, 148, 21);
			Add.add(type);
			//Description Label
			JLabel lbldescription = new JLabel("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Description</h4></html>");
			lbldescription.setBounds(10, 231, 377, 21);
			Add.add(lbldescription);
			//Editor Pane embedded in scroll-pane to allow scrolling if the character limit was higher
			JScrollPane scrollPane_1 = new JScrollPane();
			scrollPane_1.setBounds(10, 254, 377, 239);
			Add.add(scrollPane_1);
				JEditorPane description = new JEditorPane();
				description.setFont(new Font("Tahoma", Font.PLAIN, 16));
				scrollPane_1.setViewportView(description);
			//Submit button to create the event if all error checks pass
			JButton btnEvent = new JButton("Submit");
			btnEvent.setBounds(10, 500, 89, 23);
			Add.add(btnEvent);
			
			JLabel lbllocation = new JLabel("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Location</h4></html>");
			lbllocation.setBounds(177, 177, 210, 21);
			Add.add(lbllocation);
			
			JScrollPane locationScroll = new JScrollPane();
			locationScroll.setBounds(176, 196, 148, 24);
			Add.add(locationScroll);
			JTextField location = new JTextField();
			locationScroll.setViewportView(location);
			location.setColumns(10);
			
			JButton btnOpenMap = new JButton("Map");
			btnOpenMap.setBounds(334, 196, 53, 25);
			Add.add(btnOpenMap);
			btnOpenMap.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent mouse) {
					if(location.getText().length()>0){
						if(Desktop.isDesktopSupported())
						{
						  try {
							Desktop.getDesktop().browse(new URI("https://www.google.com/maps?q="+getFormattedText(location.getText())));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (URISyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						}
					}
				}
			});
			location.addCaretListener(new CaretListener(){
				//overrides method that fires when caret position is updated
				@Override
				public void caretUpdate(CaretEvent carEv) {
					int strLen = location.getText().length();//get length of string
					if(strLen<25){//checks if it is less than a 100 chars
						btnEvent.setEnabled(true);//enable button if previously disabled
						//Change the label text to notify user of characters used
						lbllocation.setText("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Location ("+strLen+"/24)</h4></html>");
					}else{
						//if description is too long disable further input
						location.setEditable(false);
						//disable button
						btnEvent.setEnabled(false);
						//check for backspace key to allow user to edit the description appropriately
						location.addKeyListener(new KeyListener(){
							@Override
							public void keyPressed(KeyEvent typed) {
								if(typed.getKeyCode()==KeyEvent.VK_BACK_SPACE){
									location.setEditable(true);
								}
							}
							//unused events that must be implemented
							@Override
							public void keyReleased(KeyEvent typed){}
							@Override
							public void keyTyped(KeyEvent typed){}

						});
						//set description label if text too long
						lbllocation.setText("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Location ("+strLen+"/24) <span style='color: red'>Too long!</span></h4></html>");
					}
				}
			});
			//Checks if entered text is too long and disables input and event submission
			description.addCaretListener(new CaretListener(){
				//overrides method that fires when caret position is updated
				@Override
				public void caretUpdate(CaretEvent carEv) {
					int strLen = description.getText().length();//get length of string
					if(strLen<101){//checks if it is less than a 100 chars
						btnEvent.setEnabled(true);//enable button if previously disabled
						//Change the label text to notify user of characters used
						lbldescription.setText("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Description ("+strLen+"/100)</h4></html>");
					}else{
						//if description is too long disable further input
						description.setEditable(false);
						//disable button
						btnEvent.setEnabled(false);
						//check for backspace key to allow user to edit the description appropriately
						description.addKeyListener(new KeyListener(){
							@Override
							public void keyPressed(KeyEvent typed) {
								if(typed.getKeyCode()==KeyEvent.VK_BACK_SPACE){
									description.setEditable(true);
								}
							}
							//unused events that must be implemented
							@Override
							public void keyReleased(KeyEvent typed){}
							@Override
							public void keyTyped(KeyEvent typed){}

						});
						//set description label if text too long
						lbldescription.setText("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Description ("+strLen+"/100) <span style='color: red'>Description too long</span></h4></html>");
					}
				}
			});
			//Checks if entered text is too long and disables input and event submission
			eventName.addCaretListener(new CaretListener(){
				//overrides method that fires when caret position is updated
				@Override
				public void caretUpdate(CaretEvent carEv) {
					int strLen = eventName.getText().length();//get current string length
					if(strLen<25){//check if length is less than 25
						btnEvent.setEnabled(true);//Enable button if previously disabled
						//Set label text to show remaining chars
						lblEventName.setText("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Event Name ("+strLen+"/24)</h4></html>");
					}else{
						eventName.setEditable(false);//disable editing if string too long
						btnEvent.setEnabled(false);//disable button
						//check for backspace to allow user to shorten name accordingly
						eventName.addKeyListener(new KeyListener(){
							@Override
							public void keyPressed(KeyEvent typed) {
								if(typed.getKeyCode()==KeyEvent.VK_BACK_SPACE){
									eventName.setEditable(true);
								}
							}
							//unused events that must be overridden
							@Override
							public void keyReleased(KeyEvent typed){}
							@Override
							public void keyTyped(KeyEvent typed){}

						});
						//Alert user that entered text is too long
						lblEventName.setText("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Event Name ("+strLen+"/24) <span style='color: red'>Name too long</span></h4></html>");
					}
				}
			});
			//prevent incorrect date entering
			//update max days in February if year is or isn't a leap year
			Year.addChangeListener(new ChangeListener(){
				public void stateChanged(ChangeEvent e){
					String curMonth = (String) Months.getValue();
					int curYear = (int) Year.getValue();
					int maxDays=0;
					if (curMonth == "March" || curMonth == "May" || curMonth == "September" || curMonth == "November")
						maxDays = 30;
					else if (curMonth == "February")
					{ boolean isLeapYear = (curYear % 4 == 0);//leap year check
					if (isLeapYear)
						maxDays = 29;
					else
						maxDays = 28;
					}
					else
						maxDays = 31;
					days.setMaximum(maxDays);
					//if previously selected date is greater than the new maximum then set it to the new maximum
					if((int)Day.getValue() > (int)days.getMaximum()){
						days.setValue(maxDays);
					}
				}
			});
			//Set max days in day model depending on month and year
			Months.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					String curMonth = (String) Months.getValue();
					int curYear = (int) Year.getValue();
					int maxDays=0;
					if (curMonth == "March" || curMonth == "May" || curMonth == "September" || curMonth == "November")
						maxDays = 30;
					else if (curMonth == "February")
					{ boolean isLeapYear = (curYear % 4 == 0);//leap year check
					if (isLeapYear)
						maxDays = 29;
					else
						maxDays = 28;
					}
					else
						maxDays = 31;
					days.setMaximum(maxDays);
					//if previously selected date is greater than the new maximum then set it to the new maximum
					if((int)Day.getValue() > (int)days.getMaximum()){
						days.setValue(maxDays);
					}
				}
			});
			//add event to database
			btnEvent.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent mouse) {
					if(btnEvent.isEnabled()==true){
						int evMonth=0;
						for(int i = 0; i < months.length; ++i){
							if(Months.getValue()==months[i]) evMonth = i+1;
						}
						int evType=-1;
						for(int i = 0; i < eventTypes.length; ++i){
							if(type.getValue()==eventTypes[i]) evType = i;
						}
						int startTime = getTime((int)startHour.getValue(),(int)startMinutes.getValue());
						int endTime = getTime((int)endHour.getValue(),(int)endMinutes.getValue());
						Database.insertEvent(new Event(eventName.getText(), (int)Year.getValue(),evMonth, (int)Day.getValue(), startTime, endTime, evType, location.getText(), description.getText(), 4));
					}
				}
			});
		//Search Tab
		JPanel Search = new JPanel();
		Search.setBackground(new Color(255, 255, 204));
		tabbedPane.addTab("<html><h4 style='margin-right:5px;margin-left:5px;margin-top:1px;margin-bottom:0px'>Search</h4></html>",Search);
		Search.setLayout(null);
			//Event name search label
			JLabel lblsearchEventName = new JLabel("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Event Name</h4></html>");
			lblsearchEventName.setBounds(10, 11, 377, 21);
			Search.add(lblsearchEventName);
			//Event name search field
			JTextField searchEventName = new JTextField();
			searchEventName.setColumns(10);
			searchEventName.setBounds(10, 32, 264, 21);
			Search.add(searchEventName);
			//Month search label
			JLabel lblsearchMonth = new JLabel("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Month</h4></html>");
			lblsearchMonth.setBounds(10, 64, 53, 21);
			Search.add(lblsearchMonth);
			//Month search selector
			SpinnerModel searchMonthModel = new SpinnerListModel(months);
			JSpinner searchMonth = new JSpinner(searchMonthModel);
			searchMonth.setBounds(10, 82, 84, 21);
			Search.add(searchMonth);
			//Day search label
			JLabel lblsearchDay = new JLabel("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Day</h4></html>");
			lblsearchDay.setBounds(105, 64, 53, 21);
			Search.add(lblsearchDay);
			//Day search selector
			SpinnerNumberModel searchDays = new SpinnerNumberModel(1, 1, 31, 1);
			JSpinner searchDay = new JSpinner(searchDays);
			searchDay.setBounds(105, 82, 84, 21);
			Search.add(searchDay);
			//Year search label
			JLabel lblsearchYear = new JLabel("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Year</h4></html>");
			lblsearchYear.setBounds(199, 64, 53, 21);
			Search.add(lblsearchYear);
			//Year search selector
			SpinnerModel searchYearModel = new SpinnerNumberModel(currentYear, currentYear-20, currentYear+20, 1);//range 20 years before and after current year
			JSpinner searchYear = new JSpinner(searchYearModel);
			searchYear.setEditor(new JSpinner.NumberEditor(searchYear,"#"));
			searchYear.setBounds(199, 82, 84, 21);
			Search.add(searchYear);
			//Type search label
			JLabel lblsearchType = new JLabel("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Type</h4></html>");
			lblsearchType.setBounds(11, 114, 53, 21);
			Search.add(lblsearchType);
			//Type search selector
			SpinnerModel searchTypes = new SpinnerListModel(eventTypes);
			JSpinner searchType = new JSpinner(searchTypes);
			searchType.setBounds(11, 136, 148, 21);
			Search.add(searchType);
			JLabel lblsearchLocation = new JLabel("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Location</h4></html>");
			lblsearchLocation.setBounds(11, 168, 210, 21);
			Search.add(lblsearchLocation);
			
			JRadioButton rdbtnSearchByLocation = new JRadioButton("Search By Location");
			buttonGroup.add(rdbtnSearchByLocation);
			rdbtnSearchByLocation.setBackground(new Color(255, 255, 204));
			rdbtnSearchByLocation.setBounds(262, 196, 126, 23);
			Search.add(rdbtnSearchByLocation);
			
			JRadioButton rdbtnSearchByType = new JRadioButton("Search By Type");
			buttonGroup.add(rdbtnSearchByType);
			rdbtnSearchByType.setBackground(new Color(255, 255, 204));
			rdbtnSearchByType.setBounds(168, 135, 107, 23);
			Search.add(rdbtnSearchByType);
			
			JRadioButton rdbtnSearchByName = new JRadioButton("Search By Name");
			buttonGroup.add(rdbtnSearchByName);
			rdbtnSearchByName.setBackground(new Color(255, 255, 204));
			rdbtnSearchByName.setBounds(278, 31, 109, 23);
			Search.add(rdbtnSearchByName);
			
			JRadioButton rdbtnSearchByDate = new JRadioButton("Search By Date");
			buttonGroup.add(rdbtnSearchByDate);
			rdbtnSearchByDate.setBackground(new Color(255, 255, 204));
			rdbtnSearchByDate.setBounds(288, 81, 99, 23);
			Search.add(rdbtnSearchByDate);
			//Search button
			JButton btnSearch = new JButton("Search");
			btnSearch.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					currentlyShown.clear();
					if(rdbtnSearchByName.isSelected()){
						currentlyShown = Database.getByName(searchEventName.getText());
					}else if(rdbtnSearchByDate.isSelected()){
						int evMonth=0;
						for(int i = 0; i < months.length; ++i){
							if(searchMonth.getValue()==months[i]) evMonth = i+1;
						}
						currentlyShown = Database.getByDate(Integer.toString((int) searchYear.getValue()),Integer.toString(evMonth), Integer.toString((int)searchDay.getValue()));
					}else if(rdbtnSearchByLocation.isSelected()){
						currentlyShown = Database.getByLocation(searchLocation.getText());
					}else if(rdbtnSearchByType.isSelected()){
						int evType=-1;
						for(int i = 0; i < eventTypes.length; ++i){
							if(searchType.getValue()==eventTypes[i]) evType = i;
						}
						currentlyShown = Database.getAllByType(evType);
					}
					printEvent();
				}
			});
			btnSearch.setBounds(11, 231, 89, 23);
			Search.add(btnSearch);
			
			JButton btnDeleteAllEvents = new JButton("Delete All Events");
			btnDeleteAllEvents.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0){
					Database.deleteAllEvents();
					currentlyShown.clear();
					currentlyShown = Database.getAllEvents();
					printEvent();
					panel.revalidate();
					panel.repaint();
				}
			});
         //Deletes all events created by user
			btnDeleteAllEvents.setBounds(200, 265, 147, 23);
			Search.add(btnDeleteAllEvents);
			
			JScrollPane scrollLocation = new JScrollPane();
			scrollLocation.setBounds(10, 196, 243, 24);
			Search.add(scrollLocation);
			
			searchLocation = new JTextField();
			searchLocation.setColumns(10);
			scrollLocation.setViewportView(searchLocation);
			
			JButton btnShowAllEvents = new JButton("Show All Events");
			btnShowAllEvents.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					currentlyShown = Database.getAllEvents();
					printEvent();
				}
			});
			btnShowAllEvents.setBounds(11, 265, 147, 23);
			Search.add(btnShowAllEvents);
			//Checks if entered text is too long and disables input and event submission
			searchEventName.addCaretListener(new CaretListener(){
				//overrides method that fires when caret position is updated
				@Override
				public void caretUpdate(CaretEvent carEv) {
					int strLen = searchEventName.getText().length();//get current string length
					if(strLen<25){//check if length is less than 25
						btnSearch.setEnabled(true);//Enable button if previously disabled
						//Set label text to show remaining chars
						lblsearchEventName.setText("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Event Name ("+strLen+"/24)</h4></html>");
					}else{
						searchEventName.setEditable(false);//disable editing if string too long
						btnSearch.setEnabled(false);//disable button
						//check for backspace to allow user to shorten name accordingly
						searchEventName.addKeyListener(new KeyListener(){
							@Override
							public void keyPressed(KeyEvent typed) {
								if(typed.getKeyCode()==KeyEvent.VK_BACK_SPACE){
									searchEventName.setEditable(true);
								}
							}
							//unused events that must be overridden
							@Override
							public void keyReleased(KeyEvent typed){}
							@Override
							public void keyTyped(KeyEvent typed){}

						});
						//Alert user that entered text is too long
						lblsearchEventName.setText("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Event Name ("+strLen+"/24) <span style='color: red'>Name too long</span></h4></html>");
					}
				}
			});
			searchLocation.addCaretListener(new CaretListener(){
				//overrides method that fires when caret position is updated
				@Override
				public void caretUpdate(CaretEvent carEv) {
					int strLen = searchLocation.getText().length();//get current string length
					if(strLen<25){//check if length is less than 25
						btnSearch.setEnabled(true);//Enable button if previously disabled
						//Set label text to show remaining chars
						lblsearchLocation.setText("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Location ("+strLen+"/24)</h4></html>");
					}else{
						searchLocation.setEditable(false);//disable editing if string too long
						btnSearch.setEnabled(false);//disable button
						//check for backspace to allow user to shorten name accordingly
						searchLocation.addKeyListener(new KeyListener(){
							@Override
							public void keyPressed(KeyEvent typed) {
								if(typed.getKeyCode()==KeyEvent.VK_BACK_SPACE){
									searchLocation.setEditable(true);
								}
							}
							//unused events that must be overridden
							@Override
							public void keyReleased(KeyEvent typed){}
							@Override
							public void keyTyped(KeyEvent typed){}

						});
						//Alert user that entered text is too long
						lblsearchLocation.setText("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Location ("+strLen+"/24) <span style='color: red'>Name too long</span></h4></html>");
					}
				}
			});
			//prevent incorrect date entering
			//update max days in February if year is or isn't a leap year
			searchYear.addChangeListener(new ChangeListener(){
				public void stateChanged(ChangeEvent e){
					String curMonth = (String) searchMonth.getValue();
					int curYear = (int) searchYear.getValue();
					int maxDays=0;
					if (curMonth == "March" || curMonth == "May" || curMonth == "September" || curMonth == "November")
						maxDays = 30;
					else if (curMonth == "February")
					{ boolean isLeapYear = (curYear % 4 == 0);//leap year check
						if (isLeapYear)
							maxDays = 29;
						else
							maxDays = 28;
					}else
						maxDays = 31;
					searchDays.setMaximum(maxDays);
					//if previously selected date is greater than the new maximum then set it to the new maximum
					if((int)searchDay.getValue() > (int)searchDays.getMaximum()){
						searchDays.setValue(maxDays);
					}
				}
			});
			//Set max days in day model depending on month and year
			searchMonth.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					String curMonth = (String) searchMonth.getValue();
					int curYear = (int) searchYear.getValue();
					int maxDays=0;
					if (curMonth == "March" || curMonth == "May" || curMonth == "September" || curMonth == "November")
						maxDays = 30;
					else if (curMonth == "February")
					{ boolean isLeapYear = (curYear % 4 == 0);//leap year check
					if (isLeapYear)
						maxDays = 29;
					else
						maxDays = 28;
					}
					else
						maxDays = 31;
					searchDays.setMaximum(maxDays);
					//if previously selected date is greater than the new maximum then set it to the new maximum
					if((int)searchDay.getValue() > (int)searchDays.getMaximum()){
						searchDays.setValue(maxDays);
					}
				}
			});
		//creation of buttons and labels 
		JPanel Details = new JPanel();
		tabbedPane.addTab("<html><h4 style='margin-right:5px;margin-left:5px;margin-top:1px;margin-bottom:0px'>Details</h4></html>",Details);
		Details.setLayout(null);
		
		JLabel detailsEventName = new JLabel("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Event Name</h4></html>");
		detailsEventName.setBounds(10, 11, 377, 21);
		Details.add(detailsEventName);
		
		JLabel lbldetailsDate = new JLabel("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Date</h4></html>");
		lbldetailsDate.setBounds(10, 64, 53, 21);
		Details.add(lbldetailsDate);
		
		JLabel lbldetailsStartTime = new JLabel("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px;text-decoration:underline;'>Start Time</h4></html>");
		lbldetailsStartTime.setBounds(10, 114, 84, 21);
		Details.add(lbldetailsStartTime);
		
		JLabel lbldetailsEndTime = new JLabel("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px;text-decoration:underline;'>End Time</h4></html>");
		lbldetailsEndTime.setBounds(167, 114, 84, 21);
		Details.add(lbldetailsEndTime);
		
		JLabel lbldetailsType = new JLabel("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Type</h4></html>");
		lbldetailsType.setBounds(10, 177, 53, 21);
		Details.add(lbldetailsType);
		
		JLabel lbldetailsDescription = new JLabel("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Description</h4></html>");
		lbldetailsDescription.setBounds(10, 231, 377, 21);
		Details.add(lbldetailsDescription);
		
		JLabel lbldetailsLocation = new JLabel("<html><h4 style='margin-right:5px;margin-top:1px;margin-bottom:0px'>Location</h4></html>");
		lbldetailsLocation.setBounds(177, 177, 210, 21);
		Details.add(lbldetailsLocation);
		
		JButton btndetailsMap = new JButton("Map");
		btndetailsMap.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(detailsLocation.getText().length()>0){
					if(Desktop.isDesktopSupported())
					{
					  try {
						Desktop.getDesktop().browse(new URI("https://www.google.com/maps?q="+getFormattedText(detailsLocation.getText())));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
				}
			}
		});
		btndetailsMap.setBounds(334, 205, 53, 25);
		Details.add(btndetailsMap);
		
		
		lbldetailsName = new JLabel("");
		lbldetailsName.setBounds(10, 39, 377, 21);
		Details.add(lbldetailsName);
		
		detailsDate = new JLabel("");
		detailsDate.setBounds(10, 82, 377, 21);
		Details.add(detailsDate);
		
		detailsStart = new JLabel("");
		detailsStart.setBounds(10, 145, 84, 21);
		Details.add(detailsStart);
		
		detailsEnd = new JLabel("");
		detailsEnd.setBounds(167, 146, 84, 21);
		Details.add(detailsEnd);
		
		detailsType = new JLabel("");
		detailsType.setBounds(10, 209, 148, 21);
		Details.add(detailsType);
		
		detailsLocation = new JLabel("");
		detailsLocation.setBounds(176, 209, 148, 21);
		Details.add(detailsLocation);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(10, 263, 377, 228);
		Details.add(scrollPane_2);
		
		detailsDescription = new JEditorPane();
		scrollPane_2.setViewportView(detailsDescription);
		detailsDescription.setEditable(false);
		
      //Function definition of delete button
		JButton btnDeleteEvent = new JButton("Delete Event");
		btnDeleteEvent.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Database.deleteEvent(evID.getText());
				currentlyShown.clear();
				if(rdbtnSearchByName.isSelected()){
					currentlyShown = Database.getByName(searchEventName.getText());
				}else if(rdbtnSearchByDate.isSelected()){
					int evMonth=0;
					for(int i = 0; i < months.length; ++i){
						if(Months.getValue()==months[i]) evMonth = i+1;
					}
					currentlyShown = Database.getByDate(Integer.toString((int)searchYear.getValue()),Integer.toString(evMonth), Integer.toString((int)searchDay.getValue()));
				}else if(rdbtnSearchByLocation.isSelected()){
					currentlyShown = Database.getByLocation(searchLocation.getText());
				}else if(rdbtnSearchByType.isSelected()){
					int evType=-1;
					for(int i = 0; i < eventTypes.length; ++i){
						if(type.getValue()==eventTypes[i]) evType = i;
					}
					currentlyShown = Database.getAllByType(evType);
				}else{
					currentlyShown = Database.getAllEvents();
				}
				printEvent();
				lbldetailsName.setText("");
				detailsLocation.setText("");
				detailsType.setText("");
				detailsEnd.setText("");
				detailsStart.setText("");
				detailsDescription.setText("");
				detailsDate.setText("");
				evID.setText("");
			}
		});
		btnDeleteEvent.setBounds(10, 500, 109, 23);
		Details.add(btnDeleteEvent);
		
		evID = new JLabel("");
		evID.setBounds(261, 114, 0, 21);
		Details.add(evID);
	}
	//Format text to fit  search parameters
	private String getFormattedText(String text){
		char[] arr = text.toCharArray();
		for(int i = 0; i< arr.length; ++i){
			if(arr[i]==' '){
				arr[i] = '+';
			}
		}
		return new String(arr);
	}
	//concatenates the hour and minutes for use with db
	private int getTime(int hour, int minutes){
		return hour*100+minutes;
	}
   //Returns time formatted for presentation in UI
	private String getFormattedTime(int i){
		String temp = "";
		if(i/1000>0){
			temp = Integer.toString(i/100);
			i%=100;
			temp += ":";
			temp += Integer.toString(i);
		}else if(i/100>0){
			temp = "0";
			temp += Integer.toString(i/10);
			i%=10;
			temp += ":";
			temp += Integer.toString(i);
		}else if(i/10>0){
			temp = "00:";
			temp += Integer.toString(i);
		}else if(i>=0){
			temp = "00:0";
			temp += Integer.toString(i);
		}
		
		
		return temp;		
	}
   //Prints selected event
	public void printEvent(){
		distanceFromTop = 10;
		panel.removeAll();

		panel.revalidate();
		panel.repaint();
		for(int i = 0; i< currentlyShown.size();i++){
			Event ayy = currentlyShown.get(i);
			JButton temp = new JButton();
			temp.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent arg0) {
					tabbedPane.setSelectedIndex(2);
					lbldetailsName.setText(ayy.getName());
					detailsLocation.setText(ayy.getLocation());
					detailsType.setText(Database.getNameOfType(ayy.getType()));
					detailsEnd.setText(getFormattedTime(ayy.getEnd()));
					detailsStart.setText(getFormattedTime(ayy.getStart()));
					detailsDescription.setText(ayy.getDescription());
					detailsDate.setText(ayy.getDate()+"-"+ayy.getMonth()+"-"+ayy.getYear());
					evID.setText(Integer.toString(ayy.eventId));
				}
			});
			temp.setText("<html><h4 style='margin-right:5px;margin-left:5px;margin-top:1px;margin-bottom:0px;'>"+ayy.getDate()+"-"+ayy.getMonth()+"-"+ayy.getYear()+"    "+ayy.getName()+"</h4></html>");
			temp.setBounds(10, distanceFromTop, 413, height);
			temp.setBackground(new Color(255,255,255));
			temp.setBorderPainted(false);
			temp.setFocusPainted(false);
			panel.add(temp);
			panel.revalidate();
			panel.repaint();
			distanceFromTop += height+5;
		}
	}
}
