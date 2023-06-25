package interfaces;

import business.Presenter;
import business.Presenter.Message;

public abstract class UI {
    protected String type;
    protected Presenter p;

    public UI(String type){
        this.type = type;
        this.p = null;
    }

    public String getType(){
        return this.type;
    }

    public void setPresenter(Presenter presenter){
        this.p = presenter;
    }

    public abstract void showMessage(String msg, Message type);
    public abstract String requestInput();
    public abstract void run();
}
