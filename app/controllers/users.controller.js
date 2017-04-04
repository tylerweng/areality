import requireLogin from '../middlewares/auth';
import User from '../models/user';

export const getUsers = (req, res) => {
  console.log("getting all users");
  User.find({}, (err, users) => {
    if (err) throw err;
    res.json({ users: users });
  });
};

export const getUser = (req, res) => {
  Users.findOne({ _id: req.params.id }, (err, user) => {
    res.json({ user: user });
  });
};

export const patchUser = (req, res) => {

};
