package dataRecording;

import pacman.controllers.*;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;
import pacman.game.Constants.MOVE;

/**
 * This class is used to collect data when the AI plays.
 * @author Jessica
 *
 */
public class DataCollectorAIController extends Controller<MOVE>{
	private Controller<MOVE> c;
	
	public DataCollectorAIController(Controller<MOVE> input){
		c = input;
	}
	
	@Override
	public MOVE getMove(Game game, long dueTime) {
		MOVE move = c.getMove(game, dueTime);
		
		DataTuple data = new DataTuple(game, move);
				
		DataSaverLoader.SavePacManData(data);		
		return move;
	}

}
