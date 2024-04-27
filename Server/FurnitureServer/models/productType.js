const mongoose = require("mongoose");

const productTypeSchema = new mongoose.Schema({
  nameType: { type: String, required: true, unique: true },
  imgPr_T: String,
});

const ProductType = mongoose.model("ProductType", productTypeSchema);

module.exports = ProductType;
