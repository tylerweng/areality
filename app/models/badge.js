const mongoose = require('mongoose');

const badgeSchema = mongoose.Schema({
  name: String
});

export default mongoose.model('Badge', badgeSchema);
