const express = require("express");
const router = express.Router();
const cartController = require("../controllers/cartController");


//------------Android---------
router.put("/updatecart/:id", cartController.updateCart );
router.get("/view/:id", cartController.getCartForCustomer );
router.delete('/customer/:id/cart/:productid', cartController.removeFromCart);





module.exports = router;
