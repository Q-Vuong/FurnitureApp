const Customer = require("../models/customer");
const bcrypt = require("bcrypt");
const mongoose = require('mongoose');
const ObjectId = mongoose.Types.ObjectId;

exports.getAllCustomers = async (req, res) => {
  try {
    const Customers = await Customer.find();
    res.render("customers/view_customer.hbs", { Customers: Customers });
  } catch (error) {
    console.error(error);
    res.status(500).send("Internal Server Error");
  }
};

exports.deleteCustomerById = async (req, res) => {
  try {
    const Customers = await Customer.findByIdAndDelete(req.params.id);
    if (!Customers) {
      return res.status(404).send();
    } else {
      res.redirect("/customer/view");
    }
    // res.send(banner);
  } catch (err) {
    res.status(500).send(err);
  }
};

exports.searchCustomer = async (req, res) => {
  const query = req.query.q; // Lấy tham số truy vấn từ yêu cầu

  try {
    // Tìm kiếm sản phẩm dựa trên tên sản phẩm hoặc mô tả
    const customers = await Customer.find({
      $or: [
        { fullName: { $regex: query, $options: "i" } },
        { email: { $regex: query, $options: "i" } },
        { phoneNumber: { $regex: query, $options: "i" } },
        { address: { $regex: query, $options: "i" } },
      ],
    });

    res.render("customers/view_customer.hbs", { Customers: customers });
  } catch (err) {
    console.error(err);
    res.status(500).json({ message: "Lỗi khi tìm kiếm sản phẩm." });
  }
};

//----------------Android-----------------

// Hàm xử lý yêu cầu đăng ký tài khoản
exports.registerCustomer = async (req, res) => {
  try {
    // Lấy thông tin từ yêu cầu đăng ký
    const {
      googleId = "",
      email = "",
      password = "",
      fullName = "",
      phoneNumber = "",
      address = "",
      gender = "",
      birthDate = "",
      avatar = "",
    } = req.body;

    // Kiểm tra xem người dùng đã tồn tại trong cơ sở dữ liệu chưa
    const existingCustomer = await Customer.findOne({ email });
    if (existingCustomer) {
      return res.status(400).json({
        message: "Email đã được sử dụng. Vui lòng chọn một email khác.",
      });
    }

    // Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
    const hashedPassword = await bcrypt.hash(password, 10); // Số vòng lặp để tạo salt, thường là 10

    // Tạo một đối tượng khách hàng mới với mật khẩu đã được mã hóa
    const newCustomer = new Customer({
      googleId,
      email,
      password: hashedPassword, // Mật khẩu đã được mã hóa
      fullName,
      phoneNumber,
      address,
      gender,
      birthDate,
      avatar,
      favoriteProducts: [],
      cart: [],
    });

    // Lưu thông tin khách hàng mới vào cơ sở dữ liệu
    await newCustomer.save();

    // Trả về kết quả thành công
    return res.status(200).json({
      message: "Đăng ký tài khoản thành công.",
      thisCustomer: newCustomer,
    });
  } catch (error) {
    // Xử lý lỗi nếu có
    console.error("Error registering customer:", error);
    return res
      .status(500)
      .json({ message: "Đã xảy ra lỗi khi đăng ký tài khoản." });
  }
};

// Hàm xử lý yêu cầu đăng nhập
exports.loginCustomer = async (req, res) => {
  try {
    // Lấy thông tin từ yêu cầu đăng nhập
    const { email, password } = req.body;

    // Tìm người dùng trong cơ sở dữ liệu
    const existingCustomer = await Customer.findOne({ email });
    if (!existingCustomer) {
      return res
        .status(400)
        .json({ message: "Email không tồn tại trong hệ thống." });
    }

    // Xác minh mật khẩu
    const isPasswordValid = await bcrypt.compare(
      password,
      existingCustomer.password
    );
    if (!isPasswordValid) {
      return res.status(400).json({ message: "Mật khẩu không chính xác." });
    }
    console.log("id: ------ ", existingCustomer);
    // Nếu xác minh thành công, có thể tạo và trả về token ở đây nếu cần
    // Trả về phản hồi thành công
    return res.status(200).json({
      message: "Đăng nhập thành công.",
      thisCustomer: existingCustomer,
    });
  } catch (error) {
    // Xử lý lỗi nếu có
    console.error("Error logging in customer:", error);
    return res.status(500).json({ message: "Đã xảy ra lỗi khi đăng nhập." });
  }
};

