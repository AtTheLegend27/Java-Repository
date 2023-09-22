/** A class that represents a path via pursuit curves. */
public class Path {

    // TODO
    private Point Current;
    private Point Next;


    public Path(double x, double y) {
        this.Current = new Point(0, 0);
        this.Next = new Point(x, y);
    }

    public double getCurrX() {
        return Current.getX();
    }

    public double getCurrY() {
        return Current.getY();
    }

    public double getNextX() {
        return Next.getX();
    }

    public double getNextY() {
        return Next.getY();
    }

    public Point getCurrentPoint() {
        return Current;
    }

    public void setCurrentPoint(Point point) {
        Current.setX(point.getX());
        Current.setY(point.getY());
    }

    public void iterate(double dx, double dy){
        Current = new Point(Next);
        Next = new Point(Next.getX()+dx, Next.getY()+dy);
    }

}
