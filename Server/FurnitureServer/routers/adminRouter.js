const express = require("express");
const router = express.Router();
const adminController = require("../controllers/adminController");

router.post("/login", adminController.login);

router.post("/changePassword", adminController.changePassword);

router.get("/updateAdmin", adminController.updateAdmin);

router.get("/view", adminController.getAdmin);

module.exports = router;
