package TestExerciseNaumen;

import java.util.Arrays;

public class TestSolver {
    public static void main(String[] args) {
        Solver solver = new Solver();
//        int[] solution = solver.resolve(new int[]{1, 2, 3, 4, 0, 5, 6, 7});
//        int[] solution = solver.resolve(new int[]{2, 1, 3, 4, 0, 5, 6, 7});
        int[] solution = solver.resolve(new int[]{0, 1, 2, 3, 4, 5, 6, 7}); //2 1 3 4 5 1 3 2 3 1 5 4 2 1 3 1 2 4

        System.out.println(Arrays.toString(solution));
    }
}
