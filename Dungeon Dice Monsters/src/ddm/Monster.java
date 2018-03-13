package ddm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Monster {
	
	private String name = "", showName = "", memeText = "";
	private ArrayList <String> abilityText = new ArrayList<String>(), abilityCostPic = new ArrayList<String>(),
			abilityCostxNum = new ArrayList<String>();
	private int hp = 0, atk = 0, def = 0, originalHp = 0, level = 0;
	private Player owner;
	private int attacksOriginalAmount = 1, attacksLeft = 1;
	private JPanel rightShark;
	private Panel mainPanel;
	private JLabel monsterPicLabel, monsterHPLabel, monsterATKLabel, monsterDEFLabel, monsterLvLabel, monsterAbilityLabel;
	private JLabel[] monsterAbilityCostLabel;
	private JTextPane[] monsterAbilityTextPane;
	private JTextPane monsterNameTextPane, monsterMemeTextPane;
	private Tile myTile = null;
	private Tile[][] tiles;
	private int moveOpponentCost = 0;
	private Monster monsterToSummon = null;
	private JLabel lastLabelTouchedInGrid = null;
	
	private ArrayList <Tile> abilityTiles = new ArrayList<Tile>();
	private ArrayList <Monster> abilityMonsters = new ArrayList<Monster>();
	
    private boolean hasActivatableAbility = false;
	
	public Monster(String my_name, int my_hp, int my_atk, int my_def, Player my_owner, Panel main_panel, int my_level) {
		name = my_name;
		hp = my_hp;
		atk = my_atk;
		def = my_def;
		originalHp = hp;
		owner = my_owner;
		mainPanel = main_panel;
		tiles = mainPanel.getTiles();
		level = my_level;
		
		setUpAbility();
		setUpPanel();
	}
	
	private void setUpAbility() {
		if (name.equalsIgnoreCase("Monster Lord 1")) {
			hasActivatableAbility = true;
			
			abilityText.add("Spend ALL (20+) crests you own to destroy all neiboring monsters in a 5x5 square");
			abilityCostPic.add("cost20");
			abilityCostxNum.add("");
			
			memeText = "Mirage's Opinion";
			return;
		}
		else if (name.equalsIgnoreCase("Monster Lord 2")) {
			hasActivatableAbility = true;
			
			abilityText.add("Spend ALL (20+) crests you own to destroy all neiboring monsters in a 5x5 square");
			abilityCostPic.add("cost20");
			abilityCostxNum.add("");
			
			memeText = "Mesa's opinion";
			return;
		}
		else if (name.equalsIgnoreCase("Kuriboh")) {
			hasActivatableAbility = false;
			memeText = "I tributed Yugi to summon this";
			return;
		}
		else if (name.equalsIgnoreCase("Uraby")) {
			hasActivatableAbility = false;
			memeText = "ITS URABY";
			return;
		}
		else if (name.equalsIgnoreCase("Dark Magician")) {
			hasActivatableAbility = false;
			memeText = "Look at my long, shiny staff";
			return;
		}
		else if (name.equalsIgnoreCase("Rabidragon")) {
			hasActivatableAbility = false;
			
			abilityText.add("Its a blue-peeps, but rabid");
			abilityCostPic.add("atk1");
			abilityCostxNum.add("");
			
			abilityText.add("Very intimidating");
			abilityCostPic.add("def1");
			abilityCostxNum.add("");
			
			abilityText.add("Where did you hide the rabbit?");
			abilityCostPic.add("spell1");
			abilityCostxNum.add("");
			
			abilityText.add("Much GG");
			abilityCostPic.add("move1");
			abilityCostxNum.add("");
			
			memeText = "Pull the rabbit out of the hat";
			return;
		}
		else if (name.equalsIgnoreCase("Labyrinth Wall")) {
			hasActivatableAbility = false;
			
			abilityText.add("Can move up to 3 spaces for each movement crest spent");
			abilityCostPic.add("move1");
			abilityCostxNum.add("");
			
			memeText = "Let's build a wall";
			return;
		}
		else if (name.equalsIgnoreCase("Mr. Volcano")) {
			hasActivatableAbility = false;
			
			abilityText.add("On end phase: deal 3 damage to a random opponent's monster");
			abilityCostPic.add("cost0");
			abilityCostxNum.add("");
			
			abilityText.add("When a monster you control is destroyed: deal 2 damage to a random opponent's monster");
			abilityCostPic.add("cost0");
			abilityCostxNum.add("");
			
			memeText = "Something something burn damage";
			return;
		}
		else if (name.equalsIgnoreCase("Wall Shadow")) {
			hasActivatableAbility = false;
			
			abilityText.add("Can move up to 2 spaces for each movement crest spent");
			abilityCostPic.add("move1");
			abilityCostxNum.add("");
			
			abilityText.add("Can move up to 5 spaces for each movement crest spent if you control \"Labyrinth Wall\"");
			abilityCostPic.add("move1");
			abilityCostxNum.add("");
			
			memeText = "Slide into those DMs like";
			return;
		}
		else if (name.equalsIgnoreCase("Dangerous Machine Type-6")) {
			hasActivatableAbility = false;
			
			abilityText.add("On roll: randomly add or subtract up to 3 dice in your standard roll");
			abilityCostPic.add("cost0");
			abilityCostxNum.add("");
			
			memeText = "Let's play a little game";
			return;
		}
		else if (name.equalsIgnoreCase("Overdrive")) {
			hasActivatableAbility = true;
			
			abilityText.add("Deal 10 damage to all monsters in a straight line");
			abilityCostPic.add("atk1");
			abilityCostxNum.add("x3");
			
			abilityText.add("Can attack 2 times per turn");
			abilityCostPic.add("atk1");
			abilityCostxNum.add("");
			attacksOriginalAmount = 2;
			attacksLeft = attacksOriginalAmount;
			
			memeText = "Dakka dakka dakka";
			return;
		}
		else if (name.equalsIgnoreCase("Gandora The Dragon Of Destruction")) {
			hasActivatableAbility = false;
			
			abilityText.add("On summon: deal up to 100 damage to a single target");
			abilityCostPic.add("cost0");
			abilityCostxNum.add("");
			
			memeText = "*explosion noises";
			return;
		}
		else if (name.equalsIgnoreCase("Interplanetarypurplythorny Dragon")) {
			hasActivatableAbility = false;
			
			abilityText.add("Can attack 2 times per turn");
			abilityCostPic.add("atk1");
			abilityCostxNum.add("");
			attacksOriginalAmount = 2;
			attacksLeft = attacksOriginalAmount;
			
			abilityText.add("On being attacked: the attacker takes 3 damage");
			abilityCostPic.add("def1");
			abilityCostxNum.add("x0");
			
			memeText = "For Vanu\nBecause of this card I have to use text areas for names";
			return;
		}
		else if (name.equalsIgnoreCase("Interplanetarypurplythorny Beast")) {
			hasActivatableAbility = false;
			
			abilityText.add("On being attacked: the attacker takes damage equal to its attack");
			abilityCostPic.add("spell1");
			abilityCostxNum.add("x1");
			
			abilityText.add("On being attacked: the attacker takes 3 damage");
			abilityCostPic.add("def1");
			abilityCostxNum.add("x0");
			
			memeText = "MOAR VANU\nBecause of this card I have to use text areas for names";
			return;
		}
		else if (name.equalsIgnoreCase("TRUE KING V.F.D, THE BEAST")) {
			hasActivatableAbility = false;
			
			abilityText.add("Can attack 5 times per turn");
			abilityCostPic.add("atk1");
			abilityCostxNum.add("");
			attacksOriginalAmount = 5;
			attacksLeft = attacksOriginalAmount;
			
			abilityText.add("Can move up to 2 spaces for each movement crest spent");
			abilityCostPic.add("move1");
			abilityCostxNum.add("");
			
			memeText = "A small problem";
			return;
		}
		else if (name.equalsIgnoreCase("Shining Friendship")) {
			hasActivatableAbility = true;
			
			abilityText.add("Heal a monster to full health");
			abilityCostPic.add("spell1");
			abilityCostxNum.add("x1");
			
			abilityText.add("On summon: heal your Monster Lord for 1 hp if your opponent's Monster Lord has more hp");
			abilityCostPic.add("cost0");
			abilityCostxNum.add("");
			
			memeText = "Quick get me 20 cc's of friendship!";
			return;
		}
		else if (name.equalsIgnoreCase("Penguin Soldier")) {
			hasActivatableAbility = true;
			
			abilityText.add("Move an opponent's monster by using your movement crests");
			abilityCostPic.add("spell1");
			abilityCostxNum.add("x2");
			moveOpponentCost = 2;
			
			memeText = "Flip me face up, I dare you";
			return;
		}
		else if (name.equalsIgnoreCase("Blackwing - Shura The Blue Flame")) {
			hasActivatableAbility = false;
			
			abilityText.add("On destroying a monster by battle: roll an extra die next turn");
			abilityCostPic.add("cost0");
			abilityCostxNum.add("");
			
			abilityText.add("On summon: if you control another Blackwing monster, gain an extra roll next turn");
			abilityCostPic.add("cost0");
			abilityCostxNum.add("");
			
			memeText = "Let's just swarm the field";
			return;
		}
		else if (name.equalsIgnoreCase("Madolche Queen Tiaramisu")) {
			hasActivatableAbility = false;
			
			abilityText.add("Can attack unlimited times per turn");
			abilityCostPic.add("atk1");
			abilityCostxNum.add("");
			attacksOriginalAmount = Integer.MAX_VALUE;
			attacksLeft = attacksOriginalAmount;
			
			abilityText.add("Attacking gives the player +1 attack crest");
			abilityCostPic.add("atk1");
			abilityCostxNum.add("");
			
			abilityText.add("Movement gives the player +1 movement crest per tile");
			abilityCostPic.add("move1");
			abilityCostxNum.add("");
			
			memeText = "You can't spell \"Madolche\" without \"mad\"";
			return;
		}
		else if (name.equalsIgnoreCase("Gameciel, The Sea Turtle Kaiju")) {
			hasActivatableAbility = true;
			
			abilityText.add("Tribute an opponent's monster and replace it with this monster under your opponent's control"
					+ "\n(if you do, this monster's cost to attack will become 3 attack crests)");
			abilityCostPic.add("spell1");
			abilityCostxNum.add("x0");
			
			memeText = "Do you want a Kaiju Sea Turtle?";
			return;
		}
		else if (name.equalsIgnoreCase("Gameciel The Sea Turtle Kaiju")) {
			hasActivatableAbility = false;
			
			abilityText.add("The cost to attack is 3 attack crests");
			abilityCostPic.add("atk1");
			abilityCostxNum.add("x3");
			
			memeText = "Enjoy the Kaiju Sea Turtle";
			return;
		}
		else if (name.equalsIgnoreCase("Blackwing - Gale The Whirlwind")) {
			hasActivatableAbility = true;
			
			abilityText.add("Half the atk of an opponent's monster and reduce its def by 10");
			abilityCostPic.add("spell1");
			abilityCostxNum.add("x1");
			
			abilityText.add("On summon: if you control another Blackwing monster, gain an extra roll next turn");
			abilityCostPic.add("cost0");
			abilityCostxNum.add("");
			
			memeText = "Let's just swarm the field";
			return;
		}
		else if (name.equalsIgnoreCase("Junk Synchron")) {
			hasActivatableAbility = true;
			
			abilityText.add("Tribute 2 monsters of the same level to summon a 1 level higher monster "
					+ "(this will spawn a new path)");
			abilityCostPic.add("spell1");
			abilityCostxNum.add("x1");
			
			memeText = "I'm going to ride my girlfriend all night long - I'm talking about the motorcycle";
			return;
		}
		else if (name.equalsIgnoreCase("King Of The Skull Servants")) {
			hasActivatableAbility = false;
			
			abilityText.add("This monster gains 2 hp, 3 atk, and 1 def for each of your destroyed monsters");
			abilityCostPic.add("cost0");
			abilityCostxNum.add("");
			
			memeText = "Who said level 1 monsters are weak?";
			return;
		}
		else if (name.contains("Juunishishi")) {
			hasActivatableAbility = true;
			
			abilityText.add("Destroy this monster and summon a different juunishishi monster in its place");
			abilityCostPic.add("spell1");
			if (name.equalsIgnoreCase("Juunishishi Molmorat") || name.equalsIgnoreCase("Juunishishi Thoroughblade"))
				abilityCostxNum.add("x2");
			else
				abilityCostxNum.add("x0");
			
			memeText = "Guess the zodiac!\nYes, all of them are actually in the game.";
			return;
		}	
		else if (name.equalsIgnoreCase("Neo Spacian Grand Mole")) {
			hasActivatableAbility = false;
			
			abilityText.add("When this monster battles: "
						+ "return both this monster and the monster it is battling to their owner's monster pool");
			abilityCostPic.add("cost0");
			abilityCostxNum.add("");
			
			abilityText.add("The cost to attack is 3 attack crests");
			abilityCostPic.add("atk1");
			abilityCostxNum.add("x3");
			
			memeText = "Bounce!\nThis is definitely not a troll card.";
			return;
		}	
		
		else
			System.out.println("Error 69: FORGOT TO ADD ABILITIES");
	}
	
	public String abilityPhaseText() {
		if (name.equalsIgnoreCase("Overdrive"))
			return "Click a direction";
		else if (name.equalsIgnoreCase("Gandora The Dragon Of Destruction"))
			return "Choose a target";
		else if (name.equalsIgnoreCase("Shining Friendship"))
			return "Choose a target";
		else if (name.equalsIgnoreCase("Penguin Soldier"))
			return "Choose a target";
		else if (name.equalsIgnoreCase("Gameciel, The Sea Turtle Kaiju"))
			return "Choose a tribute";
		else if (name.equalsIgnoreCase("Blackwing - Gale The Whirlwind"))
			return "Choose a target";
		else if (name.equalsIgnoreCase("Junk Synchron"))
			return "Select the tributes";
		else if (name.contains("Juunishishi"))
			return "Select a monster";
		
		else if (hasActivatableAbility)
			return "UNKNOWN ABILITY";
		return "~";
	}
	
	public void summon() {
		if (name.equalsIgnoreCase("Gandora The Dragon Of Destruction")) {
			if (mainPanel.opponent().summonedMonsters().size() < 1) {
				mainPanel.changePhase("Action");
				return;
			}
			JOptionPane.showMessageDialog(null, owner.name() + " (" + owner.color() + "), "
					+ " select a monster to damage for " + name + "'s ability.", 
	 				name + "'s ability", JOptionPane.INFORMATION_MESSAGE);
			for (Monster mon : mainPanel.opponent().summonedMonsters()) {
				abilityTiles.add(mon.tile());
				mon.tile().setBorder(BorderFactory.createLineBorder(Color.green, 3));
			}
		}
		else if (name.equalsIgnoreCase("Shining Friendship")) {
			Monster[] lords = new Monster[3];
			lords[2] = mainPanel.getTiles()[2][(mainPanel.getTiles().length-1)/2].monster();
			lords[1] = mainPanel.getTiles()[mainPanel.getTiles().length-3][(mainPanel.getTiles().length-1)/2].monster();
			int turnLord = mainPanel.currentPlayer().turnPlayer();
			int opponentLord = mainPanel.opponent().turnPlayer();
			if (lords[turnLord].hp() < lords[opponentLord].hp()) {
				lords[turnLord].changeHp(lords[turnLord].hp() + 1);
				mainPanel.updateMonsterPanel(lords[turnLord]);
			}
			mainPanel.changePhase("Action");
		}
		else if (name.contains("Blackwing")) {
			boolean hasOtherBlackwing = false;
			for (Monster mon : owner.summonedMonsters())
				if (mon.name().contains("Blackwing") && !mon.name().equalsIgnoreCase(this.name))
					hasOtherBlackwing = true;
			if (hasOtherBlackwing) {
				owner.setDieBonus(owner.dieBonus() + 1);
			}
			mainPanel.changePhase("Action");
		}
		else if (name.equalsIgnoreCase("King Of The Skull Servants")) {
			atk = 3 * owner.destroyedMonsters();
			def = 1 * owner.destroyedMonsters();
			originalHp = originalHp + (2 * owner.destroyedMonsters());
			hp = hp + (2 * owner.destroyedMonsters());
			mainPanel.updateMonsterPanel(this);
		}
		else if (name.equalsIgnoreCase("Juunishishi Molmorat") 
				|| name.equalsIgnoreCase("Juunishishi Thoroughblade")) {
			Monster monsterToRemove = null;
			
			for (Monster mon : owner.allMonsters())
				if (mon.name().equalsIgnoreCase(name))
					monsterToRemove = mon;
			if (monsterToRemove != null)
				owner.allMonsters().remove(monsterToRemove);
			
			for (Monster mon : owner.juunishishiArray())
				if (mon.name().equalsIgnoreCase(name))
					monsterToRemove = mon;
			if (monsterToRemove != null)
				owner.juunishishiArray().remove(monsterToRemove);
			
			mainPanel.changePhase("Action");
		}
		else if (name.contains("Juunishishi")) {
			Monster monsterToRemove = null;
			
			for (Monster mon : owner.juunishishiArray())
				if (mon.name().equalsIgnoreCase(name))
					monsterToRemove = mon;
			if (monsterToRemove != null)
				owner.juunishishiArray().remove(monsterToRemove);

			mainPanel.changePhase("Action");
		}
		
		else
			mainPanel.changePhase("Action");
	}
	
	public void ability() {
		if (name.equalsIgnoreCase("Monster Lord 1") || name.equalsIgnoreCase("Monster Lord 2")) {
			if (owner.atk() + owner.def() + owner.move() + owner.spell() < 20) {
				JOptionPane.showMessageDialog(null, owner.name() + " (" + owner.color() + "), "
						+ " you need at least 20 crests to activate " + name + "'s ability.", 
		 				"Not enough crests", JOptionPane.INFORMATION_MESSAGE);
				mainPanel.changePhase("Action");
 				return;
			}
 			int response = JOptionPane.showConfirmDialog(null, 
 					owner.name() + " (" + owner.color() + "),"
 					+ " do you want to destroy all monsters in a 5x5 square around " + name + "?\n"
	    				+ "This will cost all of your crests (20 minimum).",
	    				name + "'s ability",
	    				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
 			if (response != JOptionPane.YES_OPTION) {
 				mainPanel.changePhase("Action");
 				return;
 			}
 			owner.subtractAtk(owner.atk());
 			owner.subtractDef(owner.def());
 			owner.subtractMove(owner.move());
 			owner.subtractSpell(owner.spell());
 			
			for (int r=-2; r<3; r++)
				for (int c=-2; c<3; c++)
					if (tiles[myTile.r()+r][myTile.c()+c].monster() != null 
							&& !tiles[myTile.r()+r][myTile.c()+c].monster().name().contains("Monster Lord")
							&& (tiles[myTile.r()+r][myTile.c()+c].getState().equalsIgnoreCase("path1") 
									|| tiles[myTile.r()+r][myTile.c()+c].getState().equalsIgnoreCase("path2"))) 
					{
						tiles[myTile.r()+r][myTile.c()+c].monster().changeHp(0);
						mainPanel.updateMonsterPanel(tiles[myTile.r()+r][myTile.c()+c].monster());
						tiles[myTile.r()+r][myTile.c()+c].checkAndDestroyMonster();
					}	
			mainPanel.changePhase("Action");
		}
		else if (name.equalsIgnoreCase("Overdrive")) {
			if (owner.atk() < 3) {
				JOptionPane.showMessageDialog(null, owner.name() + " (" + owner.color() + "), "
						+ " you need at least 3 attack crests to activate " + name + "'s ability.", 
		 				"Not enough crests", JOptionPane.INFORMATION_MESSAGE);
				mainPanel.changePhase("Action");
 				return;
			}
 			int response = JOptionPane.showConfirmDialog(null, 
 					owner.name() + " (" + owner.color() + "),"
 					+ " do you want to deal 10 damage to all monsters in a straight line from " + name + "?\n"
	    				+ "This will cost 3 attack crests.",
	    				name + "'s ability",
	    				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
 			if (response != JOptionPane.YES_OPTION) {
 				mainPanel.changePhase("Action");
 				return;
 			}
 			owner.subtractAtk(3);
 			JOptionPane.showMessageDialog(null, owner.name() + " (" + owner.color() + "), "
					+ " click on a direction.", 
	 				name + "'s ability", JOptionPane.INFORMATION_MESSAGE);
 			for (int i=0; i<tiles.length; i++) {
 				Tile temp = tiles[i][myTile.c()];
 				if (!temp.getState().equalsIgnoreCase("dne")) {
 					abilityTiles.add(temp);
 					temp.setBackground(Color.green);
 					if (temp.monster() != null)
 						temp.setBorder(BorderFactory.createLineBorder(Color.green, 3));
 				}
 			}
 			for (int i=0; i<tiles[0].length; i++) {
 				Tile temp = tiles[myTile.r()][i];
 				if (!temp.getState().equalsIgnoreCase("dne")) {
 					abilityTiles.add(temp);
 					temp.setBackground(Color.green);
 					if (temp.monster() != null)
 						temp.setBorder(BorderFactory.createLineBorder(Color.green, 3));
 				}
 			}
		}
		else if (name.equalsIgnoreCase("Shining Friendship")) {
			boolean hasHealableTarget = false;
			for (Monster mon : owner.summonedMonsters()) {
 				if (mon.hp() < mon.originalHp()) {
 					hasHealableTarget = true;
 				}
			}
 			for (Monster mon : mainPanel.opponent().summonedMonsters()) {
 				if (mon.hp() < mon.originalHp()) {
 					hasHealableTarget = true;
 				}
			}
			if (!hasHealableTarget) {
				JOptionPane.showMessageDialog(null, "There are no targets.", 
		 				name + "'s ability", JOptionPane.INFORMATION_MESSAGE);
				mainPanel.changePhase("Action");
				return;
			}
			if (owner.spell() < 1) {
				JOptionPane.showMessageDialog(null, owner.name() + " (" + owner.color() + "), "
						+ " you need at least 1 spell crest to activate " + name + "'s ability.", 
		 				"Not enough crests", JOptionPane.INFORMATION_MESSAGE);
				mainPanel.changePhase("Action");
 				return;
			}
 			int response = JOptionPane.showConfirmDialog(null, 
 					owner.name() + " (" + owner.color() + "),"
 					+ " do you want to heal a monster to max hp?\n"
	    				+ "This will cost 1 spell crest.",
	    				name + "'s ability",
	    				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
 			if (response != JOptionPane.YES_OPTION) {
 				mainPanel.changePhase("Action");
 				return;
 			}
 			owner.subtractSpell(1);
 			for (Monster mon : owner.summonedMonsters()) {
 				if (mon.hp() < mon.originalHp()) {
 					abilityTiles.add(mon.tile());
 					mon.tile().setBorder(BorderFactory.createLineBorder(Color.green, 3));
 				}
			}
 			for (Monster mon : mainPanel.opponent().summonedMonsters()) {
 				if (mon.hp() < mon.originalHp()) {
 					abilityTiles.add(mon.tile());
 					mon.tile().setBorder(BorderFactory.createLineBorder(Color.green, 3));
 				}
			}
		}
		else if (name.equalsIgnoreCase("Penguin Soldier")) {
			if (mainPanel.opponent().summonedMonsters().size() < 1) {
				JOptionPane.showMessageDialog(null, "There are no targets.", 
		 				name + "'s ability", JOptionPane.INFORMATION_MESSAGE);
				mainPanel.changePhase("Action");
				return;
			}
			if (owner.spell() < 2) {
				JOptionPane.showMessageDialog(null, owner.name() + " (" + owner.color() + "), "
						+ " you need at least 2 spell crests to activate " + name + "'s ability.", 
		 				"Not enough crests", JOptionPane.INFORMATION_MESSAGE);
				mainPanel.changePhase("Action");
 				return;
			}
 			int response = JOptionPane.showConfirmDialog(null, 
 					owner.name() + " (" + owner.color() + "),"
 					+ " do you want to move an opponent's monster?\n"
	    				+ "This will cost 2 spell crests + movement crests.",
	    				name + "'s ability",
	    				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
 			if (response != JOptionPane.YES_OPTION) {
 				mainPanel.changePhase("Action");
 				return;
 			}
 			owner.subtractSpell(2);
 			for (Monster mon : mainPanel.opponent().summonedMonsters()) {
 				abilityTiles.add(mon.tile());
 				mon.tile().setBorder(BorderFactory.createLineBorder(Color.green, 3));
			}
		
		}
		else if (name.equalsIgnoreCase("Gameciel, The Sea Turtle Kaiju")) {
			if (mainPanel.opponent().summonedMonsters().size() < 1) {
				JOptionPane.showMessageDialog(null, "There are no targets.", 
		 				name + "'s ability", JOptionPane.INFORMATION_MESSAGE);
				mainPanel.changePhase("Action");
				return;
			}
 			int response = JOptionPane.showConfirmDialog(null, 
 					owner.name() + " (" + owner.color() + "),"
 					+ " do you want to tribute an opponent's monster for " + name + "?\n",
	    				name + "'s ability",
	    				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
 			if (response != JOptionPane.YES_OPTION) {
 				mainPanel.changePhase("Action");
 				return;
 			}
 			for (Monster mon : mainPanel.opponent().summonedMonsters()) {
 				abilityTiles.add(mon.tile());
 				mon.tile().setBorder(BorderFactory.createLineBorder(Color.green, 3));
			}
		}
		else if (name.equalsIgnoreCase("Blackwing - Gale The Whirlwind")) {
			if (mainPanel.opponent().summonedMonsters().size() < 1) {
				JOptionPane.showMessageDialog(null, "There are no targets.", 
		 				name + "'s ability", JOptionPane.INFORMATION_MESSAGE);
				mainPanel.changePhase("Action");
				return;
			}
			if (owner.spell() < 1) {
				JOptionPane.showMessageDialog(null, owner.name() + " (" + owner.color() + "), "
						+ " you need at least 1 spell crest to activate " + name + "'s ability.", 
		 				"Not enough crests", JOptionPane.INFORMATION_MESSAGE);
				mainPanel.changePhase("Action");
 				return;
			}
 			int response = JOptionPane.showConfirmDialog(null, 
 					owner.name() + " (" + owner.color() + "),"
 					+ " do you want to half the attack and reduce 10 defence of an opponent's monster?\n"
	    				+ "This will cost 1 spell crest.",
	    				name + "'s ability",
	    				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
 			if (response != JOptionPane.YES_OPTION) {
 				mainPanel.changePhase("Action");
 				return;
 			}
 			owner.subtractSpell(1);
 			for (Monster mon : mainPanel.opponent().summonedMonsters()) {
 				abilityTiles.add(mon.tile());
 				mon.tile().setBorder(BorderFactory.createLineBorder(Color.green, 3));
			}
		}
		else if (name.equalsIgnoreCase("Junk Synchron")) {
			int lv1 = 0, lv2 = 0, lv3 = 0, lv4 = 0;	//monster tributing
			boolean r2 = false, r3 = false, r4 = false, r5 = false;	//remaining monsters
 			for (Monster mon : owner.summonedMonsters()) {
 				if (mon.level() == 1)
 					lv1++;
 				if (mon.level() == 2)
 					lv2++;
 				if (mon.level() == 3)
 					lv3++;
 				if (mon.level() == 4)
 					lv4++;
			}
 			for (Monster mon : owner.allMonsters()) {
 				if (mon.level() == 2)
 					r2 = true;
 				if (mon.level() == 3)
 					r3 = true;
 				if (mon.level() == 4)
 					r4 = true;
 				if (mon.level() == 5)
 					r5 = true;
 			}
 			int totalOptions = 0;
 			if (lv1 >= 2 && r2)
 				totalOptions++;
 			if (lv2 >= 2 && r3)
 				totalOptions++;
 			if (lv3 >= 2 && r4)
 				totalOptions++;
 			if (lv4 >= 2 && r5)
 				totalOptions++;
 			if (totalOptions == 0) {
 				JOptionPane.showMessageDialog(null, "You do not have 2 monsters of the same level,\n"
 						+ "Or you do not have a monster left to summon for the next level", 
		 				name + "'s ability", JOptionPane.INFORMATION_MESSAGE);
				mainPanel.changePhase("Action");
				return;
 			}
			if (owner.spell() < 1) {
				JOptionPane.showMessageDialog(null, owner.name() + " (" + owner.color() + "), "
						+ " you need at least 1 spell crest to activate " + name + "'s ability.", 
		 				"Not enough crests", JOptionPane.INFORMATION_MESSAGE);
				mainPanel.changePhase("Action");
 				return;
			}
 			String[] lvOptions = new String[totalOptions];
 			for (int i=0; i<totalOptions; i++) {
 	 			if (lv1 >= 2 && r2) {
 	 				lvOptions[i] = "Level 2";
 	 				lv1 = -1;
 	 			}
 	 			else if (lv2 >= 2 && r3) {
 	 				lvOptions[i] = "Level 3";
 	 				lv2 = -1;
 	 			}
 	 			else if (lv3 >= 2 && r4) {
 	 				lvOptions[i] = "Level 4";
 	 				lv3 = -1;
 	 			}
 	 			else if (lv4 >= 2 && r5) {
 	 				lvOptions[i] = "Level 5";
 	 				lv4 = -1;
 	 			}
 			}
			int response = JOptionPane.showOptionDialog(null, 
					 owner.name() + " (" + owner.color() + "), chose a level TO SUMMON.\n"
					 		+ "This will require 2 tributes of monsters who are of 1 level less.\n"
					 		+ "This will spawn a new path.",
					 name + "'s ability - Synchro Summon?", 
				     JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, 
				     lvOptions, "default");
			int lvToSummon = 0;
			if (response < 0) {
				mainPanel.changePhase("Action");
 				return;
			}
			else
				lvToSummon = Integer.parseInt(lvOptions[response].charAt(6) + "");
			
			owner.subtractSpell(1);
			mainPanel.setMonsterLevelToSummon(lvToSummon);
 			
 			for (Monster mon : owner.summonedMonsters()) {
 				if (mon.level() == lvToSummon-1) {
 					abilityTiles.add(mon.tile());
 	 				mon.tile().setBorder(BorderFactory.createLineBorder(Color.green, 3));
 				}
			}
		}
		else if (name.equalsIgnoreCase("Juunishishi Molmorat") 
				|| name.equalsIgnoreCase("Juunishishi Thoroughblade")) {
			if (owner.spell() < 2) {
				JOptionPane.showMessageDialog(null, owner.name() + " (" + owner.color() + "), "
						+ " you need at least 2 spell crests to activate " + name + "'s ability.", 
		 				"Not enough crests", JOptionPane.INFORMATION_MESSAGE);
				mainPanel.changePhase("Action");
 				return;
			}
			if (owner.juunishishiArray().size() <= 0) {
 				JOptionPane.showConfirmDialog(null, owner.name() 
 						+ " (" + owner.color() + ")," 
 						+ " there are no more Juunishishi monsters to summon."
 						+ "\nYou actually summoned them all!"
 						+ "\nAre you satisfied?", 
 		 				"You Used Up All Your Juunishishi Monsters!", JOptionPane.YES_NO_OPTION);
 				mainPanel.changePhase("Action");
 				return;
 			}
 			juunishishiSummon(2);
 			endAbilityPhase();
		}
		else if (name.contains("Juunishishi")) {
 			if (owner.juunishishiArray().size() <= 0) {
 				JOptionPane.showConfirmDialog(null, owner.name() 
 						+ " (" + owner.color() + ")," 
 						+ " there are no more Juunishishi monsters to summon."
 						+ "\nYou actually summoned them all!"
 						+ "\nAre you satisfied?", 
 		 				"You Used Up All Your Juunishishi Monsters!", JOptionPane.YES_NO_OPTION);
 				mainPanel.changePhase("Action");
 				return;
 			}
 			juunishishiSummon(0);
 			endAbilityPhase();
		}
		else if (name.equalsIgnoreCase("Mr. Volcano")) {
			Player actualOpponent = mainPanel.theActualOpponent(owner.turnPlayer());
			if (actualOpponent.summonedMonsters().size() > 0) {
				Monster burnTarget = actualOpponent.summonedMonsters()
						.get((int)(Math.random()*actualOpponent.summonedMonsters().size()));
				burnTarget.changeHp(burnTarget.hp() - 2);
				burnTarget.tile().checkAndDestroyMonster();
			}
		}
		else if (name.equalsIgnoreCase("King Of The Skull Servants")) {
			changeAtk(atk + 3);
			changeDef(def + 1);
			originalHp = originalHp + 2;
			changeHp(hp() + 2);
		}
		
		
	}
	
	public void receivedAbility(Tile tile, Monster target) {
		if (name.equalsIgnoreCase("Overdrive")) {
			if (abilityTiles.contains(tile) && !tile.equals(myTile)) {
				int shoot = 10;
				if (tile.r() > myTile.r())
					for (Tile aTile : abilityTiles)
						if (aTile.r() > myTile.r() && aTile.monster() != null 
						&& !aTile.monster().name().contains("Monster Lord")) {
							aTile.monster().changeHp(aTile.monster().hp() - shoot);
							mainPanel.updateMonsterPanel(aTile.monster());
							aTile.checkAndDestroyMonster();
						}
				if (tile.r() < myTile.r())
					for (Tile aTile : abilityTiles)
						if (aTile.r() < myTile.r() && aTile.monster() != null
						&& !aTile.monster().name().contains("Monster Lord")) {
							aTile.monster().changeHp(aTile.monster().hp() - shoot);
							mainPanel.updateMonsterPanel(aTile.monster());
							aTile.checkAndDestroyMonster();
						}
				if (tile.c() > myTile.c())
					for (Tile aTile : abilityTiles)
						if (aTile.c() > myTile.c() && aTile.monster() != null
						&& !aTile.monster().name().contains("Monster Lord")) {
							aTile.monster().changeHp(aTile.monster().hp() - shoot);
							mainPanel.updateMonsterPanel(aTile.monster());
							aTile.checkAndDestroyMonster();
						}
				if (tile.c() < myTile.c())
					for (Tile aTile : abilityTiles)
						if (aTile.c() < myTile.c() && aTile.monster() != null
						&& !aTile.monster().name().contains("Monster Lord")) {
							aTile.monster().changeHp(aTile.monster().hp() - shoot);
							mainPanel.updateMonsterPanel(aTile.monster());
							aTile.checkAndDestroyMonster();
						}		
				endAbilityPhase();
			}
		}
		else if (name.equalsIgnoreCase("Gandora The Dragon Of Destruction")) {
			if (abilityTiles.contains(tile)) {
	 			int response = JOptionPane.showConfirmDialog(null, 
	 					"Do you want to want to target " + target.name() + "?\n",
		    				name + "'s ability",
		    				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	 			if (response != JOptionPane.YES_OPTION) {
	 				return;
	 			}
				int percent = 1 + (int)(Math.random()*100);
				int damage = 0;
				if (percent >= 1 && percent <= 40)
					damage = 50;
				else if (percent >= 41 && percent <= 90)
					damage = 25;
				else if (percent >= 91 && percent <= 100)
					damage = 100;
	 			JOptionPane.showMessageDialog(null, damage + " damage to " + target.name(), 
		 				name + "'s ability", JOptionPane.INFORMATION_MESSAGE);
				tile.monster().changeHp(tile.monster().hp() - damage);
				mainPanel.updateMonsterPanel(tile.monster());
				tile.checkAndDestroyMonster();
				endAbilityPhase();
			}
		}
		else if (name.equalsIgnoreCase("Shining Friendship")) {
			if (abilityTiles.contains(tile)) {
	 			int response = JOptionPane.showConfirmDialog(null, 
	 					"Do you want to want to heal " + target.name() + " to max hp?\n",
		    				name + "'s ability",
		    				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	 			if (response != JOptionPane.YES_OPTION) {
	 				return;
	 			}
	 			target.changeHp(target.originalHp());
	 			mainPanel.updateMonsterPanel(target);
	 			endAbilityPhase();
			}
		}
		else if (name.equalsIgnoreCase("Penguin Soldier")) {
			if (abilityTiles.contains(tile)) {
	 			int response = JOptionPane.showConfirmDialog(null, 
	 					"Do you want to want to move " + target.name() + "?\n",
		    				name + "'s ability",
		    				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	 			if (response != JOptionPane.YES_OPTION) {
	 				return;
	 			}
	 			endAbilityPhase();
	 			mainPanel.moveAMonster(target.tile().r(), target.tile().c(), true);
			}
		}
		else if (name.equalsIgnoreCase("Gameciel, The Sea Turtle Kaiju")) {
			if (abilityTiles.contains(tile)) {
	 			int response = JOptionPane.showConfirmDialog(null, 
	 					"Do you want to want to tribute " + target.name() + " for " 
	 						+ "Gameciel The Sea Turtle Kaiju" + "?\n",
	 						name + "'s ability",
		    				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	 			if (response != JOptionPane.YES_OPTION) {
	 				return;
	 			}
	 			Tile targetTile = target.tile();
	 			target.changeHp(0);
	 			targetTile.checkAndDestroyMonster();
	 			Monster normalKaiju =
	 					new Monster("Gameciel The Sea Turtle Kaiju", hp, atk, def, mainPanel.opponent(), mainPanel, 3);
	 			mainPanel.addMonsterToTileLv0(normalKaiju, targetTile, mainPanel.opponent());

	 			owner.summonedMonsters().remove(this);
	 			this.tile().removeMonster();

	 			mainPanel.updateMonsterPanel(normalKaiju);
	 			endAbilityPhase();
			}
		}
		else if (name.equalsIgnoreCase("Blackwing - Gale The Whirlwind")) {
			if (abilityTiles.contains(tile)) {
	 			int response = JOptionPane.showConfirmDialog(null, 
	 					"Do you want to half the attack and reduce 10 defence of " + target.name() + "?\n",
	 						name + "'s ability",
		    				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	 			if (response != JOptionPane.YES_OPTION) {
	 				return;
	 			}
	 			target.changeAtk(target.atk()/2);
	 			target.changeDef(target.def()-10);
	 			if (target.def()<0)
	 				target.changeDef(0);
	 			mainPanel.updateMonsterPanel(target);
	 			endAbilityPhase();
			}
		}
		else if (name.equalsIgnoreCase("Junk Synchron")) {
			if (abilityTiles.contains(tile)) {
	 			int response = JOptionPane.showConfirmDialog(null, 
	 					"Do you want to tribute " + target.name() + "?\n",
	 						name + "'s ability",
		    				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	 			if (response != JOptionPane.YES_OPTION) {
	 				return;
	 			}
	 			abilityMonsters.add(target);
	 			target.changeHp(0);
	 			tile.checkAndDestroyMonster();
	 			
	 			if (abilityMonsters.size() == 2) {
	 				for (Tile aTile : abilityTiles)
	 					aTile.updatePics();
	 				abilityTiles.clear();
	 				abilityMonsters.clear();
	 				mainPanel.changePhase("Pattern");
	 			}
			}
		}
	}
	
	private void endAbilityPhase() {
		for (Tile aTile : abilityTiles)
			aTile.updatePics();
		abilityTiles.clear();
		abilityMonsters.clear();
		mainPanel.changePhase("Action");
	}
	
	public void endTurnAbility() {
		if (name.equalsIgnoreCase("Mr. Volcano")) {
			if (mainPanel.opponent().summonedMonsters().size() > 0) {
				Monster burnTarget = mainPanel.opponent().summonedMonsters()
						.get((int)(Math.random()*mainPanel.opponent().summonedMonsters().size()));
				burnTarget.changeHp(burnTarget.hp()-3);
				mainPanel.updateMonsterPanel(burnTarget);
				burnTarget.tile().checkAndDestroyMonster();
			}
		}
	}
	
	public int rollAbility() {
		if (name.equalsIgnoreCase("Dangerous Machine Type-6")) {
			if ((int)(Math.random()*4) == 0)	//25%
				return 0;
			else {
				int percent = 1 + (int)(Math.random()*100);
				if (percent >= 1 && percent <= 33)
					return 1;
				else if (percent >= 34 && percent <= 62)
					return -1;
				else if (percent >= 63 && percent <= 81)
					return 2;
				else if (percent >= 82 && percent <= 96)
					return -2;
				else if (percent >= 96 && percent <= 99)
					return 3;
				else if (percent == 100)
					return -3;
			}
		}
		return 0;
	}
	
	public double moveCost() {	//per tile
		if (name.equalsIgnoreCase("Labyrinth Wall"))
			return 0.33;
		else if (name.equalsIgnoreCase("Wall Shadow")) {
			for (Monster monst : owner.summonedMonsters()) {
				if (monst.name().equalsIgnoreCase("Labyrinth Wall"))
					return 0.2;
			}
			return 0.5;
		}
		else if (name.equalsIgnoreCase("TRUE KING V.F.D, THE BEAST")) {
			return 0.5;
		}
		else if (name.equalsIgnoreCase("Madolche Queen Tiaramisu"))
			return -1;
		return 1;
	}
	
	public int attackCost() {
		if (name.equalsIgnoreCase("Madolche Queen Tiaramisu"))
			return -1;
		else if (name.equalsIgnoreCase("Gameciel The Sea Turtle Kaiju"))
			return 3;
		else if (name.equalsIgnoreCase("Neo Spacian Grand Mole"))
			return 3;
		
		return 1;
	}
	
	public int defenceCost() {
		return 1;
	}
	
	public void destroy() {
		if (name.contains("Monster Lord")) {
			mainPanel.changePhase("Game Over");
			JLabel deathLabel = new JLabel();
			deathLabel.setIcon(getResizedGif("exodia", 500, 500));
			JOptionPane.showMessageDialog(null, deathLabel,
					mainPanel.currentPlayer().name().toUpperCase() + " WINS!!!!!",
					JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	public boolean hasAttackedMaxTimes() {
		return (attacksLeft < 1);
	}
	
	public void newTurnAttackReset() {
		attacksLeft = attacksOriginalAmount;
	}

	public void attack() {		//return false if attack interupted (returned to monster pool);
		attacksLeft--;
	}
	
	public void defend(Monster attacker) {
		
		if (name.equalsIgnoreCase("Monster Lord 1") || name.equalsIgnoreCase("Monster Lord 2")) {
			changeHp(hp - 1);
			mainPanel.updateMonsterPanel(this);
			myTile.checkAndDestroyMonster();
			return;
		}
		else if (name.equalsIgnoreCase("Neo Spacian Grand Mole")) {
			attacker.changeHp(attacker.originalHp());
			if (attacker.name().contains("Juunishishi")) {
				mainPanel.theActualOpponent(owner.turnPlayer()).juunishishiArray().add(attacker);
				if (attacker.name().equalsIgnoreCase
						("Juunishishi Molmorat") || name.equalsIgnoreCase("Juunishishi Thoroughblade"))
					mainPanel.theActualOpponent(owner.turnPlayer()).allMonsters().add(attacker);
			}
			else
				mainPanel.theActualOpponent(owner.turnPlayer()).allMonsters().add(attacker);
			attacker.tile().removeMonster();
			this.changeHp(this.originalHp());
			this.owner().allMonsters().add(this);
			this.tile().removeMonster();
			mainPanel.updateMonsterPanel(this);
			return;
		}
		
		defaultDefend(attacker);
		
		if (name.equalsIgnoreCase("Interplanetarypurplythorny Dragon") 
				|| name.equalsIgnoreCase("Interplanetarypurplythorny Beast")) {
			if (!attacker.name().contains("Monster Lord"))
				attacker.changeHp(attacker.hp() - 3);
		}
		
		if (name.equalsIgnoreCase("Interplanetarypurplythorny Beast")) {
			if (!attacker.name().contains("Monster Lord")) {
				if (owner.spell() >= 1 && attacker.hp() > 0) {
					mainPanel.updateMonsterPanel(attacker);
					int response = JOptionPane.showConfirmDialog(null, 
			 				owner.name() + " (" + owner.color() + "),"
			 				+ " do you want to deal damage to " + attacker.name() 
			 				+ " equal to its attack (" + attacker.atk() + ") for " + name + "'s ability?\n"
				    		+ "This will cost 1 spell crest.",
				    		name + "'s ability",
				    		JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			 		if (response == JOptionPane.YES_OPTION) {
			 			owner.subtractSpell(1);
						attacker.changeHp(attacker.hp() - attacker.atk());
			 		}
				}
			}
		}
		if (hp <= 0)
			attacker.destroyAMonsterByBattle();
		attacker.tile().checkAndDestroyMonster();
		mainPanel.updateMonsterPanel(this);
		myTile.checkAndDestroyMonster();
	}
	
	public void defaultDefend(Monster attacker) {
		int activeDef = 0;
		
		System.out.println(attacker.name()+" atk: " + attacker.atk() + "  -attacks-  " + name + " hp: " + hp + " def: " + def);
		
		if (def > 0 && owner.def() > 0)
			if (defendPopup(attacker))
				activeDef = def;
		
		int modifier = attacker.atk() - activeDef;
		if (modifier < 0)
			modifier = 0;
				
		changeHp(hp - modifier);
		
		System.out.println(name + " hp: " + hp);
	}
	
	public boolean defendPopup(Monster attacker) {
		if (defenceCost() < 1)
			return true;
		int response = JOptionPane.showConfirmDialog(null, owner.name() + " (" + owner.color() + "), "
				+ "do you want to spend " + defenceCost()
				+ " defence crest to protect " + name + " for " + def + " points?\n"
				+ "Attacker:   \"" + attacker.name() + "\"  atk - " + attacker.atk()
				+ "\nDefender:   \"" + name + "\"  hp - " + hp,
				"Defend?",
	       JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

		if (response == JOptionPane.YES_OPTION) {
			owner.subtractDef(defenceCost());
			return true;
		}
	    return false;
	}
	
	public void destroyAMonsterByBattle() {
		if (name.equalsIgnoreCase("Blackwing - Shura The Blue Flame")) {
			owner.setDieBonus(owner.dieBonus() + 1);
		}
	}
	
	public boolean attackingInterupt(Monster target) {
		if (name.equalsIgnoreCase("Neo Spacian Grand Mole")) {
			target.changeHp(target.originalHp());
			if (target.name().contains("Juunishishi")) {
				mainPanel.theActualOpponent(owner.turnPlayer()).juunishishiArray().add(target);
				if (target.name().equalsIgnoreCase
						("Juunishishi Molmorat") || name.equalsIgnoreCase("Juunishishi Thoroughblade"))
					mainPanel.theActualOpponent(owner.turnPlayer()).allMonsters().add(target);
			}
			else
				mainPanel.theActualOpponent(owner.turnPlayer()).allMonsters().add(target);
			target.tile().removeMonster();
			this.changeHp(this.originalHp());
			this.owner().allMonsters().add(this);
			this.tile().removeMonster();
			mainPanel.updateMonsterPanel(this);
			return true;
		}
		return false;
	}
	
	public ImageIcon getResizedImageIcon(int width, int height) {
		
		URL imageURL = getClass().getResource("resources/" + "monster_" + name.replace(" ", "_").toLowerCase() + ".jpg");	
		if (imageURL != null) {
			ImageIcon imageIcon = new ImageIcon(imageURL);
			Image image = imageIcon.getImage();
			Image newimg = image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
			imageIcon = new ImageIcon(newimg);
			if (imageIcon != null)
				return imageIcon;
		}
		System.out.println("imageURL: " + imageURL);
		JOptionPane.showConfirmDialog(null, "ERROR 69: bad imageURL recieved from Monster class for .jpg\n"
				+ "Do something about it.", 
 				"ERROR", JOptionPane.ERROR_MESSAGE);
		return null;
	}
	
	public ImageIcon getResizedGif(String fileName, int width, int height) {
		
		URL imageURL = getClass().getResource("resources/" + fileName.toLowerCase() + ".gif");	
		//"monster_" + name.replace(" ", "_").toLowerCase()
		if (imageURL != null) {
			ImageIcon imageIcon = new ImageIcon(imageURL);
			Image image = imageIcon.getImage();
			Image newimg = image.getScaledInstance(width, height, java.awt.Image.SCALE_DEFAULT);
			imageIcon = new ImageIcon(newimg);
			if (imageIcon != null)
				return imageIcon;
		}
		System.out.println("imageURL: " + imageURL);
		JOptionPane.showConfirmDialog(null, "ERROR 69: bad imageURL recieved from Monster class for .gif\n"
				+ "Do something about it.", 
 				"ERROR", JOptionPane.ERROR_MESSAGE);
		return null;	//create a default pic to return to
	}

	
	public String name() {
		return name;
	}
	public int hp() {
		return hp;
	}
	public int atk() {
		return atk;
	}
	public int def() {
		return def;
	}
	public int originalHp() {
		return originalHp;
	}
	public int attacksPerTurn() {
		return attacksOriginalAmount;
	}
	public int level() {
		return level;
	}
	public Player owner() {
		return owner;
	}
	public int numberOfAbilities() {
		return abilityText.size();
	}
	public boolean hasActivatableAbility() {
		return hasActivatableAbility;
	}
	public int moveOpponentCost() {
		return moveOpponentCost;
	}
	
	public void changeHp(int newHp) {
		hp = newHp;
		monsterHPLabel.setText(" " + hp + " / " + originalHp);
	}
	public void changeAtk(int newAtk) {
		atk = newAtk;
		monsterATKLabel.setText(" " + atk);
		if (atk <= 0) {
			myTile.removeMonster();
			myTile.addMonster(this);
			mainPanel.addPopUp(myTile);
		}
	}
	public void changeDef(int newDef) {
		def = newDef;
		monsterDEFLabel.setText(" " + def);
	}
	
	public Tile tile() {
		return myTile;
	}
	public void setTile(Tile newTile) {
		myTile = newTile;
	}
	
	public void initalSetup() {
		tiles = mainPanel.getTiles();
	}
	
	public void changeOwner(Player newOwner) {
		owner = newOwner;
	}
	
	private void juunishishiSummon(int costForJuunishishi) {
			String costString = "";
			if (costForJuunishishi > 0)
				costString = "\nThis will cost " + costForJuunishishi + " spell crest.";
			int response = JOptionPane.showConfirmDialog(null, 
					owner.name() + " (" + owner.color() + "),"
					+ " do you want to destroy " + name + " to summon a different juunishishi monster?" + costString,
    				name + "'s ability",
    				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (response != JOptionPane.YES_OPTION) {
				mainPanel.changePhase("Action");
				return;
			}
			if (costForJuunishishi > 0)
				owner.subtractSpell(costForJuunishishi);
			int monstersForGrid = owner.juunishishiArray().size();
			int gridWidth = (int)(Math.sqrt(monstersForGrid));
			if (monstersForGrid == 0)
				gridWidth = 1;
			if (monstersForGrid > 2)
				gridWidth = (int)Math.ceil(Math.sqrt(monstersForGrid));

			JPanel monsterGridPanel = new JPanel();
			monsterGridPanel.setBorder(BorderFactory.createLineBorder(Color.darkGray, 2));
			monsterGridPanel.setBackground(Color.lightGray);
			monsterGridPanel.setLayout(new GridLayout(gridWidth, gridWidth, 1, 1));	//have a (0, 0) border?
			lastLabelTouchedInGrid = null;
			final Border grayBorder = BorderFactory.createLineBorder(Color.gray, 2);
			
			final ArrayList <JLabel> smallMonsterLabels = new ArrayList<JLabel>();
			for (int i=0; i<owner.juunishishiArray().size(); i++) {
				final int monsterIndex = i, labelIndex = smallMonsterLabels.size();
				smallMonsterLabels.add(new JLabel());
				//smallMonsterLabels.get(labelIndex).setPreferredSize(new Dimension(60, 60));
				smallMonsterLabels.get(labelIndex).setIcon(owner.juunishishiArray().get(monsterIndex)
						.getResizedImageIcon(120,120));
				smallMonsterLabels.get(labelIndex).setHorizontalAlignment(SwingConstants.CENTER);
				smallMonsterLabels.get(labelIndex).setMinimumSize(new Dimension(10, 10));
				smallMonsterLabels.get(labelIndex).setBorder(BorderFactory.createLineBorder(Color.gray, 2));
				if (labelIndex == 0) {
					smallMonsterLabels.get(labelIndex).setBorder(BorderFactory.createLineBorder(Color.green, 2));
					monsterToSummon = owner.juunishishiArray().get(monsterIndex);
					mainPanel.updateMonsterPanel(monsterToSummon);
					lastLabelTouchedInGrid = smallMonsterLabels.get(labelIndex);
				}
				smallMonsterLabels.get(labelIndex).addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {   
						lastLabelTouchedInGrid.setBorder(grayBorder);
						//smallMonsterLabels.get(labelIndex).setPreferredSize(new Dimension(60,60));
						smallMonsterLabels.get(labelIndex).setBorder(BorderFactory.createLineBorder(Color.green, 2));
						monsterToSummon = owner.juunishishiArray().get(monsterIndex);
						mainPanel.updateMonsterPanel(monsterToSummon);
						lastLabelTouchedInGrid = smallMonsterLabels.get(labelIndex);
					}});
				monsterGridPanel.add(smallMonsterLabels.get(labelIndex));
			}
			if (monstersForGrid == 1) {
				this.changeHp(0);
				this.tile().checkAndDestroyMonster();
			mainPanel.addMonsterToTile(monsterToSummon, myTile);	
			monsterToSummon = null;
			return;
			}
			JOptionPane.showMessageDialog(null, monsterGridPanel, "Choose a monster", JOptionPane.PLAIN_MESSAGE);
			this.changeHp(0);
			this.tile().checkAndDestroyMonster();
			mainPanel.addMonsterToTile(monsterToSummon, myTile);	
			monsterToSummon = null;
	}
	
	private void setUpPanel() {
		rightShark = new JPanel();
		rightShark.setSize(156, 700);
		rightShark.setLayout(null);
		rightShark.setBackground(Color.pink);
		
		monsterNameTextPane = new JTextPane();
		monsterNameTextPane.setEditable(false);  
		monsterNameTextPane.setCursor(null);  
		monsterNameTextPane.setOpaque(false);  
		monsterNameTextPane.setFocusable(false);
		monsterNameTextPane.setSize(rightShark.getWidth(), 1000);
		showName = name;
		int index = 0, wordLength = 0;
		while (index<name.length()) {	//check if one word in name is too long (purple... thorny)
			char c = name.charAt(index);
			if (c == ' ')
				wordLength = 0;
			else
				wordLength++;
			if (wordLength == 15)	//might cause problems on borderline states (words)
				showName = name.substring(0, index) + "... " + name.substring(index);
			index++;
		}
		monsterNameTextPane.setText(showName);
		monsterNameTextPane.setFont(new Font("Times New Roman", Font.BOLD, 18));
	    StyledDocument doc1 = monsterNameTextPane.getStyledDocument();
	    SimpleAttributeSet center1 = new SimpleAttributeSet();
	    StyleConstants.setAlignment(center1, StyleConstants.ALIGN_CENTER);
	    doc1.setParagraphAttributes(0, doc1.getLength(), center1, false);
		monsterNameTextPane.setSize(rightShark.getWidth(), (int)monsterNameTextPane.getPreferredSize().getHeight());
		monsterNameTextPane.setLocation(0, 0);
	    rightShark.add(monsterNameTextPane);
		
		monsterPicLabel = new JLabel();
		monsterPicLabel.setOpaque(false);
		monsterPicLabel.setFocusable(false);
		monsterPicLabel.setHorizontalAlignment(SwingConstants.CENTER);
		monsterPicLabel.setSize(rightShark.getWidth(), rightShark.getWidth());
		monsterPicLabel.setLocation(0, monsterNameTextPane.getY() + monsterNameTextPane.getHeight());
		monsterPicLabel.setIcon(getResizedImageIcon(monsterPicLabel.getWidth(), monsterPicLabel.getHeight()));
		rightShark.add(monsterPicLabel);
		
		monsterLvLabel = new JLabel();
		monsterLvLabel.setOpaque(false);
		monsterLvLabel.setFocusable(false);
		monsterLvLabel.setHorizontalAlignment(SwingConstants.CENTER);
		monsterLvLabel.setSize(rightShark.getWidth(), 40);
		monsterLvLabel.setLocation(0, monsterPicLabel.getY() + monsterPicLabel.getHeight() + 3);
		if (level == 0)
			monsterLvLabel.setSize(0,0);
		else
			monsterLvLabel.setIcon(getRegularResizedPic(level +"", 40, 40));
		rightShark.add(monsterLvLabel);
		
		monsterHPLabel = new JLabel();
		monsterHPLabel.setOpaque(false);
		monsterHPLabel.setFocusable(false);
		monsterHPLabel.setHorizontalAlignment(SwingConstants.CENTER);
		monsterHPLabel.setSize(rightShark.getWidth(), 40);
		monsterHPLabel.setLocation(0, monsterLvLabel.getY() + monsterLvLabel.getHeight());
		monsterHPLabel.setText(" " + hp + " / " + originalHp);
		monsterHPLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		monsterHPLabel.setIcon(getRegularResizedPic("hp", 30, 30));
		rightShark.add(monsterHPLabel);
		
		monsterATKLabel = new JLabel();
		monsterATKLabel.setOpaque(false);
		monsterATKLabel.setFocusable(false);
		monsterATKLabel.setHorizontalAlignment(SwingConstants.CENTER);
		monsterATKLabel.setSize(rightShark.getWidth(), 40);
		monsterATKLabel.setLocation(0, monsterHPLabel.getY() + monsterHPLabel.getHeight());
		monsterATKLabel.setText(" " + atk);
		monsterATKLabel.setFont(monsterHPLabel.getFont());
		monsterATKLabel.setIcon(getRegularResizedPic("atk1", 30, 30));
		rightShark.add(monsterATKLabel);
		
		monsterDEFLabel = new JLabel();
		monsterDEFLabel.setOpaque(false);
		monsterDEFLabel.setFocusable(false);
		monsterDEFLabel.setHorizontalAlignment(SwingConstants.CENTER);
		monsterDEFLabel.setSize(rightShark.getWidth(), 40);
		monsterDEFLabel.setLocation(0, monsterATKLabel.getY() + monsterATKLabel.getHeight());
		monsterDEFLabel.setText(" " + def);
		monsterDEFLabel.setFont(monsterHPLabel.getFont());
		monsterDEFLabel.setIcon(getRegularResizedPic("def1", 30, 30));
		rightShark.add(monsterDEFLabel);
		
		if (abilityText.size() > 0) {
			monsterAbilityCostLabel = new JLabel[abilityText.size()];
			monsterAbilityTextPane = new JTextPane[abilityText.size()];
		}
		for (int i=0; i<abilityText.size(); i++) {	
			monsterAbilityLabel = new JLabel();
			monsterAbilityLabel.setOpaque(false);
			monsterAbilityLabel.setFocusable(false);
			monsterAbilityLabel.setHorizontalAlignment(SwingConstants.CENTER);
			monsterAbilityLabel.setSize(rightShark.getWidth(), 20);
			monsterAbilityLabel.setLocation(0, monsterDEFLabel.getY() + monsterDEFLabel.getHeight() + 5);
			monsterAbilityLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
			if (abilityText.size() < 2)
				monsterAbilityLabel.setText("Ability:");
			else
				monsterAbilityLabel.setText("Abilities:");
			rightShark.add(monsterAbilityLabel);
			
			monsterAbilityCostLabel[i] = new JLabel();
			monsterAbilityCostLabel[i].setOpaque(false);
			monsterAbilityCostLabel[i].setFocusable(false);
			monsterAbilityCostLabel[i].setHorizontalAlignment(SwingConstants.CENTER);
			monsterAbilityCostLabel[i].setSize(48, 30);
			if (i == 0) {
				monsterAbilityCostLabel[i].setLocation(2, monsterAbilityLabel.getY() + monsterAbilityLabel.getHeight() + 3);
			}
			else if (monsterAbilityTextPane[i-1].getHeight() < 45) {
				monsterAbilityCostLabel[i].setLocation(2,
						monsterAbilityTextPane[i-1].getY() + 47);
			}
			else {
				monsterAbilityCostLabel[i].setLocation(2,
						monsterAbilityTextPane[i-1].getY() + monsterAbilityTextPane[i-1].getHeight() + 2);
			}
			monsterAbilityCostLabel[i].setIcon(getRegularResizedPic(abilityCostPic.get(i), 30, 30));
			monsterAbilityCostLabel[i].setText(abilityCostxNum.get(i));
			monsterAbilityCostLabel[i].setSize((int)monsterAbilityCostLabel[i].getPreferredSize().getWidth(), 30);
			rightShark.add(monsterAbilityCostLabel[i]);
			
			monsterAbilityTextPane[i] = new JTextPane(); 
			monsterAbilityTextPane[i].setEditable(false);  
			monsterAbilityTextPane[i].setCursor(null);  
			monsterAbilityTextPane[i].setOpaque(false);  
			monsterAbilityTextPane[i].setFocusable(false);
			monsterAbilityTextPane[i].setSize(rightShark.getWidth() - monsterAbilityCostLabel[i].getWidth() - 2, 1000);
			monsterAbilityTextPane[i].setText(abilityText.get(i));
			monsterAbilityTextPane[i].setFont(new Font("serif", Font.BOLD, 15));
		    StyledDocument doc2 = monsterAbilityTextPane[i].getStyledDocument();
		    SimpleAttributeSet center2 = new SimpleAttributeSet();
		    StyleConstants.setAlignment(center2, StyleConstants.ALIGN_LEFT);
		    doc2.setParagraphAttributes(0, doc2.getLength(), center2, false);
			monsterAbilityTextPane[i].setSize(rightShark.getWidth() - monsterAbilityCostLabel[i].getWidth() - 2,
					(int)monsterAbilityTextPane[i].getPreferredSize().getHeight());
			monsterAbilityTextPane[i].setLocation(monsterAbilityCostLabel[i].getX() + monsterAbilityCostLabel[i].getWidth() + 1,
					monsterAbilityCostLabel[i].getY());
		    rightShark.add(monsterAbilityTextPane[i]);
		}
	    
	    monsterMemeTextPane = new JTextPane();
	    if (abilityText.size() > 0 && (int)monsterAbilityTextPane[monsterAbilityTextPane.length-1].getHeight() < 45) {
		    monsterMemeTextPane.setLocation(0, monsterAbilityTextPane[monsterAbilityTextPane.length-1].getY() + 47);
	    }
	    else if (abilityText.size() > 0) {
		    monsterMemeTextPane.setLocation(0, monsterAbilityTextPane[monsterAbilityTextPane.length-1].getY()
		    		+ monsterAbilityTextPane[monsterAbilityTextPane.length-1].getHeight() + 3);
	    }
	    else {
	    	monsterMemeTextPane.setLocation(2, monsterDEFLabel.getY() + monsterDEFLabel.getHeight() + 3);
	    }
	    monsterMemeTextPane.setEditable(false);  
	    monsterMemeTextPane.setCursor(null);  
	    monsterMemeTextPane.setOpaque(false);  
	    monsterMemeTextPane.setFocusable(false);
	    StyledDocument doc3 = monsterMemeTextPane.getStyledDocument();
	    SimpleAttributeSet center3 = new SimpleAttributeSet();
	    StyleConstants.setAlignment(center3, StyleConstants.ALIGN_CENTER);
	    doc3.setParagraphAttributes(0, doc3.getLength(), center3, false);
	    monsterMemeTextPane.setSize(rightShark.getWidth(), 1000);
	    monsterMemeTextPane.setText(memeText);
	    //monsterMemeTextPane.setText("");
	    monsterMemeTextPane.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
	    monsterMemeTextPane.setSize(rightShark.getWidth(), (int)monsterMemeTextPane.getPreferredSize().getHeight() + 5);
	    rightShark.add(monsterMemeTextPane);

	    rightShark.setSize(156, monsterMemeTextPane.getY() + monsterMemeTextPane.getHeight());
	    
		if (owner.turnPlayer() == 1) {
			rightShark.setBorder(BorderFactory.createLineBorder(Color.blue, 2));
			monsterPicLabel.setBorder(BorderFactory.createLineBorder(Color.blue, 2));
		}
		if (owner.turnPlayer() == 2) {
			rightShark.setBorder(BorderFactory.createLineBorder(Color.orange, 2));
			monsterPicLabel.setBorder(BorderFactory.createLineBorder(Color.orange, 2));
		}
	}
	private ImageIcon getRegularResizedPic(String picName, int picWidth, int picHeight) {
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
	public void updatePanel() {	
		
		monsterNameTextPane.setText(showName);
		//monsterPicLabel.setIcon(getResizedImageIcon(monsterPicLabel.getWidth(), monsterPicLabel.getHeight()));
		monsterHPLabel.setText(" " + hp + " / " + originalHp);
		monsterATKLabel.setText(" " + atk);
		monsterDEFLabel.setText(" " + def);
		monsterMemeTextPane.setText(memeText);
		//monsterMemeTextPane.setText("");
		if (hp() < 1)
			monsterHPLabel.setText(" [K.O.] / " + originalHp);
	}
	public JPanel panel() {
		return rightShark;
	}
}
