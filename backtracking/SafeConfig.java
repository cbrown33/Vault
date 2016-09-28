package backtracking;

import model.LasersModel;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * The class represents a single configuration of a safe.  It is
 * used by the backtracker to generate successors, check for
 * validity, and eventually find the goal.
 *
 * This class is given to you here, but it will undoubtedly need to
 * communicate with the model.  You are free to move it into the model
 * package and/or incorporate it into another class.
 *
 * @author Sean Strout @ RIT CS
 * @author Conor Brown
 * @author Gavin Xie
 */
public class SafeConfig implements Configuration {

    private String[][] Vault;
    private static LasersModel model;

    private static int rows;
    private static int cols;

    private int rPointer = 0;
    private int cPointer = 0;



    public SafeConfig(String args) throws FileNotFoundException {
        model = new LasersModel(args);
        this.Vault = model.getVault();
        rows = model.getRows();
        cols = model.getCols();
//        for (int row = 0; row < rows; row++) {
//            for (int col = 0; col < cols; col++){
//                if (Vault[row][col].equals("4")){
//                    this.Vault = maxPillar(this.Vault, row, col);
//                }
//                else if (Vault[row][col].matches("[1-3]")){
//                    this.Vault = addOpenSpots(this.Vault, row, col);
//                }
//            }
//        }
    }

