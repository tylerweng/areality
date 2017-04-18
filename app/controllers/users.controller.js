import passport from 'passport';
import User from '../models/user';

export const getUsers = (req, res) => {
  User.find({}, { _id: 0, __v: 0, "landmarks._id": 0 }, (err, users) => {
    if (err) throw err;
    res.json({ users });
  });
};

export const getUser = (req, res) => {
  const username = req.body.username || req.query.username;
  User.findOne(
    { username: username.toLowerCase() },
    { _id: 0, __v: 0, "landmarks._id": 0 },
    (err, user) => {
      res.json({ user });
  });
};

export const deleteUser = (req, res) => {
  const username = req.body.username || req.query.username;
  User.findOneAndDelete({ username: username.toLowerCase() }, (err, user) => {
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
  const landmarkId = req.body.landmarkId || req.query.landmarkId;
  const landmarkLat = req.body.landmarkLat || req.query.landmarkLat;
  const landmarkLon = req.body.landmarkLon || req.query.landmarkLon;
  const landmarkName = req.body.landmarkName || req.query.landmarkName;

  const newLandmark = {
    id: landmarkId,
    lat: landmarkLat,
    lon: landmarkLon,
    name: landmarkName
  };

  User.findOneAndUpdate(
    { username: username.toLowerCase() },
    { $push: { landmarks: newLandmark } },
    { projection: { _id: 0, __v: 0, "landmarks._id": 0 } },
    (err, user) => {
      if (err) res.status(500).send(err);
      if (!user) res.status(401).json({ error: "User not found" });
      user.landmarks = [...user.landmarks, newLandmark];
      res.status(200).json(user);
    }
  );
}
