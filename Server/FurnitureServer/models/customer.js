const mongoose = require("mongoose");

const customerSchema = new mongoose.Schema({
  googleId: String, 
  email: String,
  password: String,
  fullName: String,
  phoneNumber: String,
  address: String,
  gender: String,
  birthDate: String,
  avatar: String,
  favoriteProducts: [{ type: mongoose.Schema.Types.ObjectId, ref: 'Product' }],
  cart: [{
    productId: { type: mongoose.Schema.Types.ObjectId, ref: "Product", required: true  },
    quantity: { type: Number, required: true  }
  }]
});

const Customer = mongoose.model("Customer", customerSchema);

module.exports = Customer;
