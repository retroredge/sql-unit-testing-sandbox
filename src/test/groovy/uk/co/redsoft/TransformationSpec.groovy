package uk.co.redsoft

import groovy.sql.Sql
import spock.lang.Shared
import spock.lang.Specification

class TransformationSpec extends Specification {
    @Shared sql = Sql.newInstance("jdbc:h2:mem:testspockdb", "sa", "", "org.h2.Driver")

    def setupSpec() {
        File createDeclarationTable = new File("src/main/resources/declaration.ddl")
        sql.execute(createDeclarationTable.getText("UTF-8"))
        File createGoodsItemTable = new File("src/main/resources/goods-item.ddl")
        sql.execute(createGoodsItemTable.getText("UTF-8"))
    }

    def "Declaration and GoodsItem data is transformed successfully"() {

        given: 'data exists in the Declaration and GoodsItem tables'
        createDeclarationAndGoodsItemData()

        when: 'the transformation is executed'
        File transformSql = new File("src/main/resources/transform.sql")
        def rows = sql.rows(transformSql.getText("UTF-8"));

        then: 'the data is combined and transformed as expected'
        rows.size() == 5
        rows[0].ID == 1
        rows[0].commodityCode == "FOOD"
        rows[0].description == "Fruit and veg : Apples"
        rows[0].createdDate == "2022-07-22T13:10:00.000Z"
        rows[0].traderName == "Acme"

        // and so on
    }

    void createDeclarationAndGoodsItemData() {
        String insertData = """
                INSERT INTO Declaration (ID, createdDate, description, traderName) VALUES (1, '2022-07-22T13:10:00.000Z', 'Fruit and veg', 'Acme');
                INSERT INTO Declaration (ID, createdDate, description, traderName) VALUES (2, '2022-07-22T13:11:00.000Z', 'Computer chips', 'Sinclair');
                            
                INSERT INTO GoodsItem (ID, declarationId, description, commodityCode) VALUES (1, 1, 'Apples', 'FOOD');
                INSERT INTO GoodsItem (ID, declarationId, description, commodityCode) VALUES (2, 1, 'Figs', 'FOOD');
                            
                INSERT INTO GoodsItem (ID, declarationId, description, commodityCode) VALUES (3, 2, 'Z80 CPUs', 'COMPUTER PARTS');
                INSERT INTO GoodsItem (ID, declarationId, description, commodityCode) VALUES (4, 2, 'ROM chips', 'COMPUTER PARTS');
                INSERT INTO GoodsItem (ID, declarationId, description, commodityCode) VALUES (5, 2, 'RAM chips', 'COMPUTER PARTS');
                """;

        sql.execute(insertData)
    }
}