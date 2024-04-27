const express = require("express");
const router = express.Router();
const productTypeController = require("../controllers/productTypeController");

// Create a new type of product
router.post("/add", productTypeController.createProductType);
// Get all types of products
router.get("/view", productTypeController.getAllProductTypes );
// Get a type of product by ID
//router.get("/:id", productTypeController.getProductTypeById);
// Update a type of product by ID
router.get("/update/:id", productTypeController.updateProductTypeById);
// Delete a type of product by ID
router.get("/delete/:id", productTypeController.deleteProductTypeById);

router.get('/add-page', (req, res) => {
    const successMessage = req.query.success === 'true' ? 'Danh mục nội thất được thêm thành công!' : null;
    const dangerMessage = req.query.success === 'errAdd' ? 'Đã xảy ra lỗi khi thêm danh mục nội thất, có thể dữ liệu này đã có trên hệ thống. Vui lòng kiểm tra lại!' : null; 
    res.render('productTypes/addOrEdit_productType.hbs', {viewTitle: "Thêm loại nội thất", successMessage, dangerMessage });
});

router.get("/search", productTypeController.searchProductType );

///--------------android-------------
router.get('/view-app', productTypeController.getAllProductTypeForApp);

module.exports = router;
