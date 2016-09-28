package model;

import backtracking.SafeConfig;
import ptui.LasersPTUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Optional;
import java.util.Scanner;

public class LasersModel extends Observable {

    private int rows = 0;
    private int cols = 0;
    private String[][] Vault;
    private String curFile;
    private String curMessage;
    private Optional<SafeConfig> Solution;
    private int verifyRow;
    private int verifyCol;
    private boolean changed;

    public LasersModel(String filename) throws FileNotFoundException {
        this.Solution = Optional.empty();
        verifyCol = -1;
        verifyRow = -1;
        changed = false;

        String[] ln;
        File file = new File(filename);
        Scanner in = new Scanner(file);


        String s = in.nextLine().trim();
        ln = s.split(" ");
        this.rows = Integer.parseInt(ln[0]);
        this.cols = Integer.parseInt(ln[1]);

        this.Vault = new String[this.rows][this.cols];

        for (int row = 0; row < this.rows; row++) {
            ln = in.nextLine().trim().split(" ");
            for (int col = 0; col < this.cols; col++){
                this.Vault[row][col] = ln[col];
            }
        }
        in.close();
        this.curFile = filename;
        this.curMessage = file.getName() + " loaded";
    }

    public int getCols(){
        return cols;
    }

    public int getRows(){
        return rows;
    }

    public void setVerifyCol(int verifyCol) {
        this.verifyCol = verifyCol;
    }

    public void setVerifyRow(int verifyRow) {
        this.verifyRow = verifyRow;
    }

    public int getVerifyCol() {
        return verifyCol;
    }

    public int getVerifyRow() {
        return verifyRow;
    }

    public String[][] getVault(){
        return Vault;
    }

    public void setVault(java.lang.String[][] vault) {
        for (int row = 0; row < this.rows; row++) {
            System.arraycopy(vault[row], 0, this.Vault[row], 0, vault[row].length);
        }
    }

    public String getCurFile(){ return curFile; }

    public String getCurMessage(){ return curMessage; }

    public Optional<SafeConfig> getSolution() {
        return Solution;
    }

    public void setSolution(Optional<SafeConfig> solution) {
        Solution = solution;
    }

    public void setCurMessage(String message){
        this.curMessage = message;
        announceChange();
    }

    /**
     * removes a laser from a target point, if a laser is present
     * it calls the removeBeams function to remove the beams from this laser
     * @param r the row to remove the laser at
     * @param c the column to remove the laser at
     */
    public void remove(int r, int c){
        if (r >= rows || c >= cols || r < 0 || c < 0){
            curMessage = ("Error removing laser at: (" + r + ", " + c + ")" );
        }
        else if (Vault[r][c].equals("L")){
            Vault[r][c] = ".";
            removeBeams(r, c);
            for (int row = 0; row < rows; row++){
                for (int col = 0; col < cols; col++){
                    if (Vault[row][col].equals("L")){
                        addBeams(row, col);
                    }
                }
            }

            curMessage = ("Laser removed at: (" + r + ", " + c + ")");
        }
        else {
            curMessage = ("Error removing laser at: (" + r + ", " + c + ")");
        }
    }

    @Override
    /**
     * Provides a string representation of the vault
     * @return The string representations of the vault
     */
    public String toString() {
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
        return(S);
    }

    /**
     * removes the beams from a laser that was removed, repopulates the beams with the addBeams method
     * @param r the row of the removed laser
     * @param c the column of the removed laser
     */
    public void removeBeams(int r , int c){
        if (r < rows-1) {
            for (int row = r + 1; row < rows; row++) {
                if (!(Vault[row][c].matches("[0-9]")) && !(Vault[row][c].equals("X")) && !Vault[row][c].equals("L")) {
                    Vault[row][c] = ".";
                } else {
                    break;
                }
            }
        }
        if (r > 0) {
            for (int row = r - 1; row >= 0; row--) {
                if (!(Vault[row][c].matches("[0-9]")) && !(Vault[row][c].equals("X")) && !Vault[row][c].equals("L")) {
                    Vault[row][c] = ".";
                } else {
                    break;
                }
            }
        }
        if (c < cols-1) {
            for (int col = c + 1; col < cols; col++) {
                if (!(Vault[r][col].matches("[0-9]")) && !(Vault[r][col].equals("X")) && !Vault[r][col].equals("L")) {
                    Vault[r][col] = ".";
                } else {
                    break;
                }
            }
        }
        if (c > 0) {
            for (int col = c - 1; col >= 0; col--) {
                if (!(Vault[r][col].matches("[0-9]")) && !(Vault[r][col].equals("X")) && !Vault[r][col].equals("L")) {
                    Vault[r][col] = ".";
                } else {
                    break;
                }
            }
        }
    }

    /**
     * adds a laser to a target point, the adds the beams with the addBeams method
     * @param r the row for the laser to be placed at
     * @param c the column for the laser to be placed at
     */
    public void add(int r, int c){
        changed = true;
        if (r >= rows || c >= cols || r < 0 || c < 0){
            curMessage = ("Error adding laser at: (" + r + ", " + c + ")" );
        }
        else if (!(Vault[r][c].matches("[0-9]")) && !(Vault[r][c].equals("X"))){
            Vault[r][c] = "L";
            curMessage = ("Laser added at: (" + r + ", " + c + ")");
            addBeams(r, c);
        }
        else {
            curMessage = ("Error adding laser at: (" + r + ", " + c + ")");
        }
    }

    /**
     * adds the beams for a laser at a point
     * @param r the row for the laser beams to originate
     * @param c the column for the laser beams to originate
     */
    public void addBeams(int r , int c){
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
    }

    /**
     * verifies if a given vault layout is valid
     * @return if the given vault layout is valid
     */
    public boolean verify(){
        String point;
        for (int row = 0; row < rows; row++){
            for (int col = 0 ; col < cols; col++){
                point = Vault[row][col];
                switch (point){
                    case ".":
                        curMessage = ("Error verifying at: (" + row + ", " + col +")");
                        verifyCol = col;
                        verifyRow = row;
                        announceChange();
                        return false;
                    case "L":
                        if (!laserHelp(row, col)){
                            curMessage = ("Error verifying at: (" + row + ", " + col +")");
                            verifyCol = col;
                            verifyRow = row;
                            announceChange();
                            return false;
                        }
                        break;
                }
                if (point.matches("[0-9]")){
                    if (!pillarHelp(row, col)){
                        curMessage = ("Error verifying at: (" + row + ", " + col +")");
                        verifyCol = col;
                        verifyRow = row;
                        announceChange();
                        return false;
                    }
                }
            }
        }
        curMessage = ("Safe is fully verified!");
        announceChange();
        return true;
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

    /**
     * makes sure a given pillar has the correct amount of lasers attached to it
     * @param r the row of the pillar
     * @param c the column of the pillar
     * @return if the pillar has the correct amount of lasers
     */
    public boolean pillarHelp(int r, int c){
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
        return pillar == count;
    }

    /**
     * prints out the help function
     */
    public void printHelp(){
        System.out.println("a|add r c: Add laser to (r,c)\n" +
                "d|display: Display safe\n" +
                "h|help: Print this help message\n" +
                "q|quit: Exit program\n" +
                "r|remove r c: Remove laser from (r,c)\n" +
                "v|verify: Verify safe correctness");
    }

    /**
     * A utility method that indicates the model has changed and
     * notifies observers
     */
    public void announceChange() {
        setChanged();
        notifyObservers();
    }
}
