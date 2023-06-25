package entities;

import java.io.*;
import java.util.ArrayList;

public class TargetFileSystem {
    private TargetFileSystem[] subsystems;
    private Parser ps;
    private File init_path_file;

    public TargetFileSystem(Parser ps){
        this.subsystems = null;
        this.init_path_file = null;
        this.ps = ps;
    }

    public void init(File path_file){
        if(path_file.isDirectory()){
            TargetFileSystem new_child;
            ArrayList<TargetFileSystem> children = new ArrayList<TargetFileSystem>();
            for(File child_file : path_file.listFiles()){
                new_child = new TargetFileSystem(this.ps);
                new_child.init(child_file);
                if(new_child.hasTargets()){
                    children.add(new_child);
                }
            }
            if(children.size() > 0){
                this.init_path_file = path_file;
                this.subsystems = new TargetFileSystem[children.size()];
                for(int i = 0; i < children.size(); i++){
                    subsystems[i] = children.get(i);
                }
            }
        }
        else if(path_file.getAbsolutePath().endsWith(this.ps.getParseExtension())){
            this.init_path_file = path_file;
        }
    }

    public boolean hasTargets(){
        return this.init_path_file != null;
    }

    public boolean isSingleFile(){
        return this.init_path_file != null && this.subsystems == null;
    }

    public boolean hasSubSystems(){
        return this.init_path_file != null && this.subsystems != null;
    }

    public File getFile(){
        if(this.isSingleFile()) return this.init_path_file;
        return null;
    }

    public TargetFileSystem[] getSubSystems(){
        if(this.hasSubSystems()) return this.subsystems;
        return null;
    }

    public String toString(){
        return this.prefixedToString("", '\t');
    }

    public String prefixedToString(String prefix, char seperator){
        String result = "";
        if(this.hasTargets()){
            if(this.isSingleFile()){
                result = prefix + this.init_path_file.getName() + '\n';
            }
            else{
                result = prefix + this.init_path_file.getName() + ":\n";
                prefix += seperator;
                for(TargetFileSystem obj : this.subsystems){
                    result += obj.prefixedToString(prefix, seperator);
                }
            }
        }
        else{
            result = prefix + "<empty>\n";
        }
        return result;
    }

    public String navigationToString(){
        String result = "";
        if(this.hasSubSystems()){
            result += this.init_path_file.getName() + ":\n\n[1] ..\n";
            for(int i = 0; i < this.subsystems.length; i++){
                result += "[" + (i + 2) + "] " + this.subsystems[i].init_path_file.getName() + "\n";
            }
        }
        return result;
    }
}
