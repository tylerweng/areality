import requireLogin from '../middlewares/auth';
import User from '../models/user';

export const getUsers = (req, res) => {
  Users.find({}, (err, users) => {
    if (err) throw err;
    res.json({ users: users });
  });
};

export const getUser = (req, res) => {
  Users.findOne({ _id: req.params.id }, (err, user) => {
    res.json({ user: user });
  });
};

export const postUser = (req, res) => {
  console.log(req);
  res.json({ user: "post request made" });
};

// export const patchUser = (req, res) => {
//
// };

// seed users
// const users = [
//   { username: 'Magnus', passwordDigest: User.generateHash('wordpass') },
//   { username: 'Wanda', passwordDigest: User.generateHash('wordpass') },
//   { username: 'Nemo', passwordDigest: User.generateHash('wordpass') }
// ];
//
// const seedUsers = (req, res) => {
//   users.forEach(user => {
//     const newUser = new User(user);
//     newUser.save();
//   });
// };
