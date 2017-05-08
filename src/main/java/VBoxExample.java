import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.*;

public class VBoxExample extends Application {
    Stage stg;
    String lastDir;
    // CheckBox chrome;

    TextArea textArea, textArea2, textAreaComment;
    public TextField user, thrCnt, sleep;
    public PasswordField pw;
    public selenyumThread th;
    public Button btn1, btn2;
    public ProgressBar pb = new ProgressBar(0);
    public ProgressIndicator pi = new ProgressIndicator(0);
    int ThreadCount = 2;
    double done;
    int sleepSec = 0;
    selenyumThread threadArr[];

    @Override
    public void start(Stage stage) {
// Buttons
        stg = stage;
        btn1 = new Button("Start Commenting");
        btn2 = new Button("Stop Commenting");
        btn2.setDisable(true);
        btn2.setOnAction(new stopComment());
        btn1.setOnAction(new startComment());
        HBox buttonHb1 = new HBox(10);
        // chrome = new CheckBox("Use Chrome Browser (Visual but slower)");

        //chrome.setSelected(true);
        thrCnt = new TextField("1");
        sleep = new TextField("0");
        thrCnt.setVisible(false);
        thrCnt.setPrefSize(35, 9);
        // buttonHb1.setAlignment(Pos.CENTER);
        sleep.setPrefSize(44, 9);

        Label secLabel = new Label("  Seconds to wait between comments (Instagram might ban you for often commenting) :");
        buttonHb1.getChildren().addAll(secLabel, sleep, btn1, btn2, pb, pi);
        buttonHb1.setMargin(secLabel, new Insets(12, 0, 9, 0));
        buttonHb1.setMargin(sleep, new Insets(9, 11, 9, 1));
        buttonHb1.setMargin(btn1, new Insets(9, 10, 9, 10));
        buttonHb1.setMargin(thrCnt, new Insets(9, 10, 9, 10));
        buttonHb1.setMargin(btn2, new Insets(9, 10, 9, 10));
        buttonHb1.setMargin(pb, new Insets(9, 10, 9, 10));
        HBox texts = new HBox(10);

        stage.setMaximized(true);

        textArea = new TextArea();
        textArea2 = new TextArea();

        texts.getChildren().addAll(textArea, textArea2);
        //Instantiating the VBox class
        VBox vBox = new VBox();
        textArea2.setEditable(false);
        textArea2.appendText("Logs ");
        Label title = new Label("  Enter links of Instagram accounts to below area one link per line:");

        try (BufferedReader br = new BufferedReader(new FileReader("LastLinks.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                textArea.appendText(line + "\n");

            }
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
        if (textArea.getText().trim().isEmpty())
            textArea.setText("https://www.instagram.com/natgeo");
        //textArea.appendText("https://www.instagram.com/natgeo/");

        //Setting the space between the nodes of a VBox pane
        //vBox.setSpacing(10);

        HBox userBox = new HBox();
        user = new TextField();
        pw = new PasswordField();
        userBox.getChildren().addAll(new Label("Your Instagram Username: "), user, new Label("   Your Instagram Password: "), pw, new Label("   (Account info to login and comment with) "));
        //retrieving the observable list of the VBox
        ObservableList list = vBox.getChildren();
        textAreaComment = new TextArea();
        //Adding all the nodes to the observable list
        HBox commentHbox = new HBox();
        commentHbox.getChildren().addAll(new Label("  Enter Your Comment :  "), textAreaComment);
        list.addAll(userBox, commentHbox, title, texts, buttonHb1);
        //Setting the margin to the nodes
        vBox.setMargin(userBox, new Insets(10, 10, 3, 10));
        vBox.setMargin(texts, new Insets(10, 10, 3, 10));
        vBox.setMargin(title, new Insets(9, 3, 0, 3));
        vBox.setMargin(commentHbox, new Insets(6, 3, 5, 3));
        pi.setAccessibleText("Finished");

        //Creating a scene object
        Scene scene = new Scene(vBox);
        textArea.setPrefSize(9444, 9444);
        textArea2.setPrefSize(9444, 9444);
        //Setting title to the Stage
        stage.setTitle("InstaComment by ulvital@gmail.com");
        try (BufferedReader br = new BufferedReader(new FileReader("LastUsername.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                user.setText(line);
                break;
            }
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
        if (!user.getText().isEmpty()) pw.requestFocus();
        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();

    }

    public String[] urls;

    @Override
    public void stop() {
        try {
            Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe ");
        } catch (IOException e) {
            System.out.println("kill driver exception");
        }
        setLastDir(textArea.getText());
        saveLastUser();
    }

    public synchronized void done() {
        done++;
        pb.setProgress(done / urls.length);
        pi.setProgress(done / urls.length);

        if (done == urls.length) {
            btn1.setDisable(false);
            btn2.setDisable(true);
            VBoxExample.this.textArea.setEditable(true);

        }
    }

    public class startComment implements EventHandler<ActionEvent> {

        public String comm;
        int order = 0;

        public startComment() {

        }

        public synchronized Pair<String, Integer> getUrl() {
            if (order >= urls.length) {


                return new Pair<>("-1", -1);
            }
            if (order != 0) {
                try {
                    Thread.sleep(sleepSec * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (urls[order].trim().isEmpty()) {
                order++;
                done();
            }

            return new Pair<>(urls[order++], order);
        }

        @Override
        public void handle(ActionEvent e) {
            if (textAreaComment.getText().isEmpty()) {
                textArea2.setText("ERROR: Please write your comment into 'Comment' box!");
                return;
            }
            if (user.getText().isEmpty()) {
                textArea2.setText("ERROR: Please enter username!");
                return;
            }
            if (pw.getText().isEmpty()) {
                textArea2.setText("ERROR: Please enter password!");
                return;
            }
            try {
                sleepSec = Integer.parseInt(VBoxExample.this.sleep.getText());
            } catch (Exception ee) {
                sleepSec = 0;
            }
            btn1.setDisable(true);
            btn2.setDisable(false);
            done = 0;
            order = 0;
            try {
                ThreadCount = Integer.parseInt(VBoxExample.this.thrCnt.getText());
            } catch (Exception ee) {
                ThreadCount = 1;
            }
            threadArr = new selenyumThread[ThreadCount];
            this.comm = VBoxExample.this.textAreaComment.getText();
            VBoxExample.this.textArea.setEditable(false);
            urls = VBoxExample.this.textArea.getText().trim().split("\\n");
            VBoxExample.this.textArea2.setText("");
            for (int i = 0; i < ThreadCount; i++) {
                (threadArr[i] = new selenyumThread(this, VBoxExample.this)).start();
            }
            pb.setProgress(0);
            pi.setProgress(0);


        }
    }

    private class stopComment implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            btn2.setDisable(true);
            for (int i = 0; i < ThreadCount; i++) {
                (threadArr[i]).stop();
            }
            btn1.setDisable(false);

            textArea.setEditable(true);
        }
    }


    public void setLastDir(String lastDir) {
        this.lastDir = lastDir;
        FileWriter fw = null;
        try {
            fw = new FileWriter("LastLinks.txt");
            fw.write(lastDir);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveLastUser() {
        FileWriter fw;
        try {
            fw = new FileWriter("LastUsername.txt");
            fw.write(user.getText());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws Exception {

        launch(args);


    }
}