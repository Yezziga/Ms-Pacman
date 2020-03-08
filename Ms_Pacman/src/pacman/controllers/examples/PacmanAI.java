package pacman.controllers.examples;

import java.util.ArrayList;

import pacman.controllers.Controller;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.STRATEGY;
import pacman.game.Game;

/**
 * AI for pacman which makes decisions based on some information. The pacman chooses to flee if any non-edible ghosts are too close, eats pills if 
 * @author Jessica
 *
 */
// TODO: change strats so that it looks for power pill if ghosts are close
public class PacmanAI extends Controller<MOVE> {
	private static final int MIN_DIST = 25; // the minimum distance between pacman & ghosts. Pacman flees if dist <= 25

	@Override
	public MOVE getMove(Game game, long timeDue) {
		int currentIndex = game.getPacmanCurrentNodeIndex();

		for (GHOST ghost : GHOST.values()) {
			
			// Strat 1 - flee if any non-edible ghost is close
			if (game.getGhostEdibleTime(ghost) == 0 && game.getGhostLairTime(ghost) == 0) {
				if (game.getShortestPathDistance(currentIndex, game.getGhostCurrentNodeIndex(ghost)) < MIN_DIST) {
					game.strategy = STRATEGY.FLEE;
					System.out.println("Strat = flee");
					// TODO: bestäm distance matrix
					return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(),
							game.getGhostCurrentNodeIndex(ghost), DM.PATH);
				}
			}
			
			// Strategy 2 - chase nearest edible ghost
			else if (game.getGhostEdibleTime(ghost) > 0) {
				int minDistance = Integer.MAX_VALUE;
				GHOST minGhost = null;
				int distance = game.getShortestPathDistance(currentIndex, game.getGhostCurrentNodeIndex(ghost));
				if (distance < minDistance) {
					minDistance = distance;
					minGhost = ghost;
				}
				if (minGhost != null) {
					game.strategy = STRATEGY.CHASE;
					System.out.println("Strat = chase");
					return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),
							game.getGhostCurrentNodeIndex(minGhost), DM.PATH);
				}
			}
		}

		// Strategy 3 - eat pills and power pills
		System.out.println("Strat = eat pills");

		game.strategy = STRATEGY.EATPILLS;
		return game.getNextMoveTowardsTarget(currentIndex,
				game.getClosestNodeIndexFromNodeIndex(currentIndex, game.getAllAvailablePillsAndPowerPills(), DM.PATH), // TODO: decide DM
				DM.PATH);
	}

}
