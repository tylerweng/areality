/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId])
/******/ 			return installedModules[moduleId].exports;
/******/
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// identity function for calling harmony imports with the correct context
/******/ 	__webpack_require__.i = function(value) { return value; };
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, {
/******/ 				configurable: false,
/******/ 				enumerable: true,
/******/ 				get: getter
/******/ 			});
/******/ 		}
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = 15);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ (function(module, exports) {

module.exports = require("passport");

/***/ }),
/* 1 */
/***/ (function(module, exports) {

module.exports = require("express");

/***/ }),
/* 2 */
/***/ (function(module, exports) {

module.exports = require("mongoose");

/***/ }),
/* 3 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
var requireLogin = function requireLogin(req, res, next) {
  if (req.user) next();else res.status(401).end();
};

exports.default = requireLogin;

/***/ }),
/* 4 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
var mongoose = __webpack_require__(2);
var bcrypt = __webpack_require__(13);

var userSchema = mongoose.Schema({
  username: { type: String, trim: true, required: true },
  email: { type: String, trim: true, required: true },
  passwordDigest: { type: String, required: true },
  points: { type: Number, default: 0 },
  badgeIds: { type: [Number], default: [] }
});

userSchema.methods.generateHash = function (password) {
  return bcrypt.hashSync(password, bcrypt.genSaltSync(8), null);
};

userSchema.methods.validPassword = function (password, localPassword) {
  return bcrypt.compareSync(password, localPassword);
};

exports.default = mongoose.model('User', userSchema);

/***/ }),
/* 5 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _passport = __webpack_require__(0);

var _passport2 = _interopRequireDefault(_passport);

var _passportLocal = __webpack_require__(14);

var _passportLocal2 = _interopRequireDefault(_passportLocal);

var _user = __webpack_require__(4);

var _user2 = _interopRequireDefault(_user);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var LocalStrategy = _passportLocal2.default.Strategy;


var configurePassport = function configurePassport() {
  _passport2.default.serializeUser(function (user, done) {
    done(null, user.id);
  });

  _passport2.default.deserializeUser(function (id, done) {
    _user2.default.findById(id, function (err, user) {
      done(err, user);
    });
  });

  _passport2.default.use('local-register', new LocalStrategy({
    passReqToCallback: true
  }, function (req, username, password, done) {
    username = username.toLowerCase();
    var email = req.body.email || req.query.email;

    _user2.default.findOne({ $or: [{ username: username }, { email: email }] }, function (err, user) {
      if (err) return done(err);
      if (user) return done(null, false, {
        error: user.username == username ? "That username is taken" : "That email is already registered"
      });

      var newUser = new _user2.default();
      newUser.username = username;
      newUser.email = email;
      newUser.passwordDigest = newUser.generateHash(password);

      newUser.save(function (err) {
        if (err) return done(err);
        return done(null, newUser, { success: 'Welcome, ' + newUser.username + '!' });
      });
    });
  }));

  _passport2.default.use('local-signin', new LocalStrategy({
    usernameField: 'email',
    passReqToCallback: true
  }, function (req, email, password, done) {
    email = req.body.email || req.query.email;
    console.log("email: ");
    console.log(email);

    _user2.default.findOne({ email: email }, function (err, user) {
      if (err) return done(err);
      if (!user) return done(null, false, { error: "That user could not be found" });
      if (!user.validPassword(password, user.passwordDigest)) return done(null, false, { error: "Incorrect password" });
      return done(null, user, { success: 'Welcome, ' + user.username + '!' });
    });
  }));
};

exports.default = configurePassport;

/***/ }),
/* 6 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _passport = __webpack_require__(0);

var _passport2 = _interopRequireDefault(_passport);

var _express = __webpack_require__(1);

var _express2 = _interopRequireDefault(_express);

var _users = __webpack_require__(12);

var usersController = _interopRequireWildcard(_users);

var _badges = __webpack_require__(11);

var badgesController = _interopRequireWildcard(_badges);

var _require = __webpack_require__(3);

var _require2 = _interopRequireDefault(_require);

function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj.default = obj; return newObj; } }

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var router = _express2.default.Router();

router.route('/users').get(usersController.getUsers);
router.route('/profile').get(usersController.getUser).delete(usersController.deleteUser);

router.route('/signup').post(function (req, res, next) {
  _passport2.default.authenticate('local-register', function (err, user, info) {
    if (err) {
      res.status(500).send(err);
    } else if (!user) {
      res.status(401).json(info);
    } else {
      res.status(200).json(user);
    }
  })(req, res, next);
});

router.route('/error').get(function (req, res) {
  res.json(req.session.flash);
});

// router.route('/login').post(passport.authenticate('local-signin', {
//   successRedirect: '/api/profile',
//   failureRedirect: '/api/error',
//   failureFlash: true
// }));

router.route('/login').post(function (req, res, next) {
  _passport2.default.authenticate('local-signin', function (err, user, info) {
    if (err) {
      res.status(500).send(err);
    } else if (!user) {
      res.status(401).json(info);
    } else {
      res.status(200).json(user);
    }
  })(req, res, next);
});

function logIn(req, res, next) {
  if (req.isAuthenticated()) {
    console.log("in middleware, req.user: ");
    console.log(req.user);

    res.user = req.user;
    return next();
  }
  console.log("was not authenticated");
  console.log("req.user: ");
  console.log(req.user);
  return next();
}

exports.default = router;

/***/ }),
/* 7 */
/***/ (function(module, exports) {

module.exports = require("body-parser");

/***/ }),
/* 8 */
/***/ (function(module, exports) {

module.exports = require("connect-flash");

/***/ }),
/* 9 */
/***/ (function(module, exports) {

module.exports = require("dotenv");

/***/ }),
/* 10 */
/***/ (function(module, exports) {

module.exports = require("express-session");

/***/ }),
/* 11 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _require = __webpack_require__(3);

var _require2 = _interopRequireDefault(_require);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var router = function router(app) {
  app.post('/', _require2.default, function (req, res) {
    console.log("creating new badge");
  });
};

exports.default = router;

/***/ }),
/* 12 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.patchUser = exports.deleteUser = exports.getUser = exports.getUsers = undefined;

var _passport = __webpack_require__(0);

var _passport2 = _interopRequireDefault(_passport);

var _user = __webpack_require__(4);

var _user2 = _interopRequireDefault(_user);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var getUsers = exports.getUsers = function getUsers(req, res) {
  _user2.default.find({}, function (err, users) {
    if (err) throw err;
    res.json({ users: users });
  });
};

var getUser = exports.getUser = function getUser(req, res) {
  _user2.default.findOne({ username: req.username }, function (err, user) {
    res.json({ user: user });
  });
};

var deleteUser = exports.deleteUser = function deleteUser(req, res) {
  _user2.default.findOneAndDelete({ username: req.params.username.toLowerCase() }, function (err, user) {
    res.json({ user: user });
  });
};

var patchUser = exports.patchUser = function patchUser(req, res) {};

/***/ }),
/* 13 */
/***/ (function(module, exports) {

module.exports = require("bcrypt-nodejs");

/***/ }),
/* 14 */
/***/ (function(module, exports) {

module.exports = require("passport-local");

/***/ }),
/* 15 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/* WEBPACK VAR INJECTION */(function(__dirname) {

var _express = __webpack_require__(1);

var _express2 = _interopRequireDefault(_express);

var _mongoose = __webpack_require__(2);

var _mongoose2 = _interopRequireDefault(_mongoose);

var _expressSession = __webpack_require__(10);

var _expressSession2 = _interopRequireDefault(_expressSession);

var _bodyParser = __webpack_require__(7);

var _bodyParser2 = _interopRequireDefault(_bodyParser);

var _cookieParser = __webpack_require__(16);

var _cookieParser2 = _interopRequireDefault(_cookieParser);

var _connectFlash = __webpack_require__(8);

var _connectFlash2 = _interopRequireDefault(_connectFlash);

var _passport = __webpack_require__(0);

var _passport2 = _interopRequireDefault(_passport);

var _routes = __webpack_require__(6);

var _routes2 = _interopRequireDefault(_routes);

var _passport3 = __webpack_require__(5);

var _passport4 = _interopRequireDefault(_passport3);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

__webpack_require__(9).config({ silent: true });

var app = (0, _express2.default)();

_mongoose2.default.connect(process.env.MLAB_URI, function (err) {
  if (err) throw err;else console.log('Mongoose successfully connected.');

  (0, _passport4.default)();

  app.use(_express2.default.static(__dirname + '/assets'));
  app.use((0, _cookieParser2.default)(process.env.SESSION_SECRET));
  app.use(_bodyParser2.default.urlencoded({ extended: true }));
  app.use((0, _expressSession2.default)({ secret: process.env.SESSION_SECRET, resave: false, saveUninitialized: false }));
  app.use((0, _connectFlash2.default)());
  app.use(_passport2.default.initialize());
  app.use(_passport2.default.session());

  app.use('/api', _routes2.default);

  app.listen(process.env.PORT || 8080, function () {
    console.log('Listening on port ' + (process.env.PORT || 8080) + '...');
  });
});
/* WEBPACK VAR INJECTION */}.call(exports, "/"))

