package interfaces.UIs;

import java.util.Scanner;

import business.Presenter.Message;
import interfaces.UI;

public class CLI extends UI{
    private static String[] menuItems = {"[S]elect Class", "[P]resenter Status", "[Q]uit"};

    private static void showMenu(){
        System.out.println("Menu:\n");
        for(int i = 0; i < menuItems.length; i++){
            System.out.println((i + 1) + ") " + menuItems[i]);
        }
        System.out.print("\n> ");
    }

    private Scanner sc;

    public CLI(){
        super("Command Line Interface");
        this.sc = new Scanner(System.in);
    }
    
    public void showMessage(String msg, Message type){
        if(type == Message.INFORMATION_MESSAGE){
            System.out.println(msg);
        }
        else if(type == Message.WARNING_MESSAGE){
            System.err.println("Warning: " + msg);
        }
        else if(type == Message.ERROR_MESSAGE){
            System.err.println("Error: " + msg);
        }
    }

    public void mapAction(char input){
        switch(input){
            case 'S': this.p.selectActiveFile();
                      break;
            case 'P': if(this.p == null){
                          this.showMessage("presenter uninitalized", Message.ERROR_MESSAGE);
                      }
                      else{
                          this.showMessage(this.p.toString(), Message.INFORMATION_MESSAGE);
                      }
                      break;
            case 'Q': sc.close();
                      System.exit(0);
                      break;
            default:  this.showMessage("option not recognized", Message.ERROR_MESSAGE);
        }
    }

    public String requestInput(){
        return sc.nextLine();
    }

    public void run(){
        while(true){
            CLI.showMenu();
            this.mapAction(Character.toUpperCase(this.requestInput().charAt(0)));
            System.out.println("");
        }
    }
}
