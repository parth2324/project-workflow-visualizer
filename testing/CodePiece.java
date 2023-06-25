package testing;
import java.util.ArrayList;

import entities.Pair;

public class CodePiece{
    public static CodePiece getCodePiece(ArrayList<Pair<Meta, Object>> collec){
        Meta[] meta = new Meta[collec.size()];
        Object[] data = new Object[collec.size()];
        for(int i = 0; i < collec.size(); i++){
            meta[i] = collec.get(i).first;
            data[i] = collec.get(i).second;
        }
        return new CodePiece(meta, data);
    }

    public Meta[] meta;
    public Object[] data;

    public CodePiece(Meta[] meta, Object[] data){
        /*
         * PRE : meta.length = data.length
         */
        this.meta = meta;
        this.data = data;
    }

    public String toString(){
        String prt = "::: code piece start\n";
        for(int i = 0; i < meta.length; i++){
            prt += meta[i].toString() + "\n";
            if(data[i] != null){
                prt += ((CodePiece)data[i]).toString() + "\n";
            }
        }
        prt += "::: code piece end\n";
        return prt;
    }
}