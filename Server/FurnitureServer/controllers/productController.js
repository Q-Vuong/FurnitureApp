const Product = require("../models/product");
const multer = require("multer");

// Thiết lập Multer để xử lý tải tệp
const storage = multer.diskStorage({
  destination: function (req, file, cb) {
    cb(null, "uploads/products/"); // Thư mục lưu trữ tệp
  },
  filename: function (req, file, cb) {
    cb(null, file.originalname); // Tên tệp sau khi lưu trữ
  },
});
const upload = multer({ storage: storage }).single("productImage"); // Đặt tên cho trường hình ảnh là 'bannerImage'

//create or update
exports.createProduct = async (req, res) => {
  try {
    // Gọi hàm upload trước khi kiểm tra req.body.id
    upload(req, res, async function(err) {
      if (err) {
        // Xử lý lỗi nếu có
        return res.status(400).send(err);
      }

      console.log("id: ------ ", req.body.id);

      if (req.body.id == "") {
        // Nếu không có ID được cung cấp, sẽ thêm
        await addRecord(req, res);
      } else {
        console.log("update ", req.body.id);
        await updateRecord(req, res);
      }
    });
  } catch (err) {
    console.error("Error creating/updating type of product:", err);
    return res.status(500).send("Error creating/updating type of product");
  }
};


async function addRecord(req, res) {
  try {
    if (!req.file) {
      // Kiểm tra xem có file được tải lên không
      return res.status(400).send("No file uploaded");
    }

    // Kiểm tra trạng thái của checkbox "Sản phẩm mới"
    const isNew = req.body.isNew ? true : false;

    // Kiểm tra trạng thái của checkbox "Nổi bật"
    const featured = req.body.featured ? true : false;

    // Tạo một thể hiện sản phẩm mới với các trường đã được trích xuất từ req.body
    const newProduct = new Product({
      namePr: req.body.namePr,
      imagePr: "/products/" + req.file.filename,
      featured: featured,
      isNew: isNew,
      price: req.body.price,
      discount: req.body.discount,
      size: req.body.size,
      quantity: req.body.quantity,
      description: req.body.description,
      type: req.body.type,
    });
    
    // Lưu sản phẩm mới vào cơ sở dữ liệu
    await newProduct.save();

    // Chuyển hướng người dùng đến trang thành công hoặc gửi phản hồi thành công
    return res.json({ redirectUrl: "/product/add-page?success=true" });
  } catch (err) {
    // Xử lý lỗi nếu có trong quá trình thực hiện
    console.error("Error creating product:", err);
    return res.json({ redirectUrl: "/product/add-page?success=errAdd" });
  }
}

async function updateRecord(req, res) {
  try {
    let updatedFields = req.body; // Lấy thông tin cập nhật từ req.body
    if (req.file) {
      // Nếu có tệp mới được tải lên, cập nhật đường dẫn ảnh mới
      updatedFields.imagePr = "/products/" + req.file.filename;
    }
    
    // Kiểm tra trạng thái của checkbox "Sản phẩm mới"
    const isNew = req.body.isNew ? true : false;

    // Kiểm tra trạng thái của checkbox "Nổi bật"
    const featured = req.body.featured ? true : false;

    updatedFields.isNew = isNew; // Cập nhật giá trị của checkbox "Sản phẩm mới"
    updatedFields.featured = featured; // Cập nhật giá trị của checkbox "Nổi bật"

    const updatedProduct = await Product.findByIdAndUpdate(req.body.id, updatedFields, { new: true });
    if (!updatedProduct) {
      return res.status(404).send("Type of product not found");
    }
    return res.json({ redirectUrl: "/product/view?success=true" });
  } catch (err) {
    console.error("Error updating type of product:", err);
    const idValue = req.body.id;
    const redirectUrl = `/product/update/${idValue}?success=errUpdate`;
    return res.json({ redirectUrl });
  }
}


// Get all products
exports.getAllProducts = async (req, res) => {
  try {
    // Sử dụng populate để lấy thông tin của ProductType cho mỗi sản phẩm
    const products = await Product.find().populate('type');
    const successMessage =
      req.query.success === "true" ? "Nội thất được sửa thành công!" : null;
    res.render("products/view_product.hbs", {
      products: products,
      successMessage,
    });
  } catch (error) {
    console.error(error);
    res.status(500).send("Internal Server Error");
  }
};


