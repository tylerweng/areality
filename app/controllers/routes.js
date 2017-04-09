import passport from 'passport';
import express from 'express';
import * as usersController from './users.controller';
import * as badgesController from './badges.controller';
import requireLogin from '../middlewares/require.login';

const router = express.Router();

router.route('/users').get(usersController.getUsers);
router.route('/profile')
  .get(usersController.getUser)
  .delete(usersController.deleteUser);

router.route('/signup').post(passport.authenticate('local-register', {
  successRedirect: '/api/profile',
  failureRedirect: '/api/error',
  failureFlash: true
}));

router.route('/error').get((req, res) => {
  res.json(req.session.flash);
});

router.route('/login').post(passport.authenticate('local-signin', {
  successRedirect: '/api/profile',
  failureRedirect: '/api/error',
  failureFlash: true
}));

export default router;
