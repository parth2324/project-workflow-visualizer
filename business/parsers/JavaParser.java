package business.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

import entities.*;

public class JavaParser extends Parser{
    public static final String TYPE = "java", EXTENSION = "java";
    private static final String[][] keywords = {{"do", "while", "for"},
                                          {"if", "else"},
                                          {"try", "catch", "finally"},
                                          {"return", "continue", "break", "assert", "throw"},
                                          {"class", "enum"}};

    public JavaParser(){
        super(JavaParser.TYPE, JavaParser.EXTENSION, "<str", ">", "<doc", ">", keywords);
    }

    public Pattern getClassPattern(){
        return Pattern.compile("([^a-zA-Z0-9.]|^)(class|interface)[\s\t\n]([^{]*)");
    }

    public Pattern getPackagePattern(){
        return Pattern.compile("([^a-zA-Z0-9.]|^)package[\s\t\n]([^;]*)");
    }

    public void addClasses(List<String> classes){
        if(this.isLoadedClean()){
            String package_name = "";
            Matcher matcher = this.getPackagePattern().matcher(this.cleaned_code.first);
            if(matcher.find()){
                package_name = matcher.group(2).trim() + Parser.PACKAGE_CON;
            }
            matcher = this.getClassPattern().matcher(this.cleaned_code.first);
            while(matcher.find()){
                classes.add(package_name + matcher.group(3).trim().replaceAll("extends", Parser.EXTENDS_REP).replaceAll("implements", Parser.IMPLEMENTS_REP));
            }
        }
    }

    public Pair<String, Pair<String[], String[]>> clean(){
        if(!this.isLoaded()) return null;
        String cleaned = this.raw_code;
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

    public Pair<String[], Pair<String[], String[]>> parse(){
        if(!this.isLoadedClean()) return null;
        return new Pair<String[], Pair<String[], String[]>>(this.segmentize(this.cleaned_code.first), this.cleaned_code.second);
    }

    private String[] segmentize(String code){
        /*
         * PRE : code clean and not null.
         */
        int start = 0, end = -1;
        ArrayList<String> segments = new ArrayList<String>();
        while(start < code.length()){
            end = JavaParser.nextEnd(code, start);
            segments.add(code.substring(start, end));
            start = end;
        }
        if(segments.size() > 0){
            String[] segments_array = new String[segments.size()];
            for(int i = 0; i < segments.size(); i++){
                segments_array[i] = segments.get(i);
            }
            return segments_array;
        }
        return null;
    }

    private static int nextEnd(String str, int index){
        while(index < str.length()){
            if(str.charAt(index) == '('){
                while(str.charAt(index) != ')') index++;
            }
            else if(str.charAt(index) == ';' || str.charAt(index) == '{'){
                return index + 1;
            }
            else index++;
        }
        return str.length();
    }
}