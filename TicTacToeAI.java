import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.awt.Font;
import java.awt.GridLayout;

public class TicTacToeAI extends JFrame implements ActionListener {

	private JButton [][]buttons = new JButton[10][10];
	private JButton playButton = new JButton("Play Game");
	private JLabel statusLabel = new JLabel("");
	private JButton playerAI = new JButton("Player 1(Komputer)");
	private JButton playerOrang = new JButton("Player 1(Orang)");
	
	private String [][]arr = new String[10][10];
	
	private boolean  isPlay;	
	
	private static final String EMPTY  = ".";
	private static final String PLAYER = "X";
    private static final String COMPUTER =  "O";
	
	public void setStatus(String s) {
		statusLabel.setText(s);
	}

	public void setButtonsEnabled(boolean enabled) {
		for(int i=0;i<10;i++) {
			for(int j=0;j<10;j++) {
				buttons[i][j].setEnabled(enabled);
				if(enabled) buttons[i][j].setText(" ");
			}
		}
	}

	public TicTacToeAI() {
		setTitle("Tic Tac Toe");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		JPanel centerPanel = new JPanel(new GridLayout(10,10));
		Font font = new Font("Arial",Font.BOLD, 32);
  
		for(int i=0;i<10;i++) {
			for(int j=0;j<10;j++) {
				buttons[i][j] = new JButton(" ");
				buttons[i][j].setFont(font);
				buttons[i][j].addActionListener(this);
				buttons[i][j].setFocusable(false);
				centerPanel.add(buttons[i][j]);
			}
		}

		playButton.addActionListener(this);

		JPanel northPanel = new JPanel();
		northPanel.add(statusLabel);

		JPanel southPanel = new JPanel();
		southPanel.add(playButton);
		
		add(northPanel,"North");
		add(centerPanel,"Center");
		add(southPanel,"South");

		setSize(600,600);
		
		setLocationRelativeTo(null);
		
		setStatus("Player  vs AI");
		setButtonsEnabled(false);

		for(int i=0;i<10;i++) {
			for(int j=0;j<10;j++) {
				arr[i][j]= EMPTY;
			}
		}
	}

	public static void main(String []args) {
		new TicTacToeAI().setVisible(true);
	}
	
	public void actionPerformed(ActionEvent event) {
		if(event.getSource()==playButton) {
			play();
		}else {
			for(int i=0;i<10;i++) {
				for(int j=0;j<10;j++) {
					if(event.getSource()==buttons[i][j]) {
						playerTurn(i,j);
					}
				}
			}
		}
	}

	public void play() 
	{
		setButtonsEnabled(true);
		setStatus("Your Turn :");
		isPlay = true;
	}
	
	//For player
	public void playerTurn(int i,int j) 
	{
		if(arr[i][j]==EMPTY)
		{
			buttons[i][j].setText(PLAYER);
			arr[i][j] = PLAYER;
			
			//Check the winner
			if(checkWinner(PLAYER))
			{	
				//The game is end
				isPlay= false;
				statusLabel.setText("");
				setButtonsEnabled(false);
				
				JOptionPane.showMessageDialog(this, "YOU ARE WIN !!");
			}
			else
			{
				//Change to computer turn
				computerTurn();
			}
		}			
	}
	
	//For computer
	public void computerTurn() 
	{
		//setButtonsEnabled(false);
	
		int[] result = minimax(2, COMPUTER); // depth, max turn
        buttons[result[1]][result[2]].setText(COMPUTER);
		//setButtonsEnabled(true);
	}
	
	public boolean checkWinner(String _turn)
	{		
		//check row
		for(int i =0; i<10;i++)
		{
			for(int j = 0; j<6; j++)
			{
				if(arr[i][j] == _turn && arr[i][j] == arr[i][j+1] && arr[i][j+1] == arr[i][j+2] && arr[i][j+2] == arr[i][j+3] && arr[i][j+3] == arr[i][j+4]) 
				{
					return true;
				}
			}
		}
		
		//check column
	 	for(int i =0; i<6;i++)
		{
			for(int j = 0; j<10; j++)
			{
				if(arr[i][j] == _turn && arr[i][j] == arr[i+1][j] && arr[i+1][j] == arr[i+2][j] && arr[i+2][j] == arr[i+3][j] && arr[i+3][j] == arr[i+4][j]) 
				{
					return true;
				}
			}
		} 
		
		//check diagonal (\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\)
		for(int i =0; i<6;i++)
		{
			for(int j = 0; j<6; j++)
			{
				if(arr[i][j] ==_turn && arr[i][j] == arr[i+1][j+1] && arr[i+1][j+1] == arr[i+2][j+2] && arr[i+2][j+2] == arr[i+3][j+3] && arr[i+3][j+3] == arr[i+4][j+4]) 
				{
					return true;
				}
			}
		}
		
		//check diagonal (///////////////////////////////////////////////////)
		for(int i =0; i<6;i++)
		{
			for(int j = 9; j>3; j--)
			{
				if(arr[i][j] == _turn && arr[i][j] == arr[i+1][j-1] && arr[i+1][j-1] == arr[i+2][j-2] && arr[i+2][j-2] == arr[i+3][j-3] && arr[i+3][j-3] == arr[i+4][j-4]) 
				{
					return true;
				}
			}
		}
		return false;
	} 	
	
