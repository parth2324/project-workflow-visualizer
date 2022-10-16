import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public abstract class Parser{
    public String str_heap_head, str_heap_tail, doc_heap_head, doc_heap_tail;
    private String raw, type;
    private String[][] keywords;
    private boolean initalized;

    public Parser(String type, String str_heap_head, String str_heap_tail, String doc_heap_head, String doc_heap_tail, String[][] keywords){
        this.raw = "";
        this.type = type;
        this.str_heap_head = str_heap_head;
        this.str_heap_tail = str_heap_tail;
        this.doc_heap_head = doc_heap_head;
        this.doc_heap_tail = doc_heap_tail;
        this.keywords = keywords;
        this.initalized = false;
    }

    public boolean init(File f, boolean silent, Presenter p){
        try{
            if(f.isDirectory()) throw new IllegalArgumentException("Got a directory to parse.");
            if(!f.getAbsolutePath().endsWith(this.getParseType())) throw new IllegalArgumentException("Got non-" + this.getParseType() + " files to parse.");
            Scanner src = new Scanner(f);
            src.useDelimiter("\\Z");
            this.raw = src.next();
            src.close();
            this.initalized = true;
        }catch(FileNotFoundException e1){
            if(!silent)p.showMessage("File not found.", Message.ERROR_MESSAGE);
            this.raw = null;
            this.initalized = false;
        }catch(IllegalArgumentException e2){
            if(!silent)p.showMessage(e2.getMessage(), Message.ERROR_MESSAGE);
            this.raw = null;
            this.initalized = false;
        }
        return this.initalized;
    }

    public int getRawCodeType(String line){
        /*
         * returns rough code type analysis
         * PRE: query 'line' is clean
         */
        int result = 0, flag = 1, ind;
        for(String[] list : this.keywords){
            for(String keyword : list){
                ind = line.indexOf(keyword);
                if(ind == -1)continue;
                // proper keyword check here !!
                // add array handling to parser !!
                if(ind != -1){
                    result = result | flag;
                    break;
                }
            }
            flag = flag << 1;
        }
        if(result == 0) return flag;
        return result;
    }

    public boolean isReady(){
        return this.initalized;
    }

    public String getParseType(){
        return this.type;
    }

    public String getRawCode(){
        return this.raw;
    }

    /*
     * returns pair(root code piece, pair(strings heap, documentation heap)).
     */
    public abstract Pair<CodePiece, Pair<String[], String[]>> parse();
}

class JavaParser extends Parser{
    private static String[][] keywords = {{"do", "while", "for"},
                                          {"if", "else"},
                                          {"try", "catch", "finally"},
                                          {"return", "continue", "break", "assert", "throw"},
                                          {"class", "enum"}};

    public JavaParser(){
        super("java", "<str", ">", "<doc", ">", keywords);
    }

