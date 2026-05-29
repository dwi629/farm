package farm;

public class Wheat extends FarmObject {
    public Wheat(int id, String name, String type) {
        super(id, name, type);
    }

    @Override
    public String care() {
        return getName() +"【小麦】浇水、施肥、除草";
    }
    @Override
    public String toString() {
        return "编号：" + id + "\n名称：" + name + "\n具体类：Wheat";
    }
}