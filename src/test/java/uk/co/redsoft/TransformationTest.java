package uk.co.redsoft;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import static org.assertj.core.api.Assertions.assertThat;

public class TransformationTest {

    private static final String JDBC_URL = "jdbc:h2:mem:testdb";

    private static Connection connection;

    @BeforeAll
    public static void createDb() throws Exception {
        connection = DriverManager.getConnection(JDBC_URL, "sa", "");
        var createDeclarationTable = Files.readString(Paths.get("src/main/resources/declaration.ddl"));
        var createGoodsItemTable = Files.readString(Paths.get("src/main/resources/goods-item.ddl"));
        var statement = connection.createStatement();
        statement.execute(createDeclarationTable);
        statement.execute(createGoodsItemTable);
    }

    @Test
    public void declarationAndGoodsItemRecordsTransformSuccessfully() throws Exception {

        givenDataExistsInTheDeclarationAndGoodsItemTable();

        var results = whenTheTransformationIsExecuted();

        // Then
        results.next();
        assertThat(results.getInt("ID")).isEqualTo(1);
        assertThat(results.getString("commodityCode")).isEqualTo("FOOD");
        assertThat(results.getString("description")).isEqualTo("Fruit and veg : Apples");
        assertThat(results.getString("createdDate")).isEqualTo("2022-07-22T13:10:00.000Z");
        assertThat(results.getString("traderName")).isEqualTo("Acme");

        // and so on
    }

    private ResultSet whenTheTransformationIsExecuted() throws Exception {
        var statement = connection.createStatement();
        return statement.executeQuery(Files.readString(Paths.get("src/main/resources/transform.sql")));
    }

    private void givenDataExistsInTheDeclarationAndGoodsItemTable() throws Exception {
        var insertData = """
                INSERT INTO Declaration (ID, createdDate, description, traderName) VALUES (1, '2022-07-22T13:10:00.000Z', 'Fruit and veg', 'Acme');
                INSERT INTO Declaration (ID, createdDate, description, traderName) VALUES (2, '2022-07-22T13:11:00.000Z', 'Computer chips', 'Sinclair');
                            
                INSERT INTO GoodsItem (ID, declarationId, description, commodityCode) VALUES (1, 1, 'Apples', 'FOOD');
                INSERT INTO GoodsItem (ID, declarationId, description, commodityCode) VALUES (2, 1, 'Figs', 'FOOD');
                            
                INSERT INTO GoodsItem (ID, declarationId, description, commodityCode) VALUES (3, 2, 'Z80 CPUs', 'COMPUTER PARTS');
                INSERT INTO GoodsItem (ID, declarationId, description, commodityCode) VALUES (4, 2, 'ROM chips', 'COMPUTER PARTS');
                INSERT INTO GoodsItem (ID, declarationId, description, commodityCode) VALUES (5, 2, 'RAM chips', 'COMPUTER PARTS');
                """;
        var statement = connection.createStatement();
        statement.execute(insertData);
    }
}
