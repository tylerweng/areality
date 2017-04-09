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
      const email = req.body.email || req.query.email;

      User.findOne({ $or: [{ username: username }, { email: email }] }, (err, user) => {
        if (err) return done(err);
        if (user) return done(null, false, {
          error: user.username == username ? "That username is taken" : "That email is already registered"
        });

        const newUser = new User();
        newUser.username = username;
        newUser.email = email;
        newUser.passwordDigest = newUser.generateHash(password);

        newUser.save(err => {
          if (err) return done(err);
          return done(null, newUser, { success: `Welcome, ${newUser.username}!` });
        });
      });
    }
  ));

  passport.use('local-signin', new LocalStrategy({
      usernameField: 'email'
    },
    (email, password, done) => {
      User.findOne({ email: email }, (err, user) => {
        if (err) return done(err);
        if (!user) return done(null, false, { error: "That user could not be found" });
        if (!user.validPassword(password, user.passwordDigest)) return done(null, false, { error: "Incorrect password" });
        return done(null, user, { success: `Welcome, ${user.username}!` });
      });
    }
  ));
}

export default configurePassport;
