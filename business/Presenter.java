package business;

import java.io.File;

import javax.swing.JFileChooser;

import business.parsers.JavaParser;
import entities.ActiveClass;
import entities.Parser;
import entities.TargetClasses;
import entities.TargetFileSystem;
import interfaces.UI;

public class Presenter{
    public enum Message{
        INFORMATION_MESSAGE,
        WARNING_MESSAGE,
        ERROR_MESSAGE
    }

    private static File askSource(){
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.showOpenDialog(null);
        return chooser.getSelectedFile();
    }

    private TargetFileSystem tfs;
    private TargetClasses tcs;
    private ActiveClass ac;
    private Parser ps;
    private UI ui;

    public Presenter(UI ui){
        this.tfs = null;
        this.tcs = null;
        this.ac = null;
        this.ps = null;
        this.ui = ui;
    }

    public void init(File source, String parser){
        if(source == null){
            source = Presenter.askSource();
        }
        if(parser.equalsIgnoreCase(JavaParser.TYPE)){
            this.ps = new JavaParser();
        }
        if(this.ps != null){
            this.tfs = new TargetFileSystem(this.ps);
            this.tfs.init(source);
            if(this.isReady()){
                this.tcs = new TargetClasses(this.ps);
                this.tcs.init(this.tfs);
            }
        }
    }

    public void selectActiveFile(){
        this.ui.showMessage("Select file:\n", Message.INFORMATION_MESSAGE);

        TargetFileSystem curr = this.tfs;
        int option = -1;
        while(curr.hasSubSystems()){
            this.ui.showMessage(curr.navigationToString(), Message.INFORMATION_MESSAGE);
            try{
                option = Integer.parseInt(this.ui.requestInput());
                if(option <= 0 || option >= (curr.getSubSystems().length + 2)){
                    throw new NumberFormatException();
                }
            }
            catch(NumberFormatException e){
                this.ui.showMessage("option not recognized", Message.ERROR_MESSAGE);
                continue;
            }
            if(option == 1){
                if(!curr.hasParent()){
                    this.ui.showMessage("parent not found", Message.ERROR_MESSAGE);
                }
                else curr = curr.getParent();
            }
            else{
                curr = (curr.getSubSystems())[option - 2];
            }
        }
        if(curr.isSingleFile()){
            this.ac = new ActiveClass(curr.getFile().getAbsolutePath(), this.ps);
            this.ui.showMessage(curr.getFile().getName() + " selected", Message.INFORMATION_MESSAGE);
        }
        else this.ac = null;
    }

    public boolean isReady(){
        return this.tfs.hasTargets() && ui != null;
    }

    public String toString(){
        return "Presenter:-\n\nUI: " + (this.ui == null ? "undefined" : this.ui.getType()) +
               "\n\nTarget File System:\n" + this.tfs.toString() +
               "\nTarget Classes:\n" + (this.tcs == null ? "<empty>\n" : this.tcs.toString());
    }
}