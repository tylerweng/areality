import passport from 'passport';
import User from '../models/user';

export const getUsers = (req, res) => {
  User.find({}, (err, users) => {
    if (err) throw err;
    res.json({ users });
  });
};

export const getUser = (req, res) => {
  User.findOne({ username: req.username }, (err, user) => {
    res.json({ user });
  });
};

export const deleteUser = (req, res) => {
  User.findOneAndDelete({ username: req.params.username.toLowerCase() }, (err, user) => {
    res.json({ user });
  });
}

export const addCoins = (req, res) => {
  const username = req.body.username || req.query.username;
  User.findOneAndUpdate(
    { username: username.toLowerCase() },
    { $inc: { "points": req.body.points || req.query.points } },
    { returnNewDocument: true },
    (err, user) => {
      if (err) res.status(500).send(err);
      if (!user) res.status(401).json({ error: "User not found" });
      res.status(200).json(user);
    }
  );
};

export const addLandmark = (req, res) => {
  const username = req.body.username || req.query.username;
  const landmark = req.body.landmark || req.query.landmark;

  User.findOneAndUpdate(
    { username: username.toLowerCase() },
    { $push: { landmarkIds: parseInt(landmark) } },
    (err, user) => {
      if (err) res.status(500).send(err);
      if (!user) res.status(401).json({ error: "User not found" });
      user.landmarkIds = [...user.landmarkIds, parseInt(landmark)];
      res.status(200).json(user);
    }
  );
}
