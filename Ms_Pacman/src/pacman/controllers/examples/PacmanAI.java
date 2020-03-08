package pacman.controllers.examples;

import pacman.controllers.Controller;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.STRATEGY;
import pacman.game.Game;

public class PacmanAI extends Controller<MOVE>{
	private static final int MIN_DIST = 25; // the minimum distance between pacman & ghosts. Pacman flees if dist <= 25
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		int currentIndex = game.getPacmanCurrentNodeIndex();
		
		// Strat 1 - flee if any non-editable ghost is close
		for(GHOST ghost : GHOST.values()) {
			if(game.getGhostEdibleTime(ghost) == 0 && game.getGhostLairTime(ghost) == 0) {
				if(game.getShortestPathDistance(currentIndex, game.getGhostCurrentNodeIndex(ghost))<MIN_DIST) {
					game.strategy = STRATEGY.FLEE;
					// TODO: bestäm distance matrix
					return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), DM.PATH);	
				}				
			}
			//Strategy 2 - chase nearest edible ghost 
			else if(game.getGhostEdibleTime(ghost)>0) {
				int minDistance = Integer.MAX_VALUE;
				GHOST minGhost = null;
				int distance = game.getShortestPathDistance(currentIndex, game.getGhostCurrentNodeIndex(ghost));
				if(distance<minDistance) {
					minDistance = distance;
					minGhost = ghost;
				}
				if(minGhost!= null) {
				game.strategy = STRATEGY.CHASE;
				return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), DM.PATH);
				}
			}			
		}
		
		// Strategy 3 - eat pills and power pills
		
		
		
		
		return null;
	}

}
