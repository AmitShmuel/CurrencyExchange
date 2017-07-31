package com.abelski.finalproject;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTable;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.text.DecimalFormat;
import java.util.Vector;
import javax.swing.ImageIcon;

import java.awt.BorderLayout;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.SystemColor;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * The "view" part of the mvc pattern.
 * this class is responsible for showing the GUI of the application
 * and handle inputs from the user.
 * @author amit
 */
public class WindowGUI {

	private Refresher refresher;
	private JFrame appFrame, tableFrame;
	private JTable table;
	private Vector<Vector<String>> tableData;
	private JComboBox<String> fromComboBox, toComboBox;
	private JTextField resultTextField, amountTextField, txtSearch, txtSearch2;

	/**
	 * Create the application.
	 */
	public WindowGUI(Refresher r) {
		refresher = r;
		initialize();
		appFrame.setLocationRelativeTo(null);
		appFrame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		appFrame = new JFrame();
		appFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 13));
		appFrame.setFont(new Font("Dialog", Font.PLAIN, 12));
		appFrame.setTitle("Currency Exchange App");
		appFrame.setBounds(100, 100, 503, 308);
		appFrame.getContentPane().setLayout(null);
		appFrame.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent windowEvent) {
		    		MyLogger.getInstance().logger.info("Closing application..\n");
		            System.exit(0);
		    }
		});
		
		JLabel imgLabel = new JLabel("");
		ImageIcon img = new ImageIcon(getClass().getResource("/dollar-icon.png"));
		imgLabel.setIcon(img);
		imgLabel.setBounds(28, 11, 170, 247);
		appFrame.getContentPane().add(imgLabel);
		
		fromComboBox = new JComboBox<String>();
		fromComboBox.setBounds(208, 156, 93, 20);
		appFrame.getContentPane().add(fromComboBox);
		
		JLabel lblConvertFrom = new JLabel("Convert from :");
		lblConvertFrom.setBounds(208, 131, 93, 14);
		appFrame.getContentPane().add(lblConvertFrom);
		
		toComboBox = new JComboBox<String>();
		toComboBox.setBounds(377, 156, 93, 20);
		appFrame.getContentPane().add(toComboBox);
		
		JLabel lblConvertTo = new JLabel("Convert to :");
		lblConvertTo.setBounds(377, 131, 81, 14);
		appFrame.getContentPane().add(lblConvertTo);
		
		JLabel lblResult = new JLabel("Result:");
		lblResult.setBounds(329, 227, 45, 29);
		appFrame.getContentPane().add(lblResult);
		
		resultTextField = new JTextField();
		resultTextField.setForeground(new Color(0, 0, 0));
		resultTextField.setBackground(SystemColor.window);
		resultTextField.setFont(new Font("Tahoma", Font.BOLD, 11));
		resultTextField.setEnabled(false);
		resultTextField.setBounds(377, 224, 93, 34);
		appFrame.getContentPane().add(resultTextField);
		resultTextField.setColumns(10);
		
		amountTextField = new JTextField();
		amountTextField.setBackground(SystemColor.controlHighlight);
		amountTextField.setBounds(259, 100, 81, 20);
		appFrame.getContentPane().add(amountTextField);
		amountTextField.setColumns(10);
		
		JLabel lblAmount = new JLabel("Amount:");
		lblAmount.setBounds(208, 100, 57, 20);
		appFrame.getContentPane().add(lblAmount);
		
		JButton convertButton = new JButton("Convert!");
		convertButton.addActionListener(e -> {
				double result;
				try{
					result = convert(amountTextField.getText(), fromComboBox.getSelectedIndex(), 
																toComboBox.getSelectedIndex());
				}
				catch(NumberFormatException | NullPointerException ex) {
					JOptionPane.showMessageDialog(null,"there is a problem with your input","Error", 0);
					return;
				}					  				
				resultTextField.setText(new DecimalFormat("#.####").format(result));
				MyLogger.getInstance().logger.info("Conversion has been made");
		});
		convertButton.setBounds(208, 224, 111, 29);
		appFrame.getContentPane().add(convertButton);
		
		JButton addCurrencyButton = new JButton("Add Currency");
		addCurrencyButton.addActionListener(e -> {addCurrency();});
		addCurrencyButton.setBounds(359, 11, 111, 23);
		appFrame.getContentPane().add(addCurrencyButton);
		
		JButton refreshButton = new JButton("Refresh");
		refreshButton.addActionListener(e -> {
				refresher.setRefreshFlag(true);
		});
		refreshButton.setBounds(208, 45, 111, 23);
		appFrame.getContentPane().add(refreshButton);
		
		JButton btnShowTable = new JButton("Show table");
		btnShowTable.addActionListener(e -> {tableFrame.setVisible(true);});
		btnShowTable.setBounds(359, 45, 111, 23);
		appFrame.getContentPane().add(btnShowTable);
		
		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(e -> {reset();});
		btnReset.setBounds(208, 11, 111, 23);
		appFrame.getContentPane().add(btnReset);
		
		JLabel lblSearch = new JLabel("search:");
		lblSearch.setBounds(208, 187, 45, 20);
		appFrame.getContentPane().add(lblSearch);
		
		JLabel lblSearch2 = new JLabel("search:");
		lblSearch2.setBounds(377, 187, 45, 20);
		appFrame.getContentPane().add(lblSearch2);
		
		txtSearch = new JTextField();
		txtSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent k) {
				if(txtSearch.getText().length() > 2)
					k.consume();
			}
		});
		txtSearch.addActionListener(e -> {
				if(!txtSearch.getText().matches("[A-Z]{3}"))
					JOptionPane.showMessageDialog(null,"Please enter 3 capital letters","Error",0);
				else
					search(txtSearch.getText(), 1);
		});
		txtSearch.setBackground(SystemColor.controlHighlight);
		txtSearch.setBounds(256, 187, 45, 20);
		appFrame.getContentPane().add(txtSearch);
		txtSearch.setColumns(10);
		
		txtSearch2 = new JTextField();
		txtSearch2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(txtSearch2.getText().length() > 2)
					e.consume();
			}
		});
		txtSearch2.addActionListener(e -> {
			if(!txtSearch2.getText().matches("[A-Z]{3}"))
				JOptionPane.showMessageDialog(null,"Please enter 3 capital letters","Error",0);
			else
				search(txtSearch2.getText(), 1);
		});
		txtSearch2.setBackground(SystemColor.controlHighlight);
		txtSearch2.setColumns(10);
		txtSearch2.setBounds(425, 187, 45, 20);
		appFrame.getContentPane().add(txtSearch2);
		
		try {
			Thread.sleep(500);
			createCurrencyTable(); // letting the controller parse the file before creating table
		} catch (InterruptedException e1) {e1.printStackTrace();}
	}
	
	/**
	 * Creating the currency table
	 */
	private void createCurrencyTable() {
		
		if((tableData = refresher.getTableData()) != null) //check if the table data is ready at the controller 
		{
			MyLogger.getInstance().logger.info("Creating Currency table");
			
			tableFrame = new JFrame();
			tableFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			tableFrame.setTitle("Currency Table - last updated: " +refresher.getCurrencyDate());
			//adding the columns of the table
			Vector<String> coloumns = new Vector<>(); 
			coloumns.add("Name"); coloumns.add("Unit"); coloumns.add("Currency Code"); 
			coloumns.add("Country"); coloumns.add("Rate"); coloumns.add("Change");
			//creating the table
			table = new JTable(tableData , coloumns){ 
				
				private static final long serialVersionUID = -8543155634392823341L;

				public boolean isCellEditable(int data, int columns) {return false;}
			};
			//formatting the table
			table.setRowHeight(25);
			table.setPreferredScrollableViewportSize(new Dimension(500, 50));
			table.setFillsViewportHeight(true);
			table.getTableHeader().setReorderingAllowed(false);
			JScrollPane scrollPane = new JScrollPane(table);
			tableFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);
			tableFrame.setSize(600, 350);
			tableFrame.setVisible(true);
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
			for(int i = 0; i < table.getColumnCount(); i++)
				table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
			//adding data to combo boxes
			fromComboBox.addItem("ILS");
			toComboBox.addItem("ILS");
			for(Vector<String> vec : tableData)
			{
				fromComboBox.addItem(vec.elementAt(2));
				toComboBox.addItem(vec.elementAt(2));
			}
		}
	}
	
	/**
	 * Converting the currencies
	 * @throws NumberFormatException
	 * @throws NullPointerException
	 */
	private double convert(String amount, int fromIndex, int toIndex) throws NumberFormatException,
																			NullPointerException {
		double dAmount, fromRate = 0, toRate = 0;
		int fromUnit = 0, toUnit = 0;
		
		dAmount = Double.parseDouble(amount);
		if(dAmount < 0 || amount.equals("-0"))
			throw new NumberFormatException();
		
		if(fromIndex != 0)//not ILS (ILS isn't in the table)
		{
			fromRate = Double.parseDouble((String)table.getModel().getValueAt(fromIndex-1,4));
			fromUnit = Integer.parseInt((String)table.getModel().getValueAt(fromIndex-1,1));
		}
			
		if(toIndex != 0)//not ILS (ILS isn't in the table)
		{
			toRate = Double.parseDouble((String)table.getModel().getValueAt(toIndex-1,4));
			toUnit = Integer.parseInt((String)table.getModel().getValueAt(toIndex-1,1));
		}
		
		if(fromIndex == toIndex)//same currencies, return the amount
			return dAmount;

		else if(fromIndex == 0)// from ILS
			return dAmount/(toRate/toUnit);

		else if(toIndex == 0)// to ILS
			return dAmount*(fromRate/fromUnit);

		else //other currencies
			return (dAmount*(fromRate/fromUnit))/(toRate/toUnit);
	}
	
	/**
	 * Updating the currency table
	 */
	public void update() {
		
		MyLogger.getInstance().logger.info("Updating Currency table");
		//if the controller didn't have the table data ready when the GUI was initialized,
		//we build the table again when update is made.
		if(table == null)
			createCurrencyTable();
		
		tableData = refresher.getTableData(); //updating the table data from the controller
		if(tableData != null) //updating values on the table
			for(int i = 0; i < tableData.size(); i++)
				for(int j = 0; j < tableData.get(i).size(); j++)
					table.setValueAt(tableData.get(i).get(j), i, j);
	}
	
	/**
	 * Adding a new currency to the table.
	 */
	private void addCurrency() {
		
		Vector<String> row = new Vector<>();
		//checking inputs with "JOptionPane", when the user presses cancel - method finishes without adding.
		if(checkInput("Enter currency name: ", "String", row) == false) return;		
		if(checkInput("Enter currency unit: ", "Integer", row) == false) return;		
		if(checkInput("Enter currency code: ", "String", row) == false) return;		
		if(checkInput("Enter currency country: ", "String", row) == false) return;		
		if(checkInput("Enter currency rate: ", "Double", row) == false) return;		
		if(checkInput("Enter currency change: ", "Double", row) == false) return;
		
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.insertRow(model.getRowCount(), row); //inserting row
		fromComboBox.addItem(row.get(0));//inserting to combo boxes
		toComboBox.addItem(row.get(0));
		
		MyLogger.getInstance().logger.info("A new Currency has been added");
	}
	
	/**
	 * Search a currency by code, focus on the relevant table row and combo box's value
	 */
	private void search(String code, int leftOrRight) {
		
		if(code.equals("ILS"))
		{
			if(leftOrRight == 1)
				fromComboBox.setSelectedIndex(0);
			else
				toComboBox.setSelectedIndex(0);
			return;
		}
		for(int i = 0; i < table.getRowCount(); i++) //search the code in the table
			if(table.getModel().getValueAt(i, 2).equals(code))
			{
				table.setRowSelectionInterval(i,i); //selecting the row
				if(leftOrRight == 1)
					fromComboBox.setSelectedIndex(i+1); //selecting value on combo box
				else
					toComboBox.setSelectedIndex(i+1); //selecting value on combo box
				return;
			}
		JOptionPane.showMessageDialog(null, "There's not such coin code", "Error", 0);
	}
	
	/**
	 * reseting all values in the application.
	 */
	private void reset() {
		
		MyLogger.getInstance().logger.info("Reset has been made");
		
		txtSearch.setText(null);
		txtSearch2.setText(null);
		resultTextField.setText(null);
		amountTextField.setText(null);
		fromComboBox.setSelectedIndex(0);
		toComboBox.setSelectedIndex(0);
		table.removeRowSelectionInterval(0, table.getRowCount()-1);
	}
	
	/**
	 * Checking inputs
	 */
	private boolean checkInput(String message, String type, Vector<String> vec)
	{
		String input;
		while(true)
		{
			input = JOptionPane.showInputDialog(null,message,"Add Currency", 3);
			if(input == null) //user cancels
				return false;
			try{
				if(input.equals("")){ //empty input
					JOptionPane.showMessageDialog(null,"Enter value","Error", 0); continue;}
				if(type.equals("Integer"))
				{
					int res = Integer.parseInt(input); //checking if integer
					if(res <= 0) //unit must be > 0
						throw new NumberFormatException(); 
				}						
				else if(type.equals("Double"))
				{
					double res = Double.parseDouble(input); //checking if double
					if(res < 0 && message.equals("Enter currency rate: ")) //rate must be positive
						throw new NumberFormatException(); 
				}
				else if(message.equals("Enter currency code: ")) {
					if(!input.matches("[A-Z]{3}")){ // regular expression to code
						JOptionPane.showMessageDialog(null,"Code must be 3 capital letters","Error",0); continue;}
					else 
					{ //check if the code entered is not already in the table
						int i;
						for(i = 0; i < table.getRowCount(); i++)
							if(table.getValueAt(i, 2).equals(input)){
								JOptionPane.showMessageDialog(null,"Duplicate codes are not allowed","Error",0); break;}
						if(i != table.getRowCount())
							continue;
					}
				}				
				vec.add(input);
				return true;
			}
			catch(NumberFormatException e) {
				if(type.equals("Integer"))
						JOptionPane.showMessageDialog(null,"Only Integers above 0","Error", 0);	
				else if(type.equals("Double"))
				{
					if(message.equals("Enter currency rate: "))
						JOptionPane.showMessageDialog(null,"Positive numbers only","Error", 0);
					else
						JOptionPane.showMessageDialog(null,"Numbers only","Error", 0);
				}	
			}
		}
	}
}