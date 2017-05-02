

public class SquareMatrix {
    private int[][] matrix;
    private int size;

    public SquareMatrix(int size) {
        this.size = size;
        this.matrix = new int[size][size];
    }

    public SquareMatrix(int[][] matrix) {
        this.matrix = matrix;
        this.size = matrix.length;
    }

    public void print(){
        System.out.println();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void generate(int lower_lim, int upper_lim){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = lower_lim + (int) (Math.random() * upper_lim);
            }
        }
    }

    public void generate(int size, int lower_lim, int upper_lim){
        if (this.size != size) {
            this.matrix = new int[size][size];
            this.size = size;
        }

        generate(lower_lim, upper_lim);
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public int getSize() {
        return size;
    }

    public int getElement(int i, int j) throws IndexOutOfBoundsException{
        if ( (i < size) && (j < size)) {
            return matrix[i][j];
        } else {
            throw new IndexOutOfBoundsException("Index is out of range!");
        }
    }

    public void setElement(int i, int j, int value){
        this.matrix[i][j] = value;
    }

    public void resize(int newSize) {

        if (newSize > size) {
            int[][] newMatrix = new int[newSize][newSize];

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    newMatrix[i][j] = matrix[i][j];
                }
            }

            this.size = newSize;
            this.matrix = newMatrix;
        } else if (newSize < size){
            int[][] newMatrix = new int[newSize][newSize];

            for (int i = 0; i < newSize; i++) {
                for (int j = 0; j < newSize; j++) {
                    newMatrix[i][j] = matrix[i][j];
                }
            }

            this.size = newSize;
            this.matrix = newMatrix;
        }
    }



    public String toString() {
        String matrix_str = "[\n";

        int i, j;
        for (i = 0; i < size - 1; i++) {
            matrix_str += '[';
            for (j = 0; j < size - 1; j++) {
                matrix_str += ' ';
                matrix_str += matrix[i][j];
                matrix_str += ',';
            }

            matrix_str += ' ';
            matrix_str += matrix[i][j];
            matrix_str += "]\n";
        }

        matrix_str += "]";
        return matrix_str;
    }
}



