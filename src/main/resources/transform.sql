-- Example SQL that denormalises the Declaration and GoodsItem data

SELECT
    GoodsItem.ID,
    GoodsItem.commodityCode,
    CONCAT(Declaration.description, ' : ', GoodsItem.description) as description,
    Declaration.createdDate,
    Declaration.traderName
From GoodsItem
INNER JOIN Declaration on GoodsItem.declarationId = Declaration.ID;