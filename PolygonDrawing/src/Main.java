import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application
{

    public static ObservableList<Node> child;
    //
    private static final String title = "JellyBeanci";
    public static final int width = 800;
    public static final int height = 800;
    private static Color backcolor = Color.rgb(51, 51, 51);
    //
    static GraphicsContext gc;
    static double angle = 0;
    static double length = width * 0.25;
    static double hue = 180;

    static double[] xPoints;
    static double[] yPoints;

    static int vertexCount = 6;
    static boolean desc = false;
    static double r = width * 0.35;
    static boolean site = true;

    static Color color;
    static Point2D center = new Point2D(width / 2, height / 2);

    //
    private static Timeline update;

    @Override
    public void start(Stage stage) throws Exception
    {
        Pane root = new Pane();
        child = root.getChildren();
        Circle center = new Circle(width / 2, height / 2, 5, Color.RED);
        Canvas canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(2);
        canvas.setLayoutX(0);
        canvas.setLayoutY(0);

        child.addAll(canvas, center);
        //
        root.setOnKeyPressed(e -> {
            switch (e.getCode())
            {
                case F1:
                {
                    //PLAY
                    update.play();
                    break;
                }
                case F2:
                {
                    //PAUSE
                    update.pause();
                    break;
                }
                case F3:
                {
                    //Show Child Count
                    clearFrame();
                    System.out.println("Child Count: " + child.size());
                    break;
                }
            }
        });
        update = new Timeline(new KeyFrame(Duration.millis(16), e -> {
            //60 fps
            //System.out.println("loop test");
            drawFrame();
        }));
        update.setCycleCount(Timeline.INDEFINITE);
        update.setRate(1);
        update.setAutoReverse(false);
        //update.play(); //uncomment for play when start
        //
        stage.setTitle(title);
        stage.setResizable(false);
        stage.setScene(new Scene(root, width - 10, height - 10, backcolor));
        stage.show();
        root.requestFocus();
    }

    private static void clearFrame()
    {
        gc.clearRect(0, 0, width, height);
    }

    private static void drawFrame()
    {
        hue += 0.5;
        color = Color.hsb(hue, 1, 1);
        gc.setStroke(color);
        generatePolygon(vertexCount, r, angle);
        gc.strokePolyline(xPoints, yPoints, vertexCount + 1);
        //
        if (r >= width * 0.45 && !desc)
        {
            desc = true;
            //
            siteCheck();
        } else if (r < width * 0.2 && desc)
        {
            desc = false;
            //
            siteCheck();
        } else
        {
            if (desc)
            {
                r--;
            } else
            {
                r++;
            }
        }

        //
        angle += 1;
        if (angle > 360)
        {
            angle = 0;
        }
    }

    private static void generatePolygon(int vertex, double radius, double startAngle)
    {
        xPoints = new double[vertex + 1];
        yPoints = new double[vertex + 1];
        for (int i = 0; i <= vertex; i++)
        {
            Point2D temp = Utils.endPoint(center, startAngle + (360 / (double) vertex) * i, radius);
            xPoints[i] = temp.getX();
            yPoints[i] = temp.getY();
        }
    }

    private static void siteCheck()
    {
        if (site)
        {
            if (vertexCount > 3)
            {
                vertexCount--;
            } else
            {
                site = false;
            }
        } else
        {
            if (vertexCount < 5)
            {
                vertexCount++;
            } else
            {
                site = true;
            }
        }
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}