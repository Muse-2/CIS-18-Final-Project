import java.awt .*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

class PurchaseData { // receipt data
	private String ItemName;
	private int ItemAmount;
	private int ItemMonth, ItemDay, ItemHour;
	private String ItemMinute;
	static private String receipt = ""; // start empty
	
	PurchaseData(String n, String mi, int a, int mo, int d, int h) {
		ItemName = n;
		ItemAmount = a;
		ItemMinute = mi;
		ItemHour = h;
		ItemDay = d;
		ItemMonth = mo;
		
		ReceiptFormat();
	}
	private void ReceiptFormat() {
		String temp;
		// format the information nicely
		temp = ItemName + " | " + ItemAmount + " --- " + ItemMonth + "/" + ItemDay + " - " + ItemHour + ":" + ItemMinute + "\n";
		// store new formatted string in the receipt
		receipt += temp;
	}
	static public String returnReceipt() {
		return receipt;
	}
	
	static public void createFile() {
		try {	// this is apparently necessary to do to make files work
			File receiptFile = new File("receipt.txt");
			receiptFile.delete(); // delete the old file if it exists
			receiptFile.createNewFile(); // create the new file, ready for information
			
			// opens the file, writes to it, closes the file
			FileWriter receiptWrite = new FileWriter("receipt.txt");
			receiptWrite.write(receipt);
			receiptWrite.close();
		} catch(IOException e) {
			System.out.println("My god, What Have I Done?"); // error message
			e.printStackTrace();
		}
		
	}
}

public class DeliveryService implements ActionListener {
	//frames for the various windows
	JFrame mainmenu;		// main menu
	JFrame menuChoice;		// menu to choose item
	JFrame orderSpec; 		// menu to choose variables
	JFrame orderConfirmed;	// dialog box to confirm successful order
	JFrame orderSummary; 	// display of the current receipt
	
	// define main menu buttons and label
	
	JButton breakfast;
	JButton lunch;
	JButton dinner;
	JButton order;
	JButton exit;
	JLabel titleText;

	
	
	//order confirmation menu (orderSpec)
	
	JComboBox<Integer> month;
	JComboBox<Integer> day;
	JComboBox<Integer> hour;
	JComboBox<String> minute; // string for proper formatting, no mathematical checks means doesn't matter
	
	JLabel finish = new JLabel(""); // label for the orderSpec frame, to inform user of bad inputs
	JTextField amount; // text field for the amount of an order
	
	String name = "How Did I Get Here?"; // stored name of the order for the receipt
	
	//order confirmed dialog (orderConfirmed)
	JButton done;
	JLabel doneLabel;
	
	//
	PurchaseData addNew;
	
	DeliveryService() {
		// Create a new JFrame container.
		mainmenu = new JFrame("Jenny's Diner Delivery Program");
		orderConfirmed = new JFrame("Success");
		
		
		// Specify FlowLayout for the layout manager.
		mainmenu.setLayout(new FlowLayout());
		orderConfirmed.setLayout(new FlowLayout());
		
		//Give the frame an initial size.
		mainmenu.setSize(200,300);
		orderConfirmed.setSize(500,100);
		
		
		//Terminate the program when the user closes the application.
		mainmenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		orderConfirmed.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		//Create buttons.
		
		breakfast = new JButton("Breakfast Menu");
		lunch = new JButton("Lunch Menu");
		dinner = new JButton("Dinner Menu");
		exit = new JButton("Exit Program");
		order = new JButton("Order Summary");
		done = new JButton("Okay");
		
		//Add action listener for buttons.

		breakfast.addActionListener(this);
		lunch.addActionListener(this);
		dinner.addActionListener(this);
		exit.addActionListener(this);
		order.addActionListener(this);
		done.addActionListener(this);
		
		//Create the labels.
		titleText = new JLabel("Jenny's Diner - Open 9-8!");
		doneLabel = new JLabel("Success! Your order has been stored and is available in the Order Summary.");
		
		//Add the components to the content pane.
		
		mainmenu.add(titleText);
		mainmenu.add(breakfast);
		mainmenu.add(lunch);
		mainmenu.add(dinner);
		mainmenu.add(order);
		mainmenu.add(exit);
		
		orderConfirmed.add(doneLabel);
		orderConfirmed.add(done);

		
		//Display the frame.
		mainmenu.setVisible(true);
		orderConfirmed.setVisible(false); // only one of these exists in the program, just hidden or shown as needed
	}
		
	//perform an action depending on the button pressed.
	public void actionPerformed(ActionEvent ae) {
		int m, d, h;
		String temp;// variables for conversion of
		int a;		// the meal amount
		
		
		switch(ae.getActionCommand()) {
			case "Exit Program":
			System.exit(0);
			break;
			case "Breakfast Menu":
			menuCreate(0);	//the same method is called with a different initializing number, to change the menu being created
			break;
			case "Lunch Menu":
			menuCreate(1);
			break;
			case "Dinner Menu":
			menuCreate(2);
			break;
			case "Return to Main Menu":
			menuChoice.dispose();
			mainmenu.setVisible(true); // Bring the Main Menu back
			break;
			case "Breakfast Sandwich":
			case "Caesar Salad":
			case "Fish & Chips":
			case "Bacon and Eggs":
			case "Chicken Strips":
			case "Steak & Eggs":
			case "Pancakes w/ Berries":
			case "Cheese Sliders":
			case "Cheeseburger w/ Fries":
			menuChoice.setVisible(false);
			name = ae.getActionCommand();
			orderSpecifications(ae.getActionCommand());
			break;
			case "Confirm Order":
			m = (Integer) month.getSelectedItem();	// i have no idea why this works i saw it in a stackoverflow thread
			d = (Integer) day.getSelectedItem();
			h = (Integer) hour.getSelectedItem();
			temp = amount.getText();
			
			if(m == 2 && d > 28) {	// entire else-if is checking input for month/day and the amount
				finish.setText("This month only has 28 days. Please check date.");
			}
			else if((m == 4 || m == 6 || m == 9 || m == 11) && d == 31) {
				finish.setText("This month only has 30 days. Please check date.");
			}
			else if(temp.matches("[0-9]*[^0-9]+[0-9]*")){ // regex is pain
				finish.setText("You must enter a valid amount (Whole number greater than 0.)");
			}
			else if(temp.matches("^0+")) {
				finish.setText("You must enter a valid amount (Whole number greater than 0.)");
			}
			else {
				a = Integer.parseInt(temp);
				finish.setText("");
				addNew = new PurchaseData(name, minute.getSelectedItem().toString(), a, m, d, h);
				orderSpec.dispose();
				menuChoice.setVisible(true);
				orderConfirmed.setVisible(true);
				
			}
			
			break;
			case "Order Summary":
			orderSummary();
			break;
			case "Okay":
			orderConfirmed.setVisible(false);
			break;
			case "Cancel":
			orderSpec.dispose();
			menuChoice.setVisible(true);
			break;
			case "Back":
			orderSummary.dispose();
			break;
			case "Download":
			PurchaseData.createFile();
			break;
		}
	}
	
