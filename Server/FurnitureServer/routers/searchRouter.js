const express = require("express");
const router = express.Router();
const searchController = require("../controllers/searchController");

router.get("/infor", searchController.searchProduct );


module.exports = router;
