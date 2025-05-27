package _notebooks.ML;

import tech.tablesaw.api.Table;
import tech.tablesaw.api.BooleanColumn;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.NumericColumn;
import java.io.File;
import java.io.InputStream;

public class TitanicPreprocess {
    public static void main(String[] args) throws Exception {
        // Load Titanic dataset from the classpath into a Tablesaw Table
        InputStream inputStream = TitanicAnalysis.class.getResourceAsStream("/data/titanic.csv");
        if (inputStream == null) {
            throw new IllegalArgumentException("File not found: titanic.csv");
        }
        Table titanic = Table.read().csv(inputStream);

        // Add "alone" column based on "SibSp (Siblings/Spouses)" and "Parch (Parents/Children)"
        NumericColumn<?> sibSpColumn = titanic.numberColumn("SibSp");
        NumericColumn<?> parchColumn = titanic.numberColumn("Parch");
        BooleanColumn aloneColumn = BooleanColumn.create("Alone", titanic.rowCount());
        // Add a new column for each row, indicating whether the passenger is alone
        for (int i = 0; i < titanic.rowCount(); i++) {
            // If both SibSp and Parch are 0, then the passenger is alone otherwise not
            // Number casting is required because Tablesaw does not support primitive types
            boolean isAlone = ((Number) sibSpColumn.get(i)).doubleValue() == 0 && ((Number) parchColumn.get(i)).doubleValue() == 0;
            aloneColumn.set(i, isAlone);
        }
        titanic.addColumns(aloneColumn);

        // Remove unwanted columns
        titanic = titanic.removeColumns("PassengerId", "Name", "Ticket", "Cabin");

        // Convert 'Sex' column: male -> 1, female -> 0
        DoubleColumn sexNumeric = DoubleColumn.create("Sex");
        for (int i = 0; i < titanic.rowCount(); i++) {
            String value = titanic.stringColumn("Sex").get(i);
            sexNumeric.append(value.equals("male") ? 1.0 : 0.0);
        }
        titanic.replaceColumn("Sex", sexNumeric);

        // Convert 'Embarked' column: C -> 1, Q -> 2, S -> 3
        DoubleColumn embarkedNumeric = DoubleColumn.create("Embarked");
        for (int i = 0; i < titanic.rowCount(); i++) {
            String value = titanic.stringColumn("Embarked").get(i);
            if (value.equals("C")) embarkedNumeric.append(1.0);
            else if (value.equals("Q")) embarkedNumeric.append(2.0);
            else embarkedNumeric.append(3.0); // Default to 'S' = 3
        }
        titanic.replaceColumn("Embarked", embarkedNumeric);

        // Fill missing 'Age' and 'Fare' with median
        titanic.doubleColumn("Age").set(titanic.numberColumn("Age").isMissing(), titanic.numberColumn("Age").median());
        titanic.doubleColumn("Fare").set(titanic.numberColumn("Fare").isMissing(), titanic.numberColumn("Fare").median());

        // Save the cleaned dataset
        File outputFile = new File("src/main/resources/data/titanic_cleaned.csv");
        titanic.write().csv(outputFile);

        System.out.println("Data cleaned and saved as titanic_cleaned.csv!");
    }
}

