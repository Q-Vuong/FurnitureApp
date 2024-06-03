const express = require("express");
const router = express.Router();
const discountController = require("../controllers/discountController");

// Create a new type of product
router.post("/add", discountController.createDiscount);
// Get all types of products
router.get("/view", discountController.getAllDiscounts );
// Update a type of product by ID
router.get("/update/:id", discountController.updateDiscountById);
// Delete a type of product by ID
router.get("/delete/:id", discountController.deleteDiscountById);

router.get('/add-page', (req, res) => {
    const successMessage = req.query.success === 'true' ? 'Danh mục nội thất được thêm thành công!' : null;
    const dangerMessage = req.query.success === 'errAdd' ? 'Đã xảy ra lỗi khi thêm danh mục nội thất, có thể dữ liệu này đã có trên hệ thống. Vui lòng kiểm tra lại!' : null; 
    res.render('discounts/addOrEdit_discounts.hbs', {viewTitle: "Thêm mã giảm giá", successMessage, dangerMessage });
});

router.get("/search", discountController.searchDiscount );

///--------------android-------------
router.get('/view-app', discountController.getAllDiscountForApp);
router.post("/check", discountController.checkDiscount );

module.exports = router;
