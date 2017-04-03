import User from '../models/user';

const loadUser = (req, res, next) => {
  if (req.session && req.session.user) {
    User.findOne({ id: req.params.id }, (err, user) => {
      if (err) throw err;
      if (user) {
        req.user = user;
      } else {
        delete req.user;
        delete req.session.user;
      }

      next();
    });
  } else {
    next();
  }
};

export default loadUser;
