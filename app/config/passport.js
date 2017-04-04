import passport from 'passport';
import passportLocal from 'passport-local';
const LocalStrategy = passportLocal.Strategy;
import User from '../models/user';

const configurePassport = () => {
  passport.serializeUser((user, done) => {
    done(null, user.id);
  });

  passport.deserializeUser((id, done) => {
    User.findById(id, (err, user) => {
      done(err, user);
    });
  });

  passport.use('local-register', new LocalStrategy(
    (username, password, done) => {
      User.findOne({ username: username }, (err, user) => {
        if (err) return done(err);
        if (user) return done(null, false);

        const newUser = new User();
        newUser.username = username;
        newUser.passwordDigest = newUser.generateHash(password);

        newUser.save(err => {
          if (err) return done(err);
          return done(null, newUser);
        });
      });
    }
  ));

  passport.use('local-signin', new LocalStrategy(
    (username, password, done) => {
      User.findOne({ username: username }, (err, user) => {
        if (err) return done(err);
        if (!user) return done(null, false);
        if (!user.validPassword(password)) return done(null, false);
        return done(null, user);
      });
    }
  ));
}

export default configurePassport;
