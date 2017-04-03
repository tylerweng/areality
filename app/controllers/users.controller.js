import requireLogin from '../middlewares/auth';
import User from '../models/user';

export const getUsers = (req, res) => {
  res.send("all users go here");
};

export const getUser = (req, res) => {
  console.log(req.params.id);
  res.send("specific user information here");
};

export const postUser = (req, res) => {

};

export const patchUser = (req, res) => {

};