/***/ }),
/* 16 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*!
 * cookie-parser
 * Copyright(c) 2014 TJ Holowaychuk
 * Copyright(c) 2015 Douglas Christopher Wilson
 * MIT Licensed
 */



/**
 * Module dependencies.
 * @private
 */

var cookie = __webpack_require__(17);
var signature = __webpack_require__(18);

/**
 * Module exports.
 * @public
 */

module.exports = cookieParser;
module.exports.JSONCookie = JSONCookie;
module.exports.JSONCookies = JSONCookies;
module.exports.signedCookie = signedCookie;
module.exports.signedCookies = signedCookies;

/**
 * Parse Cookie header and populate `req.cookies`
 * with an object keyed by the cookie names.
 *
 * @param {string|array} [secret] A string (or array of strings) representing cookie signing secret(s).
 * @param {Object} [options]
 * @return {Function}
 * @public
 */

function cookieParser(secret, options) {
  return function cookieParser(req, res, next) {
    if (req.cookies) {
      return next();
    }

    var cookies = req.headers.cookie;
    var secrets = !secret || Array.isArray(secret)
      ? (secret || [])
      : [secret];

    req.secret = secrets[0];
    req.cookies = Object.create(null);
    req.signedCookies = Object.create(null);

    // no cookies
    if (!cookies) {
      return next();
    }

    req.cookies = cookie.parse(cookies, options);

    // parse signed cookies
    if (secrets.length !== 0) {
      req.signedCookies = signedCookies(req.cookies, secrets);
      req.signedCookies = JSONCookies(req.signedCookies);
    }

    // parse JSON cookies
    req.cookies = JSONCookies(req.cookies);

    next();
  };
}

