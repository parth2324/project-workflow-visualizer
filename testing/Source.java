package testing;
import java.io.File;

import business.Presenter;
import entities.Pair;
import entities.Parser;

public class Source{
    public static final String ROOT = "__root__", NO_PACKAGE = "__file__";

    public String package_name;
    private Source[] data;
    private Pair<CodePiece, Pair<String[], String[]>> codefile;

    public Source(){
        this.package_name = "";
        this.data = null;
        this.codefile = null;
    }

    public boolean init(File f, boolean silent, Presenter pr, Parser ps){
        if(f.isDirectory()){
            this.package_name = "[dir] "+ f.getName();
            File[] sub_files = f.listFiles();
            this.data = new Source[sub_files.length];
            this.codefile = null;
            for(int i = 0; i < this.data.length; i++){
                this.data[i] = new Source();
                if(!this.data[i].init(sub_files[i], silent, pr, ps)) return false;
            }
        }else{
            this.package_name = "[file] "+ f.getName();
            this.data = null;
            if(!ps.init(f, silent, pr)) return false;
            this.codefile = ps.parse();
        }
        return true;
    }

    public String toString(Parser ps){
        String prt = "Package name : " + this.package_name + "\n";
        if(this.data == null){
            prt += "String map :-\n";
            for(int i = 0; i < this.codefile.second.first.length; i++){
                prt += ps.str_heap_head + i + ps.str_heap_tail + " : " + this.codefile.second.first[i] + "\n";
            }
            prt += "Documentation map :-\n";
            for(int i = 0; i < this.codefile.second.second.length; i++){
                prt += ps.doc_heap_head + i + ps.doc_heap_tail + " : " + this.codefile.second.second[i] + "\n";
            }
            prt += "File contents :-\n";
            prt += this.codefile.first.toString();
        }else{
            for(Source i : this.data){
                prt += i.toString(ps);
            }
        }
        prt += "Package ends here.\n";
        return prt;
    }
}