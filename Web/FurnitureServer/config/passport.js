// config/passport.js
const passport = require("passport");
const GoogleStrategy = require("passport-google-oauth20").Strategy;
const User = require("../models/user"); // Đường dẫn có thể thay đổi

passport.use(new GoogleStrategy({
  clientID: "YOUR_GOOGLE_CLIENT_ID",
  clientSecret: "YOUR_GOOGLE_CLIENT_SECRET",
  callbackURL: "YOUR_CALLBACK_URL",
},
(accessToken, refreshToken, profile, done) => {
  // Xác thực thành công, kiểm tra xem người dùng đã tồn tại trong cơ sở dữ liệu chưa
  User.findOne({ googleId: profile.id }, (err, user) => {
    if (err) {
      return done(err);
    }

    if (!user) {
      // Nếu người dùng chưa tồn tại, tạo mới người dùng
      const newUser = new User({
        googleId: profile.id,
        email: profile.emails[0].value,
        fullName: profile.displayName,
        // Các trường khác có thể được điền tùy thuộc vào dữ liệu bạn muốn lưu
      });

      newUser.save((saveErr) => {
        if (saveErr) {
          return done(saveErr);
        }
        return done(null, newUser);
      });
    } else {
      // Người dùng đã tồn tại, đăng nhập thành công
      return done(null, user);
    }
  });
}));

// Serialize và Deserialize người dùng để lưu thông tin vào session
passport.serializeUser((user, done) => {
  done(null, user.id);
});

passport.deserializeUser((id, done) => {
  User.findById(id, (err, user) => {
    done(err, user);
  });
});

module.exports = passport;
