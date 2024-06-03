const mongoose = require("mongoose");

const discountSchema = new mongoose.Schema({
  code: {type: String, required: true, unique: true},
  amount: {type: Number, required: true}
});

const Discount = mongoose.model("Discount", discountSchema);

module.exports = Discount;
