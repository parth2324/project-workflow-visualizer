package entities;

import java.util.*;

public class TargetClasses {
    /*
     * data format <target class, link to file in tfs>
     */
    private ArrayList<Pair<String, TargetFileSystem>> data;
    private String[] classes;
    private Parser ps;

    public TargetClasses(Parser ps){
        this.data = null;
        this.classes = null;
        this.ps = ps;
    }

    public void init(TargetFileSystem root){
        this.data = new ArrayList<Pair<String, TargetFileSystem>>();
        this.rec_init(root);
        this.classes = new String[this.data.size()];
        for(int i = 0; i < classes.length; i++){
            classes[i] = this.data.get(i).first;
        }
    }

    private void rec_init(TargetFileSystem src){
        if(src.isSingleFile()){
            this.ps.load(src.getFile());
            this.ps.addClasses(src, this.data);
        }
        else if(src.hasSubSystems()){
            for(TargetFileSystem tfs : src.getSubSystems()){
                rec_init(tfs);
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
