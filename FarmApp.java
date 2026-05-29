package farm;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class FarmApp extends Application {

    private Farm farm;

    private VBox farmRowsBox = new VBox(15);
    private TextArea resultArea = new TextArea();

    private TextField initRowField;
    private ComboBox<String> typeBox;
    private TextField idField;
    private TextField nameField;
    private TextField addRowField;
    private TextField queryField;
    private TextField operateRowField;
    private TextField operateIdField;

    private volatile boolean autoRunning = true;
    private Label threadStatusLabel;

    @Override
    public void start(Stage stage) {
        farm = new Farm(10);
        farm.loadFromFile();
        refreshFarmView();

        BorderPane root = new BorderPane();
        root.setTop(createHeader());
        root.setLeft(createLeftPanel());
        root.setCenter(createCenterPanel());
        root.setRight(createRightPanel());

        Scene scene = new Scene(root, 1500, 900);
        stage.setTitle("开心农场图形化管理系统");
        stage.setScene(scene);
        stage.show();

        refreshFarmView();
        startAutoGrowThread();
    }

    /**
     * 顶部标题
     */
    private Label createHeader() {
        Label title = new Label("🌾 开心农场图形化管理系统");
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);
        title.setStyle("""
            -fx-font-size:28;
            -fx-font-weight:bold;
            -fx-text-fill:white;
            -fx-padding:15;
            -fx-background-color: linear-gradient(to right,#75a043,#215957);
        """);
        return title;
    }

    /**
     * 左侧功能区
     */
    private VBox createLeftPanel() {
        VBox left = new VBox(10);
        left.setPadding(new Insets(10));
        left.setPrefWidth(350);

        // -----------------------------------
        // 初始化农场
        // -----------------------------------
        TitledPane initPane = new TitledPane();
        initPane.setText("初始化农场");
        VBox initBox = new VBox(8);

        Label rowLabel = new Label("农场行数：");
        initRowField = new TextField("10");
        Button initBtn = new Button("初始化");
        initBtn.setMaxWidth(Double.MAX_VALUE);

        initBtn.setOnAction(e -> {
            try {
                int row = Integer.parseInt(initRowField.getText());
                farm = new Farm(row);
                refreshFarmView();
                resultArea.appendText("农场初始化成功，共 " + row + " 行\n\n");
            } catch (Exception ex) {
                resultArea.appendText("初始化失败\n\n");
            }
        });

        initBox.getChildren().addAll(rowLabel, initRowField, initBtn);
        initPane.setContent(initBox);

        // -----------------------------------
        // 添加农场对象
        // -----------------------------------
        TitledPane addPane = new TitledPane();
        addPane.setText("添加农场对象");
        VBox addBox = new VBox(8);

        typeBox = new ComboBox<>();
        typeBox.getItems().addAll("小麦", "玉米", "鸡", "牛");
        typeBox.getSelectionModel().selectFirst();

        idField = new TextField();
        idField.setPromptText("对象编号");

        nameField = new TextField();
        nameField.setPromptText("对象名称");

        addRowField = new TextField();
        addRowField.setPromptText("添加到第几行");

        Button addBtn = new Button("添加对象");
        addBtn.setMaxWidth(Double.MAX_VALUE);

        addBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                int row = Integer.parseInt(addRowField.getText()) - 1;

                FarmObject obj = null;
                switch (typeBox.getValue()) {
                    case "小麦": obj = new Wheat(id, name, "农作物"); break;
                    case "玉米": obj = new Corn(id, name, "农作物"); break;
                    case "鸡": obj = new Chicken(id, name, "动物"); break;
                    case "牛": obj = new Cow(id, name, "动物"); break;
                }

                farm.add(row, obj);
                resultArea.appendText("添加成功！\n" + obj + "\n\n");
                refreshFarmView();
            } catch (Exception ex) {
                resultArea.appendText("添加失败！\n\n");
            }
        });

        addBox.getChildren().addAll(new Label("对象类型"), typeBox, idField, nameField, addRowField, addBtn);
        addPane.setContent(addBox);

        // -----------------------------------
        // 按名称查询
        // -----------------------------------
        TitledPane queryPane = new TitledPane();
        queryPane.setText("按名称查询");
        VBox queryBox = new VBox(8);

        queryField = new TextField();
        queryField.setPromptText("输入名称");
        Button queryBtn = new Button("查询");
        queryBtn.setMaxWidth(Double.MAX_VALUE);

        queryBtn.setOnAction(e -> {
            String result = farm.query(queryField.getText());
            if (result != null) {
                resultArea.appendText(result + "\n\n");
            }
        });

        queryBox.getChildren().addAll(queryField, queryBtn);
        queryPane.setContent(queryBox);

        // -----------------------------------
        // 按位置操作
        // -----------------------------------
        TitledPane operatePane = new TitledPane();
        operatePane.setText("按位置操作");
        VBox operateBox = new VBox(8);

        operateRowField = new TextField();
        operateRowField.setPromptText("行号");

        operateIdField = new TextField();
        operateIdField.setPromptText("编号");

        Button careBtn = new Button("照料对象");
        Button deleteBtn = new Button("删除对象");
        careBtn.setMaxWidth(Double.MAX_VALUE);
        deleteBtn.setMaxWidth(Double.MAX_VALUE);

        careBtn.setOnAction(e -> {
            try {
                int row = Integer.parseInt(operateRowField.getText()) - 1;
                int id = Integer.parseInt(operateIdField.getText());
                farm.care(row, id);
                resultArea.appendText("\n\n" + farm.care(row, id));
            } catch (Exception ex) {
                resultArea.appendText("照料失败\n\n");
            }
        });

        deleteBtn.setOnAction(e -> {
            try {
                int row = Integer.parseInt(operateRowField.getText()) - 1;
                int id = Integer.parseInt(operateIdField.getText());
                if (row >= 0 && row < farm.getFarm().size()) {
                    refreshFarmView();
                    refreshFarmView();
                    resultArea.appendText("\n\n" + farm.del(row, id));
                }
            } catch (Exception ex) {
                resultArea.appendText("删除失败！\n\n");
            }
        });

        operateBox.getChildren().addAll(operateRowField, operateIdField, careBtn, deleteBtn);
        operatePane.setContent(operateBox);

        // -----------------------------------
        // 其他功能
        // -----------------------------------
        TitledPane otherPane = new TitledPane();
        otherPane.setText("其他功能");
        GridPane otherGrid = new GridPane();
        otherGrid.setHgap(10);
        otherGrid.setVgap(10);

        Button autoOnBtn = new Button("开启自动照料");
        Button autoOffBtn = new Button("暂停自动照料");
        threadStatusLabel = new Label("自动照料：运行中");
        threadStatusLabel.setStyle("-fx-text-fill:green;");

        autoOnBtn.setOnAction(e -> {
            autoRunning = true;
            threadStatusLabel.setText("自动照料：运行中");
            threadStatusLabel.setStyle("-fx-text-fill:#58af58;");
            resultArea.appendText("已开启自动照料\n\n");
        });

        autoOffBtn.setOnAction(e -> {
            autoRunning = false;
            threadStatusLabel.setText("自动照料：已暂停");
            threadStatusLabel.setStyle("-fx-text-fill:#de7373;");
            resultArea.appendText("已暂停自动照料\n\n");
        });
        
        otherGrid.add(autoOnBtn, 0, 3);
        otherGrid.add(autoOffBtn, 1, 3);
        otherGrid.add(threadStatusLabel, 0, 4, 2, 1);

        Button showBtn = new Button("显示所有");
        Button clearBtn = new Button("清空农场");
        Button saveBtn = new Button("保存文件");
        Button loadBtn = new Button("读取文件");
        Button refreshBtn = new Button("刷新界面");
        Button exitBtn = new Button("保存退出");

        showBtn.setMaxWidth(Double.MAX_VALUE);
        clearBtn.setMaxWidth(Double.MAX_VALUE);
        saveBtn.setMaxWidth(Double.MAX_VALUE);
        loadBtn.setMaxWidth(Double.MAX_VALUE);
        refreshBtn.setMaxWidth(Double.MAX_VALUE);
        exitBtn.setMaxWidth(Double.MAX_VALUE);

        showBtn.setOnAction(e -> resultArea.setText(farm.showAll()));

        clearBtn.setOnAction(e -> {
            farm.clear();
            refreshFarmView();
            resultArea.appendText("农场已清空\n\n");
        });

        saveBtn.setOnAction(e -> {
            farm.saveToFile();
            resultArea.appendText("保存成功\n\n");
        });

        loadBtn.setOnAction(e -> {
            boolean ok = farm.loadFromFile();
            if (ok) {
                resultArea.appendText("读取成功\n\n");
                refreshFarmView();
            } else {
                resultArea.appendText("读取失败或文件不存在\n\n");
            }
        });

        refreshBtn.setOnAction(e -> refreshFarmView());
        exitBtn.setOnAction(e -> {
            farm.saveToFile();
            System.exit(0);
        });

        otherGrid.add(showBtn, 0, 0);
        otherGrid.add(clearBtn, 1, 0);
        otherGrid.add(saveBtn, 0, 1);
        otherGrid.add(loadBtn, 1, 1);
        otherGrid.add(refreshBtn, 0, 2);
        otherGrid.add(exitBtn, 1, 2);

        otherPane.setContent(otherGrid);
        left.getChildren().addAll(initPane, addPane, queryPane, operatePane, otherPane);

        return left;
    }

    /**
     * 中间农场区域
     */
    private ScrollPane createCenterPanel() {
        farmRowsBox.setPadding(new Insets(15));
        ScrollPane scrollPane = new ScrollPane(farmRowsBox);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    /**
     * 右侧结果区
     */
    private VBox createRightPanel() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setPrefWidth(360);

        Label title = new Label("执行结果");
        title.setStyle("""
            -fx-font-size:20;
            -fx-font-weight:bold;
        """);

        resultArea.setEditable(false);
        VBox.setVgrow(resultArea, Priority.ALWAYS);
        box.getChildren().addAll(title, resultArea);

        return box;
    }

    /**
     * 刷新农场界面
     */
    private void refreshFarmView() {
        farmRowsBox.getChildren().clear();

        for (int row = 0; row < farm.getFarm().size(); row++) {
            VBox rowBox = new VBox(10);
            Label rowLabel = new Label("第 " + (row + 1) + " 行");
            rowLabel.setStyle("""
                -fx-font-size:18;
                -fx-font-weight:bold;
                -fx-text-fill:#3db273;
            """);

            FlowPane flow = new FlowPane();
            flow.setHgap(15);
            flow.setVgap(15);

            for (FarmObject obj : farm.getFarm().get(row)) {
                Button card = new Button(getEmoji(obj) + " " + obj.getName());
                card.setPrefSize(150, 80);
                card.setStyle("""
                    -fx-font-size:18;
                    -fx-font-weight:bold;
                    -fx-background-radius:10;
                    -fx-background-color: linear-gradient(#cfd576,#a9d576);
                """);

                int finalRow = row;
                card.setOnAction(e -> {
                    operateRowField.setText(String.valueOf(finalRow + 1));
                    operateIdField.setText(String.valueOf(obj.getId()));
                    resultArea.setText(obj.toString());
                });

                flow.getChildren().add(card);
            }

            rowBox.getChildren().addAll(rowLabel, flow);
            rowBox.setPadding(new Insets(10));
            rowBox.setStyle("""
                -fx-border-color:#cccccc;
                -fx-border-radius:8;
                -fx-background-color:white;
                -fx-background-radius:8;
            """);

            farmRowsBox.getChildren().add(rowBox);
        }
    }

    private void startAutoGrowThread() {
        Thread autoThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                if (!autoRunning) {
                    try {
                        Thread.sleep(500);
                        continue;
                    } catch (InterruptedException e) {
                        break;
                    }
                }
                try {
                    Thread.sleep(8000);

                    for (int i = 0; i < farm.getFarm().size(); i++) {
                        for (FarmObject obj : farm.getFarm().get(i)) {
                            obj.care();
                        }
                    }

                    javafx.application.Platform.runLater(() -> {
                        resultArea.appendText("【自动照料】所有作物/动物已完成照料成长\n\n");
                    });

                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        autoThread.setDaemon(true);
        autoThread.setName("farm-auto-grow-thread");
        autoThread.start();
    }

    private String getEmoji(FarmObject obj) {
        if (obj instanceof Wheat) return "🌾";
        if (obj instanceof Corn) return "🌽";
        if (obj instanceof Chicken) return "🐔";
        if (obj instanceof Cow) return "🐄";
        return "📦";
    }

    public static void main(String[] args) {
        launch(args);
    }
}