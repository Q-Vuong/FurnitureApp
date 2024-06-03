const express = require("express");
const router = express.Router();
const orderController = require("../controllers/orderController");

router.get("/view", orderController.getAllOrders );
router.get("/detail/:id", orderController.getDetailOrderById);
router.get("/search", orderController.searchOrder );

router.put("/confirm/:id", orderController.confirmOrder);

//_________Android____________
router.post("/add", orderController.createOrder);
router.get('/view-app', orderController.getAllOrdersForApp);



module.exports = router;
