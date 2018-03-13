package ddm;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Player {
	
	private final int turnPlayer;
	private int atk = 0, def = 0, move = 0, spell = 0;
	private String name = "";
	private ArrayList <Monster> summonedMonsters = new ArrayList<Monster>();
	private int destroyedMonsters = 0;
	private String color;
	private Color panelForeground;
	private JPanel panel;
	private Panel mainPanel;
	private JLabel nameLabel, attackLabel, defenceLabel, moveLabel, spellLabel, trapLabel;
	private ArrayList <Monster> juunishishiArray = new ArrayList<Monster>();
	
	private int dieBonus = 0;
	
	private String[] nameTemp = {"Yugi", "Tristan", "Mai", "Joey Wheeler", "Bakura", "Marik", "Kaiba", 
			"Pegasus", "Duke Devlin", "Jack Atlas", "Mokuba", "Tea", "Jaden", "Youngster Joey", "Weevil", "Dartz"};
	
	private ArrayList <Monster> allMonsters = new ArrayList<Monster>();
	
	
	public Player(int turn, Panel main_panel) {
		turnPlayer = turn;
		mainPanel = main_panel;
		
		setUpPanel();
		
		randomizeName();
		
		if (turnPlayer == 1)
			color = "blue";
		if (turnPlayer == 2)
			color = "orange";
		if (turnPlayer != 1 && turnPlayer != 2) {
			color = null;
		}
		setUpAllMonsters();
		setUpJuunishishi();
	}
	
	public void setUpAllMonsters() {
		allMonsters.add(new Monster("Kuriboh", 15, 10, 10, this, mainPanel, 1));
		allMonsters.add(new Monster("Uraby", 25, 25, 10, this, mainPanel, 2));
		allMonsters.add(new Monster("Dark Magician", 35, 40, 20, this, mainPanel, 3));
		allMonsters.add(new Monster("Rabidragon", 60, 40, 40, this, mainPanel, 4));
		allMonsters.add(new Monster("Labyrinth Wall", 45, 0, 0, this, mainPanel, 1));
		allMonsters.add(new Monster("Mr. Volcano", 15, 15, 5, this, mainPanel, 2));
		allMonsters.add(new Monster("Wall Shadow", 10, 25, 0, this, mainPanel, 2));
		allMonsters.add(new Monster("Dangerous Machine Type-6", 15, 0, 0, this, mainPanel, 1));
		allMonsters.add(new Monster("Overdrive", 5, 8, 8, this, mainPanel, 1));
		allMonsters.add(new Monster("Gandora The Dragon Of Destruction", 2, 25, 0, this, mainPanel, 3));
		allMonsters.add(new Monster("Interplanetarypurplythorny Dragon", 15, 15, 10, this, mainPanel, 2));
		allMonsters.add(new Monster("Interplanetarypurplythorny Beast", 25, 8, 10, this, mainPanel, 2));
		allMonsters.add(new Monster("TRUE KING V.F.D, THE BEAST", 30, 21, 5, this, mainPanel, 4));
		allMonsters.add(new Monster("Shining Friendship", 5, 2, 0, this, mainPanel, 1));
		allMonsters.add(new Monster("Penguin Soldier", 8, 8, 5, this, mainPanel, 1));
		allMonsters.add(new Monster("Madolche Queen Tiaramisu", 999, 999, 999, this, mainPanel, 5));
		allMonsters.add(new Monster("Gameciel, The Sea Turtle Kaiju", 20, 20, 15, this, mainPanel, 3));
		allMonsters.add(new Monster("Blackwing - Shura The Blue Flame", 15, 21, 10, this, mainPanel, 2));
		allMonsters.add(new Monster("Blackwing - Gale The Whirlwind", 15, 15, 8, this, mainPanel, 2));
		allMonsters.add(new Monster("Junk Synchron", 15, 15, 10, this, mainPanel, 2));
		allMonsters.add(new Monster("King Of The Skull Servants", 1, 0, 0, this, mainPanel, 1));
		allMonsters.add(new Monster("Juunishishi Molmorat", 5, 0, 0, this, mainPanel, 1));
		allMonsters.add(new Monster("Juunishishi Thoroughblade", 2, 7, 0, this, mainPanel, 1));
		//allMonsters.add(new Monster("Neo Spacian Grand Mole", 10, 1, 0, this, mainPanel, 1));
		
	}
	
	public void addAtk(int i) {
		atk = atk + i;
		attackLabel.setText(" " + atk);
	}
	public void addDef(int i) {
		def = def + i;
		defenceLabel.setText(" " + def);
	}
	public void addMove(int i) {
		move = move + i;
		moveLabel.setText(" " + move);
	}
	public void addSpell(int i) {
		spell = spell + i;
		spellLabel.setText(" " + spell);
	}
	
	public void subtractAtk(int i) {
		atk = atk - i;
		attackLabel.setText(" " + atk);
	}
	public void subtractDef(int i) {
		def = def - i;
		defenceLabel.setText(" " + def);
	}
	public void subtractMove(int i) {
		move = move - i;
		moveLabel.setText(" " + move);
	}
	public void subtractSpell(int i) {
		spell = spell - i;
		spellLabel.setText(" " + spell);
	}
	
	public void setDieBonus(int newDieBonus) {
		dieBonus = newDieBonus;
	}
	public int dieBonus() {
		return dieBonus;
	}
	
	public ArrayList <Monster> summonedMonsters() {
		return summonedMonsters;
	}
	public void summonAMonster(Monster monster) {
		summonedMonsters.add(monster);	//monster lord not included
		
		int index = allMonsters.indexOf(monster);	//for one time use of every monster
		if (index >= 0) {
			allMonsters.remove(monster);
		}
	}
	public void destroyMonster (Monster monster) {
		summonedMonsters.remove(monster);
		destroyedMonsters++;
		
		onDestroyMonsterAbilities();
	}
	public int destroyedMonsters() {
		return destroyedMonsters;
	}
	public void onDestroyMonsterAbilities() {
		for (Monster mon : summonedMonsters) {
			if (mon.name().equalsIgnoreCase("King Of The Skull Servants")) {
				mon.ability();
			}
			else if (mon.name().equalsIgnoreCase("Mr. Volcano")) {
				mon.ability();
			}
		}
	}
	
	public int turnPlayer() {
		return turnPlayer;
	}
	
	public int atk() {
		return atk;
	}
	public int def() {
		return def;
	}
	public int move() {
		return move;
	}
	public int spell() {
		return spell;
	}
	public String name() {
		return name;
	}
	public void randomizeName() {
		name = nameTemp[(int)(Math.random()*nameTemp.length)];
		nameLabel.setText(name);
	}
	public String color() {
		return color;
	}
	public ArrayList<Monster> allMonsters() {
		return allMonsters;
	}
	public ArrayList<Monster> juunishishiArray() {
		return juunishishiArray;
	}
	
	private void setUpPanel() {
		panel = new JPanel();
		panel.setSize(126, 250);
		panel.setLayout(new GridLayout(5,0));
		if (turnPlayer == 1) {
			panel.setBackground(Color.blue);
			panelForeground = Color.orange;
		}
		else if (turnPlayer == 2) {
			panel.setBackground(Color.orange);
			panelForeground = Color.blue;
		}
		
		nameLabel = new JLabel();
		nameLabel.setSize(126,25);
		nameLabel.setFocusable(false);
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		nameLabel.setForeground(panelForeground);
		nameLabel.setFont(new Font("Ariel", Font.BOLD, 16));
		nameLabel.setText(name);
		nameLabel.setBorder(BorderFactory.createLineBorder(Color.black, 3));
		panel.add(nameLabel);
		
		attackLabel = new JLabel();
		attackLabel.setSize(100,25);
		attackLabel.setFocusable(false);
		attackLabel.setHorizontalAlignment(SwingConstants.CENTER);
		attackLabel.setForeground(panelForeground);
		attackLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
		attackLabel.setText(" " + atk);
		attackLabel.setIcon(getIcon("atk1", 25, 25));
		panel.add(attackLabel);
		
		defenceLabel = new JLabel();
		defenceLabel.setSize(100,25);
		defenceLabel.setFocusable(false);
		defenceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		defenceLabel.setForeground(panelForeground);
		defenceLabel.setFont(attackLabel.getFont());
		defenceLabel.setText(" " + def);
		defenceLabel.setIcon(getIcon("def1", 25, 25));
		panel.add(defenceLabel);
		
		moveLabel = new JLabel();
		moveLabel.setSize(100,25);
		moveLabel.setFocusable(false);
		moveLabel.setHorizontalAlignment(SwingConstants.CENTER);
		moveLabel.setForeground(panelForeground);
		moveLabel.setFont(attackLabel.getFont());
		moveLabel.setText(" " + move);
		moveLabel.setIcon(getIcon("move1", 25, 25));
		panel.add(moveLabel);
		
		spellLabel = new JLabel();
		spellLabel.setSize(100,25);
		spellLabel.setFocusable(false);
		spellLabel.setHorizontalAlignment(SwingConstants.CENTER);
		spellLabel.setForeground(panelForeground);
		spellLabel.setFont(attackLabel.getFont());
		spellLabel.setText(" " + spell);
		spellLabel.setIcon(getIcon("spell1", 25, 25));
		panel.add(spellLabel);
		
		trapLabel = new JLabel();
		trapLabel.setSize(100,25);
		trapLabel.setFocusable(false);
		trapLabel.setHorizontalAlignment(SwingConstants.CENTER);
		trapLabel.setForeground(panelForeground);
		trapLabel.setFont(attackLabel.getFont());
		trapLabel.setText(" " + spell);	//note the spell
		trapLabel.setIcon(getIcon("trap1", 25, 25));
		//panel.add(trapLabel);	//add trap? (change gridlayout)
	}
	public JPanel panel() {
		return panel;
	}
	
	private ImageIcon getIcon(String picName, int width, int height) {
		URL imageURL = getClass().getResource("resources/" + picName + ".jpg");	
		if (imageURL != null) {
			ImageIcon imageIcon = new ImageIcon(imageURL);
			Image image = imageIcon.getImage();
			Image newimg = image.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH);
			imageIcon = new ImageIcon(newimg);
			if (imageIcon != null)
				return imageIcon;
		}
		JOptionPane.showConfirmDialog(null, "ERROR 69: invalid pic inside Player class.\n"
				+ "Do something about it.", 
					"ERROR", JOptionPane.ERROR_MESSAGE);
		return null;
	}
	
	public void setUpJuunishishi() {
		juunishishiArray.add(new Monster("Juunishishi Cluckle", 6, 4, 0, this, mainPanel, 1));
		juunishishiArray.add(new Monster("Juunishishi Rabbina", 4, 4, 0, this, mainPanel, 1));
		juunishishiArray.add(new Monster("Juunishishi Ram", 8, 0, 20, this, mainPanel, 1));
		juunishishiArray.add(new Monster("Juunishishi Viper", 4, 6, 0, this, mainPanel, 1));
		juunishishiArray.add(new Monster("Juunishishi Drancia", 5, 8, 0, this, mainPanel, 2));
		juunishishiArray.add(new Monster("Juunishishi Bullhorn", 8, 5, 5, this, mainPanel, 2));
		juunishishiArray.add(new Monster("Juunishishi Wildbow", 4, 7, 0, this, mainPanel, 2));
		juunishishiArray.add(new Monster("Juunishishi Hammerkong", 7, 7, 4, this, mainPanel, 2));
		juunishishiArray.add(new Monster("Juunishishi Lyca", 6, 6, 0, this, mainPanel, 2));
		juunishishiArray.add(new Monster("Juunishishi Tigress", 6, 8, 0, this, mainPanel, 2));
		juunishishiArray.add(new Monster("Juunishishi Thoroughblade", 2, 7, 0, this, mainPanel, 1));
		juunishishiArray.add(new Monster("Juunishishi Molmorat", 5, 0, 0, this, mainPanel, 1));
	}
	
	public String toString() {
		return "atk: " + atk + "\tdef: " + def + "\tmove: " + move + "\tspell: " + spell;
	}
	
}
