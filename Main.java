import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

public class Main {
	public static String dateCode;
	public static void main(String[] args) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDateTime now = LocalDateTime.now();
		dateCode = dtf.format(now);
		
		//Swing Components
		JFrame mainFrame = new JFrame("Main Frame");
		JPanel mainPanel = new JPanel();
		JButton makeAppointmentButton = new JButton("Make Appointment");
		JButton viewScheduleButton = new JButton ("View Schedule");
		
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		mainFrame.setSize(400,200);
		mainFrame.add(mainPanel);
		
		mainPanel.setLayout(new FlowLayout());
		
		mainPanel.add(makeAppointmentButton);
		mainPanel.add(viewScheduleButton);
		
		makeAppointmentButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String code = getNextAppointment();
				String message;
				
				if(code.contentEquals("Late")) {	//The user is accessing the system after hours. Tell them to check back the following day
					message = "I'm sorry, however you seem to be accessing the appointment booking system after the store has closed. \nPlease check back in the morning to book an appointment for tomorrow!";
				}
				
				else {
					message = "Your appointment code is as follows: \n" + code;
				}
				
				JOptionPane.showMessageDialog(null, message);
			}
		});

		viewScheduleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printSched();
				JOptionPane.showMessageDialog(null, "Schedule printed in console");
			}
		});
		
		
	}
	
	/**
	 * The daily schedule will have codes for each time slot from 9-5.
	 * The code is as follows: Date of the form YYYY/MM/DD and then a 2 digit number based on what time slot it is, so 9-9:30 is slot 01, 4:30-5 is slot 16
	 */
	public static void printSched() {
		int curTime = 900;
		while(curTime < 1700) {
			System.out.println(dateCode + "-" + curTime);
			if(curTime % 100 != 0)		//In the event that this appointment being printed was the half hour appointment, add an extra 40 to get to the next hour, adding 70 to 30 to get to 100
				curTime += 40;
			curTime += 30;				//Add 30 either way
		}
		
	}
	
	//For this prototype just choose the closest appointment
	public static String getNextAppointment() {
		DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH");  
		DateTimeFormatter minFormatter = DateTimeFormatter.ofPattern("mm");  
		LocalDateTime current = LocalDateTime.now();
		int curHour = Integer.parseInt(hourFormatter.format(current));
		int curMin = Integer.parseInt(minFormatter.format(current));
		
		String timeCode = "";
		//Check if it's past closing time, if so you need to offer the next day because the store is closed
		if(curHour >= 17) {		//5:00 pm or later
			//For now just tell the user to check back tomorrow to book an appointment
			return "Late";
		}
		
		else if(curHour < 9) {	//Before 9 am
			timeCode = "900";
		}
		
		else {					//During work hours
			if(curMin < 30) {	//Return the appointment on the half hour
				timeCode += "" + curHour;
				timeCode += "30";
			}
			else {				//Return the appointment on the next hour
				timeCode += "" + (curHour+1);
				timeCode += "00";
			}
		}
		
		return dateCode + "-" + timeCode;

	}
}
