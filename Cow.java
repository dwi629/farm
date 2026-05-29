package farm;

public class Cow extends FarmObject {
    public Cow(int id, String name, String type) {
        super(id, name, type);
    }

    @Override
    public String care() {
        return getName() +"【奶牛】喂食、清理牛棚、挤奶";
    }

    @Override
    public String toString() {
        return "编号：" + id + "\n名称：" + name + "\n具体类：Cow";
    }
}