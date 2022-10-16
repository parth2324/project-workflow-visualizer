import java.io.File;

public class Visualizer {
    public static void main(String[] args) throws Exception{
        CLIPresenter cliP = new CLIPresenter();
        cliP.init(new File("C:\\Users\\parth\\OneDrive\\Desktop\\code\\Java\\Project Workflow Visualizer\\Meta.java"), false, new JavaParser());
        cliP.printData();
        cliP.start_visualisation();
    }
}