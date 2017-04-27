/**
 * Created by pavel on 27.04.17.
 */
public class MatrixOperations {

    public static int[][] native_border_mult(int[][] a, int[][] b, Borders a_borders, Borders b_borders){

        int size = a_borders.i_end - a_borders.i_beg;
        int[][] result = new int[size][size];

        int a_i = a_borders.i_beg;
        int b_j = b_borders.j_beg;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                int k_a = a_borders.j_beg;
                int k_b = b_borders.i_beg;

                while ((k_a < a_borders.j_end) && (k_b < b_borders.i_end)){
                    result[i][j] += (a[a_i + i][k_a] * b[k_b][b_j + j]);
                    k_a++;
                    k_b++;
                }
            }
        }

        return result;
    }

    public static void divide(Borders borders, Borders part_1_1, Borders part_1_2,
                               Borders part_2_1, Borders part_2_2) {

        //todo посмотреть как влияют операции деления на все время выполнения
        part_1_1.i_beg = borders.i_beg;
        part_1_1.j_beg = borders.j_beg;
        part_1_1.i_end = (borders.i_beg + borders.i_end) / 2;
        part_1_1.j_end = (borders.j_beg + borders.j_end) / 2;

        part_1_2.i_beg = borders.i_beg;
        part_1_2.j_beg = (borders.j_beg + borders.j_end) / 2;
        part_1_2.i_end = (borders.i_beg + borders.i_end) / 2;
        part_1_2.j_end = borders.j_end;

        part_2_1.i_beg = (borders.i_beg + borders.i_end) / 2;
        part_2_1.j_beg = borders.j_beg;
        part_2_1.i_end = borders.i_end;
        part_2_1.j_end = (borders.j_beg + borders.j_end) / 2;

        part_2_2.i_beg = (borders.i_beg + borders.i_end) / 2;
        part_2_2.j_beg = (borders.j_beg + borders.j_end) / 2;
        part_2_2.i_end = borders.i_end;
        part_2_2.j_end = borders.j_end;

    }

    public static int[][] add(int[][] a, int[][] b, Borders a_borders, Borders b_borders){

        int size = a_borders.i_end - a_borders.i_beg;
        int[][] result = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = a[a_borders.i_beg + i][a_borders.j_beg + j] + b[b_borders.i_beg + i][b_borders.j_beg + j];
            }
        }

        return result;
    }

    public static int[][] substract(int[][] a, int[][] b, Borders a_borders, Borders b_borders){

        int size = a_borders.i_end - a_borders.i_beg;
        int[][] result = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = a[a_borders.i_beg + i][a_borders.j_beg + j] - b[b_borders.i_beg + i][b_borders.j_beg + j];
            }
        }

        return result;

    }

    public static void borders_substract(int[][] a, int[][] b, Borders a_borders, Borders b_borders){

        int size = a_borders.i_end - a_borders.i_beg;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                a[a_borders.i_beg + i][a_borders.j_beg + j] -= b[b_borders.i_beg + i][b_borders.j_beg + j];
            }
        }
    }

    public static void borders_add(int[][] a, int[][] b, Borders a_borders, Borders b_borders){

        int size = a_borders.i_end - a_borders.i_beg;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                a[a_borders.i_beg + i][a_borders.j_beg + j] += b[b_borders.i_beg + i][b_borders.j_beg + j];
            }
        }
    }


    public static int[][] combine(int size, int[][] p1, int[][] p2, int[][] p3,
                                   int[][] p4, int[][] p5, int[][] p6, int[][] p7){

        int[][] c = new int[size][size];

        Borders c_1_1 = new Borders();
        Borders c_1_2 = new Borders();
        Borders c_2_1 = new Borders();
        Borders c_2_2 = new Borders();

        //giving values to borders' parts
        divide(new Borders(0, 0, size, size), c_1_1, c_1_2, c_2_1, c_2_2);

        Borders p_borders = new Borders(0, 0, p1.length, p1.length);

        //combining c1 part
        borders_add(c, p5, c_1_1, p_borders);
        borders_add(c, p4, c_1_1, p_borders);
        borders_add(c, p6, c_1_1, p_borders);
        borders_substract(c, p2, c_1_1, p_borders);

        //combining c2 part
        borders_add(c, p1, c_1_2, p_borders);
        borders_add(c, p2, c_1_2, p_borders);

        //combining c3 part
        borders_add(c, p3, c_2_1, p_borders);
        borders_add(c, p4, c_2_1, p_borders);

        //combining c4 part
        borders_add(c, p5, c_2_2, p_borders);
        borders_add(c, p1, c_2_2, p_borders);
        borders_substract(c, p3, c_2_2, p_borders);
        borders_substract(c, p7, c_2_2, p_borders);


        return c;

    }


    public static boolean equal(SquareMatrix a, SquareMatrix b){

        if(a.getSize() != b.getSize()) {
            return false;
        } else {

            int size = a.getSize();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (a.getElement(i, j) != b.getElement(i,j)){
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
