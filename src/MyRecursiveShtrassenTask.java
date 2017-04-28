import java.util.concurrent.RecursiveTask;

public class MyRecursiveShtrassenTask extends RecursiveTask<int[][]> {

    private int[][] a;
    private int[][] b;
    private Borders a_borders;
    private Borders b_borders;
    int size;

    private static final int MIN_SIZE = 4;

    public MyRecursiveShtrassenTask(int[][] a, int[][] b, Borders a_borders, Borders b_borders) {
        this.a = a;
        this.b = b;
        this.a_borders = a_borders;
        this.b_borders = b_borders;
        this.size = a_borders.i_end - a_borders.i_beg;

    }

    protected int[][] compute() {

        if (size < MIN_SIZE){
            return MatrixOperations.native_border_mult(a, b, a_borders, b_borders);
        } else {
            //making borders for smaller parts
            Borders a_1_1 = new Borders();
            Borders a_1_2 = new Borders();
            Borders a_2_1 = new Borders();
            Borders a_2_2 = new Borders();

            Borders b_1_1 = new Borders();
            Borders b_1_2 = new Borders();
            Borders b_2_1 = new Borders();
            Borders b_2_2 = new Borders();

            //giving values to borders' parts
            MatrixOperations.divide(a_borders, a_1_1, a_1_2, a_2_1, a_2_2);
            MatrixOperations.divide(b_borders, b_1_1, b_1_2, b_2_1, b_2_2);


            //making 10 summations and

            int[][] s1 = MatrixOperations.substract(b, b, b_1_2, b_2_2);
            int[][] s2 = MatrixOperations.add(a, a, a_1_1, a_1_2);
            int[][] s3 = MatrixOperations.add(a, a, a_2_1, a_2_2);
            int[][] s4 = MatrixOperations.substract(b, b, b_2_1, b_1_1);
            int[][] s5 = MatrixOperations.add(a, a, a_1_1, a_2_2);
            int[][] s6 = MatrixOperations.add(b, b, b_1_1, b_2_2);
            int[][] s7 = MatrixOperations.substract(a, a, a_1_2, a_2_2);
            int[][] s8 = MatrixOperations.add(b, b, b_2_1, b_2_2);
            int[][] s9 = MatrixOperations.substract(a, a, a_1_1, a_2_1);
            int[][] s10 = MatrixOperations.add(b, b, b_1_1, b_1_2);

            //all the S matrices have the same size
            Borders s_borders = new Borders(0, 0, s1.length, s1.length);


            MyRecursiveShtrassenTask p1_task = new MyRecursiveShtrassenTask(a, s1, a_1_1, s_borders);
            MyRecursiveShtrassenTask p2_task= new MyRecursiveShtrassenTask(s2, b, s_borders, b_2_2);
            MyRecursiveShtrassenTask p3_task = new MyRecursiveShtrassenTask(s3, b, s_borders, b_1_1);
            MyRecursiveShtrassenTask p4_task = new MyRecursiveShtrassenTask(a, s4, a_2_2, s_borders);
            MyRecursiveShtrassenTask p5_task = new MyRecursiveShtrassenTask(s5, s6, s_borders, s_borders);
            MyRecursiveShtrassenTask p6_task = new MyRecursiveShtrassenTask(s7, s8, s_borders, s_borders);
            MyRecursiveShtrassenTask p7_task = new MyRecursiveShtrassenTask(s9, s10, s_borders, s_borders);


            p1_task.fork();
            p2_task.fork();
            p3_task.fork();
            p4_task.fork();
            p5_task.fork();
            p6_task.fork();
            p7_task.fork();


            int[][] p1 = p1_task.join();
            int[][] p2 = p2_task.join();
            int[][] p3 = p3_task.join();
            int[][] p4 = p4_task.join();
            int[][] p5 = p5_task.join();
            int[][] p6 = p6_task.join();
            int[][] p7 = p7_task.join();


            return MatrixOperations.combine(size, p1, p2, p3, p4, p5, p6, p7);
        }
    }
}
