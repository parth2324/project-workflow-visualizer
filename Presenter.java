import java.io.File;
import javax.swing.JFileChooser;

enum Message{
    INFORMATION_MESSAGE,
    WARNING_MESSAGE,
    ERROR_MESSAGE
}

public abstract class Presenter{
    public static File askFile(){
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.showOpenDialog(null);
        return chooser.getSelectedFile();
    }

    private Source src;
    private Parser ps;
    private boolean initalized;

    public Presenter(){
        this.initalized = false;
    }

    public boolean init(File f, boolean silent, Parser ps){
        this.src = new Source();
        this.ps = ps;
        this.initalized = src.init(f, silent, this, ps);
        return this.initalized;
    }

    public boolean ready(){
        return this.initalized;
    }

    public void printData(){
        this.showMessage(src.toString(ps), Message.INFORMATION_MESSAGE);
    }

    public abstract void start_visualisation();
    public abstract void showMessage(String msg, Message code);
}

class CLIPresenter extends Presenter{
    public void start_visualisation(){
        System.out.println("work in progress, come later :)");
    }

    public void showMessage(String msg, Message code){
        if(code == Message.ERROR_MESSAGE || code == Message.WARNING_MESSAGE){
            System.err.println(msg);
        }
        else if(code == Message.INFORMATION_MESSAGE){
            System.out.println(msg);
        }
    }
}