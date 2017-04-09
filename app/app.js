import express from 'express';
import mongoose from 'mongoose';
import session from 'express-session';
import bodyParser from 'body-parser';
import flash from 'connect-flash';
import passport from 'passport';
import routes from './controllers/routes';
import configurePassport from './config/passport';

require('dotenv').config({ silent: true });

const app = express();

mongoose.connect(process.env.MLAB_URI, err => {
  if (err) throw err;
  else console.log('Mongoose successfully connected.');

  configurePassport();

  app.use(express.static(__dirname + '/assets'));
  app.use(bodyParser.urlencoded({ extended: true }));
  app.use(session({ secret: process.env.SESSION_SECRET, resave: false, saveUninitialized: false }));
  app.use(flash());
  app.use(passport.initialize());
  app.use(passport.session());

  app.use('/api', routes);

  app.listen(process.env.PORT || 8080, () => {
    console.log(`Listening on port ${process.env.PORT || 8080}...`);
  });
});
