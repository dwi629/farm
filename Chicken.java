package farm;

public class Chicken extends FarmObject {
    public Chicken(int id, String name, String type) {
        super(id, name, type);
    }

    @Override
    public String care() {
        return getName() +"【小鸡】喂食、喂水、清理鸡舍";
    }
    @Override
    public String toString() {
        return "编号：" + id + "\n名称：" + name + "\n具体类：Chicken";
    }
}