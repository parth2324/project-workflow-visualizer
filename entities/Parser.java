package entities;
import java.io.File;
import java.util.List;
import java.util.Scanner;
import java.util.regex.*;

public abstract class Parser{
    protected static final String EXTENDS_REP = "<", IMPLEMENTS_REP = "+", PACKAGE_CON = ".";

    protected String str_heap_head, str_heap_tail, doc_heap_head, doc_heap_tail;
    protected String raw_code, type, extension;
    protected String[][] keywords;
    protected Pair<String, Pair<String[], String[]>> cleaned_code;

    public Parser(String type, String extension, String str_heap_head, String str_heap_tail, String doc_heap_head, String doc_heap_tail, String[][] keywords){
        this.raw_code = "";
        this.cleaned_code = null;
        this.type = type;
        this.extension = extension;
        this.str_heap_head = str_heap_head;
        this.str_heap_tail = str_heap_tail;
        this.doc_heap_head = doc_heap_head;
        this.doc_heap_tail = doc_heap_tail;
        this.keywords = keywords;
    }

    public boolean load(File src){
        try{
            Scanner sc = new Scanner(src);
            sc.useDelimiter("\\Z");
            this.raw_code = sc.next().trim();
            sc.close();
            this.cleaned_code = this.clean();
            return true;
        }catch(Exception e){
            this.raw_code = "";
            this.cleaned_code = null;
            return false;
        }
    }

    public String getParseType(){
        return this.type;
    }

    public String getParseExtension(){
        return this.extension;
    }

    public String getRawCode(){
        return this.raw_code;
    }

    public boolean isLoaded(){
        return this.raw_code != "";
    }

    public boolean isLoadedClean(){
        return this.cleaned_code != null;
    }

    /*
     * returns pair(raw code, pair(strings heap, documentation heap)).
     */
    public abstract Pair<String, Pair<String[], String[]>> clean();

    /*
     * returns pair(segmented raw code, pair(strings heap, documentation heap)).
     * 
     * segemented implies functions extracted and class information
     */
    public abstract Pair<String[], Pair<String[], String[]>> parse();
    
    /*
     * always load parser with data, then search for classes in data. 
     */
    public abstract void addClasses(TargetFileSystem tfs_link, List<Pair<String, TargetFileSystem>> data);
    
    public abstract Pattern getFunctionPattern();
    public abstract Pattern getClassPattern();
    public abstract Pattern getPackagePattern();
}