// Update a  product by ID
exports.updateProductById = async (req, res) => {
  try {
    const updatedProduct = await Product.findById(req.params.id).populate('type');;
    const productTypes = await ProductType.find();
    if (!updatedProduct) {
      return res.status(404).send("Type of product not found");
    }
    console.log(updatedProduct);
    const dangerMessage = req.query.success === 'errUpdate' ? 'Đã xảy ra lỗi khi sửa danh mục nội thất, có thể dữ liệu này đã có trên hệ thống. Vui lòng kiểm tra lại!' : null ;
    return res.render("products/addOrEdit_product.hbs", {viewTitle: "Sửa loại nội thất", products: updatedProduct, productTypes: productTypes, dangerMessage });
  } catch (err) {
    console.error("Error updating type of product:", err);
    return res.status(500).send("Error updating type of product");
  }
};


// Delete a product by ID
exports.deleteProductById = async (req, res) => {
  try {
    const productID = await Product.findByIdAndDelete(req.params.id);
    if (!productID) {
      return res.status(404).send();
    } else {
      res.redirect("/product/view");
    }
    // res.send(banner);
  } catch (err) {
    res.status(500).send(err);
  }
};

const ProductType = require("../models/productType"); // Import model ProductType

// Get all product Types
exports.getproductType = async (req, res) => {
  try {
    const productTypes = await ProductType.find(); 
    const successMessage = req.query.success === 'true' ? 'Nội thất được thêm thành công!' : null;
    const dangerMessage = req.query.success === 'errAdd' ? 'Đã xảy ra lỗi khi thêm nội thất, có thể dữ liệu này đã có trên hệ thống. Vui lòng kiểm tra lại!' : null; 
    res.render("products/addOrEdit_product.hbs", {
      productTypes: productTypes, successMessage, dangerMessage// Pass product types to the template
    });
  } catch (error) {
    console.error(error);
    res.status(500).send("Internal Server Error");
  }
};

exports.searchProduct = async (req, res) => {
  const query = req.query.q; // Lấy tham số truy vấn từ yêu cầu

  try {
      // Tìm kiếm sản phẩm dựa trên tên sản phẩm hoặc mô tả
      const products = await Product.find({
          $or: [
              { namePr: { $regex: query, $options: 'i' } }, // Tìm kiếm theo tên sản phẩm không phân biệt chữ hoa chữ thường
              { description: { $regex: query, $options: 'i' } } // Tìm kiếm theo mô tả không phân biệt chữ hoa chữ thường
          ]
      })

      res.render("products/view_product.hbs", {
        products: products,
      });
  } catch (err) {
      console.error(err);
      res.status(500).json({ message: 'Lỗi khi tìm kiếm sản phẩm.' });
  }
};


//------------------------Android-------------------------------//
// Controller để lấy tất cả product dưới dạng JSON
exports.getAllProductsForApp = async (req, res) => {
  try {
    const products = await Product.find();
    res.json([...products]);
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: 'Internal Server Error' });
  }
};

// Controller để lấy danh sách sản phẩm từ một danh sách ID

exports.getProductsByIdsForApp = async (req, res) => {
  const  [...productIds] = req.body; // productIds là một mảng chứa các ID sản phẩm cần lấy

  try {
    console.log( productIds);
    // Kiểm tra nếu productIds không phải là một mảng
    if (!Array.isArray(productIds) || productIds.length === 0) {
      return res.status(400).json({ message: 'Dữ liệu không hợp lệ' });
    }

    const products = await Product.find({ _id: { $in: productIds } });

    if (!products || products.length === 0) {
      return res.status(404).json({ message: 'Không tìm thấy sản phẩm nào' });
    }

    res.json([...products]);
  } catch (error) {
    console.error('Lỗi khi lấy danh sách sản phẩm:', error);
    res.status(500).json({ message: 'Lỗi máy chủ nội bộ' });
  }
};



