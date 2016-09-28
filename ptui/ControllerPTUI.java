package ptui;

import model.LasersModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class represents the controller portion of the plain text UI.
 * It takes the model from the view (LasersPTUI) so that it can perform
 * the operations that are input in the run method.
 *
 * @author Sean Strout @ RIT CS
 * @author Conor Brown
 * @author Gavin Xie
 */
public class ControllerPTUI  {
    /** The UI's connection to the model */
    private LasersModel model;
    private Scanner filesc;

    /**
     * Construct the PTUI.  Create the model and initialize the view.
     * @param model The laser model
     */
    public ControllerPTUI(LasersModel model) {
        this.model = model;
    }

    /**
     * Run the main loop.  This is the entry point for the controller
     * @param inputFile The name of the input command file, if specified
     */
    public void run(String inputFile) throws FileNotFoundException{
        if ((inputFile == null)){
            inputFile = "";
        }
        runVault(inputFile);
    }

    /**
     * simulates the vault, parsing commands first from an input file, then from the user input
     * @param input the input file, if provided
     * @throws FileNotFoundException
     */
    public void runVault(String input)throws FileNotFoundException{
        boolean running = true;
        boolean hasFile = false;
        if (!input.equals("")) {
            filesc = new Scanner(new File(input));
            hasFile = true;
        }
        Scanner sc = new Scanner(System.in);
        String commands = "";
        while (running){
            System.out.print("> ");
            if (hasFile && filesc.hasNextLine()) {
                commands = filesc.nextLine().trim();
                if (!(commands.length() > 0)){
                    commands = " ";
                }
                System.out.print(commands + "\n");
            }
            else if (sc.hasNext()){
                commands = sc.nextLine().trim();
                if (!(commands.length() > 0)){
                    commands = " ";
                }
            }
            switch (commands.toLowerCase().charAt(0)) {
                case 'h':
                    model.printHelp();
                    break;
                case 'q':
                    running = false;
                    break;
                case 'd':
                    model.announceChange();
                    break;
                case 'v':
                    model.verify();
                    break;
                case 'a':
                    String[] adds = commands.split(" ");
                    if (adds.length == 3){
                        model.add(Integer.parseInt(adds[1]), Integer.parseInt(adds[2]));
                        model.announceChange();
                    }
                    else {
                        System.out.println("Incorrect coordinates");
                    }
                    break;
                case 'r':
                    String[] removes = commands.split(" ");
                    if (removes.length == 3){
                        model.remove(Integer.parseInt(removes[1]), Integer.parseInt(removes[2]));
                        model.announceChange();
                    }
                    else {
                        System.out.println("Invalid remove command.");
                    }
                    break;
                case ' ':
                    break;
                default:
                    System.out.println("Unrecognized command: " + commands);
                    break;
            }

        }

    }
}
