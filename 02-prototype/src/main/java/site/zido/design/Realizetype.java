package site.zido.design;

public class Realizetype implements Cloneable {
    private int flag;

    public Realizetype() {
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    public Object clone() throws CloneNotSupportedException {
        return (Realizetype) super.clone();
    }
}