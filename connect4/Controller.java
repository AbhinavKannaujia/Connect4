package com.abhi.connect4;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable{

    private static final int col = 7;
    private static final int row = 6;
    private static final int cirdia = 80;
    private static final String disccol1 = "#24303E";
    private static final String disccol2 = "#4CAA88";

    private static String play1;
    private static String play2;


    private boolean isplayone = true;

    private boolean isallowedtoinsert = true;

    private Disc[][] insertedDiscArr = new Disc[row][col];

    @FXML
    public GridPane rootgridpane;

    @FXML
    public Pane inserteddiscpane;

    @FXML
    public TextField Enter1;

    @FXML
    public TextField Enter2;

    @FXML
    public Button Enterbtn;

    @FXML
    public Label p1;

    public void createplaygroumnd() {
        Shape recwithholes = creategamestructuralgrid();
        rootgridpane.add(recwithholes, 0, 1);

        List<Rectangle> rectangleList = createClickableColoumns();

        for (Rectangle rectangle : rectangleList) rootgridpane.add(rectangle, 0, 1);
    }

    private Shape creategamestructuralgrid(){
        Shape recwithholes = new Rectangle((col+1)*cirdia,(row+1)*cirdia);

        for(int i = 0;i < row;i++){
            for(int j = 0;j < col;j++){
                Circle circle = new Circle();
                circle.setRadius(cirdia/2);
                circle.setCenterX(cirdia/2);
                circle.setCenterY(cirdia/2);
                circle.setSmooth(true);

                circle.setTranslateX(j*(cirdia+5)+ cirdia/4);
                circle.setTranslateY(i*(cirdia+5)+ cirdia/4);
                recwithholes = Shape.subtract(recwithholes,circle);
            }
        }

        recwithholes.setFill(Color.WHITE);

        return recwithholes;
    }

    private List<Rectangle> createClickableColoumns(){

        List<Rectangle> rectanglelist = new ArrayList<>();

        for(int i = 0; i < col; i++) {
            Rectangle rectangle = new Rectangle(cirdia, (row + 1) * cirdia);
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setTranslateX(i*(cirdia+5)+ cirdia/4);

            rectangle.setOnMouseEntered(event -> rectangle.setFill(Color.valueOf("#bebebe26")));
            rectangle.setOnMouseExited(event -> rectangle.setFill(Color.TRANSPARENT));

            final int column = i;
            rectangle.setOnMouseClicked(event -> {
                if(isallowedtoinsert){
                    isallowedtoinsert = false;
                    insertDisc(new Disc(isplayone), column);
                    }
        });


            rectanglelist.add(rectangle);
        }
        return rectanglelist;

    }

    private void insertDisc(Disc disc,int column){

        int rows = row -1;
        while(rows >= 0){
            if(getdiscifpresent(rows,column) == null){
                break;
            }
            else{
                rows--;
            }
        }
        if(rows <0){
            return;
        }
        insertedDiscArr[rows][column] = disc;
        inserteddiscpane.getChildren().add(disc);
        disc.setTranslateX(column*(cirdia+5)+ cirdia/4);
        int currrow = rows;
        TranslateTransition transtranslation = new TranslateTransition(Duration.seconds(0.5),disc);
        transtranslation.setToY(rows*(cirdia+5)+ cirdia/4);

        transtranslation.setOnFinished(event -> {

            isallowedtoinsert = true;
            if(gameended(currrow,column)){
                gameover();
            }
            isplayone = !isplayone;

            p1.setText(isplayone? play1: play2);
        });
        transtranslation.play();
    }

    private boolean gameended(int row, int column) {

        List<Point2D> verticalpoints = IntStream.rangeClosed(row -3,row+3)
                .mapToObj(r -> new Point2D(r,column)).collect(Collectors.toList());

        List<Point2D> horipoints = IntStream.rangeClosed(column -3,column+3)
                .mapToObj(c -> new Point2D(row,c)).collect(Collectors.toList());

        Point2D startpoints = new Point2D(row - 3,column +3);
        List<Point2D> dia1 = IntStream.rangeClosed(0,6)
                .mapToObj(i -> startpoints.add(i,-i)).collect(Collectors.toList());

        Point2D startpoints2 = new Point2D(row - 3,column -3);
        List<Point2D> dia2 = IntStream.rangeClosed(0,6)
                .mapToObj(i -> startpoints2.add(i,i)).collect(Collectors.toList());

        boolean isEnd = checkcomb(verticalpoints) || checkcomb(horipoints)
                || checkcomb(dia1)
                || checkcomb(dia2);
        return isEnd;
    }

    private boolean checkcomb(List<Point2D> points) {

        int chain =0;
        for (Point2D point: points){
            int rowindexForarr = (int) point.getX();
            int colindexForarr = (int) point.getY();

            Disc disc = getdiscifpresent(rowindexForarr,colindexForarr);

            if(disc != null && disc.isplayermove == isplayone){
                chain++;
                if(chain == 4){
                    return true;
                }
//                else{
//                    chain = 0;
//                }
            }
        }
        return false;
    }

    private Disc getdiscifpresent(int rowz,int column){

        if(rowz >= row || rowz < 0 || column >= col || column < 0){
            return null;
        }
        return insertedDiscArr[rowz][column];
    }
    private void gameover(){
        String winner = isplayone? play1:play2;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Connect4");
        alert.setHeaderText("The Winner is: " + winner);

        ButtonType yesbtn = new ButtonType(("YES"));
        ButtonType nobtn = new ButtonType("No, Exit");

        alert.getButtonTypes().setAll(yesbtn,nobtn);

        Platform.runLater(() -> {
            Optional<ButtonType> btnclicked = alert.showAndWait();

            if(btnclicked.isPresent() && btnclicked.get() == yesbtn){
                resetgame();
            }
            else {
                Platform.exit();
                System.exit(0);
            }
        });

    }

    public void resetgame() {
        inserteddiscpane.getChildren().clear();
        for(int i = 0;i < insertedDiscArr.length;i++){
            for (int j = 0;j < insertedDiscArr[i].length;j++){
                insertedDiscArr[i][j] = null;
            }
        }
        isplayone = true;
        p1.setText(play1);

        createplaygroumnd();
    }

    private static class Disc extends Circle{

        private final boolean isplayermove;
        public Disc(boolean isplayermove){
            this.isplayermove = isplayermove;
            setRadius(cirdia/2);
            setCenterX(cirdia/2);
            setCenterY(cirdia/2);
            setFill(isplayermove? Color.valueOf(disccol1): Color.valueOf(disccol2));
        }

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Enterbtn.setOnMouseClicked(event -> {
            play1 = Enter1.getText();
            play2 = Enter2.getText();

            if(isplayone){
                p1.setText(play1);
            }
            else{
                p1.setText(play2);
            }
        });
    }
}
