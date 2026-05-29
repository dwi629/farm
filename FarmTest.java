package farm;

import java.util.Scanner;

public class FarmTest {

    public static void main(String[] args) {
        Farm farm = new Farm(Farm.MAX);
        farm.loadFromFile();

        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("\n===== 开心农场管理系统 Plus（方案B） =====");
            System.out.println("1. 添加农场对象");
            System.out.println("2. 按名称查找对象");
            System.out.println("3. 照料指定位置对象");
            System.out.println("4. 删除指定位置对象");
            System.out.println("5. 显示所有对象");
            System.out.println("6. 保存所有对象");
            System.out.println("7. 按类型筛选对象"); // 方案B新增
            System.out.println("8. 批量照料指定行对象"); // 方案B新增
            System.out.println("0. 退出系统");
            System.out.print("请输入功能编号：");
            int choice = sc.nextInt();
            sc.nextLine();
            FarmObject obj = null;

            switch (choice){
                case 1:
                    System.out.println("\n选择创建对象类型：1-小麦 2-玉米 3-鸡 4-牛");
                    int choice2 = sc.nextInt();
                    sc.nextLine();
                    System.out.println("请输入编号：");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.println("请输入名称：");
                    String name = sc.nextLine();
                    System.out.println("请输入行号：");
                    int row = sc.nextInt();
                    switch (choice2){
                        case 1: obj=new Wheat(id,name,"小麦(农作物)");break;
                        case 2: obj=new Corn(id,name,"玉米(农作物)");break;
                        case 3: obj=new Chicken(id,name,"鸡(动物)");break;
                        case 4: obj=new Cow(id,name,"牛(动物)");break;
                    }
                    farm.add(row,obj);break;
                case 2:
                    System.out.println("请输入名称：");
                    String queryName = sc.nextLine();
                    farm.query(queryName);break;
                case 3:
                    System.out.println("请输入行号：");
                    int row1 = sc.nextInt();
                    sc.nextLine();
                    System.out.println("请输入编号：");
                    int id1 = sc.nextInt();
                    sc.nextLine();
                    farm.care(row1,id1);break;
                case 4:
                    System.out.println("请输入行号：");
                    int row2 = sc.nextInt();
                    sc.nextLine();
                    System.out.println("请输入编号：");
                    int id2 = sc.nextInt();
                    sc.nextLine();
                    farm.del(row2,id2);break;
                case 5:
                    farm.showAll();break;
                case 6:
                    farm.saveToFile();
                    break; // 修复原始代码缺失的break
                // ========== 方案B 新增逻辑 ==========
                case 7:
                    System.out.println("请输入要筛选的类型关键词（如：农作物/动物）：");
                    String typeKeyword = sc.nextLine();
                    farm.queryByType(typeKeyword);
                    break;
                case 8:
                    System.out.println("请输入要批量照料的行号：");
                    int batchRow = sc.nextInt();
                    sc.nextLine();
                    farm.batchCare(batchRow);
                    break;
                case 0:
                    System.out.println("感谢使用！");
                    sc.close();
                    return;
                default:
                    System.out.println("输入的功能编号无效，请重新输入！");
            }
        }
    }
}