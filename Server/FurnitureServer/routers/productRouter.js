const express = require("express");
const router = express.Router();
const productController = require("../controllers/productController");

// Create a new product
router.post("/add", productController.createProduct);
// Get all products
router.get("/view", productController.getAllProducts );
// Update a product by ID
router.get("/update/:id", productController.updateProductById);
// Delete a type of product by ID
router.get("/delete/:id", productController.deleteProductById);

// router.get('/add-page', (req, res) => {
//     const successMessage = req.query.success === 'true' ? 'Danh mục nội thất được thêm thành công!' : null;
//     const dangerMessage = req.query.success === 'errAdd' ? 'Đã xảy ra lỗi khi thêm danh mục nội thất, có thể dữ liệu này đã có trên hệ thống. Vui lòng kiểm tra lại!' : null; 
//     res.render('products/addOrEdit_product.hbs', {viewTitle: "Thêm loại nội thất", successMessage, dangerMessage });
// });
router.get("/add-page", productController.getproductType);

router.get("/search", productController.searchProduct );

///--------------android-------------
router.get('/view-app', productController.getAllProductsForApp);

router.post('/productbyid', productController.getProductsByIdsForApp);

module.exports = router;