	public void menuCreate(int menuType) {

		JButton back;	// back button
		JButton option[] = new JButton[3];	// 3 buttons for 3 items
		String items[][] = {	// items for the menus
								{"Breakfast Sandwich", "Bacon and Eggs", "Pancakes w/ Berries"},
								{"Caesar Salad", "Chicken Strips", "Cheese Sliders"},
								{"Fish & Chips", "Steak & Eggs", "Cheeseburger w/ Fries"}
							 };
		String menuName[] = {"Breakfast Menu", "Lunch Menu", "Dinner Menu"};	// the name of the menu shown on the window

		JLabel optionCost[] = new JLabel[3]; // 3 labels for the price of the items
		String optionCosts[] = {"2.99", "3.99", "4.99"};
		mainmenu.setVisible(false); // hide the Main Menu when in an ordering Menu
		back = new JButton("Return to Main Menu");
		back.addActionListener(this);
		menuChoice = new JFrame (menuName[menuType]); //change menu name depending on chosen menu
		menuChoice.setLayout(new FlowLayout());
		menuChoice.setSize(250,300);
		menuChoice.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exits instead of hides since main menu is hidden


		for(int x = 0; x < 3; x++) { // set the buttons and labels and add them to the menu
			
			option[x] = new JButton(items[menuType][x]);
			optionCost[x] = new JLabel(optionCosts[x]);
			option[x].addActionListener(this);
			menuChoice.add(option[x]);
			menuChoice.add(optionCost[x]);
				
		}
		menuChoice.add(back);
		menuChoice.setVisible(true);
	}

	public void orderSpecifications(String orderName) {
	
	orderSpec = new JFrame(orderName);
	orderSpec.setLayout(new FlowLayout());
	orderSpec.setSize(420,180);
	orderSpec.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // because previous menu is hidden, close the program to prevent running in the background
	JLabel ask = new JLabel("How many meals do you want?");
	amount = new JTextField("0", 3);
	JLabel date = new JLabel("Please enter the date and time you want the meals to be ready.");
	JLabel slash = new JLabel("/");
	JLabel colon = new JLabel(":");
	
	JButton back = new JButton("Cancel");
	back.addActionListener(this);
	
	Integer months[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
	Integer hours[] = {9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19};
	String minutes[] = {"00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"};
	Integer days[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 
					 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 
					 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31};
	
	month = new JComboBox<>(months); // comboboxes prevent random user input that i'd need to verify
	month.setSelectedIndex(0);	// automatically selects the first item as default
	day = new JComboBox<>(days);
	day.setSelectedIndex(0);
	hour = new JComboBox<>(hours);
	hour.setSelectedIndex(0);
	minute = new JComboBox<>(minutes);
	minute.setSelectedIndex(0);
	
	JButton confirm = new JButton("Confirm Order");
	confirm.addActionListener(this);	// forgot to include this line for 2 hours and was very confused
	
	orderSpec.add(ask);
	orderSpec.add(amount);
	orderSpec.add(date);
	orderSpec.add(month);
	orderSpec.add(slash);
	orderSpec.add(day);
	orderSpec.add(hour);
	orderSpec.add(colon);
	orderSpec.add(minute);
	orderSpec.add(confirm);
	orderSpec.add(finish);
	orderSpec.add(back);
	
	orderSpec.setVisible(true);
	
	}
	
	public void orderSummary() {
		orderSummary = new JFrame("Order Summary");
		
		orderSummary.setLayout(new FlowLayout());
		orderSummary.setSize(300,420);
		orderSummary.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); // we dont want to end the program if this is open
		
		JLabel title = new JLabel("Current Receipt:");
		JTextArea box = new JTextArea(20, 20); // box to hold the receipt
		box.setText(PurchaseData.returnReceipt()); // gets the current contents of the receipt and puts it in the box
		box.setEditable(false); // no reason to edit the text in the box
		
		JScrollPane scroll = new JScrollPane(box); // if need-be, add a scrollbar to the box so the user can see all entries
		
		JButton back = new JButton("Back");
		back.addActionListener(this);
		
		JButton download = new JButton("Download");
		download.addActionListener(this);
		
		orderSummary.add(scroll); // only add the scrollpane, the box is included with it
		orderSummary.add(back);
		orderSummary.add(download);
		orderSummary.setVisible(true);
	}
	
	public static void main(String args[]) {
		//Create the frame on the event dispatching thread.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new DeliveryService();
			}
		});
	}
}
