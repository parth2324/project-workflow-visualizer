import business.Presenter;
import interfaces.UI;
import interfaces.UIs.CLI;

public class Visualizer {
    public static void main(String[] args) throws Exception{
        UI ui = new CLI();
        Presenter p = new Presenter(ui);
        p.init(null, "java");
        ui.setPresenter(p);
        ui.run();
    }
}
