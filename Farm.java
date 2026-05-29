package farm;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Farm {
    public final static int MAX = 10;
    private static final String FILE_NAME = "farm.dat";
    public List<List<FarmObject>>farm;
    public List<List<FarmObject>> getFarm() {
        return farm;
    }
    int total = 0;

    public void saveToFile(){
        try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(FILE_NAME));
            ObjectOutputStream oos = new ObjectOutputStream(bos)){
            oos.writeObject(farm);
            oos.writeInt(total);
            System.out.println("保存成功！");
        }catch(Exception e){
            System.out.println("保存失败！" + e.getMessage());
        }
    }

    public boolean loadFromFile(){

        File file = new File(FILE_NAME);
        if(!file.exists()){
            System.out.println("不存在保存文件");
            return false;
        }
        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(FILE_NAME));
            ObjectInputStream ois = new ObjectInputStream(bis)){
            farm = (List<List<FarmObject>>)ois.readObject();
            total = ois.readInt();
            System.out.println("加载成功！");
            return true;
        } catch (Exception e) {
            System.out.println("加载失败！" + e.getMessage());
        }
        return false;
    }

    public Farm(int row){
        farm = new ArrayList<>();
        for(int i=0;i<row;i++){
            farm.add(new ArrayList<>());
        }
    }

    public boolean add(int row, FarmObject obj) {
        // 简单防重复ID
        for (var list : farm) {
            for (FarmObject o : list) {
                if (o.getId() == obj.getId()) {
                    return false;
                }
            }
        }
        farm.get(row).add(obj);
        total++;
        return true;
    }


    public String showAll() {
        StringBuilder sb = new StringBuilder();
        sb.append("所有对象：\n");
        for(int i=0;i<farm.size();i++) {
            if(farm.get(i).isEmpty()) continue;
            sb.append("第").append(i+1).append("行：\n");
            for(FarmObject obj : farm.get(i)) {
                sb.append(obj.getId())
                        .append(" ")
                        .append(obj.getName())
                        .append(" ")
                        .append(obj.getType())
                        .append("\n");
            }
        }
        sb.append("共有农场对象")
                .append(total)
                .append("个\n");
        return sb.toString();
    }

    public String query(String name){
        if(farm.isEmpty()){
            System.out.println("无对象！");return null;
        }
        for(int i=0;i<farm.size();i++){
            for(int j=0;j<farm.get(i).size();j++){
                FarmObject o = farm.get(i).get(j);
                if(o.getName().equals(name)){
                    return o.toString();
                }
            }
        }
        System.out.println("未找到名称为【" + name + "】的对象！");
        return null;
    }

    public String care(int row, int id){
        if(row<0||row>=farm.size()){
            System.out.println("行数错误！");return null;
        }
        for(int j=0;j<farm.get(row).size();j++){
            FarmObject o = farm.get(row).get(j);
            if(o.getId()==id){
                return o.care();
            }
        }
        System.out.println("编号错误！");
        return null;
    }

    public String del(int row, int id){
        if(row<0||row>=farm.size()){
            return "删除失败！";
        }
        int rowTotal = farm.get(row).size();
        for(int j=0;j<rowTotal;j++){
            FarmObject o = farm.get(row).get(j);
            if(o.getId()==id){
                String t = o.toString() + "\n出售/收获成功！\n";
                farm.get(row).remove(j);
                total--;
                return t;
            }
        }
        return "删除失败！";
    }

    // ========== 方案B 新增方法 ==========
    /**
     * 按类型筛选农场对象（如“农作物”“动物”）
     */
    public void queryByType(String typeKeyword) {
        if (farm.isEmpty()) {
            System.out.println("无对象！");
            return;
        }
        boolean found = false;
        System.out.println("类型包含【" + typeKeyword + "】的对象：");
        for (int i = 0; i < farm.size(); i++) {
            for (int j = 0; j < farm.get(i).size(); j++) {
                FarmObject o = farm.get(i).get(j);
                if (o.getType().contains(typeKeyword)) {
                    System.out.println("行 " + i + "，列 " + j + "：" + o.getId() + " " + o.getName() + " " + o.getType());
                    found = true;
                }
            }
        }
        if (!found) {
            System.out.println("未找到类型包含【" + typeKeyword + "】的对象！");
        }
    }

    /**
     * 批量照料指定行的所有对象
     */
    public void batchCare(int row) {
        if (row < 0 || row >= farm.size()) {
            System.out.println("行数错误！");
            return;
        }
        List<FarmObject> rowObjects = farm.get(row);
        if (rowObjects.isEmpty()) {
            System.out.println("该行无对象可照料！");
            return;
        }
        System.out.println("开始批量照料行 " + row + " 的所有对象：");
        for (FarmObject o : rowObjects) {
            o.care();
        }
        System.out.println("行 " + row + " 所有对象照料完成！");
    }

    public void clear() {
        for(List<FarmObject> row : farm){
            row.clear();
        }
        total = 0;
    }

    public FarmObject findById(int row, int id) {
        if (row < 0 || row >= farm.size()) {
            System.out.println("行数错误！");
            return null;
        }
        for (int j = 0; j < farm.get(row).size(); j++) {
            FarmObject o = farm.get(row).get(j);
            if (o.getId() == id) {
                return o;
            }
        }
        System.out.println("编号错误！");
        return null;
    }
}