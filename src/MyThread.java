
public class MyThread implements Runnable{
    private int[][] result;
    private int[][] a;
    private int[][] b;
    private Borders a_borders;
    private Borders b_borders;
    private Thread t;

    public MyThread(int[][] a, int[][] b, Borders a_borders, Borders b_borders) {
        this.a = a;
        this.b = b;
        this.a_borders = a_borders;
        this.b_borders = b_borders;

        t = new Thread(this, "New Task");
        t.start();
    }

    @Override
    public void run() {
        result = BigMatrixMultiplier.rec_shtrassen_mult(a, b, a_borders, b_borders);
    }


    public int[][] getResult() {
        return result;
    }

    public Thread getT() {
        return t;
    }
}