	 public int[] minimax(int depth, String _turn) 
	 {
		// Generate possible next moves in a List of int[2] of {row, col}.
		List<int[]> nextMoves = generateMoves();
 
		// COMPUTER is maximizing; while PLAYER is minimizing
		int bestScore = (_turn == COMPUTER) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		int currentScore;
		int bestRow = -1;
		int bestCol = -1;
 
		if (nextMoves.isEmpty() || depth == 0) {
			bestScore = evaluationFunction(); 
		} else {
			for (int[] move : nextMoves) {
				arr[move[0]][move[1]] = _turn;
				if (_turn == COMPUTER) { 
					currentScore = minimax(depth - 1, PLAYER)[0];
					if (currentScore > bestScore) {
						bestScore = currentScore;
						bestRow = move[0];
						bestCol = move[1];
					}
				} else {  
					currentScore = minimax(depth - 1, COMPUTER)[0];
					if (currentScore < bestScore) {
						bestScore = currentScore;
						bestRow = move[0];
						bestCol = move[1];
					}
				}
				
				// Undo move
				arr[move[0]][move[1]] = EMPTY;
			}
      }
	  
      return new int[] {bestScore, bestRow, bestCol};
   }
 

   public List<int[]> generateMoves() 
   {
      List<int[]> nextMoves = new ArrayList<int[]>(); // allocate List
 
      if ( checkWinner(PLAYER) || checkWinner(COMPUTER)) {
         return nextMoves;   
      }
 
      // Search for empty cells and add to the List
      for (int row = 0; row < 10; ++row) {
         for (int col = 0; col < 10; ++col) {
            if (arr[row][col]==EMPTY) {
               nextMoves.add(new int[] {row, col});
            }
         }
      }
      return nextMoves;
   }
   
