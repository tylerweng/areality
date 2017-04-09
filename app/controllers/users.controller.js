import passport from 'passport';
import User from '../models/user';

export const getUsers = (req, res) => {
  User.find({}, (err, users) => {
    if (err) throw err;
    res.json({ users });
  });
};

export const getUser = (req, res) => {
  console.log("req.user: ");
  console.log(req.user);

  User.findOne({ _id: req.user }, (err, user) => {
    console.log("found user: ");
    console.log(user);
    res.json({ user });
  });
};

export const deleteUser = (req, res) => {
  User.findOneAndDelete({ username: req.params.username.toLowerCase() }, (err, user) => {
    res.json({ user });
  });
}

export const patchUser = (req, res) => {

};
