package red;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.JTextArea;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.misc.SerializedClassifier;
import weka.core.Attribute;
import weka.core.Debug;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;

public class NeuralNet {

    public void train(String paramANN, JTextArea tx) {
        
        try {
            
            FileReader trainReader = new FileReader("src/files/Diamond_Train.arff");
            //instances
            Instances insTrain = new Instances(trainReader);
            insTrain.setClassIndex(insTrain.numAttributes() - 1);

            //multilayer perceptron
            MultilayerPerceptron mlp = new MultilayerPerceptron();
            mlp.setOptions(Utils.splitOptions(paramANN));
            mlp.buildClassifier(insTrain);
            Debug.saveToFile("TrainedModel.train", mlp);

            //serialize
            SerializedClassifier classifier = new SerializedClassifier();
            classifier.setModelFile(new File("TrainedModel.train"));

            //eval model
            Evaluation evaluation = new Evaluation(insTrain);
            evaluation.evaluateModel(classifier, insTrain);
            System.out.println(evaluation.toSummaryString());
            System.out.println(evaluation.toMatrixString("Resultados Matrix"));
            tx.append(evaluation.toSummaryString()+"\n"+evaluation.toMatrixString(" \n" +
                    "Confusion Matrix (Training)"));
            trainReader.close();

        } catch (FileNotFoundException ex) {
            tx.append("Archivo No encontrado");
            ex.printStackTrace();
        } catch (Exception ex) {
             ex.printStackTrace();
        }
    }
    
    public void test(JTextArea tx) {
        
        try {
            
            FileReader testReader = new FileReader("src/files/Diamond_Test.arff");
            Instances insTest = new Instances(testReader);
            insTest.setClassIndex(insTest.numAttributes() - 1);
            SerializedClassifier classifier = new SerializedClassifier();
            classifier.setModelFile(new File("TrainedModel.train"));
            Classifier mlp = classifier.getCurrentModel();
            Evaluation evalTest = new Evaluation(insTest);
            evalTest.evaluateModel(mlp, insTest);
            System.out.println(evalTest.toSummaryString());
            System.out.println(evalTest.toMatrixString("Matrix results"));
            tx.append(evalTest.toSummaryString()+"\n"+evalTest.toMatrixString(" \n" +
                    "Confusion Matrix (Testing)"));
            testReader.close();

        } catch (FileNotFoundException ex) {
            tx.append("File not found");
            ex.printStackTrace();
        } catch (Exception ex) {
             ex.printStackTrace();
        }
    }
    
	public void predict(JTextArea tx, String [] datos) {
        StringBuilder salida= new StringBuilder();
        try {
            /* Contruyendo atributos manualmente */
            // Declarando atributos
            Attribute carat = new Attribute("carat"); // numerico: kilates
            // nominal: corte
            ArrayList<String> lcut = new ArrayList<>(5); 
            lcut.add("Ideal");
            lcut.add("Premium");
            lcut.add("Good");
            lcut.add("Very_Good");
            lcut.add("Fair");          
            Attribute cut = new Attribute("cut", lcut);
            // nominal: color
            ArrayList<String> lcolor = new ArrayList<>(7);
            lcolor.add("E");
            lcolor.add("I");
            lcolor.add("J");
            lcolor.add("H");
            lcolor.add("F");
            lcolor.add("G");
            lcolor.add("D");
            Attribute color = new Attribute("color", lcolor);
            // nominal: claridad
            ArrayList<String> fvclar = new ArrayList<>(8);
            fvclar.add("SI2");
            fvclar.add("SI1");
            fvclar.add("VS1");
            fvclar.add("VS2");
            fvclar.add("VVS2");
            fvclar.add("VVS1");
            fvclar.add("I1");
            fvclar.add("IF");
            Attribute clarity = new Attribute("clarity", fvclar);
            // clase nominal: precio
            ArrayList<String> lClassPrice = new ArrayList<>(5);
            lClassPrice.add("300-699");
            lClassPrice.add("1000-4999");
            lClassPrice.add("5000-9999");
            lClassPrice.add("10000+");
            lClassPrice.add("700-999");
            Attribute price = new Attribute("price", lClassPrice);
            
            // Arraylist de todos los atributos
            ArrayList<Attribute> listaAtr = new ArrayList<>(5);
            listaAtr.add(carat);
            listaAtr.add(cut);
            listaAtr.add(color);
            listaAtr.add(clarity);
            listaAtr.add(price);

            // Instancias para la prediccion
            Instances dataset = new Instances("Dataset", listaAtr, listaAtr.size());
           
            // Instancias con datos seleccionados
            Instance inst = new DenseInstance(5);
            inst.setValue(carat, Double.parseDouble(datos[0]));
            inst.setValue(cut, datos[1]);
            inst.setValue(color, datos[2]);
            inst.setValue(clarity, datos[3]);
            inst.setValue(price, datos[4]);//A predecir
            dataset.add(inst);
            
            dataset.setClassIndex(dataset.numAttributes() - 1);
            SerializedClassifier classifier = new SerializedClassifier();
            classifier.setModelFile(new File("TrainedModel.train"));
            Classifier mlp = classifier.getCurrentModel();
            Evaluation evalTest = new Evaluation(dataset);
            evalTest.evaluateModel(mlp, dataset);
            
            ArrayList<Prediction> prediccion = evalTest.predictions();

            for (Prediction p : prediccion) {
                salida.append("\nPrediction: ").append(p.predicted()).append("\nForecast: ").append(p.actual()).append("\n");
            }
            tx.append(salida.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
       }

    }

}
