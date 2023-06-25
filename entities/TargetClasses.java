package entities;

import java.util.*;

public class TargetClasses {
    private String[] classes;
    private Parser ps;

    public TargetClasses(Parser ps){
        this.classes = null;
        this.ps = ps;
    }

    public void init(TargetFileSystem root){
        ArrayList<String> classes = new ArrayList<String>();
        this.rec_init(root, classes);
        if(classes.size() > 0){
            this.classes = new String[classes.size()];
            for(int i = 0; i < classes.size(); i++){
                this.classes[i] = classes.get(i);
            }
        }
    }

    private void rec_init(TargetFileSystem src, List<String> classes){
        if(src.isSingleFile()){
            this.ps.load(src.getFile());
            this.ps.addClasses(classes);
        }
        else if(src.hasSubSystems()){
            for(TargetFileSystem tfs : src.getSubSystems()){
                rec_init(tfs, classes);
            }
        }
    }

    public String[] getClasses(){
        return this.classes;
    }

    public String toString(){
        String result = "";
        for(String i : this.classes){
            result += i + '\n';
        }
        return result;
    }
}
