import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.awt.Font;
import java.awt.Color;
import java.awt.GridLayout;

public class TicTacToeAI extends JFrame implements ActionListener {

	private JButton [][]buttons = new JButton[10][10];
	private JButton playButton = new JButton("Play Game");
	private JLabel statusLabel = new JLabel("");
	
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
		isPlay = true;
		
		for(int i=0;i<10;i++) {
			for(int j=0;j<10;j++) {
				arr[i][j]= EMPTY;
			}
		}
		
		String [] options = { "PLAYER", "COMPUTER"};

        int choice = JOptionPane.showOptionDialog(null,
                      "Choose who will play first ",
                      "Option",
                      JOptionPane.YES_NO_CANCEL_OPTION,
                      JOptionPane.WARNING_MESSAGE,
                      null,
                      options,
                      options[0]);

        String option = options[choice];
		
		if(option =="COMPUTER")
		{
			computerTurn();
		}
	
	}
	
	//For player
	public void playerTurn(int i,int j) 
	{
		if(arr[i][j]==EMPTY)
		{
			buttons[i][j].setText(PLAYER);
			buttons[i][j].setForeground(Color.blue);
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
	
		//int[] result = minimax(3, COMPUTER); // depth, max turn
		int[] result = minimax2(3, COMPUTER, Integer.MIN_VALUE, Integer.MAX_VALUE);
		arr[result[1]][result[2]] = COMPUTER;
        buttons[result[1]][result[2]].setText(COMPUTER);
		buttons[result[1]][result[2]].setForeground(Color.red);
		
		setStatus("Heuristic Score (totalO - totalX) : " + result[0] + " Coordinate : " + result[1] + " , " + result[2]);
		
		
		//Check the winner
		if(checkWinner(COMPUTER))
		{	
			//The game is end
			isPlay= false;
			statusLabel.setText("");
			setButtonsEnabled(false);
				
			JOptionPane.showMessageDialog(this, "COMPUTER ARE WIN !!");
		}
		
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
   
   private int[] minimax2(int depth, String _turn, int alpha, int beta) {
      // Generate possible next moves in a list of int[2] of {row, col}.
      List<int[]> nextMoves = generateMoves();
 
      // mySeed is maximizing; while oppSeed is minimizing
      int score;
      int bestRow = -1;
      int bestCol = -1;
 
      if (nextMoves.isEmpty() || depth == 0) {
         // Gameover or depth reached, evaluate score
         score = evaluationFunction(); 
         return new int[] {score, bestRow, bestCol};
      } else {
         for (int[] move : nextMoves) {
            // try this move for the current "player"
            arr[move[0]][move[1]] = _turn;
            if (_turn == COMPUTER) {  // mySeed (computer) is maximizing player
               score = minimax2(depth - 1, PLAYER, alpha, beta)[0];
               if (score > alpha) {
                  alpha = score;
                  bestRow = move[0];
                  bestCol = move[1];
               }
            } else {  // oppSeed is minimizing player
               score = minimax2(depth - 1, COMPUTER, alpha, beta)[0];
               if (score < beta) {
                  beta = score;
                  bestRow = move[0];
                  bestCol = move[1];
               }
            }
            // undo move
            arr[move[0]][move[1]] = EMPTY;
            // cut-off
            if (alpha >= beta) break;
         }
         return new int[] {(_turn == COMPUTER) ? alpha : beta, bestRow, bestCol};
      }
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
   	
	public int evaluationFunction() 
	{	
		int totalO = 0;
		int totalX = 0;
	
		//Heuristic based on Number of Winning
		totalO += checkNumberofWinning("O");
		totalX += checkNumberofWinning("X");
		
		//Heuristic based on Threat
		int five = 100000;
		int straightFour = 10000;
		int four = 1000;
		int three = 100; //for three and broken three
		
		//Five 
		totalO += checkTotalRow("OOOOO")*five;
		totalO += checkTotalColumn("OOOOO")*five;
		totalO += checkTotalDiagonal("OOOOO")*five;
		
		totalX += checkTotalRow("XXXXX")*five;
		totalX += checkTotalColumn("XXXXX")*five;
		totalX += checkTotalDiagonal("XXXXX")*five;
		
		//Straight Four
		totalO += checkTotalRow(".OOOO.")*straightFour;
		totalO += checkTotalColumn(".OOOO.")*straightFour;
		totalO += checkTotalDiagonal(".OOOO.")*straightFour;
		
		totalX += checkTotalRow(".XXXX.")*straightFour;
		totalX += checkTotalColumn(".XXXX.")*straightFour;
		totalX += checkTotalDiagonal(".XXXX.")*straightFour;
		
		//Four
		totalO += checkTotalRow("XOOOO.")*four;
		totalO += checkTotalColumn("XOOOO.")*four;
		totalO += checkTotalDiagonal("XOOOO.")*four;
		
		totalO += checkTotalRow(".OOOOX")*four;
		totalO += checkTotalColumn(".OOOOX")*four;
		totalO += checkTotalDiagonal(".OOOOX")*four;
		
		totalX += checkTotalRow("OXXXX.")*four;
		totalX += checkTotalColumn("OXXXX.")*four;
		totalX += checkTotalDiagonal("OXXXX.")*four;
		
		totalX += checkTotalRow(".XXXXO")*four;
		totalX += checkTotalColumn(".XXXXO")*four;
		totalX += checkTotalDiagonal(".XXXXO")*four;
		
		//Three
		totalO += checkTotalRow(".OOO.")*three;
		totalO += checkTotalColumn(".OOO.")*three;
		totalO += checkTotalDiagonal(".OOO.")*three;
		
		totalX += checkTotalRow(".XXX.")*three;
		totalX += checkTotalColumn(".XXX.")*three;
		totalX += checkTotalDiagonal(".XXX.")*three;
		
		 //Broken Three
		totalO += checkTotalRow(".O.OO.")*three;
		totalO += checkTotalColumn(".O.OO.")*three;
		totalO += checkTotalDiagonal(".O.OO.")*three;
		
		totalO += checkTotalRow(".OO.O.")*three;
		totalO += checkTotalColumn(".OO.O.")*three;
		totalO += checkTotalDiagonal(".OO.O.")*three;
		
		totalX += checkTotalRow(".X.XX.")*three;
		totalX += checkTotalColumn(".X.XX.")*three;
		totalX += checkTotalDiagonal(".X.XX.")*three;
		
		totalX += checkTotalRow(".XX.X.")*three;
		totalX += checkTotalColumn(".XX.X.")*three;
		totalX += checkTotalDiagonal(".XX.X.")*three;
		 System.out.println(totalO + " - " + totalX);
		return totalO -totalX;
	}
	
	public int checkNumberofWinning(String p)
	{
		int count = 0;
		
		String _p;
		
		if(p==PLAYER)
		{
			 _p = COMPUTER;
		}else
		{
			 _p = PLAYER;
		}
		
		
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
			
				if(arr[i][j]==p)
				{
					//For row
					if(j-4 >=0 && arr[i][j-1]!=_p && arr[i][j-2]!=_p && arr[i][j-3]!=_p && arr[i][j-4]!=_p)
					{count++;}
					
					if(j+4<10 && arr[i][j+1]!=_p && arr[i][j+2]!=_p && arr[i][j+3]!=_p && arr[i][j+4]!=_p)
					{count++;}
				
					//For column
					if(i-4 >=0 && arr[i-1][j]!=_p && arr[i-2][j]!=_p && arr[i-3][j]!=_p && arr[i-4][j]!=_p)
					{count++;}
					
					if(i+4<10 && arr[i+1][j]!=_p && arr[i+2][j]!=_p && arr[i+3][j]!=_p && arr[i+4][j]!=_p)
					{count++;}
				
					//For diagonal (\\\\\)
					if(i-4 >=0 && j-4 >=0 && arr[i-1][j-1]!=_p && arr[i-2][j-2]!=_p && arr[i-3][j-3]!=_p && arr[i-4][j-4]!=_p)
					{count++;}
					
					if(i+4<10 && j+4 <10 && arr[i+1][j+1]!=_p && arr[i+2][j+2]!=_p && arr[i+3][j+3]!=_p && arr[i+4][j+4]!=_p)
					{count++;}
					
					//For diagonal (///////)
					if(i+4 <10 && j-4 >=0 && arr[i+1][j-1]!=_p && arr[i+2][j-2]!=_p && arr[i+3][j-3]!=_p && arr[i+4][j-4]!=_p)
					{count++;}
					
					if(i-4>=0 && j+4 <10 && arr[i-1][j+1]!=_p && arr[i-2][j+2]!=_p && arr[i-3][j+3]!=_p && arr[i-4][j+4]!=_p)
					{count++;}
				
				}
			}
		}
		
		return count;
		
	}
	
	
	public int checkTotalRow(String p)
	{	
		int count = 0;
		
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
		}
		return count;
		
	}
	
	public int checkTotalColumn(String p)
	{
		int count = 0;

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
		}
		return count;
		
	}

	//(\\\\\\\\\\\\\\\\\)
	public int checkTotalDiagonal(String p)
	{
		int count = 0;
		
		int N=10;
		for (int slice = 0; slice < N*2-1; ++slice) {
			int z = slice < N ? 0 : slice - N + 1;
			String diagonal = "";
			
			for (int j = z; j <= slice - z; ++j) {
				int c1=j;
				int c2=(N-1)-(slice-j);
				diagonal += arr[c1][c2];
			}
			
			if (diagonal.contains(p)) 
			{
				int x = diagonal.indexOf(p);
				while (x >= 0) {
					count++;
					x = diagonal.indexOf(p, p.length() + x);
				} 
			}
		}
    

		for (int slice = 0; slice < N*2-1; ++slice) {
			int z = slice < N ? 0 : slice - N + 1;
			String diagonal = "";
			
			for (int j = z; j <= slice - z; ++j) {
				int c1=j;
				int c2=slice-j;
				diagonal += arr[c1][c2];
			}
			
			if (diagonal.contains(p)) 
			{
				int x = diagonal.indexOf(p);
				while (x >= 0) {
					count++;
					x = diagonal.indexOf(p, p.length() + x);
				} 
			}

		}
		
		
		
		return count;
	}
		
}