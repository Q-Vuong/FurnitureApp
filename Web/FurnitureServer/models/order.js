const mongoose = require("mongoose");

const orderSchema = new mongoose.Schema({
  dateOrder: Date,
  note: String,
  userId: { type: mongoose.Schema.Types.ObjectId, ref: "Customer" },
  products: [{
    productId: { type: mongoose.Schema.Types.ObjectId, ref: "Product" },
    quantity: Number
  }],
  priceToPay: String,
  confirmed: { type: Boolean, default: false }
});

const Order = mongoose.model("Order", orderSchema);

module.exports = Order;