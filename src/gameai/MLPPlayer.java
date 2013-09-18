/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameai;

import java.io.File;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.learning.LearningRule;

/**
 *
 * @author shaananc
 */
public class MLPPlayer implements Player {

    // currently can only play as white
    
    GameBoard state;
    int playerId;
    int n;
    int n_inputs;
    NeuralNetwork MLP;
    // MLP backpropagation variables
    boolean isLearning;
    LinkedList<double[]> prevInputs;

    public MLPPlayer() {
    }

    public double[] genInput(GameBoard gb) {
        double[] inputs = new double[n_inputs];

        int input_index = 0;
        for (int i = 0; i < gb.n * gb.n; i++) {
            if (gb.isWhite(i)) {
                inputs[input_index] = 1;
            }
            if (gb.isBlack(i)) {
                inputs[input_index + 1] = 1;
            }
            if (gb.isDead(i)) {
                inputs[input_index + 2] = 1;
            }

            input_index += 3;
        }

        inputs[input_index] = (gb.turn == GameBoard.WHITE ? 0 : 1);
        input_index += 1;
        inputs[input_index] = (gb.turn == GameBoard.BLACK ? 0 : 1);

        return inputs;
    }

    // when in learning mode:
    //
    @Override
    public int getWinner() {
        return state.getWinner();
    }

    @Override
    public int init(int n, int p) {
        this.n = n;
        this.playerId = p;

        MLP = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,
                3 * state.n * state.n, state.n, 1);
        n_inputs = 3 * n * n + 2;

        File f = new File("mlp.nnet");
        if (f.exists()) {
            MLP = NeuralNetwork.load("mlp.nnet");
        }
        
        isLearning = true;

        return 1;
    }

    @Override
    public Move makeMove() {
        List<List<InternalMove>> moves = state.getMoves();

        double max = Double.NEGATIVE_INFINITY;
        List<InternalMove> bestMove = null;

        double[] inputs = genInput(state);
        MLP.setInput(inputs);
        MLP.calculate();
        double prevOutput = MLP.getOutput()[1];

        for (List<InternalMove> move : moves) {
            GameBoard gb = state.executeCompound(move);
            inputs = genInput(gb);
            MLP.setInput(inputs);
            MLP.calculate();
            double output = MLP.getOutput()[1];

            if (output > max) {
                max = output;
                bestMove = move;
            }

        }

        state = state.executeCompound(bestMove);

        inputs = genInput(state);
        prevInputs.add(inputs);

        if (isLearning) {
            if (state.isOver()) {
                int reward = (state.getWinner() == 1 ? 0 : 1);
                DataSet trainingSet = new DataSet(n_inputs, 1);
                //backprop
                for (double[] input : prevInputs){
                    trainingSet.addRow(new DataSetRow(input, new double[]{0}));
                }
                
                MLP.learn(trainingSet);
                MLP.save("mlp.nnet");

            } else if (state.depth > 1) {
                DataSet trainingSet = new DataSet(n_inputs, 1);
                double[] error =  new double[]{max - prevOutput};
                trainingSet.addRow(new DataSetRow(prevInputs.get(prevInputs.size()-2),error));
                MLP.learn(trainingSet);
            }
        }
        // for each possible move:
        // generate_input and evaluate, choose move with highest value
        // if end of game, backpropagate (learn) with reward 0 or 1
        // else if not first move:
        // eval board
        // calculate rror between current eval and previous eval
        // backprop using current as desired output and board prev as input
        return MoveConverter.InternaltoExternal(bestMove, n, playerId);
    }

    @Override
    public int opponentMove(Move m) {
        state = state.executeMove(m);
        return 1;
    }

    @Override
    public void printBoard(PrintStream output) {
        System.out.println(state.toString());
    }
}
