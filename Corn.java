package farm;

public class Corn extends FarmObject {
    public Corn(int id, String name, String type) {
        super(id, name, type);
    }

    @Override
    public String care() {
        return getName() +"【玉米】浇水、松土、除虫";
    }
    @Override
    public String toString() {
        return "编号：" + id + "\n名称：" + name + "\n具体类：Corn";
    }
}