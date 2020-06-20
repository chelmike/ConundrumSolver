package TestExerciseNaumen;

import java.util.*;
import java.util.stream.Collectors;

public class Solver implements ConundrumSolver {

    @Override
    public int[] resolve(int[] initialState) {
        if (initialState.length == 0) {
            return new int[0];
        }

        Set<State> visitedStates = new HashSet<>();

        Path path = new Path(new State(initialState));
        if (path.getLastState().isFinal()) {
            return new int[0];
        }

        List<Path> paths = new ArrayList<>();
        paths.add(path);
        List<Path> pathsToAdd = new ArrayList<>();
        List<Path> pathsToRemove = new ArrayList<>();

        while (true) {
            for (Path currPath : paths) {
                State currState = currPath.getLastState();
//                System.out.println("Current state: " + Arrays.toString(currState.getDigits()));
                Map<Integer, State> possibleStates = currState.getPossibleMoves();
//                System.out.println("Next possible states: " + possibleStates.size());
                for (Map.Entry<Integer, State> state : possibleStates.entrySet()) {
//                    System.out.printf("Digit to move: %d, new state: %s. ", state.getKey(), Arrays.toString(state.getValue().getDigits()));

                    // если в данном состоянии мы ещё не были, добавляем его в перечень посещённых
                    // состояний и переходим в него (добавляем путь), иначе не добавляем -
                    // этот путь нас больше не интересует, т.к. мы в этом состоянии уже были
                    if (!visitedStates.contains(state.getValue())) {
                        Path newPath = currPath.move(state.getKey(), state.getValue());
                        visitedStates.add(state.getValue());

//                        System.out.printf("  State is not visited. New path: %s%n", newPath);
//                        System.out.println("  Visited states count: " + visitedStates.size());
//                        System.out.printf("  Curr path: %s%n", currPath);

                        pathsToAdd.add(newPath);
                        if (state.getValue().isFinal()) {
//                            System.out.printf("State is FINAL! THE PATH IS: %s%n", newPath);
                            return newPath.getDigitsAsArray();
                        }
                    }
//                    System.out.println();
                }
                pathsToRemove.add(currPath);
            }
            paths.removeAll(pathsToRemove);
            paths.addAll(pathsToAdd);
            pathsToAdd.clear();
            pathsToRemove.clear();
        }
    }
}

/**
 * Путь решения, т.е. набор перемещений цифр и набор соответствующих состояний
 */
class Path {
    private ArrayList<Integer> digits = new ArrayList<>();
    private ArrayList<State> states = new ArrayList<>();

    public Path (State state){
        states.add(state);
    }

    public Path (ArrayList<Integer> digits, ArrayList<State> states) {
        this.digits = digits;
        this.states = states;
    }

    public State getLastState() {
        if (states.size() > 0) {
            return states.get(states.size() - 1);
        }
        return null;
    }

    public int[] getDigitsAsArray() {
        return digits.stream().mapToInt(i -> i).toArray();
    }

    /**
     * Выполняет переход в новое состояние по текущему пути. Фактически возвращает новый путь с добавленным состоянием
     * @param digit - передвигаемая цифра
     * @param state - новое состояние, в которое перемещение цифры переводит игровое поле
     * @return Возвращает путь, дополненный переходом в новое состояние
     */
    public Path move(Integer digit, State state) {
        ArrayList<Integer> digits = new ArrayList<>(this.digits);
        ArrayList<State> states = new ArrayList<>(this.states);
        digits.add(digit);
        states.add(state);
        return new Path(digits, states);
    }

    @Override
    public String toString() {
        return digits.stream().map(i -> Integer.toString(i)).collect(Collectors.joining(" "));
    }
}

/**
 * Класс для хранения и работы с состоянием игрового поля, с учётом допустимых переходов
 */
class State {
    private final int[] finalStateDigits = {1, 2, 3, 4, 0, 5, 6, 7};
    private final int[] digits;
    private final int[][] moves = {
        {1, 2},
        {0, 2, 3},
        {0, 1, 5},
        {1, 4, 6},
        {3, 5},
        {2, 4, 7},
        {3, 7},
        {5, 6}
    };

    public State(int[] digits) {
        this.digits = digits;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof State))
            return false;
        if (obj == this)
            return true;
        return Arrays.equals(digits, ((State)obj).getDigits());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(digits);
    }

    public int[] getDigits() {
        return digits;
    }

    /**
     * Определяет, является ли текущее состояние итоговым
     * @return Истина, если текущее состояние соответствует цели головоломки
     */
    public boolean isFinal() {
        return Arrays.equals(this.digits, finalStateDigits);
    }

    /**
     * Перемещение цифры на пустое место - фактически перемена мест пустой клетки и цифры
     * @param indexFrom позиция 1 в графе (где сейчас находится цифра или пусто)
     * @param indexTo позиця 2 в графе (где сейчас пусто или находится цифра)
     * @return новое состояние, где цифра и пустое место поменяны местами
     */
    protected State swap(int indexFrom, int indexTo) {
        int[] digits = Arrays.copyOf(this.digits, this.digits.length);
        int digit = digits[indexTo];
        digits[indexTo] = digits[indexFrom];
        digits[indexFrom] = digit;
        return new State(digits);
    }

    /**
     * Получение набора возможных ходов
     * @return HashMap - словарь каждый элемент которого - состояние, в которое перейдёт игровое поле переводом цифры-ключа на текущее пустое место
     */
    public HashMap<Integer, State> getPossibleMoves() {
        int currentZeroIndex = 0;
        for (int i = 0; i < digits.length; i++) {
            if (digits[i] == 0) {
                currentZeroIndex = i;
                break;
            }
        }
        int [] possibleMoves = moves[currentZeroIndex];
        HashMap<Integer, State> result = new HashMap<>();
        for(int d : possibleMoves) {
            result.put(digits[d], this.swap(currentZeroIndex, d));
        }
        return result;
    }
}