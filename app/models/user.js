const mongoose = require('mongoose');
const bcrypt = require('bcrypt-nodejs');

const userSchema = mongoose.Schema({
  username: { type: String, trim: true },
  passwordDigest: String,
  sessionToken: String,
  points: { type: Number, default: 0 },
  badgeIds: { type: [Number], default: [] }
});

userSchema.methods.generateHash = password => bcrypt.hashSync(password, bcrypt.genSaltSync(8), null);

userSchema.methods.validPassword = (password, localPassword) => bcrypt.compareSync(password, localPassword);

export default mongoose.model('User', userSchema);