    /* public int evaluationFunction() 
	{	
		//int maxValue = getExtremeScore(COMPUTER) + getHardScore(COMPUTER) + getMediumScore(COMPUTER) + getEasyScore(COMPUTER) + getVeryEasyScore(COMPUTER);
		//int minValue = getExtremeScore(PLAYER)   + getHardScore(PLAYER)   + getMediumScore(PLAYER)   + getEasyScore(PLAYER)   + getVeryEasyScore(PLAYER);
		//return maxValue - minValue;
		
		int score = 0;

		int totalExtremeO = 0;
		int totalExtremeX = 0;
		int totalHardO = 0;
		int totalHardX = 0;
		int totalMediumO = 0;
		int totalMediumX = 0;
		int totalEasyO = 0;
		int totalEasyX = 0;
		int totalVeryEasyO = 0;
		int totalVeryEasyX = 0;
		
		//EXTREME
		
		totalExtremeO += checkTotalRow("OOOOO");
		totalExtremeO += checkTotalColumn("OOOOO");
		//totalExtremeO += checkTotalDiagonal1("OOOOO");
		//totalExtremeO += checkTotalDiagonal2("OOOOO");
		
		totalExtremeX += checkTotalRow("XXXXX");
		totalExtremeX += checkTotalColumn("XXXXX");
		//totalExtremeX += checkTotalDiagonal1("XXXXX");
		//totalExtremeX += checkTotalDiagonal2("XXXXX");
		
		totalExtremeO += checkTotalRow(".OOOO.");
		totalExtremeO += checkTotalColumn(".OOOO.");
		//totalExtremeO += checkTotalDiagonal1(".OOOO.");
		//totalExtremeO += checkTotalDiagonal2(".OOOO.");
		
		totalExtremeX += checkTotalRow(".XXXX.");
		totalExtremeX += checkTotalColumn(".XXXX.");
		//totalExtremeX += checkTotalDiagonal1(".XXXX.");
		//totalExtremeX += checkTotalDiagonal2(".XXXX.");
		
		//HARD
		
		totalHardO += checkTotalRow("XOOOO.");
		totalHardO += checkTotalColumn("XOOOO.");
		//totalHardO += checkTotalDiagonal1("XOOOO.");
		//totalHardO += checkTotalDiagonal2("XOOOO.");
		
		totalHardX += checkTotalRow("OXXXX.");
		totalHardX += checkTotalColumn("OXXXX.");
		//totalHardX += checkTotalDiagonal1("OXXXX.");
		//totalHardX += checkTotalDiagonal2("OXXXX.");
		
		totalHardO += checkTotalRow(".OOO.");
		totalHardO += checkTotalColumn(".OOO.");
		//totalHardO += checkTotalDiagonal1(".OOO.");
		//totalHardO += checkTotalDiagonal2(".OOO.");
		
		totalHardX += checkTotalRow(".XXX.");
		totalHardX += checkTotalColumn(".XXX.");
		//totalHardX += checkTotalDiagonal1(".XXX.");
		//totalHardX += checkTotalDiagonal2(".XXX.");
		
		//MEDIUM
		
		totalMediumO += checkTotalRow("XOOO.");
		totalMediumO += checkTotalColumn("XOOO.");
		//totalMediumO += checkTotalDiagonal1("XOOO.");
		//totalMediumO += checkTotalDiagonal2("XOOO.");
		
		totalMediumX += checkTotalRow("OXXX.");
		totalMediumX += checkTotalColumn("OXXX.");
		//totalMediumX += checkTotalDiagonal1("OXXX.");
		//totalMediumX += checkTotalDiagonal2("OXXX.");
		
		totalMediumO += checkTotalRow(".OO.");
		totalMediumO += checkTotalColumn(".OO.");
		//totalMediumO += checkTotalDiagonal1(".OO.");
		//totalMediumO += checkTotalDiagonal2(".OO.");
		
		totalMediumX += checkTotalRow(".XX.");
		totalMediumX += checkTotalColumn(".XX.");
		//totalMediumX += checkTotalDiagonal1(".XX.");
		//totalMediumX += checkTotalDiagonal2(".XX.");
		
		//EASY
		
		totalEasyO += checkTotalRow("XOO.");
		totalEasyO += checkTotalColumn("XOO.");
		//totalEasyO += checkTotalDiagonal1("XOO.");
		//totalEasyO += checkTotalDiagonal2("XOO.");
		
		totalEasyX += checkTotalRow("OXX.");
		totalEasyX += checkTotalColumn("OXX.");
		//totalEasyX += checkTotalDiagonal1("OXX.");
		//totalEasyX += checkTotalDiagonal2("OXX.");
		
		totalEasyO += checkTotalRow(".O.");
		totalEasyO += checkTotalColumn(".O.");
		//totalEasyO += checkTotalDiagonal1(".O.");
		//totalEasyO += checkTotalDiagonal2(".O.");
		
		totalEasyX += checkTotalRow(".X.");
		totalEasyX += checkTotalColumn(".X.");
		//totalEasyX += checkTotalDiagonal1(".X.");
		//totalEasyX += checkTotalDiagonal2(".X.");
		
		//VERY EASY
		
		totalVeryEasyO += checkTotalRow("XO.");
		totalVeryEasyO += checkTotalColumn("XO.");
		//totalVeryEasyO += checkTotalDiagonal1("XO.");
		//totalVeryEasyO += checkTotalDiagonal2("XO.");
		
		totalVeryEasyX += checkTotalRow("OX.");
		totalVeryEasyX += checkTotalColumn("OX.");
		//totalVeryEasyX += checkTotalDiagonal1("OX.");
		//totalVeryEasyX += checkTotalDiagonal2("OX.");
		
		int max = 150*totalExtremeO + 120*totalHardO + 40*totalMediumO + 20*totalEasyO + 10*totalVeryEasyO;
		int min = 150*totalExtremeX + 120*totalHardX + 40*totalMediumX + 20*totalEasyX + 10*totalVeryEasyX;
		return max - min;
	} */
	
