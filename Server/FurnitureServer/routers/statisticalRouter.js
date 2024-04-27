const express = require("express");
const router = express.Router();
const statisticalController = require("../controllers/statisticalController");

router.get("/view", statisticalController.getAllProducts );

module.exports = router;