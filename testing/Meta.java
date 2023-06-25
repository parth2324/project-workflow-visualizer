package testing;
/*
 * store code meta information.
 * 
 * For shorthand cases like:
 * if(i > 1)while(i < 10)if(i > 5)break;
 * 
 * We simply return the group together without
 * processing in this version.
 */
public class Meta{
    public static final int LOOP = 1 << 0,
                     CONDITIONAL = 1 << 1,
                           ERROR = 1 << 2,
                         CONTROL = 1 << 3,
                       STRUCTURE = 1 << 4,  // add new stuff just below this for code consistency.
                       STATEMENT = 1 << 5;
    
    public String data;
    public String[] used_classes;
    public int type;
    protected boolean calibrated;

    public Meta(String data, int type){
        this.data = data;
        this.type = type;
        this.used_classes = null;
        this.calibrated = false;
    }
    
    public boolean isCalibrated(){
        return this.calibrated;
    }

    public String toString(){
        String prt = "";
        if((this.type & STATEMENT) > 0){
            prt += "Statement, ";
        }
        else if((this.type & CONDITIONAL) > 0){
            prt += "Conditional, ";
        }
        else if((this.type & LOOP) > 0){
            prt += "Loop, ";
        }
        else if((this.type & CONTROL) > 0){
            prt += "Control, ";
        }
        else if((this.type & STRUCTURE) > 0){
            prt += "Structure, ";
        }
        if((this.type & ERROR) > 0){
            prt += "Error, ";
        }
        prt = "-> " + this.data + "\n:: " + prt.substring(0, prt.length() - 2) + "\n";
        return prt;
    }
}