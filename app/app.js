import express from 'express';
import mongoose from 'mongoose';
import session from 'express-session';
import bodyParser from 'body-parser';
import passport from 'passport';
import routes from './controllers/routes';
import configurePassport from './config/passport';

require('dotenv').config({ silent: true });

const app = express();

console.log("mlab uri:");
console.log(process.env.MLAB_URI);

mongoose.connect(process.env.MLAB_URI, err => {
  if (err) throw err;
  else console.log('Mongoose successfully connected.');

  configurePassport();

  app.use(express.static(__dirname + '/assets'));
  app.use(bodyParser.urlencoded({ extended: true }));
  app.use(session({ secret: process.env.SESSION_SECRET, resave: false, saveUninitialized: false }));
  app.use(passport.initialize());
  app.use(passport.session());

  app.use('/api', routes);

  app.listen(8080, () => {
    console.log("Listening on port 8080...");
  });
});
