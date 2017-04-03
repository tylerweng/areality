import express from 'express';
import mongoose from 'mongoose';
import session from 'express-session';
import bodyParser from 'body-parser';

require('dotenv').config({ silent: true });

import routes from './controllers/routes';

const app = express();

mongoose.connect(process.env.MLAB_URI, err => {
  if (err) throw err;
  else console.log('Mongoose successfully connected.');

  app.use(express.static(__dirname + '/assets'));
  app.use(bodyParser.urlencoded({ extended: true }));
  app.use(session({ secret: 'gveikcisdxhbrmyedcazxyxdcrshhnduffu', resave: false, saveUninitialized: false }));

  app.use('/api', routes);

  app.listen(8080, () => {
    console.log("Listening on port 8080...");
  });
});
