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

  passport.use('local-register', new LocalStrategy({
      passReqToCallback: true
    },
    (req, username, password, done) => {
      username = username.toLowerCase();
      User.findOne({ username: username }, (err, user) => {
        if (err) return done(err);
        if (user) return done(null, false, req.flash('error', 'That username is taken'));

        const newUser = new User();
        newUser.username = username;
        newUser.email = req.body.email;
        newUser.passwordDigest = newUser.generateHash(password);

        newUser.save(err => {
          if (err) return done(err);
          return done(null, newUser, req.flash('success', `Welcome, ${newUser.username}!`));
        });
      });
    }
  ));

  passport.use('local-signin', new LocalStrategy(
    (username, password, done) => {
      User.findOne({ username: username }, (err, user) => {
        if (err) return done(err);
        if (!user) return done(null, false, req.flash('error', "That user could not be found"));
        if (!user.validPassword(password, user.passwordDigest)) return done(null, false, req.flash('error', "Incorrect password"));
        return done(null, user);
      });
    }
  ));
}

export default configurePassport;