    public SafeConfig(String[][] Vault, int rPointer, int cPointer){
        this.Vault = new String[rows][cols];
        this.rPointer = rPointer;
        this.cPointer = cPointer;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++){
                this.Vault[row][col] = Vault[row][col];

            }
        }

    }

    public String[][] getVault(){ return Vault; }

    /**
     * A pruning technique to add lasers to pillars that require exactly that many more pillars
     * @param Vault The vault to add the pillars in
     * @param r the row of the pillar
     * @param c the column of the pillar
     * @return the modified vault
     */
    private String[][] maxPillar(String[][] Vault, int r, int c){
        if (r < rows - 1){
            Vault = add(Vault, r+1, c);
        }
        if (r > 0){
            Vault = add(Vault, r-1, c);
        }
        if (c < cols - 1){
            Vault = add(Vault, r, c+1);
        }
        if (c > 0){
            Vault = add(Vault, r, c-1);
        }

        return Vault;
    }

    /**
     * Adds lasers to the open spots of a pillar
     * @param Vault the vault to be modified
     * @param r the row to modift at
     * @param c the column to modify at
     * @return the modified vault
     */
    private String[][] addOpenSpots(String[][]Vault, int r , int c){
        int numOpen = 0;
        int numLasers = 0;
        int pillar = Integer.parseInt(Vault[r][c]);
        if (r < rows - 1){
            if (Vault[r + 1][c].equals(".")) {
                numOpen++;
            }
            else if (Vault[r + 1][c].equals("L")){
                numLasers++;
            }
        }
        if (r > 0){
            if (Vault[r - 1][c].equals(".")) {
                numOpen++;
            }
            else if (Vault[r - 1][c].equals("L")){
                numLasers++;
            }
        }
        if (c < cols - 1){
            if (Vault[r][c + 1].equals(".")) {
                numOpen++;
            }
            else if (Vault[r][c + 1].equals("L")){
                numLasers++;
            }
        }
        if (c > 0){
            if (Vault[r][c - 1].equals(".")) {
                numOpen++;
            }
            else if (Vault[r][c - 1].equals("L")){
                numLasers++;
            }
        }
        if (pillar - numLasers == numOpen){
            if (r < rows - 1){
                if (Vault[r + 1][c].equals(".")) {
                    Vault = add(Vault, r + 1, c);
                }
            }
            if (r > 0){
                if (Vault[r - 1][c].equals(".")) {
                    Vault = add(Vault, r - 1, c);
                }
            }
            if (c < cols - 1){
                if (Vault[r][c + 1].equals(".")) {
                    Vault = add(Vault, r , c + 1 );
                }
            }
            if (c > 0){
                if (Vault[r][c - 1].equals(".")) {
                    Vault = add(Vault, r , c - 1 );
                }
            }
        }
        return Vault;
    }

    /**
     * A pruning technique for out isvalid function
     * @param Vault the vault to check the validity
     * @param r the row to check at
     * @param c the column to check at
     * @return a boolean valid or not
     */
    private boolean isValidPrune(String[][]Vault, int r , int c){
        int numOpen = 0;
        int numLasers = 0;
        int numNeeded = Integer.parseInt(Vault[r][c]);
        if (r < rows - 1){
            if (Vault[r + 1][c].equals(".")) {
                numOpen++;
            }
            else if (Vault[r + 1][c].equals("L")){
                numLasers++;
            }
        }
        if (r > 0){
            if (Vault[r - 1][c].equals(".")) {
                numOpen++;
            }
            else if (Vault[r - 1][c].equals("L")){
                numLasers++;
            }
        }
        if (c < cols - 1){
            if (Vault[r][c + 1].equals(".")) {
                numOpen++;
            }
            else if (Vault[r][c + 1].equals("L")){
                numLasers++;
            }
        }
        if (c > 0){
            if (Vault[r][c - 1].equals(".")) {
                numOpen++;
            }
            else if (Vault[r][c - 1].equals("L")){
                numLasers++;
            }
        }
        if (numNeeded == numLasers){
            return true;
        }
        else if(numNeeded - numLasers <= numOpen){
            return true;
        }
        return false;
    }

    /**
     * adds a laser to the next open spot for the getSuccessors method
     * @param Vault the vault to add to
     * @return the modified configuration
     */
    private SafeConfig nextLaser (String[][] Vault){
        add(Vault, rPointer, cPointer);
        if (cPointer == cols-1){
            cPointer = 0;
            rPointer++;
        }
        else{
            cPointer++;
        }
        return new SafeConfig(Vault, rPointer, cPointer);
    }


    /**
     * A helper method for ending the getSuccessors method
     * @param Vault the vault to modify
     * @return the modified vault
     */
    private SafeConfig nextLaserEnd (String[][] Vault){
        add(Vault, rPointer, cPointer);
        return new SafeConfig(Vault, rPointer, cPointer);
    }

    /**
     * adds an empty to the next open spot for the getSuccessors method
     * @param Vault the vault to add to
     * @return the added to vault
     */
    private SafeConfig nextEmpty(String[][] Vault){
        if (cPointer == cols-1){
            cPointer = 0;
            rPointer++;
        }
        else{
            cPointer++;
        }
        return new SafeConfig(Vault, rPointer, cPointer);
    }

    /**
     * makes successors for the getSuccessors method
     * @return a collection of SafeConfigs
     */
    public Collection<SafeConfig> successorsEnd(){
        SafeConfig orig1 = new SafeConfig(Vault, rPointer, cPointer);
        Collection<SafeConfig> successors = new ArrayList<>();

        SafeConfig b = ( orig1).nextLaserEnd(( orig1).getVault());
        successors.add(b);

        return successors;
    }

    /**
     * gets successors, one with an empty and one with a laser, for the backtracking algoritm and does some basic pruning
     * @return a collection of safeconfigs which are modified from the input safeconfig
     */
    @Override
    public Collection<SafeConfig> getSuccessors() {
//        System.out.println("c" + cPointer);
//        System.out.println("r" + rPointer);
//        System.out.println("v" + Vault[rPointer][cPointer]);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++){
                if (Vault[row][col].equals("4")){
                    this.Vault = maxPillar(this.Vault, row, col);
                }
                else if (Vault[row][col].matches("[1-3]")){
                    this.Vault = addOpenSpots(this.Vault, row, col);
                }
            }
        }
        while (!Vault[rPointer][cPointer].equals(".")){
            if (cPointer == cols-1 && rPointer == rows-1){
                return new ArrayList<>();
            }
            if (cPointer == cols-1){
                cPointer = 0;
                rPointer++;
            }
            else{
                cPointer++;
            }
        }
        if (cPointer == cols-1 && rPointer == rows-1){
            return successorsEnd();
        }

        SafeConfig orig1 = new SafeConfig(Vault, rPointer, cPointer);
        SafeConfig orig2 = new SafeConfig(Vault, rPointer, cPointer);
        Collection<SafeConfig> successors = new ArrayList<>();

        SafeConfig a = orig2.nextLaser(orig2.getVault());
        successors.add(a);
        SafeConfig b =orig1.nextEmpty(orig1.getVault());
        successors.add(b);

        return successors;
    }

    /**
     * checks to see if a given config is valid and contains some basic pruning
     * @return the validity of a configuration
     */
    @Override
    public boolean isValid() {
        String point;
        for (int row = 0; row < rows; row++){
            for (int col = 0 ; col < cols; col++){
                point = Vault[row][col];
                if (point.equals("L")){
                    if (!laserHelp(row, col)){
                        return false;
                    }
                }
                else if (point.matches("[0-9]")){
                    if (!pillarValid(row, col)){
                        return false;
                    }
                    if (!isValidPrune(this.Vault, row, col)){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * checks to see if a given config is the goal config
     * @return boolean goal config
     */
    @Override
    public boolean isGoal() {
        for (int row = 0; row < rows; row++){
            for (int col = 0 ; col < cols; col++){
                if (Vault[row][col].equals(".")){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * adds a laser to a target point, the adds the beams with the addBeams method
     * @param r the row for the laser to be placed at
     * @param c the column for the laser to be placed at
     */
    public String[][] add(String[][] Vault, int r, int c){
        Vault[r][c] = "L";
        if (r < rows-1) {
            for (int row = r + 1; row < rows; row++) {
                if (!(Vault[row][c].matches("[0-9]")) && !(Vault[row][c].equals("X")) && !Vault[row][c].equals("L")) {
                    Vault[row][c] = "*";
                } else {
                    break;
                }
            }
        }
        if (r > 0) {
            for (int row = r - 1; row >= 0; row--) {
                if (!(Vault[row][c].matches("[0-9]")) && !(Vault[row][c].equals("X")) && !Vault[row][c].equals("L")) {
                    Vault[row][c] = "*";
                } else {
                    break;
                }
            }
        }
        if (c < cols-1) {
            for (int col = c + 1; col < cols; col++) {
                if (!(Vault[r][col].matches("[0-9]")) && !(Vault[r][col].equals("X")) && !Vault[r][col].equals("L")) {
                    Vault[r][col] = "*";
                } else {
                    break;
                }
            }
        }
        if (c > 0) {
            for (int col = c - 1; col >= 0; col--) {
                if (!(Vault[r][col].matches("[0-9]")) && !(Vault[r][col].equals("X")) && !Vault[r][col].equals("L")) {
                    Vault[r][col] = "*";
                } else {
                    break;
                }
            }
        }
        return Vault;
    }

    @Override
    public String toString(){
        /**
         * Provides a string representation of the vault
         * @return The string representations of the vault
         */
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
//        System.out.println(model.getCurMessage());
//        System.out.println(S);
        return S;
    }

    /**
     * makes sure a given pillar has the correct amount of lasers attached to it
     * @param r the row of the pillar
     * @param c the column of the pillar
     * @return if the pillar has the correct amount of lasers
     */
    public boolean pillarValid(int r, int c){
        int pillar = Integer.parseInt(Vault[r][c]);
        int count = 0;
        if (r > 0){
            if (Vault[r-1][c].equals("L")){
                count++;
            }
        }
        if (r < rows-1){
            if (Vault[r+1][c].equals("L")){
                count++;
            }
        }
        if (c < cols-1){
            if (Vault[r][c+1].equals("L")){
                count++;
            }
        }
        if (c > 0){
            if (Vault[r][c-1].equals("L")){
                count++;
            }
        }
        return pillar >= count;
    }

    /**
     * a helper method for the verify function to see if a laser intersects with any other laser
     * and makes sure it has power with the powerHelp method
     * @param r the row for one of the lasers
     * @param c the column for the same laser
     * @return if the given laser intersects paths with any other laser
     */
    public boolean laserHelp(int r, int c){
        if (r < rows-1) {
            for (int row = r + 1; row < rows; row++) {
                if (!(Vault[row][c].matches("[0-9]")) && !(Vault[row][c].equals("X"))) {
                    if (Vault[row][c].equals("L")){
                        return false;
                    }
                } else {
                    break;
                }
            }
        }
        if (r > 0) {
            for (int row = r -1 ; row >= rows; row--) {
                if (!(Vault[row][c].matches("[0-9]")) && !(Vault[row][c].equals("X"))) {
                    if (Vault[row][c].equals("L")){
                        return false;
                    }
                } else {
                    break;
                }
            }
        }
        if (c < cols-1) {
            for (int col = c + 1; col < cols; col++) {
                if (!(Vault[r][col].matches("[0-9]")) && !(Vault[r][col].equals("X"))) {
                    if (Vault[r][col].equals("L")){
                        return false;
                    }
                } else {
                    break;
                }
            }
        }
        if (c > 0) {
            for (int col = c - 1; col >= 0; col--) {
                if (!(Vault[r][col].matches("[0-9]")) && !(Vault[r][col].equals("X"))) {
                    if (Vault[r][col].equals("L")){
                        return false;
                    }
                } else {
                    break;
                }
            }
        }
        if (r > 0){
            if (Vault[r-1][c].equals("0")){
                if (!powerHelp(r, c)){
                    return false;
                }
            }
        }
        if (r < rows-1){
            if (Vault[r+1][c].equals("0")){
                if (!powerHelp(r, c)){
                    return false;
                }
            }
        }
        if (c < cols-1){
            if (Vault[r][c+1].equals("0")){
                if (!powerHelp(r, c)){
                    return false;
                }
            }
        }
        if (c > 0){
            if (Vault[r][c-1].equals("0")){
                if (!powerHelp(r, c)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * makes sure a given laser has power provided to it
     * @param r the row of the laser
     * @param c the column of the laser
     * @return if the laser has power
     */
    public boolean powerHelp(int r, int c){
        if (r > 0){
            if (Vault[r-1][c].matches("[0-9]")){
                return true;
            }
        }
        if (r < rows-1){
            if (Vault[r+1][c].matches("[0-9]")){
                return true;
            }
        }
        if (c < cols-1){
            if (Vault[r][c+1].matches("[0-9]")){
                return true;
            }
        }
        if (c > 0){
            if (Vault[r][c-1].matches("[0-9]")){
                return true;
            }
        }
        return false;
    }
}
