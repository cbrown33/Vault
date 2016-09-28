package gui;

import backtracking.Backtracker;
import backtracking.SafeConfig;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

import model.*;

/**
 * The main class that implements the JavaFX UI.   This class represents
 * the view/controller portion of the UI.  It is connected to the model
 * and receives updates from it.
 *
 * @author Sean Strout @ RIT CS
 * @author Conor Browm
 * @author Gavin Xie
 */
public class LasersGUI extends Application implements Observer {
    /** The UI's connection to the model */
    private LasersModel model;

    /** The root stage of the GUI */
    private Stage rootStage;

    @Override
    public void init() throws Exception {
        // the init method is run before start.  the file name is extracted
        // here and then the model is created.
        try {
            Parameters params = getParameters();
            String filename = params.getRaw().get(0);
            this.model = new LasersModel(filename);
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
            System.exit(-1);
        }
        this.model.addObserver(this);
    }

    /**
     * A private utility function for setting the background of a button to
     * an image in the resources subdirectory.
     *
     * @param button the button control
     * @param bgImgName the name of the image file
     */
    private void setButtonBackground(Button button, String bgImgName) {
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image( getClass().getResource("resources/" + bgImgName).toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        button.setBackground(background);
    }

    /**
     * This is a private demo method that shows how to create a button
     * and attach a foreground image with a background image that
     * toggles from yellow to red each time it is pressed.
     *
     */
    private Button makeLaser(Button button, int row, int col) {
        Image laserImg = new Image(getClass().getResourceAsStream("resources/laser.png"));
        ImageView laserIcon = new ImageView(laserImg);
        button.setGraphic(laserIcon);
        setButtonBackground(button, "yellow.png");
        button.setOnMouseClicked(e -> {
            model.remove(row, col);
            model.announceChange();
                }
            );
        return button;
    }


    /**
     * The make function for the empty buttons to put into the grid pane
     * @param row the row that the button is assigned to
     * @param col the col that the button is assigned to
     * @return a button
     */
    private Button makeEmpty(Button button, int row, int col){
        Image emptyImg = new Image(getClass().getResourceAsStream("resources/white.png"));
        ImageView emptyIcon = new ImageView(emptyImg);
        setButtonBackground(button, "white.png");
        button.setGraphic(emptyIcon);

        button.setOnMouseClicked(e -> {
            model.add(row, col);
            model.announceChange();
        });

        if (row == model.getVerifyRow() && col== model.getVerifyCol()) {
            button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("resources/red.png"))));
            model.setVerifyCol(-1);
            model.setVerifyRow(-1);
        }
        return button;
    }


    /**
     * the make function for the pillars
     * @param id the type of pillar to make
     * @return a pillar button
     */
    private Button makePillar(Button button, String id, int row, int col){
        Image pillarImg = new Image(getClass().getResourceAsStream("resources/white.png"));
        switch (id){
            case "0":
                pillarImg = new Image(getClass().getResourceAsStream("resources/pillar0.png"));
                break;
            case "1":
                pillarImg = new Image(getClass().getResourceAsStream("resources/pillar1.png"));
                break;
            case "2":
                pillarImg = new Image(getClass().getResourceAsStream("resources/pillar2.png"));
                break;
            case "3":
                pillarImg = new Image(getClass().getResourceAsStream("resources/pillar3.png"));
                break;
            case "4":
                pillarImg = new Image(getClass().getResourceAsStream("resources/pillar4.png"));
                break;
            case "X":
                pillarImg = new Image(getClass().getResourceAsStream("resources/pillarX.png"));
                break;
        }
        ImageView pillarIcon = new ImageView(pillarImg);
        setButtonBackground(button, "white.png");
        button.setGraphic(pillarIcon);
        button.setOnMouseClicked(e -> {
            model.add(row, col);
            model.announceChange();
        });
        return button;
    }

    /**
     * makes a button that represents a beam
     * @return the button
     */
    private Button makeBeam(Button button, int row, int col){
        Image beamImg = new Image(getClass().getResourceAsStream("resources/beam.png"));
        ImageView beamIcon = new ImageView(beamImg);
        button.setGraphic(beamIcon);
        button.setOnMouseClicked(e -> {
            model.add(row, col);
            model.announceChange();
        });
        setButtonBackground(button, "yellow.png");
        return button;
    }

    /**
     * The
     * @param stage the stage to add UI components into
     */
    private void init(Stage stage){
        BorderPane root = new BorderPane();
        root.setTop(makeTop());
        root.setCenter(makeVault(model));
        root.setBottom(makeButtons());
        root.setBackground(new Background(new BackgroundFill(Color.web("#0080FF"), CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(root);
        stage.setScene(scene);
//        buttonDemo(stage);  // this can be removed/altered
    }

    /**
     * makes the top which contains the text from the model
     * @return the top pane
     */
    private GridPane makeTop(){
        GridPane gp = new GridPane();
        gp.setPadding(new Insets(25, 25, 0, 25));
        gp.setAlignment(Pos.CENTER);

        Text t = new Text(model.getCurMessage());
        gp.add(t, 0, 0);

        return gp;
    }

    /**
     * makes all of the buttons, which represent the bottom of the GUI
     * @return the gridpane of buttons
     */
    private GridPane makeButtons(){
        GridPane gp = new GridPane();
        gp.setPadding(new Insets(0, 25, 25, 25));
        gp.setAlignment(Pos.CENTER);
        gp.setVgap(5);
        gp.setHgap(5);

        Button Check = new Button("Check");
        Check.setOnMouseClicked(e -> model.verify());
        Check.setMinSize(80, 0);
        gp.add(Check, 0, 0);

        Button Hint = new Button("Hint");
        Hint.setOnMouseClicked(e -> hint());
        Hint.setMinSize(80, 0);
        gp.add(Hint, 1, 0);

        Button Solve = new Button("Solve");
        Solve.setOnMouseClicked(e -> solve());
        Solve.setMinSize(80, 0);
        gp.add(Solve, 2, 0);

        Button Restart = new Button("Restart");
        Restart.setOnMouseClicked(e -> restart());
        gp.add(Restart, 3, 0);
        Restart.setMinSize(80, 0);

        Button Load = new Button("Load");
        Load.setOnMouseClicked(e -> loadFile());
        Load.setMinSize(80, 0);

        gp.add(Load, 4, 0);

        return gp;
    }

    private void hint(){
        Backtracker bt = new Backtracker(false, model);
        try {
            SafeConfig sc = new SafeConfig(model.getCurFile());
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
            System.exit(-1);
        }
        SafeConfig sc = new SafeConfig(model.getVault(), 0, 0);
        ArrayList<Integer> path = bt.solveWithPath(sc);
        if (path.size() > 0){
            model.add(path.get(0), path.get(1));
            model.setCurMessage("Hint added.");
        }
        else{
            if (model.verify()){
                model.setCurMessage("Safe is fully populated.");
            }
            else {
                model.setCurMessage("No solution present.");
            }
        }
        model.announceChange();
    }

    /**
     * solves the current puzzle and shows it on the GUI
     */
    private void solve(){
        try {
            Backtracker bt = new Backtracker(false, model);
            SafeConfig sc = new SafeConfig(model.getCurFile());
            // start the clock
            double start = System.currentTimeMillis();

            Optional<SafeConfig> c = bt.solve(sc);

            // compute the elapsed time
            System.out.println("Elapsed time: " +
                    (System.currentTimeMillis() - start) / 1000.0 + " seconds.");


            if (c.isPresent()) {
                String[][] Vault = c.get().getVault();
                model.setVault(Vault);
                model.setCurMessage("Safe is solved");
            } else {
                model.setCurMessage("No solution present");
            }
            model.announceChange();
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
            System.exit(-1);
        }
    }

    /**
     * restarts the gui to the original vault provided in the file
     */
    private void restart(){
        try{
            model = new LasersModel(model.getCurFile());
            model.addObserver(this);
            model.announceChange();
        }
        catch (FileNotFoundException fnfe){
            System.out.println(fnfe.getMessage());
            System.exit(-1);
        }
    }

    /**
     * loads a file that the user provides and resets the vault to that new file
     */
    private void loadFile(){
        try{
            File f;
            FileChooser fc = new FileChooser();
            fc.setTitle("Choose a Vault file");
            try {
                fc.setInitialDirectory(new File(System.getProperty("user.dir")));
                f = fc.showOpenDialog(new Stage());
                String fn = f.getName();
                try {
                    model = new LasersModel(f.toString());
                    model.setCurMessage(fn + " loaded.");
                    model.addObserver(this);
                    model.announceChange();
                }
                catch (NumberFormatException e){
                    model.setCurMessage("Wrong file type loaded.");
                    model.announceChange();
                }
            }
            catch (NullPointerException npe){
                model.setCurMessage("No file specified");
            }

        }
        catch (FileNotFoundException fnfe){
            System.out.println(fnfe.getMessage());
            System.exit(-1);
        }
    }

    /**
     * makes a representation of the vault, which is a gridpane of all elements of the vault
     * @param model the model to make a representation of
     * @return the gridpane of tha vault
     */
    private GridPane makeVault(LasersModel model){
        GridPane gp = new GridPane();
        gp.setPadding(new Insets(25));
        gp.setHgap(-8);
        gp.setVgap(0);
        gp.setAlignment(Pos.CENTER);

        int rows = model.getRows();
        int cols = model.getCols();
        String[][] Vault = model.getVault();

        for (int row = 0; row < rows; row++){
            for (int col = 0; col < cols; col++){
                Button button = new Button();

                switch (Vault[row][col]){

                    case "L":
                        button = makeLaser(button, row, col);
                        break;
                    case ".":
                        button = makeEmpty(button, row, col);
                        break;
                    case "0":
                        button = makePillar(button, "0", row, col);
                        break;
                    case "1":
                        button = makePillar(button, "1", row, col);
                        break;
                    case "2":
                        button = makePillar(button, "2", row, col);
                        break;
                    case "3":
                        button = makePillar(button, "3", row, col);
                        break;
                    case "4":
                        button = makePillar(button, "4", row, col);
                        break;
                    case "X":
                        button = makePillar(button, "X", row, col);
                        break;
                    case "*":
                        button = makeBeam(button, row, col);

                }
                if (row == model.getVerifyRow() && col== model.getVerifyCol()){
                    setButtonBackground(button, "red.png");
                    model.setVerifyCol(-1);
                    model.setVerifyRow(-1);
                }
                gp.add(button, col, row);
            }
        }

        return gp;
    }

    @Override
    /**
     * starts the initial GUI
     */
    public void start(Stage primaryStage) throws Exception {
        init(primaryStage);  // do all your UI initialization here
        this.rootStage = primaryStage;

        primaryStage.setTitle("Lasers");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    /**
     * upadtes the GUI when changes have been made to the model
     */
    public void update(Observable o, Object arg) {
        BorderPane root = new BorderPane();
        root.setTop(makeTop());
        root.setCenter(makeVault(model));
        root.setBottom(makeButtons());
        root.setBackground(new Background(new BackgroundFill(Color.web("#0080FF"), CornerRadii.EMPTY, Insets.EMPTY)));
        Scene scene = new Scene(root);
        rootStage.setScene(scene);
    }
}
