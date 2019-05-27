package as2_amir_18406484;

//Import statement to classes which has been declared in package
import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.Timer;

public class CBallMaze extends JFrame implements ActionListener
{
	//declaring JPanel 
	private JPanel jPMazePanel, jPButtonPanel, jPKeyPanel;
	//declaring JMenuBar to the top of maze panel
	private static JMenuBar jMenuBar;
	//declaring JMenu in the top of maze panel
	private JMenu jMScenario, jMEdit, jMControls, jMHelp;
	//declaring JMenu item in the top of maze panel
	private JMenuItem jMIQuit, jMIActItem, jMIPlayItem, jMIPauseItem, jMIResetItem, jMIAboutItem;
	//declaring JLabel Array in the button panel
	private JLabel jLOptionLabel, jLSquareLabel, jLDirectionLabel, jLDigitalTimer,jLCol1, jLCol2, jLSpeed;
	//declaring JTextField Array in same button panel
	private JTextField jTOptionText, jTSquareText, jTDirectionText,jTHours, jTMins, jTSecs;
	private int nTicks =0;//declaring the value of nTicks
	private Timer timerClock,autoMoveTimer,musicTimer,autoDownTimer;//declaring timer to schedule the task
	//declaring JButton Array in the button panel
	private JButton jBUpArrow, jBDownArrow, jBRightArrow, jBLeftArrow, jBBlank1, jBBlank2, jBBlank3, jBBlank4, jBBlank5; 
	private JButton jBOption1, jBOption2, jBOption3, jBExit,jBCompassImage, jBActKey, jBRunKey, jBResetKey;
	//declaring ImageIcon for compass direction
	private ImageIcon iconNorthImage, iconEastImage, iconWestImage, iconSouthImage;
	private JSlider slider;//declaring JSlider for speed of the scenario
	Clip fall; //declaring sound clip 'fall' for sound play
	private boolean checkOption = false;//declaring boolean variable

	private JLabel jLSandTile [][] = new JLabel [16][13];
	GridBagConstraints gridBC = new GridBagConstraints();
	ImageIcon mBallImage = new ImageIcon("images/sand60x60.png");
	ImageIcon mSandImage = new ImageIcon("images/sand.jpg");
	ImageIcon mGoalStone = new ImageIcon("images/sandstone.jpg");
	ImageIcon mWhiteStone = new ImageIcon("images/white32x32.jpg");
	ImageIcon mBallImg = new ImageIcon("images/gold-ball.png");	

