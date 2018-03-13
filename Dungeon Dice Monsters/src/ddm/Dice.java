package ddm;

import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Dice {
	
	private String[] dice = new String[6];	//1, 2, 3, 4, atk, def, move, spell
	private int summonCrests = 0;
	private ArrayList <String> crestPool = new ArrayList<String>();
	
	private int atk = 25, def = 14, move = 46, spell = 15;	//percentage chance, make sure adds up to 100
	
	//atk .25, def .125, move .5, spell .125
	
	public Dice(int level) {
		
		setUpCrests();
		
		if (level == 1)
			summonCrests = 4;
		else if (level == 2)
			summonCrests = 3;
		else if (level == 3)
			summonCrests = 2;
		else if (level == 4)
			summonCrests = 1;
		else if (level == 5)
			summonCrests = 0;
		else if (level < 1 || level > 5)
			JOptionPane.showConfirmDialog(null, "ERROR 69: invalid level for dice.\n"
					+ "Do something about it.", 
 					"ERROR", JOptionPane.ERROR_MESSAGE);
		
		for (int i = 0; i < 6; i++) {
			if (i < summonCrests)
				dice[i] = Integer.toString(level);
			else {
				dice[i] = crestPool.get((int)(Math.random()*crestPool.size()));
			}
		//System.out.print(dice[i] + " ");
		}
		//System.out.println();
	}
	
	private void setUpCrests() {
		for (int i=0; i<atk; i++)
			crestPool.add("atk1");
		for (int i=0; i<def; i++)
			crestPool.add("def1");
		for (int i=0; i<spell; i++)
			crestPool.add("spell1");
		
		int move1, move2;
		move2 = move/2;
		move1 = move - move2;
		for (int i=0; i<move1; i++)
			crestPool.add("move1");
		for (int i=0; i<(move2/2); i++)
			crestPool.add("move2");
		
		//{"atk1", "atk1", "def1", "move1", "move1", "move2", "spell1"};	//original
	}
	
	public String roll() {
		return dice[(int)(Math.random()*dice.length)];
	}

}
