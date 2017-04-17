const mongoose = require('mongoose');
const bcrypt = require('bcrypt-nodejs');

const landmarkSchema = mongoose.Schema({
  id: { type: String, trim: true, required: true },
  lat: { type: String, trim: true, required: true },
  lon: { type: String, trim: true, required: true }
});

const userSchema = mongoose.Schema({
  username: { type: String, trim: true, required: true },
  email: { type: String, trim: true, required: true },
  passwordDigest: { type: String, required: true },
  points: { type: Number, default: 0 },
  badgeIds: { type: [Number], default: [] },
  landmarks: [landmarkSchema],
  streak: { type: Number, default: 1 },
  lastLogin: { type: Date, default: Date.now() }
});

userSchema.methods.generateHash = password => bcrypt.hashSync(password, bcrypt.genSaltSync(8), null);

userSchema.methods.validPassword = (password, localPassword) => bcrypt.compareSync(password, localPassword);

export default mongoose.model('User', userSchema);