	public int evaluationFunction() 
	{	
		int totalO = 0;
		int totalX = 0;
		
		totalO += checkTotalRow("OOOOO.")*1000;
		totalO += checkTotalColumn("OOOOO.")*1000;
		totalO += checkTotalRow("OOOO.")*800;
		totalO += checkTotalColumn("OOOO.")*800;
		totalO += checkTotalRow("OOO.")*600;
		totalO += checkTotalColumn("OOO.")*600;
		totalO += checkTotalRow("OO.")*400;
		totalO += checkTotalColumn("OO.")*400;
		totalO += checkTotalRow("O.")*200;
		totalO += checkTotalColumn("O.")*200;
		
		totalX += checkTotalRow("XXXXX.")*1000;
		totalX += checkTotalColumn("XXXXX.")*1000;
		totalX += checkTotalRow("XXXX.")*800;
		totalX += checkTotalColumn("XXXX.")*800;
		totalX += checkTotalRow("XXX.")*600;
		totalX += checkTotalColumn("XXX.")*600;
		totalX += checkTotalRow("XX.")*400;
		totalX += checkTotalColumn("XX.")*400;
		totalX += checkTotalRow("X.")*200;
		totalX += checkTotalColumn("X.")*200;


		return totalO - totalX;
	}
	
	
	public int checkTotalRow(String p)
	{	
		int count = 0;
		
		String reverse = new StringBuffer(p).reverse().toString();
		
		for (int i = 0; i < 10; i++) {
			String row = "";
			for (int j = 0; j < 10; j++) {
				row += arr[i][j];
			}
			if (row.contains(p)) {
				int x = row.indexOf(p);
				while (x >= 0) {
					count++;
					x = row.indexOf(p, p.length() + x);
				}
			}
			if (row.contains(reverse)) {
				int x = row.indexOf(reverse);
				while (x >= 0) {
					count++;
					x = row.indexOf(reverse, reverse.length() + x);
				}
			}
		}
		return count;
	}
	
	
	
	public int checkTotalColumn(String p)	{
		int count = 0;
		
		String reverse = new StringBuffer(p).reverse().toString();

		for (int j = 0; j < 10; j++) {
			String column = "";
			for (int i = 0; i < 10; i++) {
				column += arr[i][j];
			}
			if (column.contains(p)) {
				int x = column.indexOf(p);
				while (x >= 0) {
					count++;
					x = column.indexOf(p, p.length() + x);
				}
			}
			if (column.contains(reverse)) {
				int x = column.indexOf(reverse);
				while (x >= 0) {
					count++;
					x = column.indexOf(reverse, reverse.length() + x);
				}
			}
		}
		return count;
	}

/* 	public int checkTotalDiagonal1(String p)
	{
		int count = 0;

		for (int j = 0; j < 10; j++) {
			String diagonal = "";
			for (int i = 0; i < 10; i++) {
				diagonal += arr[i][j];
			}
			if (diagonal.contains(p)) {
				int x = diagonal.indexOf(p);
				while (x >= 0) {
					count++;
					x = diagonal.indexOf(p, p.length() + x);
				}
			}
		}

		return count;
	
	}
	
	public int checkTotalDiagonal2(String p)
	{
	} */
	
	
	
