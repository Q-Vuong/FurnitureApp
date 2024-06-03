const mongoose = require("mongoose");
const adminSchema = new mongoose.Schema({
  username: { type: String, required: true },
  password: { type: String, required: true },
  fullName: { type: String, required: true },
  phoneNumber: { type: String, required: true },
  address: { type: String, required: true },
  gender: { type: String, enum: ["Nam", "Nữ", "Khác"], required: true },
  birthDate: { type: String, required: true },
  avatar: String,
});
const Admin = mongoose.model("Admin", adminSchema);
module.exports = Admin;