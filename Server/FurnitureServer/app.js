// Import các thư viện cần thiết và khai báo server
const express = require("express");
const mongoose = require("mongoose");
const bodyParser = require("body-parser");
const path = require('path');
const flash = require('connect-flash');
const session = require("express-session");
const admin = require("firebase-admin");

const serviceAccount = require("./firebase/serviceAccountKey.json");
//Kết nối Firebase
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});
admin.auth().listUsers(10)
  .then((users) => {
    console.log('Kết nối Firebase Admin SDK thành công.');
  })
  .catch((error) => {
    console.error('Kết nối Firebase Admin SDK thất bại: ', error);
  });

const url = "mongodb+srv://PRO2052:PRO2052-123@cluster.duryqm1.mongodb.net/furniture-db?retryWrites=true&w=majority";
const PORT = 3000;
const app = express();


// Import các router
const discountRouter = require("./routers/discountRouter");
const customerRouter = require("./routers/customerRouter");
const notificationRouter = require("./routers/notificationRouter");
const productRouter = require("./routers/productRouter");
const productTypeRouter = require("./routers/productTypeRouter");
const adminRouter = require("./routers/adminRouter");
const bannerRouter = require("./routers/bannerRouter");
const searchRouter = require("./routers/searchRouter");
const cartRouter = require("./routers/cartRouter");
const orderRouter = require("./routers/orderRouter");
const statisticalRouter = require("./routers/statisticalRouter");
const authRouter = require("./routers/authRouter");


// Middleware và cấu hình
app.use(express.static("public"));
app.use(express.static("uploads"));
app.set('view engine', 'hbs');
app.set('views', path.join(__dirname, 'views'));
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(session({
  secret: "secret",
  resave: true,
  saveUninitialized: true
}));
app.use(flash());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());


// Kết nối MongoDB
mongoose.connect(url, { useUnifiedTopology: true, useNewUrlParser: true });
mongoose.connection.on("connected", () => {
  console.log("Kết nối với MongoDB thành công");
});
mongoose.connection.on("error", (err) => {
  console.error("Kết nối với MongoDB thất bại: ", err);
});




// Khai báo các route
app.use("/admin", adminRouter);
app.use("/discount", discountRouter);
app.use("/customer", customerRouter);
app.use("/notification", notificationRouter);
app.use("/product", productRouter);
app.use("/productType", productTypeRouter);
app.use("/banner", bannerRouter);
app.use("/search", searchRouter);
app.use("/cart", cartRouter);
app.use("/order", orderRouter);
app.use("/statistical", statisticalRouter);
app.use("/auth", authRouter);

// Route định tuyến
app.get("/", function (req, res) {
  const dangerMessage = req.query.danger === 'true' ? 'Sai mật khẩu hoặc tên đăng nhập!' : null;
  res.render("login.hbs", { dangerMessage });
});

app.get("/main", (req, res) => {
  const fullname = req.query.fullname;
  res.render("main.hbs", { fullname: fullname });
});

// Khởi động máy chủ
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});