	/* public int getExtremeScore(String _turn)
	{
		int count = 0;
		
		//check row for OOOOO or XXXXX
		for(int i =0; i<10;i++)
		{
			for(int j = 0; j<6; j++)
			{
				if(arr[i][j] == _turn && arr[i][j] == arr[i][j+1] && arr[i][j+1] == arr[i][j+2] && arr[i][j+2] == arr[i][j+3] && arr[i][j+3] == arr[i][j+4]) 
				{
					count++;
				}
			}
		} 
		
		//check column for OOOOO or XXXXX
	 	for(int i =0; i<6;i++)
		{
			for(int j = 0; j<10; j++)
			{
				if(arr[i][j] == _turn && arr[i][j] == arr[i+1][j] && arr[i+1][j] == arr[i+2][j] && arr[i+2][j] == arr[i+3][j] && arr[i+3][j] == arr[i+4][j]) 
				{
					count++;
				}
			}
		}  
		

		
	
		//check diagonal (\) for OOOOO or XXXXX 
		for(int i =0; i<6;i++)
		{
			for(int j = 0; j<6; j++)
			{
				if(arr[i][j] ==_turn && arr[i][j] == arr[i+1][j+1] && arr[i+1][j+1] == arr[i+2][j+2] && arr[i+2][j+2] == arr[i+3][j+3] && arr[i+3][j+3] == arr[i+4][j+4]) 
				{
					count++;
				}
			}
		}
		
		//check diagonal (/) for OOOOO or XXXXX 
		for(int i =0; i<6;i++)
		{
			for(int j = 9; j>3; j--)
			{
				if(arr[i][j] == _turn && arr[i][j] == arr[i+1][j-1] && arr[i+1][j-1] == arr[i+2][j-2] && arr[i+2][j-2] == arr[i+3][j-3] && arr[i+3][j-3] == arr[i+4][j-4]) 
				{
					count++;
				}
			}
		}
	
		//check row for .OOOO. or .XXXX.
		for(int i =0; i<10;i++)
		{
			for(int j = 0; j<5; j++)
			{
				if(arr[i][j] == EMPTY && arr[i][j+1] == _turn && arr[i][j+1] == arr[i][j+2] && arr[i][j+2] == arr[i][j+3] && arr[i][j+3] == arr[i][j+4] && arr[i][j+5] == EMPTY) 
				{
					count++;
				}
			}
		}
		
		//check column for .OOOO. or .XXXX.
	 	for(int i =0; i<5;i++)
		{
			for(int j = 0; j<10; j++)
			{
				if(arr[i][j] == EMPTY && arr[i+1][j] == _turn && arr[i+1][j] == arr[i+2][j] && arr[i+2][j] == arr[i+3][j] && arr[i+3][j] == arr[i+4][j] && arr[i+5][j] == EMPTY) 
				{
					count++;
				}
			}
		} 
		
		//check diagonal (\) for .OOOO. or .XXXX. 
		for(int i =0; i<5;i++)
		{
			for(int j = 0; j<5; j++)
			{
				if(arr[i][j] == EMPTY && arr[i+1][j+1] == _turn && arr[i+1][j+1] == arr[i+2][j+2] && arr[i+2][j+2] == arr[i+3][j+3] && arr[i+3][j+3] == arr[i+4][j+4] && arr[i+5][j+5] == EMPTY) 
				{
					count++;
				}
			}
		}
		
		//check diagonal (/) for .OOOO. or .XXXX.
		for(int i =0; i<5;i++)
		{
			for(int j = 9; j>4; j--)
			{
				if(arr[i][j] == EMPTY && arr[i+1][j-1] == _turn && arr[i+1][j-1] == arr[i+2][j-2] && arr[i+2][j-2] == arr[i+3][j-3] && arr[i+3][j-3] == arr[i+4][j-4] && arr[i+5][j-5] == EMPTY) 
				{
					count++;
				}
			}
		}
	
		return count*100;
	}
	
	public int getHardScore(String _turn)
	{
		int count = 0;
		String _noturn;
		
		if(_turn == COMPUTER)
		{
			_noturn = PLAYER;
		}else
		{
			_noturn = COMPUTER;
		}
		
		//check row for XOOOO. Atau .OOOOX Atau OXXXX. Atau .XXXXO
		for(int i =0; i<10;i++)
		{
			for(int j = 0; j<5; j++)
			{
				if((arr[i][j] == EMPTY && arr[i][j+1] == _turn && arr[i][j+1] == arr[i][j+2] && arr[i][j+2] == arr[i][j+3] && arr[i][j+3] == arr[i][j+4] && arr[i][j+5] == _noturn) ||
				   (arr[i][j] == _noturn && arr[i][j+1] == _turn && arr[i][j+1] == arr[i][j+2] && arr[i][j+2] == arr[i][j+3] && arr[i][j+3] == arr[i][j+4] && arr[i][j+5] == EMPTY)) 
				{
					count++;
				}
			}
		}
		
		//check column for XOOOO. Atau .OOOOX Atau OXXXX. Atau .XXXXO
	 	for(int i =0; i<5;i++)
		{
			for(int j = 0; j<10; j++)
			{
				if((arr[i][j] == EMPTY && arr[i+1][j] == _turn && arr[i+1][j] == arr[i+2][j] && arr[i+2][j] == arr[i+3][j] && arr[i+3][j] == arr[i+4][j] && arr[i+5][j] == _noturn) ||
				   (arr[i][j] == _noturn && arr[i+1][j] == _turn && arr[i+1][j] == arr[i+2][j] && arr[i+2][j] == arr[i+3][j] && arr[i+3][j] == arr[i+4][j] && arr[i+5][j] == EMPTY)) 
				{
					count++;
				}
			}
		} 
		
		//check diagonal (\) for XOOOO. Atau .OOOOX Atau OXXXX. Atau .XXXXO
		for(int i =0; i<5;i++)
		{
			for(int j = 0; j<5; j++)
			{
				if((arr[i][j] == EMPTY && arr[i+1][j+1] == _turn && arr[i+1][j+1] == arr[i+2][j+2] && arr[i+2][j+2] == arr[i+3][j+3] && arr[i+3][j+3] == arr[i+4][j+4] && arr[i+5][j+5] == _noturn) ||
				   (arr[i][j] == _noturn && arr[i+1][j+1] == _turn && arr[i+1][j+1] == arr[i+2][j+2] && arr[i+2][j+2] == arr[i+3][j+3] && arr[i+3][j+3] == arr[i+4][j+4] && arr[i+5][j+5] == EMPTY))
				{
					count++;
				}
			}
		}
		
		//check diagonal (/) for XOOOO. Atau .OOOOX Atau OXXXX. Atau .XXXXO
		for(int i =0; i<5;i++)
		{
			for(int j = 9; j>4; j--)
			{
				if((arr[i][j] == EMPTY && arr[i+1][j-1] == _turn && arr[i+1][j-1] == arr[i+2][j-2] && arr[i+2][j-2] == arr[i+3][j-3] && arr[i+3][j-3] == arr[i+4][j-4] && arr[i+5][j-5] == _noturn) ||
				   (arr[i][j] == _noturn && arr[i+1][j-1] == _turn && arr[i+1][j-1] == arr[i+2][j-2] && arr[i+2][j-2] == arr[i+3][j-3] && arr[i+3][j-3] == arr[i+4][j-4] && arr[i+5][j-5] == EMPTY))
				{
					count++;
				}
			}
		}
		
		
		//check row for .OOO. or .XXX.
		for(int i =0; i<10;i++)
		{
			for(int j = 0; j<6; j++)
			{
				if(arr[i][j] == EMPTY && arr[i][j+1] == _turn && arr[i][j+1] == arr[i][j+2] && arr[i][j+2] == arr[i][j+3] && arr[i][j+4] == EMPTY) 
				{
					count++;
				}
			}
		}
		
		//check column for .OOO. or .XXX.
	 	for(int i =0; i<6;i++)
		{
			for(int j = 0; j<10; j++)
			{
				if(arr[i][j] == EMPTY && arr[i+1][j] == _turn && arr[i+1][j] == arr[i+2][j] && arr[i+2][j] == arr[i+3][j] && arr[i+4][j] == EMPTY) 
				{
					count++;
				}
			}
		} 
		
		//check diagonal (\) for .OOO. or .XXX. 
		for(int i =0; i<6;i++)
		{
			for(int j = 0; j<6; j++)
			{
				if(arr[i][j] == EMPTY && arr[i+1][j+1] == _turn && arr[i+1][j+1] == arr[i+2][j+2] && arr[i+2][j+2] == arr[i+3][j+3] && arr[i+4][j+4] == EMPTY) 
				{
					count++;
				}
			}
		}
		
		//check diagonal (/) for .OOO. or .XXX. 
		for(int i =0; i<6;i++)
		{
			for(int j = 9; j>3; j--)
			{
				if(arr[i][j] == EMPTY && arr[i+1][j-1] == _turn && arr[i+1][j-1] == arr[i+2][j-2] && arr[i+2][j-2] == arr[i+3][j-3] && arr[i+4][j-4] == EMPTY) 
				{
					count++;
				}
			}
		}
	
	
		return count*80;
	}
	
	public int getMediumScore(String _turn)
	{
		int count = 0;
		String _noturn;
		
		if(_turn == COMPUTER)
		{
			_noturn = PLAYER;
		}else
		{
			_noturn = COMPUTER;
		}
		
		//check row for XOOO. Atau .OOOX Atau OXXX. Atau .XXXO
		for(int i =0; i<10;i++)
		{
			for(int j = 0; j<6; j++)
			{
				if((arr[i][j] == EMPTY && arr[i][j+1] == _turn && arr[i][j+1] == arr[i][j+2] && arr[i][j+2] == arr[i][j+3] && arr[i][j+4] == _noturn) ||
				   (arr[i][j] == _noturn && arr[i][j+1] == _turn && arr[i][j+1] == arr[i][j+2] && arr[i][j+2] == arr[i][j+3] && arr[i][j+4] == EMPTY)) 
				{
					count++;
				}
			}
		}
		
		//check column for XOOO. Atau .OOOX Atau OXXX. Atau .XXXO
	 	for(int i =0; i<6;i++)
		{
			for(int j = 0; j<10; j++)
			{
				if((arr[i][j] == EMPTY && arr[i+1][j] == _turn && arr[i+1][j] == arr[i+2][j] && arr[i+2][j] == arr[i+3][j] && arr[i+4][j] == _noturn) ||
				   (arr[i][j] == _noturn && arr[i+1][j] == _turn && arr[i+1][j] == arr[i+2][j] && arr[i+2][j] == arr[i+3][j] && arr[i+4][j] == EMPTY)) 
				{
					count++;
				}
			}
		} 
		
		//check diagonal (\) for XOOO. Atau .OOOX Atau OXXX. Atau .XXXO
		for(int i =0; i<6;i++)
		{
			for(int j = 0; j<6; j++)
			{
				if((arr[i][j] == EMPTY && arr[i+1][j+1] == _turn && arr[i+1][j+1] == arr[i+2][j+2] && arr[i+2][j+2] == arr[i+3][j+3] && arr[i+4][j+4] == _noturn) ||
				   (arr[i][j] == _noturn && arr[i+1][j+1] == _turn && arr[i+1][j+1] == arr[i+2][j+2] && arr[i+2][j+2] == arr[i+3][j+3] && arr[i+4][j+4] == EMPTY))
				{
					count++;
				}
			}
		}
		
		//check diagonal (/) for XOOO. Atau .OOOX Atau OXXX. Atau .XXXO
		for(int i =0; i<6;i++)
		{
			for(int j = 9; j>3; j--)
			{
				if((arr[i][j] == EMPTY && arr[i+1][j-1] == _turn && arr[i+1][j-1] == arr[i+2][j-2] && arr[i+2][j-2] == arr[i+3][j-3] && arr[i+4][j-4] == _noturn) ||
				   (arr[i][j] == _noturn && arr[i+1][j-1] == _turn && arr[i+1][j-1] == arr[i+2][j-2] && arr[i+2][j-2] == arr[i+3][j-3] && arr[i+4][j-4] ==  EMPTY))
				{
					count++;
				}
			}
		}
		
		
		//check row for .OO. or .XX.
		for(int i =0; i<10;i++)
		{
			for(int j = 0; j<7; j++)
			{
				if(arr[i][j] == EMPTY && arr[i][j+1] == _turn && arr[i][j+1] == arr[i][j+2] && arr[i][j+3] == EMPTY) 
				{
					count++;
				}
			}
		}
		
		//check column for .OO. or .XX.
	 	for(int i =0; i<7;i++)
		{
			for(int j = 0; j<10; j++)
			{
				if(arr[i][j] == EMPTY && arr[i+1][j] == _turn && arr[i+1][j] == arr[i+2][j] && arr[i+3][j] == EMPTY) 
				{
					count++;
				}
			}
		} 
		
		//check diagonal (\) for .OO. or .XX. 
		for(int i =0; i<7;i++)
		{
			for(int j = 0; j<7; j++)
			{
				if(arr[i][j] == EMPTY && arr[i+1][j+1] == _turn && arr[i+1][j+1] == arr[i+2][j+2] && arr[i+3][j+3] == EMPTY) 
				{
					count++;
				}
			}
		}
		
		//check diagonal (/) for .OO. or .XX. 
		for(int i =0; i<7;i++)
		{
			for(int j = 9; j>2; j--)
			{
				if(arr[i][j] == EMPTY && arr[i+1][j-1] == _turn && arr[i+1][j-1] == arr[i+2][j-2] && arr[i+3][j-3] == EMPTY) 
				{
					count++;
				}
			}
		}
	
	
		return count*40;
	}
	
	public int getEasyScore(String _turn)
	{
		int count = 0;
		String _noturn;
		
		if(_turn == COMPUTER)
		{
			_noturn = PLAYER;
		}else
		{
			_noturn = COMPUTER;
		}
		
		//check row for XOO. Atau .OOX Atau OXX. Atau .XXO
		for(int i =0; i<10;i++)
		{
			for(int j = 0; j<7; j++)
			{
				if((arr[i][j] == EMPTY && arr[i][j+1] == _turn && arr[i][j+1] == arr[i][j+2] && arr[i][j+3] == _noturn) ||
				   (arr[i][j] == _noturn && arr[i][j+1] == _turn && arr[i][j+1] == arr[i][j+2] && arr[i][j+3] == EMPTY)) 
				{
					count++;
				}
			}
		}
		
		//check column for XOO. Atau .OOX Atau OXX. Atau .XXO
	 	for(int i =0; i<7;i++)
		{
			for(int j = 0; j<10; j++)
			{
				if((arr[i][j] == EMPTY && arr[i+1][j] == _turn && arr[i+1][j] == arr[i+2][j] &&  arr[i+3][j] == _noturn) ||
				   (arr[i][j] == _noturn && arr[i+1][j] == _turn && arr[i+1][j] == arr[i+2][j] && arr[i+3][j] == EMPTY)) 
				{
					count++;				}
			}
		} 
		
		//check diagonal (\) for XOO. Atau .OOX Atau OXX. Atau .XXO
		for(int i =0; i<7;i++)
		{
			for(int j = 0; j<7; j++)
			{
				if((arr[i][j] == EMPTY && arr[i+1][j+1] == _turn && arr[i+1][j+1] == arr[i+2][j+2] && arr[i+3][j+3] == _noturn) ||
				   (arr[i][j] == _noturn && arr[i+1][j+1] == _turn && arr[i+1][j+1] == arr[i+2][j+2] && arr[i+3][j+3] == EMPTY))
				{
					count++;
				}
			}
		}
		
		//check diagonal (/) for XOO. Atau .OOX Atau OXX. Atau .XXO
		for(int i =0; i<7;i++)
		{
			for(int j = 9; j>2; j--)
			{
				if((arr[i][j] == EMPTY && arr[i+1][j-1] == _turn && arr[i+1][j-1] == arr[i+2][j-2] &&  arr[i+3][j-3] == _noturn) ||
				   (arr[i][j] == _noturn && arr[i+1][j-1] == _turn && arr[i+1][j-1] == arr[i+2][j-2] && arr[i+3][j-3] ==  EMPTY))
				{
					count++;
				}
			}
		}
		
		//check row for .O. or .X.
		for(int i =0; i<10;i++)
		{
			for(int j = 0; j<8; j++)
			{
				if(arr[i][j] == EMPTY && arr[i][j+1] == _turn && arr[i][j+2] == EMPTY) 
				{
					count++;
				}
			}
		}
		
		//check column for .O. or .X.
	 	for(int i =0; i<8;i++)
		{
			for(int j = 0; j<10; j++)
			{
				if(arr[i][j] == EMPTY && arr[i+1][j] == _turn && arr[i+2][j] == EMPTY) 
				{
					count++;
				}
			}
		} 
		
		//check diagonal (\) for .O. or .X. 
		for(int i =0; i<8;i++)
		{
			for(int j = 0; j<8; j++)
			{
				if(arr[i][j] == EMPTY && arr[i+1][j+1] == _turn && arr[i+2][j+2] == EMPTY) 
				{
					count++;
				}
			}
		}
		
		//check diagonal (/) for .O. or .X. 
		for(int i =0; i<8;i++)
		{
			for(int j = 9; j>1; j--)
			{
				if(arr[i][j] == EMPTY && arr[i+1][j-1] == _turn && arr[i+2][j-2] == EMPTY) 
				{
					count++;
				}
			}
		}
	
	
		return count*20;
	}
	
	public int getVeryEasyScore(String _turn)
	{
		int count = 0;
		String _noturn;
		
		if(_turn == COMPUTER)
		{
			_noturn = PLAYER;
		}else
		{
			_noturn = COMPUTER;
		}
		
		//check row for XO. Atau .OX Atau OX. Atau .XO
		for(int i =0; i<10;i++)
		{
			for(int j = 0; j<8; j++)
			{
				if((arr[i][j] == EMPTY && arr[i][j+1] == _turn  && arr[i][j+2] == _noturn) ||
				   (arr[i][j] == _noturn && arr[i][j+1] == _turn && arr[i][j+2] == EMPTY)) 
				{
					count++;
				}
			}
		}
		
		//check column for XO. Atau .OX Atau OX. Atau .XO
	 	for(int i =0; i<8;i++)
		{
			for(int j = 0; j<10; j++)
			{
				if((arr[i][j] == EMPTY && arr[i+1][j] == _turn &&  arr[i+2][j] == _noturn) ||
				   (arr[i][j] == _noturn && arr[i+1][j] == _turn && arr[i+2][j] == EMPTY)) 
				{
					count++;
				}
			}
		} 
		
		//check diagonal (\) for XO. Atau .OX Atau OX. Atau .XO
		for(int i =0; i<8;i++)
		{
			for(int j = 0; j<8; j++)
			{
				if((arr[i][j] == EMPTY && arr[i+1][j+1] == _turn && arr[i+2][j+2] == _noturn) ||
				   (arr[i][j] == _noturn && arr[i+1][j+1] == _turn && arr[i+2][j+2] == EMPTY))
				{
					count++;
				}
			}
		}
		
		//check diagonal (/) for XO. Atau .OX Atau OX. Atau .XO
		for(int i =0; i<8;i++)
		{
			for(int j = 9; j>1; j--)
			{
				if((arr[i][j] == EMPTY && arr[i+1][j-1] == _turn && arr[i+2][j-2] == _noturn) ||
				   (arr[i][j] == _noturn && arr[i+1][j-1] == _turn && arr[i+2][j-2] ==  EMPTY))
				{
					count++;
				}
			}
		}
	
		return count*10;
	}
	 */
	
}