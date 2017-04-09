import passport from 'passport';
import express from 'express';
import * as usersController from './users.controller';
import * as badgesController from './badges.controller';
import requireLogin from '../middlewares/require.login';

const router = express.Router();

router.route('/users').get(usersController.getUsers);
router.route('/user/:username')
  .get(usersController.getUser)
  .delete(usersController.deleteUser);
router.route('/signup').post(passport.authenticate('local-register'), usersController.postUser);
router.route('/login').post(passport.authenticate('local-signin'), usersController.postUser);

export default router;
