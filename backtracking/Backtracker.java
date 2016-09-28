package backtracking;

import model.LasersModel;

import java.util.ArrayList;
import java.util.Optional;

/**
 * This class represents the classic recursive backtracking algorithm.
 * It has a solver that can take a valid configuration and return a
 * solution, if one exists.
 *
 * This file comes from the backtracking lab. It should be useful
 * in this project. A second method has been added that you should
 * implement.
 *
 * @author Sean Strout @ RIT CS
 * @author James Heliotis @ RIT CS
 * @author Conor Brown
 * @author Gavin Xie
 */
public class Backtracker {

    private boolean debug;
    private LasersModel model;

    /**
     * Initialize a new backtracker.
     *
     * @param debug Is debugging output enabled?
     */
    public Backtracker(boolean debug, LasersModel model) {
        this.debug = debug;
        this.model = model;
        if (this.debug) {
            System.out.println("Backtracker debugging enabled...");
        }
    }

    /**
     * A utility routine for printing out various debug messages.
     *
     * @param msg    The type of config being looked at (current, goal,
     *               successor, e.g.)
     * @param config The config to display
     */
    private void debugPrint(String msg, Configuration config) {
        if (this.debug) {
            System.out.println(msg + ":\n" + config);
        }
    }

    /**
     * Try find a solution, if one exists, for a given configuration.
     *
     * @param config A valid configuration
     * @return A solution config, or null if no solution
     */
    public Optional<SafeConfig> solve(SafeConfig config) {
        if(model.getSolution().isPresent()){
            return model.getSolution();
        }
        debugPrint("Current config", config);
        if (config.isGoal()) {
            debugPrint("\tGoal config", config);
            return Optional.of(config);
        } else {
            for (SafeConfig child : config.getSuccessors()) {
                if (child.isValid()) {
                    debugPrint("\tValid successor", child);
                    Optional<SafeConfig> sol = solve(child);
                    if (sol.isPresent()) {
                        return sol;
                    }
                } else {
                    debugPrint("\tInvalid successor", child);
                }
            }
            // implicit backtracking happens here
        }
        return Optional.empty();
    }

    /**
     * solves it with the normal solve method, then removes lasers until there is 1 more than the current vault and
     * changes the vault to that configuration
     *
     * @param current the starting configuration
     * @return a list of configurations to get to a goal configuration.
     * If there are none, return null.
     */
    public ArrayList<Integer> solveWithPath(SafeConfig current) {
        ArrayList<Integer> path = new ArrayList<>();

        model.setSolution(solve(current));

        if (model.getSolution().isPresent()) {
            String[][] Vault = model.getSolution().get().getVault();
            String[][] ModelVault = model.getVault();
            for (int row = Vault.length - 1; row >= 0; row--) {
                for (int col = Vault[0].length - 1; col >= 0; col--) {
                    if (Vault[row][col].equals("L")) {
                        if (!ModelVault[row][col].equals("L")) {
                            path.add(0, col);
                            path.add(0, row);
                        }
                    }
                }
            }
        }

        return path;
    }

}
