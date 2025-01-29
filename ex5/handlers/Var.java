package ex5.handlers;

public class Var extends Component{

    private boolean init = false;
    private boolean isFinal;
    private String type;

    public Var(String name, String type, boolean init, boolean isFinal) {
        super(name);
        this.init = init;
        this.type = type;
        this.isFinal = isFinal;
    }

    public boolean tryToInitialize(String value) {
        if (isFinal) {
            return false;
        } else if () { //todo is type compatible
            return false;
        }
        return true;
    }
}
