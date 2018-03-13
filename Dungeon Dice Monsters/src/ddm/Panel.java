package ddm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class Panel extends JPanel {
	

	private static final long serialVersionUID = 1L;
	
	private final int gameWidth = 1100, gameHeight = 825;
	private Tile[][] tiles;
	private Board board;
	
	private int r = 7, c = 7;
	private Pattern[] patterns = new Pattern[48];
	private Pattern borderingTiles = new Pattern(-1,0, 0,1, 1,0, 0,-1, "hi");
	
	private int p = 0, pNonrotated = 0, rotations = 0;
	private Key key;
	private int cModifier = 0;
	
	private Player player1 = new Player(1, this), player2 = new Player(2, this);
	
	private boolean turnPlayer1 = true, canPlace = false;
	private Player currentPlayer = player1;
	
	private JLabel turnLabel, turnLabelLabel, phaseLabel;
	private JButton phaseChangeButton, infoButton;
	private JPanel rightShark;  
	
	private JPanel diePanel;
	private JLabel[] dieRolls = new JLabel[3];
	
	private String phase = "temp"; //{"Roll", "Pattern", "Action", "Move", "Attack"}
	private Tile selectedTile = null;
	private JLabel lastLabelTouchedInGrid = null;
	
	private ArrayList <Tile> movePath = new ArrayList<Tile>();
	private ArrayList <Monster> monstersWhoAttacked = new ArrayList<Monster>();
	
	private Monster monsterToSummon = null, selectedMonster = null, abilityMonster = null;
	private boolean isAbilityMovement = false;
	
	private int monsterLevel = 0;
	
	
	public Panel() {
		setFocusable(true);
		setLayout(null);
		setPreferredSize(new Dimension(gameWidth, gameHeight));
		setBackground(Color.cyan);
		init();

		key = new Key();
		addKeyListener(key);
		
		while (player1.name().equals(player2.name())) {
			player2.randomizeName();
		}
		
		for (int R=0; R<tiles.length; R++)	//adds tile recognition to all tiles
			for (int C=0; C<tiles[0].length; C++) {
				final int r69 = R, c69 = C;
				tiles[r69][c69].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {   
						selectedTile = tiles[r69][c69];
						selectedMonster = null;
						
						if (selectedTile.monster() != null) {
							selectedMonster = selectedTile.monster();
							updateMonsterPanel(selectedMonster);
						}
						if (phase.equalsIgnoreCase("Ability")) {
							abilityMonster.receivedAbility(selectedTile, selectedMonster);
							return;
						}
						if (phase.equalsIgnoreCase("Attack")
								&& selectedTile.monster() != null
								&& selectedTile.monster().owner() != tiles[r][c].monster().owner()
								&& ((Math.abs(r69-r) == 1 && Math.abs(c69-c) == 0)
										|| (Math.abs(c69-c) == 1 && Math.abs(r69-r) == 0))) {
							
				 			int response = JOptionPane.showConfirmDialog(null, 
				 					tiles[r][c].monster().owner().name() 
									+ " (" + tiles[r][c].monster().owner().color() + "),"
				 					+ " do you want to spend " 
									+ tiles[r][c].monster().attackCost() 
				 					+ " attack crest to attack?\n"
		 		    				+ "Attacker:   \"" + tiles[r][c].monster().name() 
		 		    				+ "\"  atk - " + tiles[r][c].monster().atk()
		 		    				+ "\nDefender:   \"" + selectedTile.monster().name() + "\"  hp - " 
		 		    				+ selectedTile.monster().hp(),
		 		    				"Attack?",
		 		    				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				 			
				 			if (response != JOptionPane.YES_OPTION) {
				 				for (int i=0; i<4; i++)
									tiles[borderingTiles.r(i) + r][borderingTiles.c(i) + c].updatePics();
				 				changePhase("Action");
				 				return;
				 			}
				 			tiles[r][c].monster().attack();
				 			monstersWhoAttacked.add(tiles[r][c].monster());
				 			tiles[r][c].monster().owner().subtractAtk(tiles[r][c].monster().attackCost());
							
				 			if (!tiles[r][c].monster().attackingInterupt(selectedTile.monster()))
				 				selectedTile.monster().defend(tiles[r][c].monster());
				 			
							changePhase("Action");
							for (int i=0; i<4; i++)
								tiles[borderingTiles.r(i) + r][borderingTiles.c(i) + c].updatePics();
						}
							//new elseif statement for ranged attackers!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
					}
				});
			}	
		setPatterns();
	}
	
	public void init() {
		board = new Board();
		add(board);
		board.setLocation(gameWidth/2-board.getWidth()/2, gameHeight/2-board.getHeight()/2);
		tiles = board.getTiles();
		cModifier = tiles[0][0].cModifier();

		r = 2;
		c = (tiles.length-1)/2;
		tiles[r][c].setState("path2");
		tiles[r][c].addMonster(new Monster("Monster Lord 2", 3, 10, 0, player2, this, 0));
		addPopUp(tiles[r][c]);
		r = tiles.length-3;
		c = (tiles.length-1)/2;
		tiles[r][c].setState("path1");
		tiles[r][c].addMonster(new Monster("Monster Lord 1", 3, 10, 0, player1, this, 0));
		addPopUp(tiles[r][c]);
		
		for (int i=0; i<player1.allMonsters().size(); i++) {
			player1.allMonsters().get(i).initalSetup();
			player2.allMonsters().get(i).initalSetup();
		}

		turnLabel = new JLabel();
		turnLabel.setOpaque(true);
		turnLabel.setSize(150,50);
		turnLabel.setLocation(10, gameHeight/2 + 17);
		turnLabel.setFocusable(false);
		turnLabel.setHorizontalAlignment(SwingConstants.CENTER);
		turnLabel.setText(currentPlayer.name());
		if (turnPlayer1){
			turnLabel.setBackground(Color.blue);
			turnLabel.setForeground(Color.yellow);
		}
		else {
			turnLabel.setBackground(Color.orange);
			turnLabel.setForeground(Color.blue);
		}
		add(turnLabel);
		
		turnLabelLabel = new JLabel();
		turnLabelLabel.setOpaque(false);
		turnLabelLabel.setSize(150,50);
		turnLabelLabel.setLocation(10, turnLabel.getY()-40);
		turnLabelLabel.setFocusable(false);
		turnLabelLabel.setHorizontalAlignment(SwingConstants.CENTER);
		turnLabelLabel.setText("Turn Player:");
		add(turnLabelLabel);
		
		phaseLabel = new JLabel();
		phaseLabel.setOpaque(false);
		phaseLabel.setSize(150,50);
		phaseLabel.setLocation(10, turnLabel.getY()+40);
		phaseLabel.setFocusable(false);
		phaseLabel.setHorizontalAlignment(SwingConstants.CENTER);
		phaseLabel.setText(phase);
		add(phaseLabel);
		
		infoButton = new JButton();
		infoButton.setSize(60,30);
		infoButton.setLocation(gameWidth - 110, 5);
		infoButton.setFocusable(false);	//IMPORTANT
		infoButton.setText("info");
		infoButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(null, "The goal is to defeat your opponent's Monster Lord"
							+ "\nYou need to roll 2+ summon crests to summon a monster."
							+ "\nA monster's border is his player's color."
							+ "\nA Monster Lord is not considered a \"monster\" (in ability descriptions)."
							+ "\nA monster's ability cost is the x(Number) next to the ability descriptions."
							+ "\n\nDice Pattern Placing Controls:"
							+ "\n\"wasd\"  -  move around"
							+ "\n\"r\"  -  rotate"
							+ "\n\"q\"  -  change pattern"
							+ "\n\"esc\"  -  cancel pattern/summon"
							+ "\n\"enter\"  -  finalize placement"
							+ "\n\nMonster Moving Controls:"
							+ "\n\"wasd\"  -  move around"
							+ "\n\"esc\"  -  cancel movement"
							+ "\n\"enter\"  -  finalize placement"
							+ "\n\nMonster Attacking Controls:"
							+ "\nclick the target"
							+ "\n\"esc\"  -  cancel attack"
							+ "\n\n Everything else is point and click.", 
			 				"Controls and Information", JOptionPane.INFORMATION_MESSAGE);
			  } 
		} );
		add(infoButton);
		
		phaseChangeButton = new JButton();
		phaseChangeButton.setSize(150,50);
		phaseChangeButton.setLocation(10, turnLabel.getY() - 90);
		phaseChangeButton.setFocusable(false);	//IMPORTANT
		phaseChangeButton.setText("This is Button");

		phaseChangeButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) {
				  
				  if (phase.equalsIgnoreCase("Action")) {
					  changePhase("End");
					  return;
				  }
				  else if (phase.equalsIgnoreCase("Attack")) {
					  for (int i=0; i<4; i++)
						  tiles[borderingTiles.r(i) + r][borderingTiles.c(i) + c].updatePics();
					  changePhase("Action");
					  return;
				  }
				  else if (phase.equalsIgnoreCase("Move")) {
					  for (Tile t : movePath) {
							t.setIcon(null);
							t.updatePics();
					  }
					  changePhase("Action");
					  movePath.clear();
					  return;
				  }
				  else if (phase.equalsIgnoreCase("Pattern")) {
					  int response = JOptionPane.showConfirmDialog(null, 
			 					currentPlayer.name() 
								+ " (" + currentPlayer.color() + "),"
			 					+ " do you want to skip placing your die?\n"
	 		    				+ "You will not get your monster summon.",
	 		    				"Skip Summoning?",
	 		    				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			 			
			 			if (response == JOptionPane.YES_OPTION) {
			 				changePhase("Action");
			 			}
				  }
			  } 
		} );
		add(phaseChangeButton);	
		
		rightShark = new JPanel();
		rightShark.setSize(156, 500);	//156, finalItem.getY()+finalItem.getHeight()
		rightShark.setLayout(null);
		rightShark.setBackground(Color.DARK_GRAY);
		rightShark.setLocation(board.getX() + board.getWidth() + 7, board.getY() + 150);
				//board.getX() + board.getWidth() + 7,  board.getY() + ((board.getHeight() - rightShark.getHeight())/2)
		add(rightShark);
		updateMonsterPanel(tiles[tiles.length-3][(tiles.length-1)/2].monster());
		
		diePanel = new JPanel();
		diePanel.setSize(150, 150);
	    
		player1.panel().setLocation(22, board.getY() + board.getHeight() - player1.panel().getHeight() - 15);
		add(player1.panel());
		
		player2.panel().setLocation(22, board.getY() + 15);
		add(player2.panel());
	}
	
	public void changePhase(String phaseTemp) {
		  if (phase.equalsIgnoreCase("Game Over") || phaseTemp.equalsIgnoreCase("Game Over")) {
			  phaseLabel.setText("GAME OVER");
			  phaseChangeButton.setText(currentPlayer.name().toUpperCase() + " WINS!!!!!");
			  phaseChangeButton.setEnabled(false);
			  return;
		  }
		
		  if (phase.equalsIgnoreCase("Pattern"))
			undoPreview();
		
		  phase = phaseTemp; //{"Roll", "Pattern", "Move", "Action", "Attack", "Ability", "End"}
		
		  if (phase.equalsIgnoreCase("Roll")) {
			  phaseLabel.setText("Roll the dice");
			  phaseChangeButton.setText("Roll");
			  phaseChangeButton.setEnabled(false);
			  int response = JOptionPane.showOptionDialog(null, 
					  	currentPlayer.name() 
						+ " (" + currentPlayer.color() + "), chose a level dice to roll", "Roll The Dice", 
				        JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, 
				        new String[]{"Level 1", "Level 2", "Level 3", "Level 4"}, // this is the array
				        "default");
			  monsterLevel = 5;
			  if (response == 0)
				  monsterLevel = 1;
			  else if (response == 1)
				  monsterLevel = 2;
			  else if (response == 2)
				  monsterLevel = 3;
			  else if (response == 3)
				  monsterLevel = 4;
			  
			  if (roll(monsterLevel)) {
				  changePhase("Pattern");
				  return;
			  }
			  else {
				  changePhase("Action");
				  return;
			  }
		  }
		  else if (phase.equalsIgnoreCase("Pattern")) {
			  	phaseLabel.setText("Place the die pattern");
				phaseChangeButton.setEnabled(true);
				phaseChangeButton.setText("Cancel Summon");
				r = (int)(tiles.length-1)/2;
				c = (int)(tiles.length-1)/2;
				
				if (monsterSelectionPopup()) {
					updateMonsterPanel(monsterToSummon);
					createPreview();
				}
				else
					changePhase("Action");
				
				return;
		  }
		  else if (phase.equalsIgnoreCase("Move")) {
			  phaseLabel.setText("Use wasd to move");
			  phaseChangeButton.setEnabled(true);
			  phaseChangeButton.setText("Cancel Movement");
			  tiles[r][c].setBorder(BorderFactory.createLineBorder(Color.green, 1));
			  return;
		  }
		  else if (phase.equalsIgnoreCase("Action")) {
			  phaseLabel.setText("Select a monster to act");
			  phaseChangeButton.setEnabled(true);
			  phaseChangeButton.setText("End Turn");
			  return;
		  }
		  else if (phase.equalsIgnoreCase("Attack")) {
			  phaseLabel.setText("Select a target");
			  phaseChangeButton.setEnabled(true);
			  phaseChangeButton.setText("Cancel Attack");
			  return;
		  }
		  else if (phase.equalsIgnoreCase("Ability")) {
			  phaseLabel.setText(abilityMonster.abilityPhaseText());
			  phaseChangeButton.setText("Carry out the ability");	//IMPLEAMENT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
			  phaseChangeButton.setEnabled(false);
			  if (abilityMonster.abilityPhaseText().equalsIgnoreCase("~"))
				  changePhase("Action");
			  return;
		  }
		  else if (phase.equalsIgnoreCase("End")) {
			  changeTurn();
			  return;
		  }
		phaseLabel.setText("ERROR UNRECOGNIZED PHASE");
		phaseChangeButton.setText(phase.substring(0,1).toUpperCase() + phase.substring(1).toLowerCase());
	}
	
	private void changeTurn() {
		for (Monster monst : currentPlayer.summonedMonsters()) {
			monst.endTurnAbility();
		}
		turnPlayer1 = !turnPlayer1;
		if (turnPlayer1){
			currentPlayer = player1;
			turnLabel.setBackground(Color.blue);
			turnLabel.setForeground(Color.yellow);
			turnLabel.setText(player1.name());
			}
		else {
			currentPlayer = player2;
			turnLabel.setBackground(Color.orange);
			turnLabel.setForeground(Color.blue);
			turnLabel.setText(player2.name());
		}
		for (Monster monst : monstersWhoAttacked)
			monst.newTurnAttackReset();
		monstersWhoAttacked.clear();
		changePhase("Roll");
	}
	
	public boolean monsterSelectionPopup() {	//alert popup returns if a monster to summon exists
		int monstersForGrid = 0;
		boolean hasMonsterToSummon = false;
		for (int i=0; i<currentPlayer.allMonsters().size(); i++) {
			if (currentPlayer.allMonsters().get(i).level() == monsterLevel) {
				hasMonsterToSummon = true;
				monstersForGrid++;
			}
		}
		if (!hasMonsterToSummon) {
			JOptionPane.showConfirmDialog(null, currentPlayer.name() 
					+ " (" + currentPlayer.color() + ")," 
					+ " there are no more monsters of level " + monsterLevel + " to summon."
					+ "\nDo something about it.", 
	 				"You Used Up All Your Monsters!", JOptionPane.YES_NO_OPTION);
			return false;
		}
		int gridWidth = (int)(Math.sqrt(monstersForGrid));
		if (monstersForGrid == 0)
			gridWidth = 1;
		if (monstersForGrid > 2)
			gridWidth = (int)Math.ceil(Math.sqrt(monstersForGrid));

		JPanel monsterGridPanel = new JPanel();
		monsterGridPanel.setBorder(BorderFactory.createLineBorder(Color.darkGray, 2));
		monsterGridPanel.setBackground(Color.lightGray);
		//monsterGridPanel.setSize(60*gridWidth + 4, 60*gridWidth + 4);
		monsterGridPanel.setLayout(new GridLayout(gridWidth, gridWidth, 1, 1));	//have a (0, 0) border?
		
		lastLabelTouchedInGrid = null;
		final Border grayBorder = BorderFactory.createLineBorder(Color.gray, 2);
		
		final ArrayList <JLabel> smallMonsterLabels = new ArrayList<JLabel>();
		for (int i=0; i<currentPlayer.allMonsters().size(); i++) {
			if (currentPlayer.allMonsters().get(i).level() == monsterLevel) {
				final int monsterIndex = i, labelIndex = smallMonsterLabels.size();
				smallMonsterLabels.add(new JLabel());
				//smallMonsterLabels.get(labelIndex).setPreferredSize(new Dimension(60, 60));
				smallMonsterLabels.get(labelIndex).setIcon(currentPlayer.allMonsters().get(monsterIndex)
						.getResizedImageIcon(120,120));	//(60,60)
				smallMonsterLabels.get(labelIndex).setHorizontalAlignment(SwingConstants.CENTER);
				smallMonsterLabels.get(labelIndex).setMinimumSize(new Dimension(10, 10));
				smallMonsterLabels.get(labelIndex).setBorder(BorderFactory.createLineBorder(Color.gray, 2));
				if (labelIndex == 0) {
					smallMonsterLabels.get(labelIndex).setBorder(BorderFactory.createLineBorder(Color.green, 2));
					monsterToSummon = currentPlayer.allMonsters().get(monsterIndex);
					updateMonsterPanel(monsterToSummon);
					lastLabelTouchedInGrid = smallMonsterLabels.get(labelIndex);
				}
				smallMonsterLabels.get(labelIndex).addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {   
						lastLabelTouchedInGrid.setBorder(grayBorder);
						//smallMonsterLabels.get(labelIndex).setPreferredSize(new Dimension(60,60));
						smallMonsterLabels.get(labelIndex).setBorder(BorderFactory.createLineBorder(Color.green, 2));
						monsterToSummon = currentPlayer.allMonsters().get(monsterIndex);
						updateMonsterPanel(monsterToSummon);
						lastLabelTouchedInGrid = smallMonsterLabels.get(labelIndex);
					}});
				monsterGridPanel.add(smallMonsterLabels.get(labelIndex));
			}
		}
		
		if (monstersForGrid == 1)
			return true;
		
		JOptionPane.showMessageDialog(null, monsterGridPanel, "Choose a monster", JOptionPane.PLAIN_MESSAGE);
		
		return true;
	}
	
	public boolean roll(int level) {
		int summonCrests = 0;
		int diceRolls = 3;
		
		for (Monster monst : currentPlayer.summonedMonsters()) {
			diceRolls = diceRolls + monst.rollAbility();
		}
		diceRolls = diceRolls + currentPlayer.dieBonus();
		currentPlayer.setDieBonus(0);

		updateDiePanel(diceRolls);
		
		for (int i=0; i<diceRolls; i++) {
			Dice dice = new Dice(level);
			String roll = dice.roll();
			if (roll.contains("atk")) {
				currentPlayer.addAtk(Integer.parseInt("" + roll.charAt(3)));
			}
			else if (roll.contains("def")) {
				currentPlayer.addDef(Integer.parseInt("" + roll.charAt(3)));
			}
			else if (roll.contains("move")) {
				currentPlayer.addMove(Integer.parseInt("" + roll.charAt(4)));
			}	
			else if (roll.contains("spell")) {
				currentPlayer.addSpell(Integer.parseInt("" + roll.charAt(5)));
			}
			else if (roll.equals(Integer.toString(level))) {
				summonCrests++;
			}
			else {
				JOptionPane.showConfirmDialog(null, "ERROR 69: invalid dice roll result.\nDo something about it.", 
	 					"ERROR", JOptionPane.ERROR_MESSAGE);
			}
			dieRolls[i].setIcon(getResizedPic(roll, 50, 50));
		}
		JOptionPane.showMessageDialog(null, diePanel, "Dice Rolls", JOptionPane.PLAIN_MESSAGE);

		if (summonCrests >= 2)
			return true;
		return false;
	}
	
	public void addMonsterToTile(Monster monster, Tile aTile) {	//the official way
		aTile.addMonster(monster);
        addPopUp(aTile);
        monster.owner().summonAMonster(monster);
        abilityMonster = monster;
        changePhase("Ability");
        monster.summon();
	}
	
	public void addMonsterToTileLv0(Monster monster, Tile aTile, Player the_owner) {	//the indirect illegal way
		aTile.addMonster(monster);
        addPopUp(aTile);
        the_owner.summonedMonsters().add(monster);
        abilityMonster = monster;
        changePhase("Ability");
        monster.summon();
	}

	public void addPopUp(Tile aTile) {
		final Tile myTile = aTile;
	    final JPopupMenu actionMenu = new JPopupMenu("Action Menu"); 

	    JMenuItem attackMenuItem = new JMenuItem("Attack");
	    JMenuItem abilityMenuItem = new JMenuItem("Ability");
	    JMenuItem moveMenuItem = new JMenuItem("Move");

	    attackMenuItem.addActionListener(new ActionListener() { 
		 	public void actionPerformed(ActionEvent e) {
		 		
		 		if (myTile.monster().owner().atk() < 1) {
					JOptionPane.showConfirmDialog(null, myTile.monster().owner().name() 
							+ " (" + myTile.monster().owner().color() + ")," +
							" you need 1 attack crest to attack.\n"
							+ "Do something about it.", 
		 					"Not Enough Attack Crests", JOptionPane.YES_NO_OPTION);
					return;
		 		}
		 		if (myTile.monster().hasAttackedMaxTimes()) {
					JOptionPane.showConfirmDialog(null, myTile.monster().owner().name() 
							+ " (" + myTile.monster().owner().color() + "),"
							+ " you already attacked with this " + myTile.monster().name() + " this turn.\n"
							+ "You can only attack with this monster " + myTile.monster().attacksPerTurn() 
							+ " times per turn.\n"
							+ "Do something about it.", 
		 					"Already Attacked", JOptionPane.YES_NO_OPTION);
					return;
		 		}
		 		
				boolean foundTarget = false;
				r = myTile.r();
				c = myTile.c();
				
				for (int i=0; i<4; i++){	//change pattern for ranged attackers
					Tile temp = tiles[borderingTiles.r(i) + r][borderingTiles.c(i) + c];
					
					if (temp.monster() != null
							&& temp.monster().owner() != tiles[r][c].monster().owner()) {
						
						temp.setBorder(BorderFactory.createLineBorder(Color.green, 3));
						temp.setBackground(Color.green);
						foundTarget = true;
					}
				}
				if (!foundTarget) {
					JOptionPane.showConfirmDialog(null, "There is no target for the attack.\n"
							+ "Do something about it.", 
		 					"No Target", JOptionPane.YES_NO_OPTION);
					return;
				}
			 	selectedTile = null;
			 	changePhase("Attack");
			}} );
	    
	    moveMenuItem.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    	 
	    	 if (myTile.monster().owner().move() < 1) {
	 			JOptionPane.showConfirmDialog(null, myTile.monster().owner().name() 
						+ " (" + myTile.monster().owner().color() + ")," +
						" you need at least 1 movement crest to move.\n"
						+ "Do something about it.", 
		 				"Not Enough Movement Crests", JOptionPane.YES_NO_OPTION);
	 			return;
	    	 }
	    	 moveAMonster(myTile.r(), myTile.c(), false);
	    	}} );
	      
	    abilityMenuItem.addActionListener(new ActionListener() { 
	    	public void actionPerformed(ActionEvent e) {
	    		
	    	 System.out.println(myTile.monster().name() + " Ability");
	    	 
	    	 abilityMonster = myTile.monster();
	    	 changePhase("Ability");
	    	 myTile.monster().ability();
	    	}} );

	    actionMenu.add(attackMenuItem);
	    actionMenu.add(moveMenuItem);
	    actionMenu.add(abilityMenuItem);   
	    
	    if (myTile.monster().atk() < 1)
	    	actionMenu.remove(attackMenuItem);
	    if (myTile.monster().name().contains("Monster Lord")) {
	    	actionMenu.remove(moveMenuItem);
	    }
	    if (!myTile.monster().hasActivatableAbility()) {
	    	actionMenu.remove(abilityMenuItem);
	    }
	    
	    final boolean myTurn = (myTile.monster().owner().turnPlayer() == 1);

	    MouseAdapter mouseAdapter = new MouseAdapter() {
	        public void mouseClicked(MouseEvent e) {   
	        	
	        	if (turnPlayer1 == myTurn)
	        		if (phase.equalsIgnoreCase("Action"))
	        			actionMenu.show(myTile, myTile.getWidth()+1, myTile.getHeight()/7);            
	      }
	     };
	     myTile.rememberMouseListener(mouseAdapter);
	     myTile.addMouseListener(myTile.mouseListener());
	}

	private void createPreview() {
		tiles[r][c].setBorder(BorderFactory.createLineBorder(Color.magenta, 4));
		
		canPlace = true;
		
		for (int i=0; i<6; i++){
			Tile temp = tiles[r+patterns[p].r(i)][c+patterns[p].c(i)];
			temp.setBackground(Color.green);
			if (!temp.getState().equals("empty")) {
				temp.setBackground(Color.red);
				if (temp.monster() != null)
					temp.setBorder(BorderFactory.createLineBorder(Color.red, 3));
				canPlace = false;
			}
		}
	}

	private void undoPreview() {
		
		for (int i=0; i<6; i++){
			tiles[r+patterns[p].r(i)][c+patterns[p].c(i)].updatePics();
		}
	}

	private void changePattern() {
		rotations = 0;
		undoPreview();
		pNonrotated = pNonrotated + 4;
			if (pNonrotated >= patterns.length)
				pNonrotated = 0;
		p = pNonrotated;
		isPreviewOutsideAndPushBack();
		createPreview();
	}
	
	public void isPreviewOutsideAndPushBack() {
		boolean rSmall = false, rBig = false, cSmall = false, cBig = false;
		for (int i=0; i<6; i++){
			Tile temp = tiles[r+patterns[p].r(i)][c+patterns[p].c(i)];
			if (temp.getState().equals("dne")) {
				if (temp.r() < 2)
					rSmall = true;
				if (temp.c() < 2 + cModifier)
					cSmall = true;
				if (temp.r() > tiles.length - 3)
					rBig = true;
				if (temp.c() > tiles.length - 3 - cModifier)
					cBig = true;
			}
		}	//if rotated or placed out of bounds, gets snapped back on border
		if (rSmall)
			r++;
		if (cSmall)
			c++;
		if (rBig)
			r--;
		if (cBig)
			c--;
		if (rSmall || cSmall || rBig || cBig)
			isPreviewOutsideAndPushBack();
	}


	public void updateDiePanel(int dice) {
		diePanel.removeAll();
		if (dice < 1)
			return;
		diePanel.setLayout(new GridLayout(0, dice, 15, 0));
		dieRolls = new JLabel[dice];
	    for (int i=0; i<dice; i++) {
			dieRolls[i] = new JLabel();
			dieRolls[i].setOpaque(false);
			dieRolls[i].setFocusable(false);
			dieRolls[i].setHorizontalAlignment(SwingConstants.CENTER);
			diePanel.add(dieRolls[i]);
	    }
	}
	
	public void updateMonsterPanel(Monster monst) {
		rightShark.removeAll();
		rightShark.revalidate();
		
		monst.updatePanel();
		
		rightShark.setSize(monst.panel().getWidth(), monst.panel().getHeight());
		rightShark.setLocation
			(board.getX() + board.getWidth() + 7,  board.getY() + ((board.getHeight()-rightShark.getHeight()) /2));
		
		rightShark.repaint();
		rightShark.add(monst.panel());
	}

	private ImageIcon getResizedPic(String picName, int picWidth, int picHeight) {
		URL imageURL = getClass().getResource("resources/" + picName + ".jpg");	
		if (imageURL != null) {
			ImageIcon imageIcon = new ImageIcon(imageURL);
			Image image = imageIcon.getImage();
			Image newimg = image.getScaledInstance(picWidth, picHeight,  java.awt.Image.SCALE_SMOOTH);
			imageIcon = new ImageIcon(newimg);
			if (imageIcon != null) {
				return imageIcon;
			}	
		}
		JOptionPane.showConfirmDialog(null, "ERROR 69: invalid pic \"" + picName + "\" inside getResizedPic() in Panel class.\n"
				+ "Do something about it.", 
					"ERROR", JOptionPane.ERROR_MESSAGE);
		return null;
	}
	
	public void setPatterns() {
		patterns[0] = new Pattern(0,0, 1,0, 2,0, 0,1, -1,1, -2,1);
		patterns[1] = new Pattern(0,0, -1,0, -1,-1, -1,-2, 0,1, 0,2);
		patterns[2] = new Pattern(0,0, -1,0, -2,0, 0,-1, 1,-1, 2,-1);
		patterns[3] = new Pattern(0,0, 0,-2, 0,-1, 1,0, 1,1, 1,2);
		patterns[4] = new Pattern(0,0, 0,-1, -1,-1, -2,-1, 1,0, 2,0);
		patterns[5] = new Pattern(0,0, 1,0, 1,-1, 1,-2, 0,1, 0,2);
		patterns[6] = new Pattern(0,0, -1,0, -2,0, 0,1, 1,1, 2,1);
		patterns[7] = new Pattern(0,0, 0,-1, 0,-2, -1,0, -1,1, -1,2);
		patterns[8] = new Pattern(0,0, -1,0, -1,-1, -2,-1, 0,1, 1,1);
		patterns[9] = new Pattern(0,0, 0,-1, 1,-1, 1,-2, -1,0, -1,1);
		patterns[10] = new Pattern(0,0, 0,-1, -1,-1, 1,0, 1,1, 2,1);
		patterns[11] = new Pattern(0,0, 0,1, -1,1, -1,2, 1,0, 1,-1);
		patterns[12] = new Pattern(0,0, -1,0, -1,1, -2,1, 0,-1, 1,-1);
		patterns[13] = new Pattern(0,0, 0,1, 1,1, 1,2, -1,0, -1,-1);
		patterns[14] = new Pattern(0,0, 1,0, 1,-1, 2,-1, 0,1, -1,1);
		patterns[15] = new Pattern(0,0, 0,-1, -1,-1, -1,-2, 1,0, 1,1);
		patterns[16] = new Pattern(0,0, -1,0, -2,0, -2,-1, 1,0, 1,1);
		patterns[17] = new Pattern(0,0, 0,-1, 1,-1, 0,1, 0,2, -1,2);
		patterns[18] = new Pattern(0,0, -1,0, -1,-1, 1,0, 2,0, 2,1);
		patterns[19] = new Pattern(0,0, 0,-1, 0,-2, 1,-2, 0,1, -1,1);
		patterns[20] = new Pattern(1,-1, 1,0, 0,0, -1,0, -2,0, -2,1);
		patterns[21] = new Pattern(0,0, 0,-1, -1,-1, 0,1, 0,2, 1,2);
		patterns[22] = new Pattern(0,0, -1,0, -1,1, 1,0, 2,0, 2,-1);
		patterns[23] = new Pattern(0,0, 0,1, 1,1, 0,-1, 0,-2, -1,-2);
		patterns[24] = new Pattern(0,0, 0,1, 1,1, -1,0, -2,0, -1,-1);
		patterns[25] = new Pattern(0,0, 1,0, 1,-1, -1,1, 0,1, 0,2);
		patterns[26] = new Pattern(0,0, 0,-1, -1,-1, 2,0, 1,0, 1,1);
		patterns[27] = new Pattern(0,0, -1,0, -1,1, 0,-2, 0,-1, 1,-1);
		patterns[28] = new Pattern(0,0, 0,-1, 1,-1, -2,0, -1,0, -1,1);
		patterns[29] = new Pattern(0,0, -1,0, -1,-1, 1,1, 0,1, 0,2);
		patterns[30] = new Pattern(0,0, 1,0, 2,0, 1,-1, 0,1, -1,1);
		patterns[31] = new Pattern(0,0, 1,0, 1,1, -1,-1, 0,-1, 0,-2);
		patterns[32] = new Pattern(0,0, 1,-1, 1,0, 1,1, -1,0, -2,0);
		patterns[33] = new Pattern(-1,-1, 0,-1, 1,-1, 0,0, 0,1, 0,2);
		patterns[34] = new Pattern(-1,-1, -1,0, -1,1, 0,0, 1,0, 2,0);
		patterns[35] = new Pattern(0,-2, 0,-1, 0,0, -1,1, 0,1, 1,1);
		patterns[36] = new Pattern(0,-1, 0,0, 0,1, 1,0, -1,0, -2,0);
		patterns[37] = new Pattern(0,-1, -1,0, 0,0, 1,0, 0,1, 0,2);
		patterns[38] = new Pattern(-1,0, 0,-1, 0,0, 0,1, 1,0, 2,0);
		patterns[39] = new Pattern(0,-2, 0,-1, -1,0, 0,0, 1,0, 0,1);
		patterns[40] = new Pattern(2,0, 1,0, 1,-1, 0,0, -1,0, 0,1);
		patterns[41] = new Pattern(0,-2, 0,-1, 0,0, 0,1, 1,0, -1,-1);
		patterns[42] = new Pattern(1,0, 0,0, -1,0, -2,0, 0,-1, -1,1);
		patterns[43] = new Pattern(0,-1, 0,0, -1,0, 0,1, 1,1, 0,2);
		patterns[44] = new Pattern(0,-1, 0,0, -1,0, 2,0, 1,0, 1,1);
		patterns[45] = new Pattern(0,-2, 0,-1, 0,0, 0,1, -1,0, 1,-1);
		patterns[46] = new Pattern(1,0, 0,0, -1,0, -2,0, 0,1, -1,-1);
		patterns[47] = new Pattern(0,-1, 0,0, 0,1, 0,2, 1,0, -1,1);

	}
	
	public Tile[][] getTiles() {
		return tiles;
	}
	
	public Panel getPanel() {
		return this;
	}
	
	public void moveAMonster(int r_new, int c_new, boolean isAbility) {
		r = r_new;
		c = c_new;
		movePath = new ArrayList<Tile>();
		movePath.add(tiles[r][c]);
		isAbilityMovement = false;
		changePhase("Move");
		if (isAbility) {
			isAbilityMovement = true;
			phaseLabel.setText("Move the target");
			phaseChangeButton.setText("Move the target");
			phaseChangeButton.setEnabled(false);
		}
	}
	
	public Player opponent() {
		if (currentPlayer == player1)
			return player2;
		return player1;
	}
	
	public Player currentPlayer() {
		return currentPlayer;
	}
	
	public void phaseLabel(String text) {
		phaseLabel.setText(text);
	}
	
	public Monster selectedMonster() {
		return selectedMonster;
	}
	public void clearSelectedMonster() {
		selectedMonster = null;
	}
	
	public void setMonsterLevelToSummon(int lv) {
		monsterLevel = lv;
	}
	
	public Monster monsterToSummon() {
		return monsterToSummon;
	}
	
	public Player theActualOpponent (int myIndex) {
		if (myIndex == 1)
			return player2;
		return player1;
	}
	
	private class Key implements KeyListener {

		public void keyPressed(KeyEvent e) {
			
			if (phase.equalsIgnoreCase("Move")) {
				
				int key = e.getKeyCode();
				
				if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
					Tile temp = tiles[r][c+1];
					if (movePath.get(0).monster().moveCost()*movePath.size() > currentPlayer.move()) {
						JOptionPane.showMessageDialog(null, "You would need more movement crests to go further.", 
				 				"Need More Movement Crests", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					if (!(temp.getState().equals("path1") || temp.getState().equals("path2")))
						return;
					if (temp.monster() != null)
						return;
					if (movePath.contains(temp))
						return;

					movePath.add(temp);
					movePath.get(movePath.size()-1).setIcon(movePath.get(movePath.size()-2).getIcon());
					movePath.get(movePath.size()-2).setIcon(null);
					for (Tile t : movePath) {
						t.setBackground(Color.green);
						t.setBorder(BorderFactory.createLineBorder(Color.black, 1));
					}
					movePath.get(movePath.size()-1).setBorder(BorderFactory.createLineBorder(Color.green, 1));

					c++;
					return;
				}
				if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
					Tile temp = tiles[r][c-1];
					if (movePath.get(0).monster().moveCost()*movePath.size() > currentPlayer.move()) {
						JOptionPane.showMessageDialog(null, "You would need more movement crests to go further.", 
				 				"Need More Movement Crests", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					
					if (!(temp.getState().equals("path1") || temp.getState().equals("path2")))
						return;
					if (temp.monster() != null)
						return;
					if (movePath.contains(temp))
						return;

					movePath.add(temp);
					movePath.get(movePath.size()-1).setIcon(movePath.get(movePath.size()-2).getIcon());
					movePath.get(movePath.size()-2).setIcon(null);
					for (Tile t : movePath) {
						t.setBackground(Color.green);
						t.setBorder(BorderFactory.createLineBorder(Color.black, 1));
					}
					movePath.get(movePath.size()-1).setBorder(BorderFactory.createLineBorder(Color.green, 1));

					c--;
					return;
				}
				if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
					Tile temp = tiles[r-1][c];
					if (movePath.get(0).monster().moveCost()*movePath.size() > currentPlayer.move()) {
						JOptionPane.showMessageDialog(null, "You would need more movement crests to go further.", 
				 				"Need More Movement Crests", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					
					if (!(temp.getState().equals("path1") || temp.getState().equals("path2")))
						return;
					if (temp.monster() != null)
						return;
					if (movePath.contains(temp))
						return;

					movePath.add(temp);
					movePath.get(movePath.size()-1).setIcon(movePath.get(movePath.size()-2).getIcon());
					movePath.get(movePath.size()-2).setIcon(null);
					for (Tile t : movePath) {
						t.setBackground(Color.green);
						t.setBorder(BorderFactory.createLineBorder(Color.black, 1));
					}
					movePath.get(movePath.size()-1).setBorder(BorderFactory.createLineBorder(Color.green, 1));

					r--;
					return;
				}
				if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
					Tile temp = tiles[r+1][c];
					if (movePath.get(0).monster().moveCost()*movePath.size() > currentPlayer.move()) {
						JOptionPane.showMessageDialog(null, "You would need more movement crests to go further.", 
				 				"Need More Movement Crests", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					
					if (!(temp.getState().equals("path1") || temp.getState().equals("path2")))
						return;
					if (temp.monster() != null)
						return;
					if (movePath.contains(temp))
						return;

					movePath.add(temp);
					movePath.get(movePath.size()-1).setIcon(movePath.get(movePath.size()-2).getIcon());
					movePath.get(movePath.size()-2).setIcon(null);
					for (Tile t : movePath) {
						t.setBackground(Color.green);
						t.setBorder(BorderFactory.createLineBorder(Color.black, 1));
					}
					movePath.get(movePath.size()-1).setBorder(BorderFactory.createLineBorder(Color.green, 1));

					r++;
					return;
				}
				if (key == KeyEvent.VK_ESCAPE) {
					if (isAbilityMovement)
						currentPlayer.addSpell(abilityMonster.moveOpponentCost());	//refund
					for (Tile t : movePath) {
						t.setIcon(null);
						t.updatePics();
					}
					changePhase("Action");
					movePath = new ArrayList<Tile>();
					return;
				}
				if (key == KeyEvent.VK_ENTER) {
					
					if (movePath.size()-1 < 1) {
			 			JOptionPane.showConfirmDialog(null, 
			 					currentPlayer.name() 
								+ " (" + currentPlayer.color() + "),"
			 					+ " you need to move at least 1 space if you want to move.\n"
			 					+ "Do something about it.",
	 		    				"Move?",
	 		    				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			 			for (Tile t : movePath) {
							t.setIcon(null);
							t.updatePics();
						}
						if (isAbilityMovement)
							currentPlayer.addSpell(abilityMonster.moveOpponentCost());	//refund
						movePath.clear();
						changePhase("Action");
						return;
					}
					
					int response = JOptionPane.NO_OPTION;
					if (currentPlayer.move() >= (int)Math.ceil((movePath.size()-1)*movePath.get(0).monster().moveCost()))
			 			response = JOptionPane.showConfirmDialog(null, 
			 					currentPlayer.name() 
								+ " (" + currentPlayer.color() + "),"
			 					+ " do you want to spend " 
								+ (int)Math.ceil((movePath.size()-1)*movePath.get(0).monster().moveCost())
								+ " movement crests to move "
								+ movePath.get(0).monster().name() + " " 
								+ (movePath.size()-1)
								+ " spaces?\n",
	 		    				"Move?",
	 		    				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					else
						JOptionPane.showConfirmDialog(null, currentPlayer.name() 
								+ " (" + currentPlayer.color() + ")," +
								" you need " + (int)Math.ceil((movePath.size()-1)*movePath.get(0).monster().moveCost())
								+ " movement crests to move there.\n"
								+ "Do something about it.", 
			 					"Not Enough Movement Crests", JOptionPane.YES_NO_OPTION);
		 			
		 			if (response == JOptionPane.YES_OPTION) {
		 				currentPlayer.subtractMove((int)Math.ceil((movePath.size()-1)*movePath.get(0).monster().moveCost()));
		 				movePath.get(0).moveMonster(tiles[r][c]);
						addPopUp(tiles[r][c]);
		 			}
					for (Tile t : movePath) {
						t.setIcon(null);
						t.updatePics();
					}
					
	 				if (response != JOptionPane.YES_OPTION && isAbilityMovement)
	 					currentPlayer.addSpell(abilityMonster.moveOpponentCost());	//refund
	 				
					movePath.clear();
					changePhase("Action");
					isAbilityMovement = false;
					return;
				}	
			}
			
			
			if (phase.equalsIgnoreCase("Attack")) {
				
				int key = e.getKeyCode();
				
				if (key == KeyEvent.VK_ESCAPE) {
					  for (int i=0; i<4; i++)
						  tiles[borderingTiles.r(i) + r][borderingTiles.c(i) + c].updatePics();
					changePhase("Action");		
				}
			}
			
			
			
					
			if (phase.equalsIgnoreCase("Pattern")) {
				
				int key = e.getKeyCode();
				
				if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
					for (int i=0; i<6; i++)	//prevents from moving out of bounds
						if (tiles[r+patterns[p].r(i)][c+patterns[p].c(i) +1 ].getState().equals("dne"))
							return;
					undoPreview();
					c++;
					createPreview();
					return;
				}
				if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
					for (int i=0; i<6; i++)
						if (tiles[r+patterns[p].r(i)][c+patterns[p].c(i) -1 ].getState().equals("dne"))
							return;
					undoPreview();
					c--;
					createPreview();
					return;
				}
				if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
					for (int i=0; i<6; i++)
						if (tiles[r+patterns[p].r(i) -1 ][c+patterns[p].c(i)].getState().equals("dne"))
							return;
					undoPreview();
					r--;
					createPreview();
					return;
				}
				if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
					for (int i=0; i<6; i++)
						if (tiles[r+patterns[p].r(i) +1 ][c+patterns[p].c(i)].getState().equals("dne"))
							return;
					if (tiles[r+1][c].getState().equals("dne")){
						return;
					}
					undoPreview();
					r++;
					createPreview();
					return;
				}
				if (key == KeyEvent.VK_R) {	//rotate
					rotations++;
					undoPreview();
					p = pNonrotated + rotations%4;
					isPreviewOutsideAndPushBack();
					createPreview();
					return;
				}
				if (key == KeyEvent.VK_Q) {	//new pattern
					changePattern();
					return;
				}
				if (key == KeyEvent.VK_ESCAPE) {
		 			int response = JOptionPane.showConfirmDialog(null, 
		 					currentPlayer.name() 
							+ " (" + currentPlayer.color() + "),"
		 					+ " do you want to skip placing your die?\n"
 		    				+ "You will not get your monster summon.",
 		    				"Skip Summoning?",
 		    				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		 			
		 			if (response == JOptionPane.YES_OPTION) {
		 				changePhase("Action");
		 			}		
				}
				
				if (key == KeyEvent.VK_ENTER) {	//place a pattern
					int turn = currentPlayer.turnPlayer();
					
					boolean friendlyBorder = false;
					for (int i=0; i<6; i++){
						Tile temp = tiles[r+patterns[p].r(i)][c+patterns[p].c(i)];
						if (tiles[temp.r()+1][temp.c()].getState().equals("path" + turn))
							friendlyBorder = true;
						if (tiles[temp.r()-1][temp.c()].getState().equals("path" + turn))
							friendlyBorder = true;
						if (tiles[temp.r()][temp.c()+1].getState().equals("path" + turn))
							friendlyBorder = true;
						if (tiles[temp.r()][temp.c()-1].getState().equals("path" + turn))
							friendlyBorder = true;
					}
					if (!(canPlace && friendlyBorder)) {
						String error = "";
						if (!canPlace)
							error = "One or more tiles are overlapping with a path already in the dungeon.";
						if (!friendlyBorder)
							error = "A tile needs to touch a friendly path.";
	         			JOptionPane.showConfirmDialog(null, error + "\nDo something about it.", 
	         					"Can Not Place Your Die", JOptionPane.YES_NO_OPTION);
					}
					if (canPlace && friendlyBorder) {
						for (int i=0; i<6; i++)
							tiles[r+patterns[p].r(i)][c+patterns[p].c(i)].setState("path" + turn);
						canPlace = false;				
						addMonsterToTile(monsterToSummon, tiles[r][c]);	
						monsterToSummon = null;
					}
					return;
				}		
			}
		}

		public void keyReleased(KeyEvent e) {}

		public void keyTyped(KeyEvent e) {}
	}
}
