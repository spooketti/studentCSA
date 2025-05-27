package _notebooks.ML;

import tech.tablesaw.api.*;
import tech.tablesaw.columns.Column;
import weka.classifiers.Classifier;
import weka.classifiers.functions.Logistic;
import weka.classifiers.trees.J48;
import weka.core.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TitanicML {

    public static void main(String[] args) throws Exception {
        // Step 1: Load and Clean Data using Tablesaw
        InputStream inputStream = TitanicML.class.getResourceAsStream("/data/titanic.csv");
        if (inputStream == null) {
            throw new IllegalArgumentException("File not found: titanic.csv");
        }
        Table titanic = Table.read().csv(inputStream);

        // Drop non-relevant columns
        titanic = titanic.removeColumns("Name", "Ticket", "Cabin");

        // Convert categorical values to numeric
        StringColumn sex = titanic.stringColumn("Sex");
        sex = sex.replaceAll("male", "1").replaceAll("female", "0");
        titanic.replaceColumn("Sex", sex);

        StringColumn embarked = titanic.stringColumn("Embarked");
        embarked = embarked.replaceAll("S", "0").replaceAll("C", "1").replaceAll("Q", "2");
        titanic.replaceColumn("Embarked", embarked);

        // Fill missing values
        titanic.doubleColumn("Age").setMissingTo(titanic.numberColumn("Age").median());
        titanic.doubleColumn("Fare").setMissingTo(titanic.numberColumn("Fare").median());

        // Convert "Survived" column to nominal
        StringColumn survived = titanic.intColumn("Survived").asStringColumn();
        survived = survived.replaceAll("0", "No").replaceAll("1", "Yes");
        titanic.replaceColumn("Survived", survived);

        // Normalize numeric columns
        normalizeColumn(titanic, "Age");
        normalizeColumn(titanic, "Fare");
        normalizeColumn(titanic, "SibSp");
        normalizeColumn(titanic, "Parch");

        // Step 2: Convert Tablesaw Table to Weka Instances
        Instances data = convertTableToWeka(titanic);

        // Set class index (target variable)
        data.setClassIndex(data.attribute("Survived").index());

        // Step 3: Apply Machine Learning Models
        J48 tree = new J48();
        tree.buildClassifier(data);

        Logistic logistic = new Logistic();
        logistic.buildClassifier(data);

        // Step 4: Evaluate Models
        System.out.println("Decision Tree Model:\n" + tree);
        System.out.println("Logistic Regression Model:\n" + logistic);

        // Cross-validation evaluation
        evaluateModel(tree, data, "Decision Tree");
        evaluateModel(logistic, data, "Logistic Regression");
    }

   // Normalize a numeric column to be between 0 and 1
   private static void normalizeColumn(Table table, String columnName) {
        Column<?> column = table.column(columnName);
        double min = 0;
        double max = 0;
        if (column instanceof DoubleColumn) {
            DoubleColumn doubleColumn = (DoubleColumn) column;
            min = doubleColumn.min();
            max = doubleColumn.max();
            for (int i = 0; i < doubleColumn.size(); i++) {
                double normalizedValue = (doubleColumn.getDouble(i) - min) / (max - min);
                doubleColumn.set(i, normalizedValue);
            }
        } else if (column instanceof IntColumn) {
            IntColumn intColumn = (IntColumn) column;
            min = intColumn.min();
            max = intColumn.max();
            for (int i = 0; i < intColumn.size(); i++) {
                double normalizedValue = (intColumn.getInt(i) - min) / (max - min);
                intColumn.set(i, (int) normalizedValue);
            }
        }
    }
    
    // Convert Tablesaw Table to Weka Instances
    private static Instances convertTableToWeka(Table table) {
        List<Attribute> attributes = new ArrayList<>();

        // Define attributes based on column types
        for (Column<?> col : table.columns()) {
            if (col.type().equals(ColumnType.STRING)) {
                List<String> classValues = new ArrayList<>();
                table.stringColumn(col.name()).unique().forEach(classValues::add);
                attributes.add(new Attribute(col.name(), classValues));
            } else {
                attributes.add(new Attribute(col.name()));
            }
        }

        // Create Weka dataset
        Instances data = new Instances("Titanic", new ArrayList<>(attributes), table.rowCount());

        for (Row row : table) {
            double[] values = new double[table.columnCount()];
            for (int i = 0; i < table.columnCount(); i++) {
                Column<?> col = table.column(i);

                // Handle Integer and Double columns
                if (col.type() == ColumnType.INTEGER) {
                    values[i] = row.getInt(i);
                } else if (col.type() == ColumnType.DOUBLE) {
                    values[i] = row.getDouble(i);
                } else if (col.type() == ColumnType.STRING) {
                    values[i] = attributes.get(i).indexOfValue(row.getString(i));
                }
            }
            data.add(new DenseInstance(1.0, values));
        }
        return data;
    }

    // Evaluate model using 10-fold cross-validation
    private static void evaluateModel(Classifier model, Instances data, String modelName) throws Exception {
        weka.classifiers.Evaluation eval = new weka.classifiers.Evaluation(data);
        eval.crossValidateModel(model, data, 10, new java.util.Random(1));
        System.out.printf("%s Accuracy: %.2f%%%n", modelName, eval.pctCorrect());
    }
}