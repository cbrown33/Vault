package ptui;

import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;

import model.LasersModel;

/**
 * This class represents the view portion of the plain text UI.  It
 * is initialized first, followed by the controller (ControllerPTUI).
 * You should create the model here, and then implement the update method.
 *
 * @author Sean Strout @ RIT CS
 * @author Conor Brown
 * @author Gavin Xie
 */
public class LasersPTUI implements Observer {
    /** The UI's connection to the model */
    private LasersModel model;

    /**
     * Construct the PTUI.  Create the model and initialize the view.
     * @param filename the safe file name
     * @throws FileNotFoundException if file not found
     */
    public LasersPTUI(String filename) throws FileNotFoundException {
        try {
            this.model = new LasersModel(filename);
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
            System.exit(-1);
        }
        this.model.addObserver(this);
        this.model.announceChange();
    }

    public LasersModel getModel() { return this.model; }

    @Override
    public void update(Observable o, Object arg) {
        /**
         * Provides a string representation of the vault
         * @return The string representations of the vault
         */
        int cols = model.getCols();
        int rows = model.getRows();
        String[][] Vault = model.getVault();
        String S = "  ";
        String stend = "  ";
        for (int i = 0; i < cols; i++){
            S += Integer.toString(i % 10);
            if (i < cols-1){
                S += " ";
            }
        }
        S += "\n";
        for (int i = 0; i < (2*cols)-1; i++){
            stend += "-";
        }
        stend += "\n";
        S += stend;
        for (int i = 0; i < rows; i++){
            S += Integer.toString(i % 10);
            S += "|";
            for (int j = 0; j < cols; j++){
                S += Vault[i][j];
                if (j < cols-1){
                    S += " ";
                }
            }
            if (i != rows-1) {
                S += "\n";
            }
        }
        System.out.println(model.getCurMessage());
        System.out.println(S);
    }
}
