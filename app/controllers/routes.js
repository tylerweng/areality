import express from 'express';
import * as usersController from './users.controller';
import * as badgesController from './badges.controller';

const router = express.Router();

router.route('/users').get(usersController.getUsers);
router.route('/user/:id').get(usersController.getUser);
router.route('/user').post(usersController.postUser);
router.route('/user/:id').patch(usersController.patchUser);

export default router;
