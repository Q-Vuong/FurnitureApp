const mongoose = require("mongoose");

const productSchema = new mongoose.Schema({
  namePr: {type: String, unique: true},
  imagePr: String,
  featured: Boolean,
  isNew: Boolean,
  price: Number,
  discount: String,
  size: String,
  quantity: Number,
  description: String,
  type: { type: mongoose.Schema.Types.ObjectId, ref: "ProductType" },
});

const Product = mongoose.model("Product", productSchema);

module.exports = Product;