/**
 * Parse JSON cookie string.
 *
 * @param {String} str
 * @return {Object} Parsed object or undefined if not json cookie
 * @public
 */

function JSONCookie(str) {
  if (typeof str !== 'string' || str.substr(0, 2) !== 'j:') {
    return undefined;
  }

  try {
    return JSON.parse(str.slice(2));
  } catch (err) {
    return undefined;
  }
}

/**
 * Parse JSON cookies.
 *
 * @param {Object} obj
 * @return {Object}
 * @public
 */

function JSONCookies(obj) {
  var cookies = Object.keys(obj);
  var key;
  var val;

  for (var i = 0; i < cookies.length; i++) {
    key = cookies[i];
    val = JSONCookie(obj[key]);

    if (val) {
      obj[key] = val;
    }
  }

  return obj;
}

/**
 * Parse a signed cookie string, return the decoded value.
 *
 * @param {String} str signed cookie string
 * @param {string|array} secret
 * @return {String} decoded value
 * @public
 */

function signedCookie(str, secret) {
  if (typeof str !== 'string') {
    return undefined;
  }

  if (str.substr(0, 2) !== 's:') {
    return str;
  }

  var secrets = !secret || Array.isArray(secret)
    ? (secret || [])
    : [secret];

  for (var i = 0; i < secrets.length; i++) {
    var val = signature.unsign(str.slice(2), secrets[i]);

    if (val !== false) {
      return val;
    }
  }

  return false;
}

/**
 * Parse signed cookies, returning an object containing the decoded key/value
 * pairs, while removing the signed key from obj.
 *
 * @param {Object} obj
 * @param {string|array} secret
 * @return {Object}
 * @public
 */

function signedCookies(obj, secret) {
  var cookies = Object.keys(obj);
  var dec;
  var key;
  var ret = Object.create(null);
  var val;

  for (var i = 0; i < cookies.length; i++) {
    key = cookies[i];
    val = obj[key];
    dec = signedCookie(val, secret);

    if (val !== dec) {
      ret[key] = dec;
      delete obj[key];
    }
  }

  return ret;
}


/***/ }),
/* 17 */
/***/ (function(module, exports) {

module.exports = require("cookie");

/***/ }),
/* 18 */
/***/ (function(module, exports) {

module.exports = require("cookie-signature");

/***/ })
/******/ ]);
//# sourceMappingURL=bundle.js.map