exports.updateCustomer = async (req, res) => {
  try {
    const updatedCustomer = await Customer.findByIdAndUpdate(
      req.params.id,
      req.body,
      { new: true }
    );
    if (!updatedCustomer) {
      return res.status(404).json({ error: "Customer not found" });
    }
    return res.status(200).json({ message: "Customer updated successfully" });
  } catch (err) {
    console.error("Error updating customer:", err);
    return res.status(500).json({ error: "Internal server error" });
  }
};

exports.updateFavoriteProducts = async (req, res) => {
  try {
    const customerId = req.params.id;
    const productId = req.body.favoriteProducts; // Đây là string ID của sản phẩm
    const extractedString = productId[0];
    console.log("productId", extractedString);

    // Tìm khách hàng theo ID
    const customer = await Customer.findById(customerId);
    if (!customer) {
      return res.status(404).json({ error: "Customer not found" });
    }

    const favoriteProducts = customer.favoriteProducts;

    // Chuyển productId từ string thành ObjectId
    const productObjectId = new ObjectId(extractedString);

    // Kiểm tra xem productObjectId đã tồn tại trong danh sách favoriteProducts của khách hàng hay chưa
    const isProductExists = favoriteProducts.some(
      (product) => product.equals(productObjectId)
    );

    if (isProductExists) {
      // Nếu sản phẩm đã tồn tại, xoá sản phẩm này khỏi danh sách
      await Customer.findByIdAndUpdate(
        customerId,
        { $pull: { favoriteProducts: productObjectId } },
        { new: true }
      );
      console.log("Removed", productId);
      console.log("Current favorite", customer.favoriteProducts);
      return res.status(200).json({ message: "Bỏ yêu thích" });
    } else {
      // Nếu sản phẩm chưa tồn tại, thêm sản phẩm vào danh sách
      await Customer.findByIdAndUpdate(
        customerId,
        { $push: { favoriteProducts: productObjectId } },
        { new: true }
      );
      console.log("Added", productId);
      console.log("Current favorite", customer.favoriteProducts);
      return res.status(200).json({ message: "Thêm vào danh sách yêu thích" });
    }
  } catch (err) {
    console.error("Error updating customer:", err);
    return res.status(500).json({ error: "Internal server error" });
  }
};

exports.getCustomerById = async (req, res) => {
  try {
    const customerId = req.params.id;
    const customer = await Customer.findById(customerId);

    if (!customer) {
      return res.status(404).json({ message: "Customer not found" });
    }

    return res.status(200).json({
      message: "Thành công.",
      thisCustomer: customer,
    });
  } catch (error) {
    console.error("Error fetching customer by ID:", error);
    res.status(500).json({ message: "Internal Server Error" });
  }
};

exports.changePassword = async (req, res) => {
  try {
    const { customerId } = req.params;
    const { currentPassword, newPassword } = req.body;

    // Tìm khách hàng trong cơ sở dữ liệu bằng ID
    const customer = await Customer.findById(customerId);
    if (!customer) {
      return res.status(404).json({ message: "Không tìm thấy khách hàng." });
    }

    // Kiểm tra tính hợp lệ của mật khẩu hiện tại
    const isPasswordValid = await bcrypt.compare(
      currentPassword,
      customer.password
    );
    if (!isPasswordValid) {
      return res
        .status(400)
        .json({ message: "Mật khẩu hiện tại không chính xác." });
    }

    // Mã hóa mật khẩu mới trước khi lưu vào cơ sở dữ liệu
    const hashedNewPassword = await bcrypt.hash(newPassword, 10);

    // Cập nhật mật khẩu mới cho khách hàng
    customer.password = hashedNewPassword;
    await customer.save();

    return res.status(200).json({ message: "Đổi mật khẩu thành công." });
  } catch (error) {
    console.error("Lỗi khi đổi mật khẩu:", error);
    return res.status(500).json({ message: "Đã xảy ra lỗi khi đổi mật khẩu." });
  }
};

exports.forgotPassword = async (req, res) => {
  try {
    const { email, newPassword } = req.body;

    console.log("forgotPassword", newPassword);
    console.log("email", email);

    // Tìm khách hàng trong cơ sở dữ liệu bằng email
    const customer = await Customer.findOne({ email: email });
    if (!customer) {
      return res.status(404).json({ message: "Không tìm thấy khách hàng." });
    }

    // Mã hóa mật khẩu mới trước khi lưu vào cơ sở dữ liệu
    const hashedNewPassword = await bcrypt.hash(newPassword, 10);

    // Cập nhật mật khẩu mới cho khách hàng
    customer.password = hashedNewPassword;
    await customer.save();

    return res.status(200).json({ message: "Đổi mật khẩu thành công." });
  } catch (error) {
    console.error("Lỗi khi đổi mật khẩu:", error);
    return res.status(500).json({ message: "Đã xảy ra lỗi khi đổi mật khẩu." });
  }
};
