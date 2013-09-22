/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameai;

import java.io.File;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.lma.LevenbergMarquardtTraining;
import org.encog.persist.EncogDirectoryPersistence;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.learning.DynamicBackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;

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
    //encog network
    BasicNetwork network;
    // MLP backpropagation variables
    boolean isLearning;
    LinkedList<double[]> prevInputs;
    double gamma = 0.9;

    public MLPPlayer(int n) {

        n_inputs = 3 * n * n + 3;

        File f = new File("encognn.eg");
        if (f.exists()) {
            network = (BasicNetwork) EncogDirectoryPersistence.loadObject(new File("encognn.eg"));
        } else {
            network = new BasicNetwork();
            network.addLayer(new BasicLayer(new ActivationSigmoid(), true, n_inputs));
            network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 3 * n));
            network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 2));
            network.getStructure().finalizeStructure();
            network.reset();

        }


        isLearning = true;
        prevInputs = new LinkedList();

    }

    public MLPPlayer(int n, BasicNetwork network) {
        this(n);
        this.network = network;
    }

    private double evaluate(GameBoard gb) {
        double inputs[] = genInput(gb);

        double encog_input[][] = new double[1][n_inputs];
        System.arraycopy(inputs, 0, encog_input[0], 0, inputs.length);
        double output[] = new double[2];
        network.compute(inputs, output);
        return (output[0] - output[1]) * (playerId == 1 ? 1 : -1);

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

        input_index += 1;
        inputs[input_index] = 1;

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

        state = new GameBoard(n);
        prevInputs.clear();

        return 1;
    }

    public void pauseLearning() {
        //MLP.pauseLearning();
    }

    public void resumeLearning() {
        //MLP.resumeLearning();
    }

    @Override
    public Move makeMove() {
        List<List<InternalMove>> moves = state.getMoves();

        double max = -1;

        List<InternalMove> bestMove = null;

        double[] inputs = genInput(state);
        //prevInputs.add(inputs);


        for (List<InternalMove> move : moves) {
            GameBoard gb = state.executeCompound(move);

            double eval = evaluate(gb);
            if (eval > max) {
                max = eval;
                bestMove = move;
            }

        }

        GameBoard new_state = state.executeCompound(bestMove);


        if (isLearning) {

            double training_input[][] = new double[1][n_inputs];
            double training_output[][] = new double[1][2];
            double cur_in[] = genInput(state);
            System.arraycopy(cur_in, 0, training_input[0], 0, cur_in.length);

            if (new_state.isOver()) {
                double rewardWhite = (new_state.getWinner() == 1 ? 1 : 0);
                double rewardBlack = (new_state.getWinner() == 2 ? 1 : 0);

                training_output[0][0] = rewardWhite;
                training_output[0][1] = rewardBlack;

            } else {
                network.compute(training_input[0], training_output[0]);
            }


            NeuralDataSet trainingSet = new BasicNeuralDataSet(training_input, training_output);
            final LevenbergMarquardtTraining train = new LevenbergMarquardtTraining(network, trainingSet);
            train.iteration(1);
            train.finishTraining();

        }

        state = new_state;

        return MoveConverter.InternaltoExternal(bestMove, n, playerId);
    }

    @Override
    public int opponentMove(Move m) {
        GameBoard new_state = state.executeMove(m);

        double[] inputs = genInput(state);

        if (isLearning) {

            double training_input[][] = new double[1][n_inputs];
            double training_output[][] = new double[1][2];
            double cur_in[] = genInput(state);
            System.arraycopy(cur_in, 0, training_input[0], 0, cur_in.length);

            if (new_state.isOver()) {
                double rewardWhite = (new_state.getWinner() == 1 ? 1 : 0);
                double rewardBlack = (new_state.getWinner() == 2 ? 1 : 0);

                training_output[0][0] = rewardWhite;
                training_output[0][1] = rewardBlack;

            } else {
                network.compute(training_input[0], training_output[0]);
            }


            NeuralDataSet trainingSet = new BasicNeuralDataSet(training_input, training_output);
            final LevenbergMarquardtTraining train = new LevenbergMarquardtTraining(network, trainingSet);
            train.iteration(1);
            train.finishTraining();

        }

        state = new_state;


        return 1;
    }

    @Override
    public void printBoard(PrintStream output) {
        System.out.println(state.toString());
    }
}