	public static void main(String [] args)
	{
		CBallMaze gameWindow = new CBallMaze ();
		gameWindow.setSize(775,650);//size of the scenario frame
		gameWindow.setTitle("CBallMaze - Ball Maze Application");//creating title of maze scenario
		ImageIcon mGreenfootImg = new ImageIcon("images/greenfoot.png");//setting greenfoot icon image
		gameWindow.setIconImage(mGreenfootImg.getImage());//adding image icon in game window		
		gameWindow.designGUI();
		gameWindow.setJMenuBar(jMenuBar);			
		gameWindow.setVisible(true);	
	}
	//	adding sand block in horizontal line
	public void xAxis(int am , int ir)
	{
		gridBC.gridx = am;
		gridBC.gridy = ir;
		for(int i = 0; i <16 ; i++){
			gridBC.gridx = am++;
			jLSandTile[gridBC.gridx][gridBC.gridy] = new JLabel (mSandImage); 
			jPMazePanel.add(jLSandTile [gridBC.gridx][gridBC.gridy], gridBC);
		}
	}
	//	adding sand block in vertical line 
	public void yAxis(int am, int ir)
	{
		gridBC.gridx = am;
		gridBC.gridy = ir;
		for(int i = 0; i < 2; i++)
		{
			gridBC.gridy = ir++;
			jLSandTile[gridBC.gridx][gridBC.gridy]= new JLabel (mSandImage);
			jPMazePanel.add(jLSandTile [gridBC.gridx][gridBC.gridy], gridBC);
		}		
	}
	//	placing golden-ball into the goal stone when reached to it
	public boolean goalStone()
	{
		if(gridBC.gridx == 0 && gridBC.gridy == 12)
		{
			jLSandTile[gridBC.gridx][gridBC.gridy].setIcon(mBallImg);
			timerClock.stop();
			int condition = JOptionPane.showConfirmDialog(null,"Well Done! \tIf you want to restart game click ok","Completed", JOptionPane.OK_CANCEL_OPTION);
			if(condition == JOptionPane.OK_CANCEL_OPTION)
			{
				System.exit(0);	
			}
			else
			{
			restartGame();
			}
			return true;
		}
		else 
			return false;
	}
	//	creating new method called designGUI 
	private void designGUI()
	{
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Container window = getContentPane();
		setResizable(false);
		setLocationRelativeTo(null);
		window.setLayout(null);	
		//		adding menu bar in the panel 
		jMenuBar = new JMenuBar();
		jMScenario = new JMenu("Scenario");
		jMIQuit = new JMenuItem("Exit");
		jMIQuit.addActionListener(this);
		jMScenario.add(jMIQuit);
		jMenuBar.add(jMScenario);

		jMEdit = new JMenu("Edit");
		jMenuBar.add(jMEdit);

		jMControls = new JMenu("Controls");
		jMIActItem = new JMenuItem("Act");
		jMIActItem.addActionListener(this);
		jMIActItem.setIcon(new ImageIcon("images/step.png"));
		jMIPlayItem = new JMenuItem("Run");
		jMIPlayItem.addActionListener(this);
		jMIPlayItem.setIcon(new ImageIcon("images/run.png"));
		jMIPauseItem = new JMenuItem("Pause");
		jMIPauseItem.addActionListener(this);
		jMIPauseItem.setIcon(new ImageIcon("images/pause.png"));
		jMIResetItem = new JMenuItem("Reset");
		jMIResetItem.addActionListener(this);
		jMIResetItem.setIcon(new ImageIcon("images/reset.png"));
		jMControls.add(jMIActItem);
		jMControls.add(jMIPlayItem);
		jMControls.add(jMIPauseItem);
		jMControls.add(jMIResetItem);
		jMControls.addActionListener(this);
		jMenuBar.add(jMControls);

		jMHelp = new JMenu("Help");
		jMIAboutItem = new JMenuItem("About");
		jMIAboutItem.addActionListener(this);
		jMHelp.add(jMIAboutItem);
		jMenuBar.add(jMHelp);
		window.add(jMenuBar);

		jPMazePanel = new JPanel();
		jPMazePanel.setBounds(2, 3, 595, 540);
		jPMazePanel.setBackground(Color.WHITE);
		jPMazePanel.setLayout(new GridBagLayout());
		window.add(jPMazePanel);
		//coordinating the value of maze sand in panel with accurate x-axis and y-axis
		xAxis(0,0);//first x-axis horizontal line
		yAxis(1,1);
		yAxis(5,1);
		yAxis(9,1);
		xAxis(0,3);//second x-axis horizontal line
		yAxis(2,4);
		yAxis(6,4);
		yAxis(11,4);
		xAxis(0,6);//third x-axis horizontal line
		yAxis(1,7);
		yAxis(5,7);
		yAxis(12,7);
		xAxis(0,9);//fourth x-axis horizontal line
		yAxis(2,10);
		yAxis(6,10);
		xAxis(0,12);//fifth x-axis horizontal line						
		//		to add button in right bottom panel
		jPButtonPanel = new JPanel();
		jPButtonPanel.setBounds(603, 3, 170, 540);
		jPButtonPanel.setBackground(Color.LIGHT_GRAY);
		jPButtonPanel.setLayout(null);
		window.add(jPButtonPanel);
		//		adding option text label in button panel
		jLOptionLabel = new JLabel("Option: ");
		jLOptionLabel.setBounds(5, 5, 100, 20);
		jPButtonPanel.add(jLOptionLabel);
		//		adding option text field in button panel
		jTOptionText = new JTextField(5); 
		jTOptionText.setBounds(90, 5, 80, 20);
		jTOptionText.setEditable(false);
		jPButtonPanel.add(jTOptionText);
		//		adding square text label in button panel
		jLSquareLabel = new JLabel("Square: ");
		jLSquareLabel.setBounds(5, 35, 100, 20);
		jPButtonPanel.add(jLSquareLabel);
		//		adding square text field in button panel
		jTSquareText = new JTextField(5);
		jTSquareText.setBounds(90, 35, 80, 20);
		jTSquareText.setEditable(false);
		jPButtonPanel.add(jTSquareText);
		//		adding direction text label in button panel
		jLDirectionLabel = new JLabel("Direction: ");
		jLDirectionLabel.setBounds(5, 70, 100, 20);
		jPButtonPanel.add(jLDirectionLabel);
		//		adding direction text field in button panel
		jTDirectionText = new JTextField("North   ");
		jTDirectionText.setBounds(90, 70, 80, 20);
		jTDirectionText.setEditable(false);
		jPButtonPanel.add(jTDirectionText);
		//		adding digital timer text label in button panel
		jLDigitalTimer = new JLabel("DIGITAL TIMER");
		jLDigitalTimer.setBounds(30, 120, 200, 20);


		jPButtonPanel.add(jLDigitalTimer);
		//		adding small text field for hours timer
		jTHours = new JTextField(" ");
		jTHours.setBounds(10, 150, 30, 20);
		jTHours.setBackground(Color.black);//this code will make background black
		jTHours.setForeground(Color.white);//this code will create text color white
		jPButtonPanel.add(jTHours);
		//		col1 is colon used for dividing two text field i.e. hours and minutes
		jLCol1 =new JLabel(":");
		jLCol1.setBounds(55, 150, 30, 20);
		jPButtonPanel.add(jLCol1);
		//		adding small text field for minutes timer
		jTMins = new JTextField(" ");
		jTMins.setBounds(70, 150, 30, 20);
		jTMins.setBackground(Color.black);//this code will make background black
		jTMins.setForeground(Color.white);//this code will create text color white
		jPButtonPanel.add(jTMins);
		//		col2 is colon used to divide two text field i.e. minutes and seconds
		jLCol2 =new JLabel(":");
		jLCol2.setBounds(115, 150, 30, 20);
		jPButtonPanel.add(jLCol2);
		//		adding small text label for seconds timer
		jTSecs = new JTextField(" ");    
		jTSecs.setBounds(130, 150, 30, 20);
		jTSecs.setBackground(Color.black);//this code will make background black
		jTSecs.setForeground(Color.white);//this code will create text color white
		jPButtonPanel.add(jTSecs);

		timerClock  = new Timer(1000,new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				jTHours.setText(Integer.toString(nTicks / 3600));
				jTMins.setText(Integer.toString(nTicks / 60));
				jTSecs.setText(Integer.toString(nTicks % 60));
				nTicks = nTicks + 1;
			}
		});				
		
		//		this will create a blank button 
		jBBlank1 = new JButton();
		jBBlank1.setBounds(10, 210, 50, 30);
		jBBlank1.setBackground(Color.WHITE);
		jBBlank1.setEnabled(false);
		//		this button code will make ball to move in upward direction
		jBUpArrow = new JButton("^");
		jBUpArrow.setBounds(62, 212, 46, 26);
		jBUpArrow.setBorder(null);
		//		this will create a blank button 
		jBBlank2 = new JButton();
		jBBlank2.setBounds(110, 210, 50, 30);
		jBBlank2.setBackground(Color.WHITE);
		jBBlank2.setEnabled(false);
		//		adding JButton to the button Panel
		jPButtonPanel.add(jBUpArrow);
		jPButtonPanel.add(jBBlank1);
		jPButtonPanel.add(jBBlank2);
		jBUpArrow.addActionListener(this);	
		//		this button code will make ball to move in left direction
		jBLeftArrow = new JButton("<");
		jBLeftArrow.setBounds(12, 242, 46, 26);
		jBLeftArrow.setBorder(null);
		//		adding JButton to the button Panel
		jPButtonPanel.add(jBLeftArrow);
		jBLeftArrow.addActionListener(this);
		//		this will create a blank button 
		jBBlank3 = new JButton();
		jBBlank3.setBounds(60, 240, 50, 30);
		jBBlank3.setBackground(Color.WHITE);
		jBBlank3.setEnabled(false);
		jPButtonPanel.add(jBBlank3);
		//		this button code will make ball to move in right direction
		jBRightArrow = new JButton(">");
		jBRightArrow.setBounds(112, 242, 46, 26);
		jBRightArrow.setBorder(null);
		jPButtonPanel.add(jBRightArrow);
		jBRightArrow.addActionListener(this);
		//		this will create a blank button 
		jBBlank4 = new JButton();
		jBBlank4.setBounds(10, 270, 50, 30);
		jBBlank4.setBackground(Color.WHITE);
		jBBlank4.setEnabled(false);
		//		this button code will make ball to move in downward direction
		jBDownArrow = new JButton("v");
		jBDownArrow.setBounds(62, 272, 46, 26);
		jBDownArrow.setBorder(null);
		//this will create a blank button 
		jBBlank5 = new JButton();
		jBBlank5.setBounds(110, 270, 50, 30);
		jBBlank5.setBackground(Color.WHITE);
		jBBlank5.setEnabled(false);
		//adding JButton to the button Panel
		jPButtonPanel.add(jBDownArrow);
		jPButtonPanel.add(jBBlank4);
		jPButtonPanel.add(jBBlank5);
		jBDownArrow.addActionListener(this);
		//adding option1 as a JButton in button Panel
		jBOption1 = new JButton("Option 1");
		jBOption1.setBounds(5, 335, 75, 30);
		jBOption1.setBorder(null);
		jPButtonPanel.add(jBOption1);
		jBOption1.addActionListener(this);
		//adding option2 as a JButton in button Panel
		jBOption2 = new JButton("Option 2");
		jBOption2.setBounds(87, 335, 75, 30);
		jBOption2.setBorder(null);
		jPButtonPanel.add(jBOption2);
		jBOption2.addActionListener(this);
		//adding option3 as a JButton in button Panel
		jBOption3 = new JButton ("Option 3");
		jBOption3.setBounds(5, 370, 75, 30);
		//		option3.setFont(option3.getFont().deriveFont(12));
		jBOption3.setBorder(null);
		jPButtonPanel.add(jBOption3);
		jBOption3.addActionListener(this);
		//adding exit button in button Panel to exit the whole GUI window 
		jBExit = new JButton("Exit");
		jBExit.setBounds(87, 370, 75, 30);
		jBExit.setBorder(null);
		jPButtonPanel.add(jBExit);
		jBExit.addActionListener(this);
		
		//adding image icon for the navigation of the golden ball in maze scenario
		iconWestImage = new ImageIcon("images/west.jpg");
		iconEastImage = new ImageIcon("images/east.jpg");
		iconSouthImage = new ImageIcon("images/south.jpg");
		iconNorthImage = new ImageIcon("images/north.jpg");
		jBCompassImage = new JButton();
		jBCompassImage.setBounds(35, 430, 100, 100);
		jBCompassImage.setBorder(BorderFactory.createLineBorder(Color.black));
		jBCompassImage.setIcon(iconNorthImage);
		jPButtonPanel.add(jBCompassImage);
		setVisible(true);
		//		adding new Key Panel which includes act, play, pause, rest, speed and slider
		jPKeyPanel = new JPanel();
		jPKeyPanel.setBounds(2, 550, 775, 150);
		jPKeyPanel.setBackground(Color.LIGHT_GRAY);
		jPKeyPanel.setLayout(null);
		window.add(jPKeyPanel);
		//		this act JButton will make golden to move only one step in maze scenario
		jBActKey = new JButton("Act");
		jBActKey.setBounds(90, 15, 50, 20);
		jBActKey.setIcon(new ImageIcon("images/step.png"));
		jBActKey.setFont(jBActKey.getFont().deriveFont(12));
		jBActKey.setBorder(null);
		jBActKey.addActionListener(this);
		jPKeyPanel.add(jBActKey);
		//		this run JButton will make the GUI window to start the scenario
		jBRunKey = new JButton("Run");
		jBRunKey.setBounds(150, 15, 70, 20);
		jBRunKey.setIcon(new ImageIcon("images/run.png"));
		jBRunKey.setFont(jBRunKey.getFont().deriveFont(12));
		jBRunKey.setBorder(null);
		jBRunKey.addActionListener(this);
		jPKeyPanel.add(jBRunKey);
		//this reset JButton will make the maze scenario to get reset
		jBResetKey = new JButton("Reset");
		jBResetKey.setIcon(new ImageIcon("images/reset.png"));
		jBResetKey.setBounds(225, 15, 70, 20);
		jBResetKey.setFont(jBResetKey.getFont().deriveFont(12));
		jBResetKey.addActionListener(this);
		jBResetKey.setBorder(null);
		jPKeyPanel.add(jBResetKey);
		//this speed JLabel used to show speed text in key Panel 
		jLSpeed = new JLabel("Speed: ");
		jLSpeed.setBounds(490, 10, 90, 20);
		jPKeyPanel.add(jLSpeed);
		//this JSlider is used for increase and decrease of maze scenario
		slider = new JSlider(JSlider.HORIZONTAL, 0, 50, 25);
		slider.setMinorTickSpacing(10); //minor tick spacing is used to the slider
		slider.setMajorTickSpacing(10); //major tick spacing is used to the slider
		slider.setBounds(550, 10, 200, 30);
		slider.setPaintTicks(true); //paint ticks used for tick marks in the slider
		jPKeyPanel.add(slider);		

		//starting ball 
		gridBC.gridx=15;
		gridBC.gridy=0;
		jLSandTile[gridBC.gridx][gridBC.gridy].setIcon(mBallImage);

		int am = 0;
		int ir = 12;
		jLSandTile[am][ir].setIcon(mGoalStone);
	}

	private void leftBallMoves()
	{
		jBCompassImage.setIcon(iconWestImage);
		jTDirectionText.setText("West");
		jLSandTile[gridBC.gridx-1][gridBC.gridy].setIcon(mBallImage);
		jLSandTile[gridBC.gridx][gridBC.gridy].setIcon(mSandImage);
		--gridBC.gridx;
		jTSquareText.setText(gridBC.gridx + "," + gridBC.gridy);

	}
	private void rightBallMoves()
	{
		jBCompassImage.setIcon(iconEastImage);
		jTDirectionText.setText("East");
		jLSandTile[gridBC.gridx+1][gridBC.gridy].setIcon(mBallImage);
		jLSandTile[gridBC.gridx][gridBC.gridy].setIcon(mSandImage);
		++gridBC.gridx;
		jTSquareText.setText(gridBC.gridx + "," + gridBC.gridy);
	}
	private void upBallMoves()
	{
		jBCompassImage.setIcon(iconNorthImage);
		jTDirectionText.setText("North");
		jLSandTile[gridBC.gridx][gridBC.gridy-1].setIcon(mBallImage);
		jLSandTile[gridBC.gridx][gridBC.gridy].setIcon(mSandImage);
		--gridBC.gridy;
		jTSquareText.setText(gridBC.gridx + "," + gridBC.gridy);
	}
	private void downBallMoves()
	{
		jBCompassImage .setIcon(iconSouthImage);
		jTDirectionText.setText("South");
		jLSandTile[gridBC.gridx][gridBC.gridy+1].setIcon(mBallImage);
		jLSandTile[gridBC.gridx][gridBC.gridy].setIcon(mSandImage);
		gridBC.gridy++;
		jTSquareText.setText(gridBC.gridx + "," + gridBC.gridy);


	}
	private void restartGame()
	{
		jLSandTile[gridBC.gridx][gridBC.gridy].setIcon(mSandImage);
		jLSandTile[15][0].setIcon(mBallImage);
		jLSandTile[0][12].setIcon(mGoalStone);
		gridBC.gridx = 15;
		gridBC.gridy = 0;
		timerClock.stop();
		nTicks = 0;
		jTSecs.setText(Integer.toString(0));
		jTSquareText.setText(gridBC.gridx + "," + gridBC.gridy);		
	}
	private void runAutomatically()
	{
		autoMoveTimer = new Timer(500,new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if((gridBC.gridx==9 && gridBC.gridy<3) 
						|| (gridBC.gridx==6 && gridBC.gridy<6)
						|| (gridBC.gridx==5 && gridBC.gridy<9)
						|| (gridBC.gridx==2 && gridBC.gridy<12))
					downBallMoves();

				else
					leftBallMoves();
					goalStone();

			}	
		});

		autoMoveTimer.start();		
	}
	private void runAutomatic()
	{
		autoDownTimer = new Timer(700,new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if(checkOption == true)
				{
					if(jLSandTile[gridBC.gridx][gridBC.gridy+1].getIcon().equals(mSandImage))
					{
						jLSandTile[gridBC.gridx][gridBC.gridy].setIcon(mSandImage);
						jLSandTile[gridBC.gridx][gridBC.gridy+1].setIcon(mBallImage);
						gridBC.gridy++;
						jTSquareText.setText(gridBC.gridx + "," + gridBC.gridy);
						jBCompassImage.setIcon(iconSouthImage);
						jTDirectionText.setText("South");
						ballDropSound("JumpSound.wav");
					}
				}
			}	
		});	
		autoDownTimer.start();
	}
	
	public void actMoving() 
	{
			if((gridBC.gridx==9 && gridBC.gridy<3) 
					|| (gridBC.gridx==6 && gridBC.gridy<6)
					|| (gridBC.gridx==5 && gridBC.gridy<9)
					|| (gridBC.gridx==2 && gridBC.gridy<12))
				downBallMoves();

			else
				leftBallMoves();
				goalStone();		
	}
	public void ballDropSound(String music)
	{
		try
		{
			stop();
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("sounds/" + music));
			fall = AudioSystem.getClip();
			fall.open(inputStream);
			fall.start();

		}
		catch (Exception play)
		{
			play.printStackTrace();
			stop();
		}
	}
	//http://soundbible.com/1126-Water-Drop.html
	public void stop()
	{
		if(fall!=null)
		{
			fall.stop();
			fall.close();
			fall = null;
		}
	}
	public void backgroundMusic()
	{
		musicTimer = new Timer(7000, new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) {
				ballDropSound("gameplay.wav");
				
			}
		});
		musicTimer.start();
	}

	public void actionPerformed(ActionEvent event)
	{
		if(event.getActionCommand().equals("Act"))
		{
			actMoving();
			jTSquareText.setText(gridBC.gridx + "," + gridBC.gridy);
		}
		if(event.getActionCommand().equals("Run"))
		{
			jBRunKey.setText("Pause");
			jBRunKey.setIcon(new ImageIcon("images/pause.png"));
			timerClock.start();
			runAutomatically();
		}
		if(event.getActionCommand().equals("Pause"))
		{
			jBRunKey.setText("Run");
			jBRunKey.setIcon(new ImageIcon("images/run.png"));
			timerClock.stop();
			autoMoveTimer.stop();
		}
		if(event.getActionCommand().equals("Reset"))
		{
			restartGame();
			timerClock.start();
		}
		else if(event.getActionCommand().equals("Reset"))
		{
			restartGame();
			timerClock.stop();
		}
		if(event.getActionCommand().equals("<"))
		{
			leftBallMoves();
			goalStone();
			runAutomatic();
		}		
		if(event.getActionCommand().equals(">"))
		{
			rightBallMoves();
			runAutomatic();
		}
		if(event.getActionCommand().equals("^") )
		{
			upBallMoves();

		}
		if(event.getActionCommand().equals("v"))
		{
			downBallMoves();
			
		}
		if(event.getActionCommand().equals("Exit"))
		{
			System.exit(0);
		}
		if(event.getActionCommand().equals("About"))
		{
			JOptionPane.showMessageDialog(null, "Program: \tAssignment 2: Application - Ball Maze" + 
									"\nFilename: \tCBallMaze.java\n@author: \t© Amir Bhandari (18406484)" +
									"\nCourse: \tBSc.(Hons) Computing Year 1 " +
									"\nModule: \tCSY1020 Problem Solving & Programming" + 
									"\nTutor: \tKumar Lamichhane\n@version: \t1.0" + 
									"\nDate: \t15/07/2018","About", JOptionPane.OK_CANCEL_OPTION);
		}
	

		//whenever option1 button clicked stage 1 will appear //manually play
		if(event.getActionCommand().equals("Option 1"))
		{		
			restartGame();
			checkOption = false;
			jTOptionText.setText("1");
			timerClock.start();
			
			
		}
		//whenever option1 button clicked stage 2 will appear //drop down
		if(event.getActionCommand().equals("Option 2"))
		{			
			restartGame();
			checkOption = true;
			jTOptionText.setText("2");
			timerClock.start();			
		}
		//whenever option1 button clicked stage 3 will appear //auto run
		if(event.getActionCommand().equals("Option 3"))
		{	
			restartGame();
			checkOption = true;
			jTOptionText.setText("3");
			timerClock.start();	
			backgroundMusic();
		}	
	}
}