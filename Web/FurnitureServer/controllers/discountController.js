const Discount = require("../models/discount");


//create or update
exports.createDiscount = async (req, res) => {
  console.log(req.body);
  console.log(req.body.id);
  try {
    if (req.body.id == "") {
      // Nếu không có ID được cung cấp, sẽ thêm
      addRecord(req, res);    
    } else {
      updateRecord(req, res);
    }
  } catch (err) {
    console.error("Error creating/updating type of product:", err);
    return res.status(500).send("Error creating/updating type of product");
  }
};

async function addRecord(req, res) {
  try {
    const {code, amount} = req.body;
    const newDiscount = new Discount({ code, amount }); 
    // Lưu loại sản phẩm vào cơ sở dữ liệu
    await newDiscount.save(); 
    return res.json({ redirectUrl: "/discount/add-page?success=true" });
  } catch (err) {
    // Xử lý lỗi nếu có bất kỳ lỗi nào xảy ra trong quá trình tạo loại sản phẩm
    console.error("Error creating type of product:", err);
    return res.json({ redirectUrl: "/discount/add-page?success=errAdd" });

  }
}

async function updateRecord(req, res) {
  try {
    const updatedDiscount = await Discount.findByIdAndUpdate(req.body.id, req.body, { new: true });
    if (!updatedDiscount) {
      return res.status(404).send("Type of product not found");
    }
    return res.json({ redirectUrl: "/discount/view?success=true" });
  } catch (err) {
    console.error("Error updating type of product:", err);
    const idValue = req.body.id;
    const redirectUrl = `/discount/update/${idValue}?success=errUpdate`;
    return res.json({ redirectUrl });

  }
};


// Update a type of product by ID
exports.updateDiscountById = async (req, res) => {
  try {
    const updatedDiscount = await Discount.findById(req.params.id);
    if (!updatedDiscount) {
      return res.status(404).send("Type of product not found");
    }
    console.log(updatedDiscount);
    const dangerMessage = req.query.success === 'errUpdate' ? 'Đã xảy ra lỗi khi sửa mã giảm giá, có thể dữ liệu này đã có trên hệ thống. Vui lòng kiểm tra lại!' : null ;
    return res.render("discounts/addOrEdit_discounts.hbs", {viewTitle: "Sửa mã giảm giá", discount: updatedDiscount, dangerMessage });
  } catch (err) {
    console.error("Error updating type of product:", err);
    return res.status(500).send("Error updating type of product");
  }
};

// Get all types of products
exports.getAllDiscounts = async (req, res) => {
  try {
    const discounts = await Discount.find();
    const successMessage = req.query.success === 'true' ? ' Mã giảm giá được sửa thành công!' : null;
    res.render("discounts/view_discounts.hbs", { discounts: discounts, successMessage });
  } catch (error) {
    console.error(error);
    res.status(500).send("Internal Server Error");
  }
};

// Delete a type of product by ID
exports.deleteDiscountById = async (req, res) => {
  try {
    const discounts = await Discount.findByIdAndDelete(req.params.id);
    if (!discounts) {
      return res.status(404).send();
    } else {
      res.redirect("/discount/view");
    }
    // res.send(banner);
  } catch (err) {
    res.status(500).send(err);
  }
};

exports.searchDiscount = async (req, res) => {
  const query = req.query.q; // Lấy tham số truy vấn từ yêu cầu

  try {
    let discounts;

    // Kiểm tra xem query có thể chuyển đổi thành số không
    const parsedQuery = parseFloat(query);
    if (!isNaN(parsedQuery)) {
      // Nếu query là một số hợp lệ, thực hiện truy vấn với amount là số
      discounts = await Discount.find({ amount: parsedQuery });
    } else {
      // Nếu query không phải là số, tìm kiếm theo code (chuỗi)
      discounts = await Discount.find({ code: { $regex: query, $options: 'i' } });
    }

    res.render("discounts/view_discounts.hbs", { discounts: discounts });
  } catch (err) {
    console.error(err);
    res.status(500).json({ message: 'Lỗi khi tìm kiếm sản phẩm.' });
  }
};


//__________________________android__________________
exports.getAllDiscountForApp = async (req, res) => {
  try {
    const discounts = await Discount.find();
    res.json([...discounts]);
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: 'Internal Server Error' });
  }
};

exports.checkDiscount = async (req, res) => {

  try {
    const { code } = req.body;
      //Kiểm tra xem có tồn tại max code hay không
    const existingCode = await Discount.findOne({ code });
    if (!existingCode) {
        return res
          .status(400)
          .json({ message: "Mã giảm giá không có trong hệ thống." });
      }
    return res.status(200).json({
        message: "Đã áp dụng mã giảm giá",
        thisDiscount: existingCode
    });
      
  } catch (err) {
      console.error(err);
      res.status(500).json({ message: 'Lỗi khi xác thực mã giảm giá.' });
  }
};

