package testing;

import java.util.regex.*;
import java.io.*;

import business.parsers.JavaParser;
import entities.TargetFileSystem;

public class regex_test {
    public static void main(String[] args) {
        String block_regex = "[\n\s]*\\{(\n*.*\n*;)*[\n\s]*\\}[\n\s]*";
        String for_loop_regex = "^[\n\s]*for[\n\s]*\\((\n*.)*[\n\s]*;(\n*.)*[\n\s]*;(\n*.)*[\n\s]*\\)" + block_regex;
        String while_loop_regex = "^[\n\s]*while[\n\s]*\\((\n*.)*[\n\s]*\\)" + block_regex;
        String do_while_loop_regex = "^[\n\s]*do" + block_regex + "while[\n\s]*\\((\n*.)*[\n\s]*\\)[\n\s]*;[\n\s]*";
        String if_regex = "^[\n\s]*if[\n\s]*\\((\n*.)*[\n\s]*\\)" + block_regex;
        String else_if_regex = "^[\n\s]*else[\n\s]*if[\n\s]*\\((\n*.)*[\n\s]*\\)" + block_regex;
        String else_regex = "^[\n\s]*else[\n\s]*\\((\n*.)*[\n\s]*\\)" + block_regex;
        String switch_regex = "";
        String try_catch_regex = "";
        String finally_regex = "";
        String function_regex = "";
        String class_regex = "";
        String interface_regex = "";
        String assignment_regex = "";
        String annotation_regex = "";
        // test below
        String query = "for(int i = 0; i < 10; i++)\n{\n\tbreak;\n}\n";

        int ind = 0, new_ind = -1;
        String tgt = "";
        while(ind < query.length()){
            new_ind = nextEnd(query, ind);
            tgt = query.substring(ind, new_ind);
            System.out.println("|" + tgt + "|");
            ind = new_ind;
        }

        Pattern p = Pattern.compile(for_loop_regex);
        Matcher m = p.matcher(query);
        System.out.println(m.matches());

        TargetFileSystem tfs = new TargetFileSystem(new JavaParser());
        tfs.init(new File("C:\\Users\\parth\\OneDrive\\Desktop\\code\\Java"));
        //System.out.println(tfs.prefixedToString("", '\t'));
    }

    public static Pattern getVariablePattern(String var){
        return Pattern.compile("[(,\n\s\t=;]" + var + "[),.\n\s\t=;]");
    }

    public static int nextEnd(String str, int index){
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

/*
 * 1) process code files into code blocks
 * 2) run variable recognition and interaction mapping system
 * 3) query based display to user 
 */