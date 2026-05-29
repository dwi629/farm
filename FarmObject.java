package farm;

import java.io.Serializable;

public abstract class FarmObject implements Serializable {
    // 基本属性
    public int id;
    public String name;
    public String type;
    public static final long serialVersionUID = 1L;

    public FarmObject(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public abstract String care();

    @Override
    public String toString() {
        return "编号：" + id + "\n名称：" + name + "\n类型：" + type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}