const express = require("express");
const router = express.Router();
const customerController = require("../controllers/customerController");

// Get all types of products
router.get("/view", customerController.getAllCustomers );

router.get("/delete/:id", customerController.deleteCustomerById );
router.get("/search", customerController.searchCustomer );

//------------Android---------
router.post("/register", customerController.registerCustomer );
router.post("/login", customerController.loginCustomer );
router.put("/update/:id", customerController.updateCustomer );
router.put("/updatefavoriteproduct/:id", customerController.updateFavoriteProducts );
router.get("/getbyid/:id", customerController.getCustomerById );
router.post("/changePassword/:customerId", customerController.changePassword);
router.post("/forgotPassword", customerController.forgotPassword);




module.exports = router;
