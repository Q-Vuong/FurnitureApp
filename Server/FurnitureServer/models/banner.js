const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const bannerSchema = new Schema({
  bannerLink: { type: String, required: true }
});

const Banner = mongoose.model('Banner', bannerSchema);

module.exports = Banner;
