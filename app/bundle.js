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
    _user2.default.findOne({ username: username }, function (err, user) {
      if (err) return done(err);
      if (user) return done(null, false, req.flash('error', 'That username is taken'));

      var newUser = new _user2.default();
      newUser.username = username;
      newUser.email = req.body.email;
      newUser.passwordDigest = newUser.generateHash(password);

      newUser.save(function (err) {
        if (err) return done(err);
        console.log("new user: ");
        console.log(newUser);
        return done(null, newUser, req.flash('success', 'Welcome, ' + newUser.username + '!'));
      });
    });
  }));

  _passport2.default.use('local-signin', new LocalStrategy(function (username, password, done) {
    _user2.default.findOne({ username: username }, function (err, user) {
      if (err) return done(err);
      if (!user) return done(null, false, req.flash('error', "That user could not be found"));
      if (!user.validPassword(password, user.passwordDigest)) return done(null, false, req.flash('error', "Incorrect password"));
      return done(null, user);
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
router.route('/profile').get(logIn, usersController.getUser).delete(usersController.deleteUser);

router.route('/signup').post(_passport2.default.authenticate('local-register', {
  successRedirect: '/api/profile',
  failureRedirect: '/api/error',
  failureFlash: true
}));

router.route('/error').get(function (req, res) {
  res.json(req.session.flash);
});

router.route('/login').post(_passport2.default.authenticate('local-signin', {
  successRedirect: '/api/profile',
  failureRedirect: '/api/error',
  failureFlash: true
}));

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
/* 10 */,
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
  console.log("req.user: ");
  // console.log(Object.keys(req.sessionStore.sessions[Object.keys(req.sessionStore.sessions)[0]]));
  console.log(req.user);
  console.log("res.user: ");
  console.log(res.user);

  _user2.default.findOne({ _id: req.user }, function (err, user) {
    console.log("found user: ");
    console.log(user);
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

var _cookieSession = __webpack_require__(19);

var _cookieSession2 = _interopRequireDefault(_cookieSession);

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
  app.use((0, _cookieSession2.default)({ secret: process.env.SESSION_SECRET, resave: false, saveUninitialized: false }));
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

/***/ }),
/* 19 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*!
 * cookie-session
 * Copyright(c) 2013 Jonathan Ong
 * Copyright(c) 2014-2017 Douglas Christopher Wilson
 * MIT Licensed
 */



/**
 * Module dependencies.
 * @private
 */

var debug = __webpack_require__(23)('cookie-session')
var Cookies = __webpack_require__(20)
var onHeaders = __webpack_require__(26)

/**
 * Module exports.
 * @public
 */

module.exports = cookieSession

/**
 * Create a new cookie session middleware.
 *
 * @param {object} [options]
 * @param {boolean} [options.httpOnly=true]
 * @param {array} [options.keys]
 * @param {string} [options.name=session] Name of the cookie to use
 * @param {boolean} [options.overwrite=true]
 * @param {string} [options.secret]
 * @param {boolean} [options.signed=true]
 * @return {function} middleware
 * @public
 */

function cookieSession (options) {
  var opts = options || {}

  // cookie name
  var name = opts.name || 'session'

  // secrets
  var keys = opts.keys
  if (!keys && opts.secret) keys = [opts.secret]

  // defaults
  if (opts.overwrite == null) opts.overwrite = true
  if (opts.httpOnly == null) opts.httpOnly = true
  if (opts.signed == null) opts.signed = true

  if (!keys && opts.signed) throw new Error('.keys required.')

  debug('session options %j', opts)

  return function _cookieSession (req, res, next) {
    var cookies = new Cookies(req, res, {
      keys: keys
    })
    var sess

    // to pass to Session()
    req.sessionCookies = cookies
    req.sessionOptions = Object.create(opts)
    req.sessionKey = name

    // define req.session getter / setter
    Object.defineProperty(req, 'session', {
      configurable: true,
      enumerable: true,
      get: getSession,
      set: setSession
    })

    function getSession () {
      // already retrieved
      if (sess) {
        return sess
      }

      // unset
      if (sess === false) {
        return null
      }

      // get or create session
      return (sess = tryGetSession(req) || createSession(req))
    }

    function setSession (val) {
      if (val == null) {
        // unset session
        sess = false
        return val
      }

      if (typeof val === 'object') {
        // create a new session
        sess = Session.create(this, val)
        return sess
      }

      throw new Error('req.session can only be set as null or an object.')
    }

    onHeaders(res, function setHeaders () {
      if (sess === undefined) {
        // not accessed
        return
      }

      try {
        if (sess === false) {
          // remove
          cookies.set(name, '', req.sessionOptions)
        } else if ((!sess.isNew || sess.isPopulated) && sess.isChanged) {
          // save populated or non-new changed session
          sess.save()
        }
      } catch (e) {
        debug('error saving session %s', e.message)
      }
    })

    next()
  }
};

/**
 * Session model.
 *
 * @param {Context} ctx
 * @param {Object} obj
 * @private
 */

function Session (ctx, obj) {
  Object.defineProperty(this, '_ctx', {
    value: ctx
  })

  if (obj) {
    for (var key in obj) {
      this[key] = obj[key]
    }
  }
}

/**
 * Create new session.
 * @private
 */

Session.create = function create (req, obj) {
  var ctx = new SessionContext(req)
  return new Session(ctx, obj)
}

/**
 * Create session from serialized form.
 * @private
 */

Session.deserialize = function deserialize (req, str) {
  var ctx = new SessionContext(req)
  var obj = decode(str)

  ctx._new = false
  ctx._val = str

  return new Session(ctx, obj)
}

/**
 * Serialize a session to a string.
 * @private
 */

Session.serialize = function serialize (sess) {
  return encode(sess)
}

/**
 * Return if the session is changed for this request.
 *
 * @return {Boolean}
 * @public
 */

Object.defineProperty(Session.prototype, 'isChanged', {
  get: function getIsChanged () {
    return this._ctx._new || this._ctx._val !== Session.serialize(this)
  }
})

/**
 * Return if the session is new for this request.
 *
 * @return {Boolean}
 * @public
 */

Object.defineProperty(Session.prototype, 'isNew', {
  get: function getIsNew () {
    return this._ctx._new
  }
})

/**
 * Return how many values there are in the session object.
 * Used to see if it's "populated".
 *
 * @return {Number}
 * @public
 */

Object.defineProperty(Session.prototype, 'length', {
  get: function getLength () {
    return Object.keys(this).length
  }
})

/**
 * populated flag, which is just a boolean alias of .length.
 *
 * @return {Boolean}
 * @public
 */

Object.defineProperty(Session.prototype, 'isPopulated', {
  get: function getIsPopulated () {
    return Boolean(this.length)
  }
})

/**
 * Save session changes by performing a Set-Cookie.
 * @private
 */

Session.prototype.save = function save () {
  var ctx = this._ctx
  var val = Session.serialize(this)

  var cookies = ctx.req.sessionCookies
  var name = ctx.req.sessionKey
  var opts = ctx.req.sessionOptions

  debug('save %s', val)
  cookies.set(name, val, opts)
}

/**
 * Session context to tie session to req.
 *
 * @param {Request} req
 * @private
 */

function SessionContext (req) {
  this.req = req

  this._new = true
  this._val = undefined
}

/**
 * Create a new session.
 * @private
 */

function createSession (req) {
  debug('new session')
  return Session.create(req)
}

/**
 * Decode the base64 cookie value to an object.
 *
 * @param {String} string
 * @return {Object}
 * @private
 */

function decode (string) {
  var body = new Buffer(string, 'base64').toString('utf8')
  return JSON.parse(body)
}

/**
 * Encode an object into a base64-encoded JSON string.
 *
 * @param {Object} body
 * @return {String}
 * @private
 */

function encode (body) {
  var str = JSON.stringify(body)
  return new Buffer(str).toString('base64')
}

/**
 * Try getting a session from a request.
 * @private
 */

function tryGetSession (req) {
  var cookies = req.sessionCookies
  var name = req.sessionKey
  var opts = req.sessionOptions

  var str = cookies.get(name, opts)

  if (!str) {
    return undefined
  }

  debug('parse %s', str)

  try {
    return Session.deserialize(req, str)
  } catch (err) {
    if (!(err instanceof SyntaxError)) throw err
    return undefined
  }
}


/***/ }),
/* 20 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*!
 * cookies
 * Copyright(c) 2014 Jed Schmidt, http://jed.is/
 * Copyright(c) 2015-2016 Douglas Christopher Wilson
 * MIT Licensed
 */



var deprecate = __webpack_require__(24)('cookies')
var Keygrip = __webpack_require__(21)
var http = __webpack_require__(25)
var cache = {}

/**
 * RegExp to match field-content in RFC 7230 sec 3.2
 *
 * field-content = field-vchar [ 1*( SP / HTAB ) field-vchar ]
 * field-vchar   = VCHAR / obs-text
 * obs-text      = %x80-FF
 */

var fieldContentRegExp = /^[\u0009\u0020-\u007e\u0080-\u00ff]+$/;

/**
 * RegExp to match Same-Site cookie attribute value.
 */

var sameSiteRegExp = /^(?:lax|strict)$/i

function Cookies(request, response, options) {
  if (!(this instanceof Cookies)) return new Cookies(request, response, options)

  this.secure = undefined
  this.request = request
  this.response = response

  if (options) {
    if (Array.isArray(options)) {
      // array of key strings
      deprecate('"keys" argument; provide using options {"keys": [...]}')
      this.keys = new Keygrip(options)
    } else if (options.constructor && options.constructor.name === 'Keygrip') {
      // any keygrip constructor to allow different versions
      deprecate('"keys" argument; provide using options {"keys": keygrip}')
      this.keys = options
    } else {
      this.keys = Array.isArray(options.keys) ? new Keygrip(options.keys) : options.keys
      this.secure = options.secure
    }
  }
}

Cookies.prototype.get = function(name, opts) {
  var sigName = name + ".sig"
    , header, match, value, remote, data, index
    , signed = opts && opts.signed !== undefined ? opts.signed : !!this.keys

  header = this.request.headers["cookie"]
  if (!header) return

  match = header.match(getPattern(name))
  if (!match) return

  value = match[1]
  if (!opts || !signed) return value

  remote = this.get(sigName)
  if (!remote) return

  data = name + "=" + value
  if (!this.keys) throw new Error('.keys required for signed cookies');
  index = this.keys.index(data, remote)

  if (index < 0) {
    this.set(sigName, null, {path: "/", signed: false })
  } else {
    index && this.set(sigName, this.keys.sign(data), { signed: false })
    return value
  }
};

Cookies.prototype.set = function(name, value, opts) {
  var res = this.response
    , req = this.request
    , headers = res.getHeader("Set-Cookie") || []
    , secure = this.secure !== undefined ? !!this.secure : req.protocol === 'https' || req.connection.encrypted
    , cookie = new Cookie(name, value, opts)
    , signed = opts && opts.signed !== undefined ? opts.signed : !!this.keys

  if (typeof headers == "string") headers = [headers]

  if (!secure && opts && opts.secure) {
    throw new Error('Cannot send secure cookie over unencrypted connection')
  }

  cookie.secure = secure
  if (opts && "secure" in opts) cookie.secure = opts.secure

  if (opts && "secureProxy" in opts) {
    deprecate('"secureProxy" option; use "secure" option, provide "secure" to constructor if needed')
    cookie.secure = opts.secureProxy
  }

  headers = pushCookie(headers, cookie)

  if (opts && signed) {
    if (!this.keys) throw new Error('.keys required for signed cookies');
    cookie.value = this.keys.sign(cookie.toString())
    cookie.name += ".sig"
    headers = pushCookie(headers, cookie)
  }

  var setHeader = res.set ? http.OutgoingMessage.prototype.setHeader : res.setHeader
  setHeader.call(res, 'Set-Cookie', headers)
  return this
};

function Cookie(name, value, attrs) {
  if (!fieldContentRegExp.test(name)) {
    throw new TypeError('argument name is invalid');
  }

  if (value && !fieldContentRegExp.test(value)) {
    throw new TypeError('argument value is invalid');
  }

  value || (this.expires = new Date(0))

  this.name = name
  this.value = value || ""

  for (var name in attrs) {
    this[name] = attrs[name]
  }

  if (this.path && !fieldContentRegExp.test(this.path)) {
    throw new TypeError('option path is invalid');
  }

  if (this.domain && !fieldContentRegExp.test(this.domain)) {
    throw new TypeError('option domain is invalid');
  }

  if (this.sameSite && this.sameSite !== true && !sameSiteRegExp.test(this.sameSite)) {
    throw new TypeError('option sameSite is invalid')
  }
}

Cookie.prototype.path = "/";
Cookie.prototype.expires = undefined;
Cookie.prototype.domain = undefined;
Cookie.prototype.httpOnly = true;
Cookie.prototype.sameSite = false;
Cookie.prototype.secure = false;
Cookie.prototype.overwrite = false;

Cookie.prototype.toString = function() {
  return this.name + "=" + this.value
};

Cookie.prototype.toHeader = function() {
  var header = this.toString()

  if (this.maxAge) this.expires = new Date(Date.now() + this.maxAge);

  if (this.path     ) header += "; path=" + this.path
  if (this.expires  ) header += "; expires=" + this.expires.toUTCString()
  if (this.domain   ) header += "; domain=" + this.domain
  if (this.sameSite ) header += "; samesite=" + (this.sameSite === true ? 'strict' : this.sameSite.toLowerCase())
  if (this.secure   ) header += "; secure"
  if (this.httpOnly ) header += "; httponly"

  return header
};

// back-compat so maxage mirrors maxAge
Object.defineProperty(Cookie.prototype, 'maxage', {
  configurable: true,
  enumerable: true,
  get: function () { return this.maxAge },
  set: function (val) { return this.maxAge = val }
});
deprecate.property(Cookie.prototype, 'maxage', '"maxage"; use "maxAge" instead')

function getPattern(name) {
  if (cache[name]) return cache[name]

  return cache[name] = new RegExp(
    "(?:^|;) *" +
    name.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, "\\$&") +
    "=([^;]*)"
  )
}

function pushCookie(cookies, cookie) {
  if (cookie.overwrite) {
    cookies = cookies.filter(function(c) { return c.indexOf(cookie.name+'=') !== 0 })
  }
  cookies.push(cookie.toHeader())
  return cookies
}

Cookies.connect = Cookies.express = function(keys) {
  return function(req, res, next) {
    req.cookies = res.cookies = new Cookies(req, res, {
      keys: keys
    })

    next()
  }
}

Cookies.Cookie = Cookie

module.exports = Cookies


/***/ }),
/* 21 */
/***/ (function(module, exports, __webpack_require__) {

var crypto = __webpack_require__(22)
  
function Keygrip(keys, algorithm, encoding) {
  if (!algorithm) algorithm = "sha1";
  if (!encoding) encoding = "base64";
  if (!(this instanceof Keygrip)) return new Keygrip(keys, algorithm, encoding)

  if (!keys || !(0 in keys)) {
    throw new Error("Keys must be provided.")
  }

  function sign(data, key) {
    return crypto
      .createHmac(algorithm, key)
      .update(data).digest(encoding)
      .replace(/\/|\+|=/g, function(x) {
        return ({ "/": "_", "+": "-", "=": "" })[x]
      })
  }

  this.sign = function(data){ return sign(data, keys[0]) }

  this.verify = function(data, digest) {
    return this.index(data, digest) > -1
  }

  this.index = function(data, digest) {
    for (var i = 0, l = keys.length; i < l; i++) {
      if (constantTimeCompare(digest, sign(data, keys[i]))) return i
    }

    return -1
  }
}

Keygrip.sign = Keygrip.verify = Keygrip.index = function() {
  throw new Error("Usage: require('keygrip')(<array-of-keys>)")
}

//http://codahale.com/a-lesson-in-timing-attacks/
var constantTimeCompare = function(val1, val2){
    if(val1 == null && val2 != null){
        return false;
    } else if(val2 == null && val1 != null){
        return false;
    } else if(val1 == null && val2 == null){
        return true;
    }

    if(val1.length !== val2.length){
        return false;
    }

    var matches = 1;

    for(var i = 0; i < val1.length; i++){
        matches &= (val1.charAt(i) === val2.charAt(i) ? 1 : 0); //Don't short circuit
    }

    return matches === 1;
};

module.exports = Keygrip


/***/ }),
/* 22 */
/***/ (function(module, exports) {

module.exports = require("crypto");

/***/ }),
/* 23 */
/***/ (function(module, exports) {

module.exports = require("debug");

/***/ }),
/* 24 */
/***/ (function(module, exports) {

module.exports = require("depd");

/***/ }),
/* 25 */
/***/ (function(module, exports) {

module.exports = require("http");

/***/ }),
/* 26 */
/***/ (function(module, exports) {

module.exports = require("on-headers");

/***/ })
/******/ ]);
//# sourceMappingURL=bundle.js.map