    private Pair<String, Pair<String[], String[]>> clean(){
        String cleaned = this.getRawCode();
        if(cleaned == null || cleaned.trim().length() == 0) return null;
        int from = -1, delta_str = this.str_heap_head.length() + this.str_heap_tail.length(), delta_doc = this.doc_heap_head.length() + this.doc_heap_tail.length();
        ArrayList<String> str_heap = new ArrayList<String>();
        ArrayList<String> doc_heap = new ArrayList<String>();
        for(int i = 0; i < cleaned.length();i++){
            if(cleaned.charAt(i) == '\''){
                from = i;
                i++;
                while(!(cleaned.charAt(i) == '\'' && (cleaned.charAt(i-1) != '\\' || (i - 2 >= 0 && cleaned.charAt(i-1) == '\\' && cleaned.charAt(i-2) == '\\'))))i++;
                str_heap.add(cleaned.substring(from, i+1));
                cleaned = cleaned.substring(0, from) + this.str_heap_head + (str_heap.size() - 1) + this.str_heap_tail + cleaned.substring(i+1);
                i += delta_str + ("" + (str_heap.size() - 1)).length() - str_heap.get(str_heap.size() - 1).length();
            }
            else if(cleaned.charAt(i) == '\"'){
                from = i;
                i++;
                while(!(cleaned.charAt(i) == '\"' && (cleaned.charAt(i-1) != '\\' || (i - 2 >= 0 && cleaned.charAt(i-1) == '\\' && cleaned.charAt(i-2) == '\\'))))i++;
                str_heap.add(cleaned.substring(from, i+1));
                cleaned = cleaned.substring(0, from) + this.str_heap_head + (str_heap.size() - 1) + this.str_heap_tail + cleaned.substring(i+1);
                i += delta_str + ("" + (str_heap.size() - 1)).length() - str_heap.get(str_heap.size() - 1).length();
            }
            else if(i + 1 < cleaned.length() && cleaned.charAt(i) == '/' && cleaned.charAt(i + 1) == '/'){
                from = i;
                i+=2;
                while(cleaned.charAt(i) != '\n')i++;
                doc_heap.add(cleaned.substring(from, i));
                cleaned = cleaned.substring(0, from) + this.doc_heap_head + (doc_heap.size() - 1) + this.doc_heap_tail + cleaned.substring(i);
                i += delta_doc + ("" + (doc_heap.size() - 1)).length() - doc_heap.get(doc_heap.size() - 1).length();
            }
            else if(i + 1 < cleaned.length() && cleaned.charAt(i) == '/' && cleaned.charAt(i + 1) == '*'){
                from = i;
                i+=3;
                while(!(cleaned.charAt(i) == '/' && cleaned.charAt(i - 1) == '*'))i++;
                doc_heap.add(cleaned.substring(from, i+1));
                cleaned = cleaned.substring(0, from) + this.doc_heap_head + (doc_heap.size() - 1) + this.doc_heap_tail + cleaned.substring(i+1);
                i += delta_doc + ("" + (doc_heap.size() - 1)).length() - doc_heap.get(doc_heap.size() - 1).length();
            }
        }
        String[] str_heap_ = new String[str_heap.size()];
        String[] doc_heap_ = new String[doc_heap.size()];
        for(int i = 0; i < str_heap.size(); i++){
            str_heap_[i] = str_heap.get(i);
        }
        for(int i = 0; i < doc_heap.size(); i++){
            doc_heap_[i] = doc_heap.get(i);
        }
        return new Pair<String, Pair<String[], String[]>>(cleaned, new Pair<String[], String[]>(str_heap_, doc_heap_));
    }

    public Pair<CodePiece, Pair<String[], String[]>> parse(){
        Pair<String, Pair<String[], String[]>> clean = this.clean();
        if(clean == null) return null;
        return new Pair<CodePiece, Pair<String[], String[]>>(this.rec_parse(clean.first), clean.second);
    }

    private CodePiece rec_parse(String code){     // UNTESTED !!!
        /*
         * PRE : code clean and not null.
         * private recursor for parser.
         */
        int count, from;
        String line;
        ArrayList<Pair<Meta, Object>> data = new ArrayList<Pair<Meta, Object>>();
        for(int i = 0; i < code.length(); i++){
            if(code.charAt(i) == '{'){
                line = code.substring(0, i).trim();
                count = 1;
                from = i;
                while(count > 0){
                    i++;
                    if(code.charAt(i) == '{') count++;
                    else if(code.charAt(i) == '}') count--;
                }
                data.add(new Pair<Meta, Object>(new Meta(line, this.getRawCodeType(line)), this.rec_parse(code.substring(from + 1, i).trim())));
                code = code.substring(i + 1);
                i = -1;
            }
            else if(code.charAt(i) == ';'){
                line = code.substring(0, i).trim();
                data.add(new Pair<Meta, Object>(new Meta(line, this.getRawCodeType(line)), null));
                code = code.substring(i + 1);
                i = -1;
            }
        }
        return CodePiece.getCodePiece(data);
    }
}