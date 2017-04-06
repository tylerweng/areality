import passport from 'passport';
import User from '../models/user';

export const getUsers = (req, res) => {
  User.find({}, (err, users) => {
    if (err) throw err;
    res.json({ users });
  });
};

export const getUser = (req, res) => {
  User.findOne({ username: req.params.username.toLowerCase() }, (err, user) => {
    res.json({ user });
  });
};

export const postUser = (req, res) => {
  res.json(req.user);
};

export const deleteUser = (req, res) => {
  User.findOneAndDelete({ username: req.params.username.toLowerCase() }, (err, user) => {
    res.json({ user });
  });
}

export const patchUser = (req, res) => {

};
